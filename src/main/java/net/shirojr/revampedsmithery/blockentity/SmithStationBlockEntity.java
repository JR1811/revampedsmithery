package net.shirojr.revampedsmithery.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Pair;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.block.SmithStationBlock;
import net.shirojr.revampedsmithery.compat.cca.custom.SmithStationDataComponent;
import net.shirojr.revampedsmithery.init.RevampedSmitheryBlockEntities;
import net.shirojr.revampedsmithery.init.RevampedSmitheryBlocks;
import net.shirojr.revampedsmithery.util.ShapeUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SmithStationBlockEntity extends BlockEntity {
    private final List<InteractionHitBox> hitBoxes;

    public SmithStationBlockEntity(BlockPos pos, BlockState state) {
        super(RevampedSmitheryBlockEntities.SMITH_STATION, pos, state);
        this.hitBoxes = new ArrayList<>(List.of(InteractionHitBox.values()));
    }

    public List<InteractionHitBox> getHitBoxes() {
        return hitBoxes;
    }

    public SmithStationDataComponent getData() {
        return SmithStationDataComponent.get(this);
    }

    public ActionResult attemptInteraction(ItemStack stack, PlayerEntity player, BlockHitResult hit) {
        BlockState state = this.getCachedState();
        if (!state.isOf(RevampedSmitheryBlocks.SMITH_STATION)) return ActionResult.FAIL;
        InteractionHitBox targetedHitbox = getTargetedHitbox(player);
        if (targetedHitbox == null) return ActionResult.PASS;
        return targetedHitbox.interact(this, hit.getBlockPos(), player, stack, false);
    }

    @Nullable
    public InteractionHitBox getTargetedHitbox(PlayerEntity player) {
        Pair<InteractionHitBox, Double> closestInteraction = null;

        double reachDistance = player.isCreative() ? 5.0 : 4.5;
        Vec3d eyePos = player.getEyePos();
        Vec3d fullRangeReach = player.getRotationVector().multiply(reachDistance);
        Vec3d endPos = eyePos.add(fullRangeReach);
        Direction facing = this.getCachedState().get(SmithStationBlock.FACING);

        for (InteractionHitBox hitBox : this.getHitBoxes()) {
            Optional<Vec3d> raycast = hitBox.getRotatedBox(facing).offset(this.pos).raycast(eyePos, endPos);
            if (raycast.isEmpty()) continue;
            double currentDistance = eyePos.squaredDistanceTo(raycast.get());
            if (closestInteraction == null || closestInteraction.getRight() > currentDistance) {
                closestInteraction = new Pair<>(hitBox, currentDistance);
            }
        }
        return closestInteraction == null ? null : closestInteraction.getLeft();
    }

    /**
     * Keep Super call for {@link SmithStationDataComponent SmithStationDataComponent}!
     *
     * @apiNote <a href="https://ladysnake.org/wiki/cardinal-components-api/modules/block#usage">See Wiki</a>
     */
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    /**
     * Keep Super call for {@link SmithStationDataComponent SmithStationDataComponent}!
     *
     * @apiNote <a href="https://ladysnake.org/wiki/cardinal-components-api/modules/block#usage">See Wiki</a>
     */
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

    public enum InteractionHitBox implements StringIdentifiable {
        BUCKET(
                ShapeUtil.getBoxFromVoxelCoordinates(
                        new Vec3d(10, 0, 3),
                        new Vec3d(15, 5, 8)
                ),
                new Vector3f(0.7f, 0.1f, 0.3f),
                (blockEntity, actualPos, player, stack, isAttack) -> {
                    RevampedSmithery.LOGGER.info("Used Bucket");
                    return ActionResult.SUCCESS;
                }
        ),
        PEDAL(
                ShapeUtil.getBoxFromVoxelCoordinates(
                        new Vec3d(3, 0, 3),
                        new Vec3d(7, 5, 15)
                ),
                new Vector3f(0.7f, 0.1f, 0.3f),
                (blockEntity, actualPos, player, stack, isAttack) -> {
                    RevampedSmithery.LOGGER.info("Used Pedal");
                    return ActionResult.SUCCESS;
                }
        ),
        COAL(
                ShapeUtil.getBoxFromVoxelCoordinates(
                        new Vec3d(3, 16, 3),
                        new Vec3d(13, 27, 15)
                ),
                new Vector3f(0.7f, 0.1f, 0.3f),
                (blockEntity, actualPos, player, stack, isAttack) -> {
                    RevampedSmithery.LOGGER.info("Used Coal Furnace");
                    ItemStack furnaceStack = blockEntity.getData().getFuelStack();
                    int insertionAmount = 1;
                    ItemStack toBeInsertedStack = stack.copyWithCount(insertionAmount);
                    boolean canMerge = furnaceStack.isEmpty() || (ItemStack.canCombine(toBeInsertedStack, furnaceStack) && toBeInsertedStack.getCount() + furnaceStack.getCount() <= toBeInsertedStack.getMaxCount());
                    if (AbstractFurnaceBlockEntity.canUseAsFuel(stack) && canMerge) {
                        if (furnaceStack.isEmpty()) {
                            blockEntity.getData().setFuelStack(toBeInsertedStack);
                        } else {
                            furnaceStack.increment(insertionAmount);
                        }
                        return ActionResult.SUCCESS;
                    }
                    return ActionResult.PASS;
                }
        ),
        RASP_FILE(
                ShapeUtil.getBoxFromVoxelCoordinates(
                        new Vec3d(15, 14, 10),
                        new Vec3d(21, 20, 14)
                ),
                new Vector3f(0.7f, 0.1f, 0.3f),
                (blockEntity, actualPos, player, stack, isAttack) -> {
                    RevampedSmithery.LOGGER.info("Used rasp file");
                    return ActionResult.SUCCESS;
                }
        ),
        CLAMP(
                ShapeUtil.getBoxFromVoxelCoordinates(
                        new Vec3d(15, 15, 5),
                        new Vec3d(23, 19, 8)
                ),
                new Vector3f(0.7f, 0.1f, 0.3f),
                (blockEntity, actualPos, player, stack, isAttack) -> {
                    RevampedSmithery.LOGGER.info("Used Clamp");
                    return ActionResult.SUCCESS;
                }
        ),
        BALL(
                ShapeUtil.getBoxFromVoxelCoordinates(
                        new Vec3d(26, 15, 0),
                        new Vec3d(30, 19, 4)
                ),
                new Vector3f(0.7f, 0.1f, 0.3f),
                (blockEntity, actualPos, player, stack, isAttack) -> {
                    RevampedSmithery.LOGGER.info("Used Ball");
                    boolean isServer = player.getWorld() instanceof ServerWorld;
                    SmithStationDataComponent data = blockEntity.getData();
                    ItemStack handStack = player.getMainHandStack();
                    ItemStack armorStack = data.getArmorStack();
                    if (isAttack) {
                        if (data.isArmorStackEmpty()) return ActionResult.PASS;
                        if (isServer) {
                            armorStack.setDamage(armorStack.getDamage() - 1);
                            if (player.getWorld() instanceof ServerWorld serverWorld) {
                                float pitch = MathHelper.lerp(serverWorld.getRandom().nextFloat(), 0.7f, 1f);
                                serverWorld.playSound(null, actualPos, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.BLOCKS, 1f, pitch);
                                serverWorld.playSound(null, actualPos, SoundEvents.BLOCK_LANTERN_BREAK, SoundCategory.BLOCKS, 1f, pitch);
                            }
                        }
                        return ActionResult.SUCCESS;
                    }
                    if (handStack.getItem() instanceof ArmorItem && data.isArmorStackEmpty()) {
                        if (isServer) {
                            data.setArmorStack(handStack.copy());
                            if (!player.isCreative()) {
                                player.getMainHandStack().setCount(0);
                            }
                        }
                    } else if (handStack.isEmpty() && !data.isArmorStackEmpty()) {
                        if (isServer) {
                            player.getInventory().offerOrDrop(armorStack.copy());
                            data.setArmorStack(ItemStack.EMPTY);
                        }
                    } else {
                        return ActionResult.PASS;
                    }
                    if (player.getWorld() instanceof ServerWorld serverWorld) {
                        float pitch = MathHelper.lerp(serverWorld.getRandom().nextFloat(), 0.7f, 0.8f);
                        serverWorld.playSound(null, actualPos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1f, pitch);
                    }
                    RevampedSmithery.LOGGER.info(armorStack.toString());
                    return ActionResult.SUCCESS;
                }
        );

        private final Box box;
        private final Vector3f debugColor;
        private final HitInteraction interaction;

        InteractionHitBox(Box box, Vector3f debugColor, HitInteraction interaction) {
            this.box = box;
            this.debugColor = debugColor;
            this.interaction = interaction;
        }

        /**
         * Default Boxes are only aligned towards {@link Direction#NORTH}
         */
        public Box getDefaultBox() {
            return box;
        }

        public Box getRotatedBox(Direction facing) {
            return ShapeUtil.rotateBox(getDefaultBox(), facing);
        }

        public Vector3f getDebugColor() {
            return debugColor;
        }

        public ActionResult interact(SmithStationBlockEntity blockEntity, BlockPos actualPos, PlayerEntity player, ItemStack stack, boolean isAttack) {
            return this.interaction.execute(blockEntity, actualPos, player, stack, isAttack);
        }

        @Override
        public String asString() {
            return this.name();
        }
    }

    @FunctionalInterface
    public interface HitInteraction {
        ActionResult execute(SmithStationBlockEntity blockEntity, BlockPos actualPos, PlayerEntity player, ItemStack stack, boolean isAttack);
    }
}
