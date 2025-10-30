package net.shirojr.revampedsmithery.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.shirojr.revampedsmithery.block.SmithStationBlock;
import net.shirojr.revampedsmithery.blockentity.data.*;
import net.shirojr.revampedsmithery.compat.cca.custom.SmithStationDataComponent;
import net.shirojr.revampedsmithery.init.RevampedSmitheryBlockEntities;
import net.shirojr.revampedsmithery.init.RevampedSmitheryBlocks;
import net.shirojr.revampedsmithery.util.ShapeUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;

public class SmithStationBlockEntity extends BlockEntity {
    private final HashMap<Identifier, AbstractInteractionHitbox> hitBoxes = new HashMap<>();

    public SmithStationBlockEntity(BlockPos pos, BlockState state) {
        super(RevampedSmitheryBlockEntities.SMITH_STATION, pos, state);
        this.initializeInnerHitboxes();
    }

    public HashMap<Identifier, AbstractInteractionHitbox> getHitBoxes() {
        return hitBoxes;
    }

    private void initializeInnerHitboxes() {
        this.hitBoxes.put(
                BucketHitbox.IDENTIFIER,
                new BucketHitbox(
                        ShapeUtil.getBoxFromVoxelCoordinates(
                                new Vec3d(10, 0, 3),
                                new Vec3d(15, 5, 8)
                        ),
                        AbstractInteractionHitbox.RED
                )
        );
        this.hitBoxes.put(
                PedalHitbox.IDENTIFIER,
                new PedalHitbox(
                        ShapeUtil.getBoxFromVoxelCoordinates(
                                new Vec3d(3, 0, 3),
                                new Vec3d(7, 5, 15)
                        ),
                        AbstractInteractionHitbox.RED
                )
        );
        this.hitBoxes.put(
                BallHitbox.IDENTIFIER,
                new BallHitbox(
                        this,
                        ShapeUtil.getBoxFromVoxelCoordinates(
                                new Vec3d(26, 15, 0),
                                new Vec3d(30, 19, 4)
                        ),
                        AbstractInteractionHitbox.RED
                )
        );
        this.hitBoxes.put(
                CoalHitbox.IDENTIFIER,
                new CoalHitbox(
                        ShapeUtil.getBoxFromVoxelCoordinates(
                                new Vec3d(3, 16, 3),
                                new Vec3d(13, 27, 15)
                        ),
                        AbstractInteractionHitbox.RED
                )
        );
        this.hitBoxes.put(
                RaspFileHitbox.IDENTIFIER,
                new RaspFileHitbox(
                        ShapeUtil.getBoxFromVoxelCoordinates(
                                new Vec3d(15, 14, 10),
                                new Vec3d(21, 20, 14)
                        ),
                        AbstractInteractionHitbox.RED
                )
        );
        this.hitBoxes.put(
                ClampHitbox.IDENTIFIER,
                new ClampHitbox(
                        ShapeUtil.getBoxFromVoxelCoordinates(
                                new Vec3d(15, 15, 5),
                                new Vec3d(23, 19, 8)
                        ),
                        AbstractInteractionHitbox.RED
                )
        );
    }

    public SmithStationDataComponent getData() {
        return SmithStationDataComponent.get(this);
    }

    public ActionResult attemptInteraction(ItemStack stack, PlayerEntity player, BlockHitResult hit) {
        BlockState state = this.getCachedState();
        if (!state.isOf(RevampedSmitheryBlocks.SMITH_STATION)) return ActionResult.FAIL;
        AbstractInteractionHitbox targetedHitbox = getTargetedHitbox(player);
        if (targetedHitbox == null) return ActionResult.PASS;
        return targetedHitbox.interact(this, hit.getBlockPos(), player, stack);
    }

    @Nullable
    public AbstractInteractionHitbox getTargetedHitbox(PlayerEntity player) {
        Pair<AbstractInteractionHitbox, Double> closestInteraction = null;

        double reachDistance = player.isCreative() ? 5.0 : 4.5;
        Vec3d eyePos = player.getEyePos();
        Vec3d fullRangeReach = player.getRotationVector().multiply(reachDistance);
        Vec3d endPos = eyePos.add(fullRangeReach);
        Direction facing = this.getCachedState().get(SmithStationBlock.FACING);

        for (var hitBox : this.getHitBoxes().values()) {
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
}
