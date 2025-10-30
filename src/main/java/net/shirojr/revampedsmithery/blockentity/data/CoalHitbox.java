package net.shirojr.revampedsmithery.blockentity.data;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import org.joml.Vector3f;

public class CoalHitbox extends AbstractInteractionHitbox {
    public static final Identifier IDENTIFIER = RevampedSmithery.getId("coal");

    public CoalHitbox(Box box, Vector3f debugColor) {
        super(box, debugColor);
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public ActionResult interact(SmithStationBlockEntity blockEntity, BlockPos actualPos, PlayerEntity player, ItemStack stack) {
        RevampedSmithery.LOGGER.info("Used Coal Furnace");
        ItemStack furnaceStack = blockEntity.getData().getFuelStack();
        int insertionAmount = 1;
        ItemStack toBeInsertedStack = stack.copyWithCount(insertionAmount);
        boolean canMerge = furnaceStack.isEmpty() || (ItemStack.canCombine(toBeInsertedStack, furnaceStack) && toBeInsertedStack.getCount() + furnaceStack.getCount() <= toBeInsertedStack.getMaxCount());
        if (AbstractFurnaceBlockEntity.canUseAsFuel(stack) && canMerge) {
            //FIXME: add cooldown, animation, particles
            if (furnaceStack.isEmpty()) {
                blockEntity.getData().setFuelStack(toBeInsertedStack);
            } else {
                furnaceStack.increment(insertionAmount);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
