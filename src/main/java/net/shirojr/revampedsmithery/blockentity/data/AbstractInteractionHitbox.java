package net.shirojr.revampedsmithery.blockentity.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import net.shirojr.revampedsmithery.util.ShapeUtil;
import org.joml.Vector3f;

public abstract class AbstractInteractionHitbox {
    public static final Vector3f RED = new Vector3f(0.7f, 0.1f, 0.3f);

    protected final Box originalBox;

    protected Box box;
    protected Vector3f debugColor;

    public AbstractInteractionHitbox(Box box, Vector3f debugColor) {
        this.originalBox = box;
        this.box = originalBox;
        this.debugColor = debugColor;
    }

    public abstract Identifier getIdentifier();

    /**
     * Default Boxes are only aligned towards {@link Direction#NORTH}
     */
    public Box getOriginalBox() {
        return originalBox;
    }

    /**
     * Default Boxes are only aligned towards {@link Direction#NORTH}
     */
    public Box getBox() {
        return box;
    }

    public Box getRotatedBox(Direction facing) {
        return ShapeUtil.rotateBox(getBox(), facing);
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
