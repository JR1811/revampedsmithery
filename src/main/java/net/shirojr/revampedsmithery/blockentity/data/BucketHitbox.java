package net.shirojr.revampedsmithery.blockentity.data;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import net.shirojr.revampedsmithery.compat.cca.custom.SmithStationDataComponent;
import org.joml.Vector3f;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class BucketHitbox extends AbstractInteractionHitbox {
    public static final Identifier IDENTIFIER = RevampedSmithery.getId("bucket");

    private final SmithStationBlockEntity blockEntity;

    public BucketHitbox(SmithStationBlockEntity blockEntity, Box box, Vector3f debugColor) {
        super(box, debugColor);
        this.blockEntity = blockEntity;
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }

    public SmithStationBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public long getWaterLevel() {
        return this.getBlockEntity().getData().getWaterLevel();
    }

    public void setWaterLevel(long level) {
        this.blockEntity.getData().setWaterLevel(level);
    }

    public void incrementWaterLevel() {
        setWaterLevel(getWaterLevel() + FluidConstants.BUCKET);
    }

    public void decrementWaterLevel() {
        setWaterLevel(getWaterLevel() - FluidConstants.BUCKET);
    }

    public boolean hasWater() {
        return getWaterLevel() > 0;
    }

    @Override
    public ActionResult interact(SmithStationBlockEntity blockEntity, Vec3d actualPos, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(Items.WATER_BUCKET) && stack.getItem() instanceof BucketItem bucketItem) {
            if (getWaterLevel() < SmithStationDataComponent.MAX_WATER_LEVEL) {
                incrementWaterLevel();
                ItemStack emptiedStack = BucketItem.getEmptiedStack(stack, player);
                ItemUsage.exchangeStack(stack, player, emptiedStack);
                bucketItem.onEmptied(player, blockEntity.getWorld(), stack, BlockPos.ofFloored(actualPos));
                if (blockEntity.getWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.playSound(
                            null, actualPos.x, actualPos.y, actualPos.z,
                            SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS,
                            1f, 1f
                    );
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        } else if (stack.isOf(Items.BUCKET)) {
            if (hasWater()) {
                ItemUsage.exchangeStack(stack, player, Items.WATER_BUCKET.getDefaultStack());
                if (blockEntity.getWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.playSound(
                            null, actualPos.x, actualPos.y, actualPos.z,
                            SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS,
                            1f, 1f
                    );
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        } else if (!stack.isEmpty() && hasWater()) {
            NbtCompound nbt = stack.getNbt();
            if (nbt != null) {
                nbt.remove(CoalHitbox.HEAT_ITEM_STACK_NBT_KEY);
            }
            //TODO: quench heated ItemStack to process it
            this.decrementWaterLevel();
            if (blockEntity.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.playSound(null, actualPos.x, actualPos.y, actualPos.z, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 1f, 1f);
                serverWorld.playSound(null, actualPos.x, actualPos.y, actualPos.z, SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundCategory.BLOCKS, 1f, 1f);
            }
        }
        return super.interact(blockEntity, actualPos, player, hand);
    }
}
