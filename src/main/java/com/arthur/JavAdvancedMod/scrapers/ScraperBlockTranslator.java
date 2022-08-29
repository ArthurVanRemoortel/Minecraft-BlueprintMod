package com.arthur.JavAdvancedMod.scrapers;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/* Block names from the website do not match the block names in game. Some translation is needed.
   Usually replacing " " with "_" and changing to lowercase works.
   The HashMap bellow is a manually created list to translate the other cases.
*/
public class ScraperBlockTranslator extends HashMap<String, ResourceLocation> {
    Block defaultBlock = ForgeRegistries.BLOCKS.getValue(this.get("Bedrock"));

    public ScraperBlockTranslator(){
        this.put("Bedrock", new ResourceLocation("minecraft", "bedrock"));
        this.put("Redstone Block", new ResourceLocation("minecraft", "redstone_block"));

        this.put("Lapis Lazuli Block", new ResourceLocation("minecraft", "lapis_block"));

        this.put("Block of Quartz", new ResourceLocation("minecraft", "quartz_block"));
        this.put("Chiseled Quartz Block", new ResourceLocation("minecraft", "quartz_block"));
        this.put("Pillar Quartz Block", new ResourceLocation("minecraft", "quartz_block"));
        this.put("Oak Wood Stairs", new ResourceLocation("minecraft", "oak_stairs"));

        this.put("Stone Brick Monster Egg", new ResourceLocation("minecraft", "monster_egg"));

        this.put("Oak Fence", new ResourceLocation("minecraft", "fence"));

        this.put("Hardened Clay", new ResourceLocation("minecraft", "hardened_clay"));
        this.put("Terracotta", new ResourceLocation("minecraft", "stained_hardened_clay"));

        this.put("White Stained Clay", new ResourceLocation("minecraft", "white_terracotta"));
        this.put("Orange Stained Clay", new ResourceLocation("minecraft", "orange_terracotta"));
        this.put("Magenta Stained Clay", new ResourceLocation("minecraft", "magenta_terracotta"));
        this.put("Light Blue Stained Clay", new ResourceLocation("minecraft", "light_blue_terracotta"));
        this.put("Yellow Stained Clay", new ResourceLocation("minecraft", "yellow_terracotta"));
        this.put("Lime Stained Clay", new ResourceLocation("minecraft", "lime_terracotta"));
        this.put("Pink Stained Clay", new ResourceLocation("minecraft", "pink_terracotta"));
        this.put("Gray Stained Clay", new ResourceLocation("minecraft", "gray_terracotta"));
        this.put("Light Gray Stained Clay", new ResourceLocation("minecraft", "light_gray_terracotta"));
        this.put("Cyan Stained Clay", new ResourceLocation("minecraft", "cyan_terracotta"));
        this.put("Purple Stained Clay", new ResourceLocation("minecraft", "purple_terracotta"));
        this.put("Blue Stained Clay", new ResourceLocation("minecraft", "blue_terracotta"));
        this.put("Brown Stained Clay", new ResourceLocation("minecraft", "brown_terracotta"));
        this.put("Green Stained Clay", new ResourceLocation("minecraft", "green_terracotta"));
        this.put("Red Stained Clay", new ResourceLocation("minecraft", "red_terracotta"));
        this.put("Black Stained Clay", new ResourceLocation("minecraft", "black_terracotta"));

        this.put("White Glazed Clay", new ResourceLocation("minecraft", "white_glazed_terracotta"));
        this.put("Orange Glazed Clay", new ResourceLocation("minecraft", "orange_glazed_terracotta"));
        this.put("Magenta Glazed Clay", new ResourceLocation("minecraft", "magenta_glazed_terracotta"));
        this.put("Light Blue Glazed Clay", new ResourceLocation("minecraft", "light_blue_glazed_terracotta"));
        this.put("Yellow Glazed Clay", new ResourceLocation("minecraft", "yellow_glazed_terracotta"));
        this.put("Lime Glazed Clay", new ResourceLocation("minecraft", "lime_glazed_terracotta"));
        this.put("Pink Glazed Clay", new ResourceLocation("minecraft", "pink_glazed_terracotta"));
        this.put("Gray Glazed Clay", new ResourceLocation("minecraft", "gray_glazed_terracotta"));
        this.put("Light Gray Glazed Clay", new ResourceLocation("minecraft", "light_gray_glazed_terracotta"));
        this.put("Cyan Glazed Clay", new ResourceLocation("minecraft", "cyan_glazed_terracotta"));
        this.put("Purple Glazed Clay", new ResourceLocation("minecraft", "purple_glazed_terracotta"));
        this.put("Blue Glazed Clay", new ResourceLocation("minecraft", "blue_glazed_terracotta"));
        this.put("Brown Glazed Clay", new ResourceLocation("minecraft", "brown_glazed_terracotta"));
        this.put("Green Glazed Clay", new ResourceLocation("minecraft", "green_glazed_terracotta"));
        this.put("Red Glazed Clay", new ResourceLocation("minecraft", "red_glazed_terracotta"));
        this.put("Black Glazed Clay", new ResourceLocation("minecraft", "black_glazed_terracotta"));
    }

    public Block findGuess(String rawKey){
        rawKey = rawKey.toLowerCase().replace(' ', '_');
        if (rawKey.contains("stairs")){
            rawKey = rawKey.replace("_wood_", "_");
        }
        if (rawKey.contains("slab")){
            rawKey = rawKey.replace("_wood_", "_");
        }
        ResourceLocation resourceGuess = new ResourceLocation("minecraft", rawKey);
        return ForgeRegistries.BLOCKS.getValue(resourceGuess);
    }

    public Block getBlock(String blockName){
        Block guessesBlock = findGuess(blockName);
        if (guessesBlock != Blocks.AIR)
            return guessesBlock;

        Block block = ForgeRegistries.BLOCKS.getValue(this.get(blockName));
        if (block == Blocks.AIR) {
            // System.out.println("BLOCK NOT FOUND: "+blockName);
            block = defaultBlock;
        }
        return block;
    }
}
