package net.shirojr.revampedsmithery.blockentity.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.block.SmithStationBlock;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import net.shirojr.revampedsmithery.compat.cca.custom.SmithStationDataComponent;
import org.joml.Vector3f;

public class BallHitbox extends AbstractInteractionHitbox {
    public static final Identifier IDENTIFIER = RevampedSmithery.getId("ball");
    public static final double HITBOX_SIZE_CHANGE = 0.2;

    private final SmithStationBlockEntity blockEntity;

    public BallHitbox(SmithStationBlockEntity blockEntity, Box box, Vector3f debugColor) {
        super(box, debugColor);
        this.blockEntity = blockEntity;
        if (!this.blockEntity.getData().isArmorStackEmpty()) {
            expandBoxWithArmorStack();
        }
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }

    @SuppressWarnings("unused")
    public SmithStationBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public ActionResult interact(SmithStationBlockEntity blockEntity, BlockPos actualPos, PlayerEntity player, ItemStack stack) {
        boolean isServer = player.getWorld() instanceof ServerWorld;
        SmithStationDataComponent data = blockEntity.getData();
        ItemStack handStack = player.getMainHandStack();
        ItemStack armorStack = data.getArmorStack();

        if (handStack.getItem() instanceof ArmorItem && data.isArmorStackEmpty()) {
            if (isServer) {
                data.setArmorStack(handStack.copyWithCount(1));
                if (!player.isCreative()) {
                    player.getMainHandStack().decrement(1);
                }
            }
            expandBoxWithArmorStack();
        } else if (handStack.isEmpty() && !data.isArmorStackEmpty()) {
            if (isServer) {
                player.getInventory().offerOrDrop(armorStack.copy());
                data.setArmorStack(ItemStack.EMPTY);
            }
            resetBoxWithoutArmorStack();
        } else {
            return ActionResult.PASS;
        }
        if (player.getWorld() instanceof ServerWorld serverWorld) {
            float pitch = MathHelper.lerp(serverWorld.getRandom().nextFloat(), 0.7f, 0.8f);
            serverWorld.playSound(null, actualPos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1f, pitch);
        }
        RevampedSmithery.LOGGER.info(armorStack.toString());
        RevampedSmithery.LOGGER.info("Interacted with Ball");
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult attack(SmithStationBlockEntity blockEntity, BlockPos actualPos, PlayerEntity player, ItemStack stack) {
        boolean isServer = player.getWorld() instanceof ServerWorld;
        SmithStationDataComponent data = blockEntity.getData();
        ItemStack armorStack = data.getArmorStack();
        Direction facing = blockEntity.getCachedState().get(SmithStationBlock.FACING);

        if (data.isArmorStackEmpty() || data.isArmorHitTickCoolingDown()) return ActionResult.FAIL;
        if (isServer) {
            armorStack.setDamage(armorStack.getDamage() - 1);
            if (player.getWorld() instanceof ServerWorld serverWorld) {
                Random random = serverWorld.getRandom();
                float pitch = MathHelper.lerp(random.nextFloat(), 0.7f, 1f);
                serverWorld.playSound(null, actualPos, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.BLOCKS, 1f, pitch);
                serverWorld.playSound(null, actualPos, SoundEvents.BLOCK_LANTERN_BREAK, SoundCategory.BLOCKS, 1f, pitch);
                Vec3d hitboxCenter = getRotatedBox(facing).offset(blockEntity.getPos()).getCenter();
                serverWorld.spawnParticles(
                        ParticleTypes.CRIT,
                        hitboxCenter.x, hitboxCenter.y, hitboxCenter.z,
                        random.nextBetween(5, 10),
                        0, 0, 0, 0.5
                );
            }
        }
        blockEntity.getData().startArmorHitTickCooldown(false);
        RevampedSmithery.LOGGER.info("Attacked Ball");
        return ActionResult.SUCCESS;
    }

    public void expandBoxWithArmorStack() {
        if (!this.box.equals(this.getOriginalBox())) return;
        this.box = this.box.expand(HITBOX_SIZE_CHANGE, HITBOX_SIZE_CHANGE, HITBOX_SIZE_CHANGE);
    }

    public void resetBoxWithoutArmorStack() {
        this.box = this.getOriginalBox();
    }
}
