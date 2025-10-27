package net.shirojr.revampedsmithery.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Pair;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.block.SmithStationBlock;
import net.shirojr.revampedsmithery.init.RevampedSmitheryBlockEntities;
import net.shirojr.revampedsmithery.init.RevampedSmitheryBlocks;
import net.shirojr.revampedsmithery.util.ShapeUtil;
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

    public ActionResult attemptInteraction(ItemStack stack, PlayerEntity player, BlockHitResult hit) {
        Vec3d eyePos = player.getEyePos();
        Vec3d hitPos = hit.getPos();
        BlockState state = this.getCachedState();
        if (!state.isOf(RevampedSmitheryBlocks.SMITH_STATION)) return ActionResult.FAIL;
        Direction facing = state.get(SmithStationBlock.FACING);

        Pair<InteractionHitBox, Double> closestInteraction = null;
        double reachDistance = player.isCreative() ? 5.0 : 4.5;
        Vec3d fullRangeReach = hitPos.subtract(eyePos).normalize().multiply(reachDistance);
        Vec3d endPos = eyePos.add(fullRangeReach);
        for (InteractionHitBox hitBox : this.getHitBoxes()) {
            Optional<Vec3d> raycast = hitBox.getRotatedBox(facing).offset(this.pos).raycast(eyePos, endPos);
            if (raycast.isEmpty()) continue;
            double currentDistance = eyePos.squaredDistanceTo(raycast.get());
            if (closestInteraction == null || closestInteraction.getRight() > currentDistance) {
                closestInteraction = new Pair<>(hitBox, currentDistance);
            }
        }
        if (closestInteraction == null) return ActionResult.PASS;
        return closestInteraction.getLeft().interact(this, player, stack);
    }

    public List<InteractionHitBox> getHitBoxes() {
        return hitBoxes;
    }

    public enum InteractionHitBox implements StringIdentifiable {
        BUCKET(
                ShapeUtil.getBoxFromVoxelCoordinates(
                        new Vec3d(10, 0, 3),
                        new Vec3d(15, 5, 8)
                ),
                new Vector3f(0.7f, 0.1f, 0.3f),
                (blockEntity, player, stack) -> {
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
                (blockEntity, player, stack) -> {
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
                (blockEntity, player, stack) -> {
                    RevampedSmithery.LOGGER.info("Used Coal Furnace");
                    return ActionResult.SUCCESS;
                }
        ),
        RASP_FILE(
                ShapeUtil.getBoxFromVoxelCoordinates(
                        new Vec3d(15, 14, 10),
                        new Vec3d(21, 20, 14)
                ),
                new Vector3f(0.7f, 0.1f, 0.3f),
                (blockEntity, player, stack) -> {
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
                (blockEntity, player, stack) -> {
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
                (blockEntity, player, stack) -> {
                    RevampedSmithery.LOGGER.info("Used Ball");
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

        public ActionResult interact(SmithStationBlockEntity blockEntity, PlayerEntity player, ItemStack stack) {
            return this.interaction.execute(blockEntity, player, stack);
        }

        @Override
        public String asString() {
            return this.name();
        }
    }

    @FunctionalInterface
    public interface HitInteraction {
        ActionResult execute(SmithStationBlockEntity blockEntity, PlayerEntity player, ItemStack stack);
    }
}
