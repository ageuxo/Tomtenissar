package io.github.ageuxo.TomteMod.entity.client;// Made with Blockbench 4.9.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ageuxo.TomteMod.entity.BaseTomte;
import io.github.ageuxo.TomteMod.entity.animation.TomteAnimationDefinitions;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class TomteModel<T extends BaseTomte> extends HierarchicalModel<T> {
	private final ModelPart main;
/*	private final ModelPart body;
	private final ModelPart hat;
	private final ModelPart beard;
	private final ModelPart head;

	private final ModelPart ears;
	private final ModelPart earLeft;
	private final ModelPart earRight;

	private final ModelPart legs;
	private final ModelPart legLeft;
	private final ModelPart legRight;

	private final ModelPart arms;
	private final ModelPart armLeft;
	private final ModelPart armRight;*/


	public TomteModel(ModelPart root) {
		this.main = root.getChild("main");
		/*this.body = main.getChild("body");
		this.head = body.getChild("head");
		this.hat = head.getChild("hat");
		this.beard = head.getChild("beard");

		this.ears = head.getChild("ears");
		this.earLeft = ears.getChild("earLeft");
		this.earRight = ears.getChild("earRight");

		this.legs = body.getChild("legs");
		this.legLeft = legs.getChild("legLeft");
		this.legRight = legs.getChild("legRight");

		this.arms = body.getChild("arms");
		this.armLeft = arms.getChild("armLeft");
		this.armRight = arms.getChild("armRight");*/
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 1.0F));

		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create()/*.texOffs(17, 0).addBox(-1.0F, -11.0F, -5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 10).addBox(-2.0F, -12.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))*/, PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(20, 10).addBox(-2.0F, -12.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(17, 0).addBox(-1.0F, -11.0F, -5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition beard = head.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -12.0F, -3.5F, 6.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(24, 29).addBox(-1.5F, -8.0F, -2.25F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(22, 0).addBox(-2.5F, -5.0F, -3.25F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 14).addBox(-3.5F, -2.0F, -4.25F, 7.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -11.25F, 0.5F, -0.3927F, 0.0F, 0.0F));

		PartDefinition ears = head.addOrReplaceChild("ears", CubeListBuilder.create(), PartPose.offset(-0.25F, -8.0F, -1.0F));

		PartDefinition earLeft = ears.addOrReplaceChild("earLeft", CubeListBuilder.create().texOffs(0, 23).mirror().addBox(-1.0F, -8.0F, 0.0F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition earRight = ears.addOrReplaceChild("earRight", CubeListBuilder.create().texOffs(0, 23).addBox(-6.0F, -8.0F, 0.0F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition legs = body.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(1.0F, -6.0F, -1.0F));

		PartDefinition legLeft = legs.addOrReplaceChild("legLeft", CubeListBuilder.create().texOffs(14, 23).mirror().addBox(-1.0F, 0.0F, -1.5F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition legRight = legs.addOrReplaceChild("legRight", CubeListBuilder.create().texOffs(14, 23).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

		PartDefinition arms = body.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(-2.0F, -9.0F, -1.0F));

		PartDefinition armRight = arms.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(24, 21).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition armLeft = arms.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(24, 21).mirror().addBox(0.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.animateWalk(TomteAnimationDefinitions.WALK_ANIM, limbSwing, limbSwingAmount, 2.0F, 2.5F);
		this.animate(entity.idleAnimationState, TomteAnimationDefinitions.IDLE_ANIM, ageInTicks, 1.0F);
		this.animate(entity.stealAnimationState, TomteAnimationDefinitions.STEAL_ANIM, ageInTicks, 1.0F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.main;
	}
}