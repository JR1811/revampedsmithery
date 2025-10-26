package net.shirojr.revampedsmithery.blockentity.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class SmithStationModel extends Model {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart table;
    private final ModelPart tablePlate;
    private final ModelPart tableLegs;
    private final ModelPart underTable;
    private final ModelPart waterbucket;
    private final ModelPart bellows;
    private final ModelPart bellowsTread;
    private final ModelPart chain;
    private final ModelPart overTable;
    private final ModelPart oven;
    private final ModelPart coalPile;
    private final ModelPart coalStorage;
    private final ModelPart tools;
    private final ModelPart rack;
    private final ModelPart tongs;
    private final ModelPart hammer;
    private final ModelPart raspFile;
    private final ModelPart clamp;
    private final ModelPart closerClamp;
    private final ModelPart bulgingBall;
    private final ModelPart grindingPlate;

    public SmithStationModel(ModelPart root) {
        super(RenderLayer::getEntitySolid);
        this.root = root;
        this.body = root.getChild("body");
        this.table = this.body.getChild("table");
        this.tablePlate = this.table.getChild("tableplate");
        this.tableLegs = this.table.getChild("tablelegs");
        this.underTable = this.body.getChild("undertable");
        this.waterbucket = this.underTable.getChild("waterbucket");
        this.bellows = this.underTable.getChild("bellows");
        this.bellowsTread = this.bellows.getChild("bellowstread");
        this.chain = this.bellows.getChild("chain");
        this.overTable = this.body.getChild("overthetable");
        this.oven = this.overTable.getChild("Oven");
        this.coalPile = this.oven.getChild("coalpile");
        this.coalStorage = this.oven.getChild("coalstorage");
        this.tools = this.overTable.getChild("tools");
        this.rack = this.tools.getChild("rack");
        this.tongs = this.tools.getChild("tongs");
        this.hammer = this.tools.getChild("hammer");
        this.raspFile = this.tools.getChild("raspfile");
        this.clamp = this.overTable.getChild("clamp");
        this.closerClamp = this.clamp.getChild("closerclamp");
        this.bulgingBall = this.overTable.getChild("bulgingball");
        this.grindingPlate = this.overTable.getChild("grindingplate");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        ModelPartData table = body.addChild("table", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData tablePlate = table.addChild("tableplate", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -8.0F, -8.0F, 19.0F, 2.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData tableLegs = table.addChild("tablelegs", ModelPartBuilder.create().uv(8, 81).cuboid(8.0F, -7.1F, -7.0F, 2.0F, 15.0F, 2.0F, new Dilation(0.0F))
                .uv(48, 18).cuboid(-7.0F, -7.1F, -7.0F, 2.0F, 15.0F, 14.0F, new Dilation(0.0F))
                .uv(70, 10).cuboid(-5.0F, 5.0F, 5.0F, 13.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(70, 14).cuboid(-5.0F, 0.0F, 5.0F, 13.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(80, 37).cuboid(10.0F, -6.0F, 5.0F, 9.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(80, 33).cuboid(10.0F, 5.0F, 5.0F, 9.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(80, 29).cuboid(10.0F, 0.0F, 5.0F, 9.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 77).cuboid(-5.0F, -6.0F, 5.0F, 13.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(16, 81).cuboid(19.0F, -7.1F, 5.0F, 2.0F, 15.0F, 2.0F, new Dilation(0.0F))
                .uv(30, 62).cuboid(18.0F, 4.0F, 4.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(62, 81).cuboid(7.0F, 4.0F, 4.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(78, 81).cuboid(7.0F, 4.0F, -8.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(78, 47).cuboid(-8.0F, -6.0F, 4.0F, 4.0F, 14.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 81).cuboid(8.0F, -7.1F, 5.0F, 2.0F, 15.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData underTable = body.addChild("undertable", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData waterbucket = underTable.addChild("waterbucket", ModelPartBuilder.create().uv(48, 83).cuboid(2.0F, 3.0F, 4.0F, 5.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(34, 88).cuboid(2.0F, 3.0F, 0.0F, 5.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(50, 89).cuboid(6.0F, 3.0F, 1.0F, 1.0F, 5.0F, 3.0F, new Dilation(0.0F))
                .uv(58, 89).cuboid(2.0F, 3.0F, 1.0F, 1.0F, 5.0F, 3.0F, new Dilation(0.0F))
                .uv(66, 89).cuboid(3.0F, 7.0F, 1.0F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bellows = underTable.addChild("bellows", ModelPartBuilder.create().uv(30, 70).cuboid(-5.0F, 7.0F, -7.0F, 4.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bellowsTread = bellows.addChild("bellowstread", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r1 = bellowsTread.addChild("cube_r1", ModelPartBuilder.create().uv(88, 75).cuboid(-2.0F, -1.0F, 0.0F, 2.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 8.0F, -6.0F, 0.5672F, 0.0F, 0.0F));

        ModelPartData chain = bellows.addChild("chain", ModelPartBuilder.create().uv(46, 89).cuboid(-4.0F, -7.0F, -3.0F, 2.0F, 13.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData overTable = body.addChild("overthetable", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Oven = overTable.addChild("Oven", ModelPartBuilder.create().uv(48, 47).cuboid(-7.0F, -18.0F, -7.0F, 2.0F, 10.0F, 13.0F, new Dilation(0.0F))
                .uv(80, 18).cuboid(-5.0F, -18.0F, 5.0F, 10.0F, 10.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 54).cuboid(5.0F, -18.0F, -7.0F, 2.0F, 10.0F, 13.0F, new Dilation(0.0F))
                .uv(78, 67).cuboid(-4.0F, -19.0F, 5.0F, 8.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r2 = Oven.addChild("cube_r2", ModelPartBuilder.create().uv(0, 36).cuboid(0.0F, -2.0F, 0.0F, 8.0F, 2.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(-7.2F, -17.0F, -8.0F, 0.0F, 0.0F, -0.2618F));

        ModelPartData cube_r3 = Oven.addChild("cube_r3", ModelPartBuilder.create().uv(0, 18).cuboid(-8.8F, -2.0F, 0.01F, 8.0F, 2.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(8.0F, -16.8F, -8.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData coalPile = Oven.addChild("coalpile", ModelPartBuilder.create().uv(94, 48).cuboid(-5.0F, -10.0F, 3.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(94, 56).cuboid(-6.0F, -10.0F, 1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(78, 89).cuboid(3.0F, -10.0F, 2.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(88, 89).cuboid(-3.0F, -9.0F, 2.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(70, 0).cuboid(-4.0F, -8.0F, -5.0F, 8.0F, 0.0F, 10.0F, new Dilation(0.0F))
                .uv(94, 52).cuboid(1.0F, -9.0F, 3.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(88, 69).cuboid(4.0F, -9.0F, -1.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(80, 41).cuboid(-6.0F, -9.0F, -1.0F, 3.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData coalStorage = Oven.addChild("coalstorage", ModelPartBuilder.create().uv(62, 70).cuboid(-5.0F, -6.0F, -2.9F, 5.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData tools = overTable.addChild("tools", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData rack = tools.addChild("rack", ModelPartBuilder.create().uv(78, 65).cuboid(-6.0F, -15.0F, 5.5F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData tongs = tools.addChild("tongs", ModelPartBuilder.create().uv(94, 62).cuboid(-0.4F, -12.7F, 6.1F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r4 = tongs.addChild("cube_r4", ModelPartBuilder.create().uv(94, 80).cuboid(-1.5F, -2.0F, 0.0F, 0.5F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -14.4F, 6.0F, 0.0F, 0.0F, -0.2618F));

        ModelPartData cube_r5 = tongs.addChild("cube_r5", ModelPartBuilder.create().uv(86, 94).cuboid(1.0F, 0.0F, 0.0F, 0.5F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.9F, -10.8F, 6.0F, 0.0F, 0.0F, -0.2618F));

        ModelPartData cube_r6 = tongs.addChild("cube_r6", ModelPartBuilder.create().uv(94, 85).cuboid(-1.5F, 0.0F, 0.0F, 0.5F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.1F, -10.8F, 6.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData cube_r7 = tongs.addChild("cube_r7", ModelPartBuilder.create().uv(78, 94).cuboid(-1.5F, -2.0F, 0.0F, 0.5F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.1F, -13.7F, 6.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData hammer = tools.addChild("hammer", ModelPartBuilder.create().uv(74, 93).cuboid(3.0F, -15.0F, 5.7F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(94, 60).cuboid(2.0F, -16.0F, 6.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData raspFile = tools.addChild("raspfile", ModelPartBuilder.create().uv(42, 94).cuboid(-4.0F, -14.0F, 5.6F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(82, 94).cuboid(-4.0F, -16.0F, 5.8F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData clamp = overTable.addChild("clamp", ModelPartBuilder.create().uv(34, 83).cuboid(8.0F, -9.0F, 0.0F, 4.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(94, 41).cuboid(11.0F, -11.0F, 0.0F, 1.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(94, 46).cuboid(11.0F, -9.0F, 1.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(66, 93).cuboid(13.0F, -10.0F, 0.0F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData closerClamp = clamp.addChild("closerclamp", ModelPartBuilder.create().uv(34, 94).cuboid(9.0F, -11.0F, 0.0F, 1.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bulgingBall = overTable.addChild("bulgingball", ModelPartBuilder.create().uv(30, 54).cuboid(18.0F, -11.0F, 4.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData grindingPlate = overTable.addChild("grindingplate", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r8 = grindingPlate.addChild("cube_r8", ModelPartBuilder.create().uv(24, 83).cuboid(-2.0F, -5.0F, 0.0F, 1.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(11.0F, -9.5F, -6.0F, 0.0F, 0.0F, -0.7854F));
        return TexturedModelData.of(modelData, 128, 128);
    }


    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
