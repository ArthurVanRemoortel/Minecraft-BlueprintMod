package com.arthur.JavAdvancedMod.capabilities;

import com.arthur.JavAdvancedMod.capabilities.IBlueprintData;
import com.arthur.JavAdvancedMod.util.BlueprintData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModCapabilities {

    @CapabilityInject(BlueprintData.class)
    public static Capability<IBlueprintData> CAPABILITY_BLUEPRINT = null;

    public static void registerCapabilities(){
        CapabilityManager.INSTANCE.register(IBlueprintData.class, new BlueprintCapability(), BlueprintData::new);
    }




    public static class BlueprintCapability implements Capability.IStorage<IBlueprintData> {

        @Override
        public INBT writeNBT(Capability<IBlueprintData> capability, IBlueprintData instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            instance.serializeNBT(nbt);
            return nbt;
        }

        @Override
        public void readNBT(Capability<IBlueprintData> capability, IBlueprintData instance, Direction side, INBT nbt) {
            if (nbt instanceof CompoundNBT) {
                instance.deserializeNBT(((CompoundNBT) nbt));
            }
        }
    }



    public static class BlueprintProvider implements ICapabilityProvider, INBTSerializable<CompoundNBT> {

        private LazyOptional<IBlueprintData> capInstance;

        public BlueprintProvider() {
            capInstance = LazyOptional.of(BlueprintData::new);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
            if (cap == CAPABILITY_BLUEPRINT) {
                return capInstance.cast();
            }
            return LazyOptional.empty();
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
            if (capability == CAPABILITY_BLUEPRINT) {
                return capInstance.cast();
            }
            return LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT() {
            // TODO: This is called every tick, which could either be perfectly normal or a big issue in my implementation.
            // TODO: Maybe use a dirty bit so it only get written when modified?
            CompoundNBT nbt = new CompoundNBT();
            capInstance.ifPresent(bpd -> {
                if (bpd.hasData()){
                    BlueprintData blueprintData = (BlueprintData) bpd;
                    blueprintData.serializeNBT(nbt);
                }
            });
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            capInstance.ifPresent(bpd -> {
                if (!bpd.hasData()){
                    BlueprintData blueprintData = (BlueprintData) bpd;
                    blueprintData.deserializeNBT(nbt);
                }
            });
        }
    }
}
