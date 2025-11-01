package net.shirojr.revampedsmithery.event;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.shirojr.revampedsmithery.block.SmithStationBlock;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import net.shirojr.revampedsmithery.blockentity.data.AbstractInteractionHitbox;
import net.shirojr.revampedsmithery.init.RevampedSmitheryBlocks;

public class RevampedSmitheryBlockCallbacks implements AttackBlockCallback {
    @Override
    public ActionResult interact(PlayerEntity playerEntity, World world, Hand hand, BlockPos blockPos, Direction direction) {
        BlockState state = world.getBlockState(blockPos);
        if (state.isOf(RevampedSmitheryBlocks.SMITH_STATION)) {
            Direction facing = state.get(SmithStationBlock.FACING);
            BlockPos originPos = state.get(SmithStationBlock.PART).getPosOfOrigin(blockPos, facing);
            SmithStationBlockEntity blockEntity = SmithStationBlock.getBlockEntity(world, originPos);
            if (blockEntity == null) return ActionResult.PASS;
            Pair<AbstractInteractionHitbox, Vec3d> targetedHitbox = blockEntity.getTargetedHitbox(playerEntity);
            if (targetedHitbox == null) return ActionResult.PASS;
            return targetedHitbox.getLeft().attack(blockEntity, targetedHitbox.getRight(), playerEntity, playerEntity.getStackInHand(hand));
        }
        return ActionResult.PASS;
    }
}
