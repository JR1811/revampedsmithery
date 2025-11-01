package net.shirojr.revampedsmithery.init;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.shirojr.revampedsmithery.event.RevampedSmitheryBlockCallbacks;
import net.shirojr.revampedsmithery.rendering.item.SmithStationBlockItemRenderer;

public class RevampedSmitheryEvents {
    public static void initializeCommon() {
        AttackBlockCallback.EVENT.register(new RevampedSmitheryBlockCallbacks());
    }

    public static void initializeClient() {
        BuiltinItemRendererRegistry.INSTANCE.register(RevampedSmitheryBlocks.SMITH_STATION.asItem(), new SmithStationBlockItemRenderer());
    }
}
