package io.github.ageuxo.TomteMod.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class SteamParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    public SteamParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.spriteSet = spriteSet;
        this.gravity = 0f;

        this.setSpriteFromAge(spriteSet);
        this.lifetime = 100;
        setParticleSpeed(0, 0.001, 0);
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(this.spriteSet);
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime + 5) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            setAlpha(Math.min(1f, 1.2f - ((float) age / lifetime)));
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
