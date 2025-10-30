package net.shirojr.revampedsmithery.blockentity.data;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.shirojr.revampedsmithery.RevampedSmithery;
import org.joml.Vector3f;

public class RaspFileHitbox extends AbstractInteractionHitbox {
    public static final Identifier IDENTIFIER = RevampedSmithery.getId("rasp_file");

    public RaspFileHitbox(Box box, Vector3f debugColor) {
        super(box, debugColor);
    }

    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }
}
