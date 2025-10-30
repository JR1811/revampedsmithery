package net.shirojr.revampedsmithery.blockentity.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import net.shirojr.revampedsmithery.util.ShapeUtil;
import org.joml.Vector3f;

public class AbstractInteractionHitbox {
    private final Box box;
    private final Vector3f debugColor;

    public AbstractInteractionHitbox(Box box, Vector3f debugColor) {
        this.box = box;
        this.debugColor = debugColor;
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

    public ActionResult interact(SmithStationBlockEntity blockEntity, BlockPos actualPos, PlayerEntity player, ItemStack stack) {
        return ActionResult.PASS;
    }

    public ActionResult attack(SmithStationBlockEntity blockEntity, BlockPos actualPos, PlayerEntity player, ItemStack stack) {
        return ActionResult.PASS;
    }
}
