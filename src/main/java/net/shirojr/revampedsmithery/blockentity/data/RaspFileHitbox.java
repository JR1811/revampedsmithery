package net.shirojr.revampedsmithery.blockentity.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import net.shirojr.revampedsmithery.init.RevampedSmitherySounds;
import org.joml.Vector3f;

public class RaspFileHitbox extends AbstractInteractionHitbox {
    public static final Identifier IDENTIFIER = RevampedSmithery.getId("rasp_file");
    public static final String RASPED_NBT_KEY = "rasped";

    public RaspFileHitbox(Box box, Vector3f debugColor) {
        super(box, debugColor);
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public ActionResult interact(SmithStationBlockEntity blockEntity, Vec3d actualPos, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isEmpty()) {
            if (blockEntity.getWorld() instanceof ServerWorld serverWorld) {
                //TODO: implement item interaction

                float pitch = MathHelper.lerp(serverWorld.getRandom().nextFloat(), 0.9f, 1.0f);
                serverWorld.playSound(null, actualPos.x, actualPos.y, actualPos.z, RevampedSmitherySounds.RASP_FILE.getRight(), SoundCategory.BLOCKS, 1f, pitch);
            }
            return ActionResult.SUCCESS;
        }
        return super.interact(blockEntity, actualPos, player, hand);
    }
}
