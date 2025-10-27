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
import net.minecraft.util.math.Vec3d;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.init.RevampedSmitheryBlockEntities;
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

        Pair<InteractionHitBox, Double> closestInteraction = null;
        double reachDistance = player.isCreative() ? 5.0 : 4.5;
        Vec3d fullRangeReach = hitPos.subtract(eyePos).normalize().multiply(reachDistance);
        Vec3d endPos = eyePos.add(fullRangeReach);
        for (InteractionHitBox hitBox : this.getHitBoxes()) {
            Optional<Vec3d> raycast = hitBox.getBox().offset(this.pos).raycast(eyePos, endPos);
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
                getBoxFromPixelCoordinates(
                        new Vec3d(10, 0, 3),
                        new Vec3d(15, 5, 8)
                ),
                new Vector3f(7f, 1f, 3f),
                (blockEntity, player, stack) -> {
                    RevampedSmithery.LOGGER.info("Used Bucket");
                    return ActionResult.SUCCESS;
                }
        ),
        PEDAL(
                new Box(1, 1, 1, 2, 2, 2),
                new Vector3f(2f, 2f, 2f),
                (blockEntity, player, stack) -> {
                    RevampedSmithery.LOGGER.info("Used Pedal");
                    return ActionResult.SUCCESS;
                }
        ),
        /*COAL(new Box(1, 1, 1, 2, 2, 2), new Vector3f(2f, 2f, 2f)),
        CLAMP(new Box(1, 1, 1, 2, 2, 2), new Vector3f(2f, 2f, 2f)),
        RASP_FILE(new Box(1, 1, 1, 2, 2, 2), new Vector3f(2f, 2f, 2f)),
        BALL(new Box(1, 1, 1, 2, 2, 2), new Vector3f(2f, 2f, 2f)),*/;

        private final Box box;
        private final Vector3f debugColor;
        private final HitInteraction interaction;

        InteractionHitBox(Box box, Vector3f debugColor, HitInteraction interaction) {
            this.box = box;
            this.debugColor = debugColor;
            this.interaction = interaction;
        }

        public Box getBox() {
            return box;
        }

        public Vector3f getDebugColor() {
            return debugColor;
        }

        @Override
        public String asString() {
            return this.name();
        }

        public ActionResult interact(SmithStationBlockEntity blockEntity, PlayerEntity player, ItemStack stack) {
            return this.interaction.execute(blockEntity, player, stack);
        }

        public static Box getBoxFromPixelCoordinates(Vec3d start, Vec3d end) {
            Vec3d localSpaceStart = new Vec3d(start.x / 16, start.y / 16, start.z / 16);
            Vec3d localSpaceEnd = new Vec3d(end.x / 16, end.y / 16, end.z / 16);
            return new Box(localSpaceStart, localSpaceEnd);
        }
    }

    @FunctionalInterface
    public interface HitInteraction {
        ActionResult execute(SmithStationBlockEntity blockEntity, PlayerEntity player, ItemStack stack);
    }
}
