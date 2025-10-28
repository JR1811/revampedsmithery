package net.shirojr.revampedsmithery.blockentity.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.block.SmithStationBlock;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import net.shirojr.revampedsmithery.init.RevampedSmitheryBlocks;
import net.shirojr.revampedsmithery.init.RevampedSmitheryModelLayers;
import net.shirojr.revampedsmithery.mixin.access.DebugRendererAccess;
import org.joml.Vector3f;

public class SmithStationRenderer implements BlockEntityRenderer<SmithStationBlockEntity> {
    public static final Identifier TEXTURE = RevampedSmithery.getId("textures/entity/smith_station.png");

    private final SmithStationModel model;

    public SmithStationRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new SmithStationModel(ctx.getLayerModelPart(RevampedSmitheryModelLayers.SMITHING_STATION));
    }

    @SuppressWarnings("unused")
    public SmithStationModel getModel() {
        return model;
    }

    @Override
    public void render(SmithStationBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null) return;
        if (((DebugRendererAccess) client.debugRenderer).showChunkBorder()) {
            renderInteractionBoxes(entity, matrices, vertexConsumers);
        }

        BlockState state = entity.getCachedState();
        int blockLight = WorldRenderer.getLightmapCoordinates(client.world, entity.getPos().up(2));

        matrices.push();

        matrices.translate(0.5, 1.5, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.get(SmithStationBlock.FACING).asRotation()));

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(TEXTURE));
        this.model.render(matrices, vertexConsumer, blockLight, overlay, 1f, 1f, 1f, 1f);
        matrices.pop();
    }

    private void renderInteractionBoxes(SmithStationBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        BlockState state = entity.getCachedState();
        if (!state.isOf(RevampedSmitheryBlocks.SMITH_STATION)) return;
        Direction facing = state.get(SmithStationBlock.FACING);
        entity.getHitBoxes().forEach((hitBox) -> {
            Vector3f color = hitBox.getDebugColor();
            WorldRenderer.drawBox(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), hitBox.getRotatedBox(facing),
                    color.x, color.y, color.z, 1f);
        });
    }
}
