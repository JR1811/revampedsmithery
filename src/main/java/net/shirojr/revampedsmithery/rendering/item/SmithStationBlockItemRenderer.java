package net.shirojr.revampedsmithery.rendering.item;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.shirojr.revampedsmithery.blockentity.client.SmithStationModel;
import net.shirojr.revampedsmithery.blockentity.client.SmithStationRenderer;
import net.shirojr.revampedsmithery.init.RevampedSmitheryModelLayers;
import org.jetbrains.annotations.Nullable;

public class SmithStationBlockItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    @Nullable
    private static BlockEntityRendererFactory.Context context = null;
    @Nullable
    private static SmithStationModel model = null;

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        this.initializeIfMissing();
        if (model == null) return;

        matrices.push();

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.getLayer(SmithStationRenderer.TEXTURE));
        model.render(matrices, vertexConsumer, light, overlay, 1f, 1f, 1f, 1f);

        matrices.pop();
    }

    private void initializeIfMissing() {
        if (context == null) {
            context = createMinimalContext();
            if (context == null) return;
        }
        if (model == null) {
            model = new SmithStationModel(context.getLayerModelPart(RevampedSmitheryModelLayers.SMITHING_STATION));
        }
    }

    @Nullable
    private static BlockEntityRendererFactory.Context createMinimalContext() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return null;
        return new BlockEntityRendererFactory.Context(client.getBlockEntityRenderDispatcher(), client.getBlockRenderManager(),
                client.getItemRenderer(), client.getEntityRenderDispatcher(), client.getEntityModelLoader(), client.textRenderer);
    }
}
