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
import net.minecraft.world.World;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import net.shirojr.revampedsmithery.compat.cca.RevampedSmitheryComponents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class SmithStationDataComponent implements Component, AutoSyncedComponent, CommonTickingComponent {
    public static final Identifier KEY = RevampedSmithery.getId("smith_station_data");
    public static final long MAX_WATER_LEVEL = FluidConstants.BUCKET * 5;
    public static final int ARMOR_BALL_HIT_TICK_COOLDOWN = 60;

    private final SmithStationBlockEntity blockEntity;

    @NotNull
    private ItemStack fuelStack;
    @NotNull
    private ItemStack armorStack;
    @NotNull
    private ItemStack clampStack;

    private long waterLevel;
    private float normalizedHeat;
    private boolean lit;

    private int armorHitTickCooldown;
    private boolean isArmorXAxisPositive;
    private boolean isArmorYAxisPositive;

    public SmithStationDataComponent(SmithStationBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
        this.fuelStack = ItemStack.EMPTY;
        this.armorStack = ItemStack.EMPTY;
        this.clampStack = ItemStack.EMPTY;
        this.waterLevel = 0;
        this.normalizedHeat = 0;
        this.armorHitTickCooldown = 0;
        this.lit = false;
        this.isArmorXAxisPositive = true;
        this.isArmorYAxisPositive = true;
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

    public @NotNull ItemStack getClampStack() {
        return clampStack;
    }

    public void setClampStack(@NotNull ItemStack clampStack) {
        this.clampStack = clampStack;
        this.sync();
    }

    public boolean isClampStackEmpty() {
        return getClampStack().isEmpty();
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

    public int getArmorHitTickCooldown() {
        return armorHitTickCooldown;
    }

    public void setArmorHitTickCooldown(int armorTickCooldown, boolean shouldSync) {
        this.armorHitTickCooldown = armorTickCooldown;
        if (shouldSync) {
            this.sync();
        }
    }

    public boolean isArmorXAxisPositive() {
        return isArmorXAxisPositive;
    }

    public void rerollArmorHitXAxis() {
        World world = this.getBlockEntity().getWorld();
        if (world == null) return;
        this.isArmorXAxisPositive = world.getRandom().nextBoolean();
        this.sync();
    }

    public boolean isArmorYAxisPositive() {
        return isArmorYAxisPositive;
    }

    public void rerollArmorHitYAxis() {
        World world = this.getBlockEntity().getWorld();
        if (world == null) return;
        this.isArmorYAxisPositive = world.getRandom().nextBoolean();
        this.sync();
    }

    public void startArmorHitTickCooldown(boolean overwritePrevious) {
        if (!overwritePrevious && isArmorHitTickCoolingDown()) {
            return;
        }
        this.setArmorHitTickCooldown(SmithStationDataComponent.ARMOR_BALL_HIT_TICK_COOLDOWN, true);
        this.rerollArmorHitXAxis();
        this.rerollArmorHitYAxis();
    }

    public boolean isArmorHitTickCoolingDown() {
        return this.getArmorHitTickCooldown() > 0;
    }

    public boolean isLit() {
        return lit;
    }

    public void setLit(boolean lit) {
        this.lit = lit;
        this.sync();
    }

    @Override
    public void tick() {
        int armorHitCooldown = getArmorHitTickCooldown();
        if (armorHitCooldown > 0) {
            setArmorHitTickCooldown(armorHitCooldown - 1, armorHitCooldown == 1);
        }

        if (isFuelStackEmpty() && isLit()) {
            this.setLit(false);
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.fuelStack = !nbtCompound.contains("fuel") ? ItemStack.EMPTY : ItemStack.fromNbt(nbtCompound.getCompound("fuel"));
        this.armorStack = !nbtCompound.contains("armor") ? ItemStack.EMPTY : ItemStack.fromNbt(nbtCompound.getCompound("armor"));
        this.clampStack = !nbtCompound.contains("clamp") ? ItemStack.EMPTY : ItemStack.fromNbt(nbtCompound.getCompound("clamp"));
        this.waterLevel = !nbtCompound.contains("water") ? 0L : nbtCompound.getLong("water");
        this.normalizedHeat = !nbtCompound.contains("heat") ? 0f : nbtCompound.getFloat("heat");
        this.armorHitTickCooldown = !nbtCompound.contains("armorCooldown") ? 0 : nbtCompound.getInt("armorCooldown");
        this.isArmorXAxisPositive = !nbtCompound.contains("armorXAxis") || nbtCompound.getBoolean("armorXAxis");
        this.isArmorYAxisPositive = !nbtCompound.contains("armorYAxis") || nbtCompound.getBoolean("armorYAxis");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        NbtCompound fuelNbt = new NbtCompound();
        this.getFuelStack().writeNbt(fuelNbt);
        nbtCompound.put("fuel", fuelNbt);

        NbtCompound armorNbt = new NbtCompound();
        this.getArmorStack().writeNbt(armorNbt);
        nbtCompound.put("armor", armorNbt);

        NbtCompound clampNbt = new NbtCompound();
        this.getClampStack().writeNbt(clampNbt);
        nbtCompound.put("clamp", clampNbt);

        nbtCompound.putLong("water", this.getWaterLevel());
        nbtCompound.putFloat("heat", this.getNormalizedHeat());
        nbtCompound.putInt("armorCooldown", this.getArmorHitTickCooldown());

        nbtCompound.putBoolean("armorXAxis", this.isArmorXAxisPositive());
        nbtCompound.putBoolean("armorYAxis", this.isArmorYAxisPositive());
    }

    private void sync() {
        blockEntity.markDirty();
        RevampedSmitheryComponents.SMITHING_STATION_DATA.sync(blockEntity);
    }
}
