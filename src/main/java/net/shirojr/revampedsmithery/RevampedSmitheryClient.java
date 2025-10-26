package net.shirojr.revampedsmithery;

import net.fabricmc.api.ClientModInitializer;
import net.shirojr.revampedsmithery.init.RevampedSmitheryModelLayers;

public class RevampedSmitheryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RevampedSmitheryModelLayers.initialize();
    }
}
