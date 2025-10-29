package net.shirojr.revampedsmithery.blockentity.client;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.shirojr.revampedsmithery.RevampedSmithery;
import net.shirojr.revampedsmithery.block.SmithStationBlock;
import net.shirojr.revampedsmithery.blockentity.SmithStationBlockEntity;
import net.shirojr.revampedsmithery.init.RevampedSmitheryBlocks;
import net.shirojr.revampedsmithery.init.RevampedSmitheryModelLayers;
import net.shirojr.revampedsmithery.mixin.access.DebugRendererAccess;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Map;

public class SmithStationRenderer implements BlockEntityRenderer<SmithStationBlockEntity> {
    public static final Identifier TEXTURE = RevampedSmithery.getId("textures/entity/smith_station.png");
    private static final Map<String, Identifier> ARMOR_TEXTURE_CACHE = Maps.newHashMap();

    private final SmithStationModel baseModel;
    private final ArmorStandArmorEntityModel innerArmorModel;
    private final ArmorStandArmorEntityModel outerArmorModel;

    public SmithStationRenderer(BlockEntityRendererFactory.Context ctx) {
        this.baseModel = new SmithStationModel(ctx.getLayerModelPart(RevampedSmitheryModelLayers.SMITHING_STATION));

        this.innerArmorModel = new ArmorStandArmorEntityModel(ctx.getLayerModelPart(EntityModelLayers.ARMOR_STAND_INNER_ARMOR));
        this.outerArmorModel = new ArmorStandArmorEntityModel(ctx.getLayerModelPart(EntityModelLayers.ARMOR_STAND_OUTER_ARMOR));
    }

    @SuppressWarnings("unused")
    public SmithStationModel getBaseModel() {
        return baseModel;
    }

    @Override
    public void render(SmithStationBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null) return;
        if (((DebugRendererAccess) client.debugRenderer).showChunkBorder()) {
            renderInteractionBoxes(entity, matrices, vertexConsumers);
        }

        BlockState state = entity.getCachedState();
        ItemStack armorStack = entity.getData().getArmorStack();
        int blockLight = WorldRenderer.getLightmapCoordinates(client.world, entity.getPos().up(2));

        this.baseModel.getCoalModel().visible = !entity.getData().isFuelStackEmpty();

        matrices.push();

        matrices.translate(0.5, 1.5, 0.5);

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.get(SmithStationBlock.FACING).asRotation()));

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.baseModel.getLayer(TEXTURE));
        this.baseModel.render(matrices, vertexConsumer, blockLight, overlay, 1f, 1f, 1f, 1f);

        if (armorStack.getItem() instanceof ArmorItem armorItem) {
            this.renderArmor(matrices, vertexConsumers, blockLight, armorStack, armorItem, armorItem.getSlotType());
        }
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

    private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                             int light, ItemStack stack, ArmorItem armorItem,
                             EquipmentSlot slot) {
        float scale = 0.85f;
        boolean useInnerLayer = slot == EquipmentSlot.LEGS;
        ArmorStandArmorEntityModel armorModel = useInnerLayer ? innerArmorModel : outerArmorModel;

        matrices.push();

        armorModel.setVisible(false);
        armorModel.child = false;
        armorModel.sneaking = false;

        switch (slot) {
            case HEAD -> {
                matrices.translate(1.25, 0.6, 0.375);
                armorModel.head.visible = true;
            }
            case CHEST -> {
                matrices.translate(1.25, 0.2, 0.375);
                armorModel.body.visible = true;

                float modelSplit = 7f;
                armorModel.leftArm.pivotX = modelSplit;
                armorModel.rightArm.pivotX = -modelSplit;

                armorModel.leftArm.visible = true;
                armorModel.rightArm.visible = true;
            }
            case LEGS -> {
                matrices.translate(1.25, -0.2, 0.375);
                float modelSplit = 2.5f;
                armorModel.leftLeg.pivotX = modelSplit;
                armorModel.rightLeg.pivotX = -modelSplit;

                armorModel.body.visible = true;
                armorModel.leftLeg.visible = true;
                armorModel.rightLeg.visible = true;
            }
            case FEET -> {
                matrices.translate(1.25, -0.7, 0.375);
                float modelSplit = 3f;
                armorModel.leftLeg.pivotX = modelSplit;
                armorModel.rightLeg.pivotX = -modelSplit;
                armorModel.leftLeg.visible = true;
                armorModel.rightLeg.visible = true;
            }
        }

        matrices.scale(scale, scale, scale);

        if (armorItem instanceof DyeableArmorItem dyeableArmorItem) {
            int i = dyeableArmorItem.getColor(stack);
            float r = (i >> 16 & 0xFF) / 255.0F;
            float g = (i >> 8 & 0xFF) / 255.0F;
            float b = (i & 0xFF) / 255.0F;
            this.renderArmorParts(matrices, vertexConsumers, light, armorItem, armorModel, useInnerLayer, r, g, b, null);
            this.renderArmorParts(matrices, vertexConsumers, light, armorItem, armorModel, useInnerLayer, 1.0F, 1.0F, 1.0F, "overlay");
        } else {
            this.renderArmorParts(matrices, vertexConsumers, light, armorItem, armorModel, useInnerLayer, 1.0F, 1.0F, 1.0F, null);
        }
        matrices.pop();
    }

    private void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item,
                                  ArmorStandArmorEntityModel armorModel, boolean useInnerLayer,
                                  float red, float green, float blue, @Nullable String overlay) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(this.getArmorTexture(item, useInnerLayer, overlay)));
        armorModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
    }


    private Identifier getArmorTexture(ArmorItem item, boolean secondLayer, @Nullable String overlay) {
        String string = "textures/models/armor/" + item.getMaterial().getName() + "_layer_" + (secondLayer ? 2 : 1) + (overlay == null ? "" : "_" + overlay) + ".png";
        return ARMOR_TEXTURE_CACHE.computeIfAbsent(string, Identifier::new);
    }
}
