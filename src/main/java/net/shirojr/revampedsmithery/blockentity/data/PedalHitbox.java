package net.shirojr.revampedsmithery.blockentity.data;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.shirojr.revampedsmithery.RevampedSmithery;
import org.joml.Vector3f;

public class PedalHitbox extends AbstractInteractionHitbox {
    public static final Identifier IDENTIFIER = RevampedSmithery.getId("pedal");

    public PedalHitbox(Box box, Vector3f debugColor) {
        super(box, debugColor);
    }

    @Override
    public Identifier getIdentifier() {
        return RevampedSmithery.getId("pedal");
    }
}
