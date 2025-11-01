package net.shirojr.revampedsmithery.blockentity.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import org.joml.Vector3f;

public class ClampHitbox extends AbstractInteractionHitbox {
    public static final Identifier IDENTIFIER = RevampedSmithery.getId("clamp");

    public ClampHitbox(Box box, Vector3f debugColor) {
        super(box, debugColor);
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public ActionResult interact(SmithStationBlockEntity blockEntity, Vec3d actualPos, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (blockEntity.getData().isClampStackEmpty() && !stack.isEmpty()) {

        }
        return super.interact(blockEntity, actualPos, player, hand);
    }
}
