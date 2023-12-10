package io.github.ageuxo.TomteMod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class BaseTomte extends PathfinderMob {

    public BaseTomte(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes(){
        return PathfinderMob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 1.0D);
    }


}
