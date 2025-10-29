package net.shirojr.revampedsmithery.init;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.shirojr.revampedsmithery.event.RevampedSmitheryBlockCallbacks;

public class RevampedSmitheryEvents {
    public static void initializeCommon() {
        AttackBlockCallback.EVENT.register(new RevampedSmitheryBlockCallbacks());
    }

    public static void initializeClient() {

    }
}
