package net.shirojr.revampedsmithery.blockentity.data;

import net.minecraft.util.math.Box;
import org.joml.Vector3f;

public class PedalHitbox extends AbstractInteractionHitbox {
    public PedalHitbox(Box box, Vector3f debugColor) {
        super(box, debugColor);
    }
}
