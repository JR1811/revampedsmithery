package net.shirojr.revampedsmithery.blockentity.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import net.shirojr.revampedsmithery.init.RevampedSmitheryModelLayers;

public class SmithStationRenderer implements BlockEntityRenderer<SmithStationBlockEntity> {
    public static final Identifier TEXTURE = RevampedSmithery.getId("textures/entity/smith_station.png");

    private final SmithStationModel model;

    public SmithStationRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new SmithStationModel(ctx.getLayerModelPart(RevampedSmitheryModelLayers.SMITHING_STATION));
    }

    public SmithStationModel getModel() {
        return model;
    }

    @Override
    public void render(SmithStationBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState state = entity.getCachedState();

        matrices.push();

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(TEXTURE));
        this.model.render(matrices, vertexConsumer, light, overlay, 1f, 1f, 1f, 1f);

        matrices.pop();
    }
}
