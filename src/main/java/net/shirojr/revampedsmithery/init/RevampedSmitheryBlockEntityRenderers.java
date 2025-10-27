package net.shirojr.revampedsmithery.init;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.shirojr.revampedsmithery.blockentity.client.SmithStationRenderer;

public class RevampedSmitheryBlockEntityRenderers {
    static {
        register(RevampedSmitheryBlockEntities.SMITH_STATION, SmithStationRenderer::new);
    }


    @SuppressWarnings("SameParameterValue")
    private static <T extends BlockEntity> void register(BlockEntityType<T> entry, BlockEntityRendererFactory<T> renderer) {
        BlockEntityRendererFactories.register(entry, renderer);
    }

    public static void initialize() {
        // static initialisation
    }
}
