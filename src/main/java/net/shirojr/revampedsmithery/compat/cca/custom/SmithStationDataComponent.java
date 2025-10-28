package net.shirojr.revampedsmithery.compat.cca.custom;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import net.shirojr.revampedsmithery.compat.cca.RevampedSmitheryComponents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class SmithStationDataComponent implements Component, AutoSyncedComponent, CommonTickingComponent {
    public static final Identifier KEY = RevampedSmithery.getId("smith_station_data");
    public static final long MAX_WATER_LEVEL = FluidConstants.BUCKET * 5;

    private final SmithStationBlockEntity blockEntity;

    @NotNull
    private ItemStack fuelStack;
    @NotNull
    private ItemStack armorStack;

    private long waterLevel;
    private float normalizedHeat;

    public SmithStationDataComponent(SmithStationBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
        this.fuelStack = ItemStack.EMPTY;
        this.armorStack = ItemStack.EMPTY;
        this.waterLevel = 0;
        this.normalizedHeat = 0;
    }

    @Nullable
    public static SmithStationDataComponent get(BlockEntity blockEntity) {
        if (!(blockEntity instanceof SmithStationBlockEntity smithStationBlockEntity)) return null;
        return RevampedSmitheryComponents.SMITHING_STATION_DATA.get(smithStationBlockEntity);
    }

    public SmithStationBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public @NotNull ItemStack getFuelStack() {
        return fuelStack;
    }

    public void setFuelStack(@NotNull ItemStack fuelStack) {
        this.fuelStack = fuelStack;
        this.sync();
    }

    public boolean isFuelStackEmpty() {
        return getFuelStack().isEmpty();
    }

    public @NotNull ItemStack getArmorStack() {
        return armorStack;
    }

    public void setArmorStack(@NotNull ItemStack armorStack) {
        this.armorStack = armorStack;
        this.sync();
    }

    public boolean isArmorStackEmpty() {
        return getArmorStack().isEmpty();
    }

    public long getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(long waterLevel) {
        this.waterLevel = Math.max(0, Math.min(MAX_WATER_LEVEL, waterLevel));
        this.sync();
    }

    public float getNormalizedHeat() {
        return normalizedHeat;
    }

    public void setNormalizedHeat(float normalizedHeat) {
        this.normalizedHeat = MathHelper.clamp(normalizedHeat, 0, 1);
    }


    @Override
    public void tick() {
        // RevampedSmithery.LOGGER.info("Ticked SmithStation data component");
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.fuelStack = !nbtCompound.contains("fuel") ? ItemStack.EMPTY : ItemStack.fromNbt(nbtCompound.getCompound("fuel"));
        this.armorStack = !nbtCompound.contains("armor") ? ItemStack.EMPTY : ItemStack.fromNbt(nbtCompound.getCompound("armor"));
        this.waterLevel = !nbtCompound.contains("water") ? 0L : nbtCompound.getLong("water");
        this.normalizedHeat = !nbtCompound.contains("heat") ? 0f : nbtCompound.getFloat("heat");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        NbtCompound fuelNbt = new NbtCompound();
        this.getFuelStack().writeNbt(fuelNbt);
        nbtCompound.put("fuel", fuelNbt);

        NbtCompound armorNbt = new NbtCompound();
        this.getArmorStack().writeNbt(armorNbt);
        nbtCompound.put("armor", armorNbt);

        nbtCompound.putLong("water", this.getWaterLevel());
        nbtCompound.putFloat("heat", this.getNormalizedHeat());
    }

    private void sync() {
        blockEntity.markDirty();
        RevampedSmitheryComponents.SMITHING_STATION_DATA.sync(blockEntity);
    }
}
