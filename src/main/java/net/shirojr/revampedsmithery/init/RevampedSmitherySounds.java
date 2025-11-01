package net.shirojr.revampedsmithery.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.shirojr.revampedsmithery.RevampedSmithery;

@SuppressWarnings("SameParameterValue")
public interface RevampedSmitherySounds {
    Pair<Identifier, SoundEvent> RASP_FILE = register("rasp_file");

    private static Pair<Identifier, SoundEvent> register(String id) {
        Identifier identifier = RevampedSmithery.getId(id);
        SoundEvent sound = SoundEvent.of(identifier);
        return new Pair<>(identifier, Registry.register(Registries.SOUND_EVENT, identifier, sound));
    }

    static void initialize() {
        // static initialisation
    }
}
