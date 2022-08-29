package com.arthur.JavAdvancedMod.util;

import com.arthur.JavAdvancedMod.JavaAdvancedMod;
import com.arthur.JavAdvancedMod.capabilities.IBlueprintData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.INBT;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.*;

/* Holds information about blueprints, how to build them and how to read/write them to disk.
 */
public class BlueprintData implements IBlueprintData {
    private Map<BlockPos, BlockState> blocks = new HashMap<>();
    private String sourceUsed = "";


    public BlueprintData(String sourceUsed, JsonObject jsonObject)
    {
        this.sourceUsed = sourceUsed;
        loadFromJson(jsonObject);
    }

    public BlueprintData() {}

    private void loadFromJson(JsonObject jsonObject){
        int[] baseline = getJsonBaselineAndDimensions(jsonObject);
        int baselineX = baseline[0];
        int baselineZ =  baseline[1];
        int scaleX = baseline[2];
        int scaleZ = baseline[3];
        int width = baseline[4]; // Not used anymore. Could still be useful later.
        int depth = baseline[5];
        int height = baseline[6];

        for(Map.Entry<String, JsonElement> layerEntry : jsonObject.entrySet()) {
            int layerN = Integer.parseInt(layerEntry.getKey()) - 1;

            for(JsonElement blockData : layerEntry.getValue().getAsJsonArray()){
                JsonObject blockJson = blockData.getAsJsonObject();
                String rawName = blockJson.get("h").getAsString();
                String blockName;
                String blockMeta;
                if (rawName.contains(" (")){
                    blockName = rawName.substring(0, rawName.indexOf(" ("));
                    blockMeta = rawName.substring(rawName.indexOf(" (")+1);
                } else {
                    blockName = rawName;
                    blockMeta = null;
                }

                int rawX = blockJson.get("x").getAsInt();
                int rawZ = blockJson.get("y").getAsInt();
                int boxX = (rawX - baselineX) / scaleX;
                int boxZ = (rawZ - baselineZ) / scaleZ;

                Block block = JavaAdvancedMod.scraperBlockTranslator.getBlock(blockName);
                BlockState blockState = block.getDefaultState();
                if (blockMeta != null) {
                    Direction direction = Direction.NORTH;
                    if (blockMeta.contains("South")) {
                        direction = Direction.SOUTH;
                    } else if (blockMeta.contains("West")) {
                        direction = Direction.WEST;
                    } else if (blockMeta.contains("East")) {
                        direction = Direction.EAST;
                    }
                    // TODO: Setting direction of blocks does not work.
                    // DOC: https://mcforge.readthedocs.io/en/latest/blocks/states/

                    // if (blockName.contains("Stairs") && blockState.hasProperty(BlockStateProperties.FACING)){
                    //     blockState = blockState
                    //             .with(BlockStateProperties.FACING, direction)
                    //     ;
                    //     System.out.println("");
                    // }

                }

                BlockPos thePos = new BlockPos(boxX, layerN, boxZ);
                blocks.put(thePos, blockState);
            }
        }
    }

    private int[] getJsonBaselineAndDimensions(JsonObject jsonObject){
        int minX = Integer.MAX_VALUE; // Baseline x
        int minZ = Integer.MAX_VALUE; // baseline z
        int scaleX = Integer.MAX_VALUE;
        int scaleZ = Integer.MAX_VALUE;
        int maxX = 0; // width
        int maxZ = 0; // depth
        int maxY = 0; // layer
        for(Map.Entry<String, JsonElement> layer_entry : jsonObject.entrySet()) {
            int layerN = Integer.parseInt(layer_entry.getKey()) - 1;
            if (layerN > maxY) maxY = layerN;
            for(JsonElement block_data : layer_entry.getValue().getAsJsonArray()) {
                JsonObject block_json = block_data.getAsJsonObject();
                int x = block_json.get("x").getAsInt();
                int z = block_json.get("y").getAsInt(); // y on web = z in game.
                if (x < minX)  minX = x;
                if (z < minZ)  minZ = z;
                if (x > maxX)  maxX = x;
                if (z > maxZ)  maxZ = z;

                int difX = x - minX;
                if (difX != 0 && difX < scaleX)
                    scaleX = difX;
                int difZ = z - minZ;
                if (difZ != 0 && difZ < scaleZ)
                    scaleZ = difZ;
            }
        }
        return new int[]{minX, minZ, scaleX, scaleZ, maxX, maxZ, maxY};
    }

    public void build(World worldIn, PlayerEntity playerIn, BlockPos buildPos) {
        for (Map.Entry<BlockPos, BlockState> blockEntry : blocks.entrySet()) {
            BlockPos blueprintPos = blockEntry.getKey();
            BlockState blockState = blockEntry.getValue();
            BlockPos buildBlockPos = new BlockPos(
                    buildPos.getX() + blueprintPos.getX(),
                    buildPos.getY() + blueprintPos.getY(),
                    buildPos.getZ() + blueprintPos.getZ()
            );
            worldIn.setBlockState(buildBlockPos, blockState);
        }
    }

    public void serializeNBT(CompoundNBT nbt) {
        ListNBT posList = new ListNBT();
        ListNBT blockList = new ListNBT();

        for (Map.Entry<BlockPos, BlockState> blockEntry : blocks.entrySet()) {
            BlockPos blueprintPos = blockEntry.getKey();
            BlockState blockState = blockEntry.getValue();
            CompoundNBT blockPosNBT = NBTUtil.writeBlockPos(blueprintPos);
            CompoundNBT blockStateNBT = NBTUtil.writeBlockState(blockState);
            posList.add(blockPosNBT);
            blockList.add(blockStateNBT);
        }
        nbt.put("positions", posList);
        nbt.put("blocks", blockList);
        nbt.putString("source", this.sourceUsed);
    }

    public void deserializeNBT(CompoundNBT nbt) {
        String sourceUrl = nbt.getString("source");
        Iterator<INBT> posListIt = nbt.getList("positions", Constants.NBT.TAG_COMPOUND).iterator();
        Iterator<INBT> blockListIt = nbt.getList("blocks",  Constants.NBT.TAG_COMPOUND).iterator();
        while (posListIt.hasNext() && blockListIt.hasNext()) {
            CompoundNBT blockPosNBT = (CompoundNBT) posListIt.next();
            CompoundNBT blockStateNBT = (CompoundNBT) blockListIt.next();
            this.blocks.put(
                    NBTUtil.readBlockPos(blockPosNBT),
                    NBTUtil.readBlockState(blockStateNBT)
            );
        }
        this.sourceUsed = sourceUrl;
    }

    @Override
    public boolean hasData() {
        return !this.blocks.isEmpty();
    }

    @Override
    public String shortString() {
        return this.sourceUsed;
    }


}
