package net.shirojr.revampedsmithery.init;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.blockentity.client.SmithStationModel;

public interface RevampedSmitheryModelLayers {
    EntityModelLayer SMITHING_STATION = register("smithing_station", SmithStationModel.getTexturedModelData());


    @SuppressWarnings("SameParameterValue")
    private static EntityModelLayer register(String name, TexturedModelData data) {
        EntityModelLayer layer = new EntityModelLayer(RevampedSmithery.getId(name), "main");
        EntityModelLayerRegistry.registerModelLayer(layer, () -> data);
        return layer;
    }

    static void initialize() {
        // static initialisation
    }
}
