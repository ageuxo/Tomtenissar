package io.github.ageuxo.TomteMod;

import io.github.ageuxo.TomteMod.entity.BaseTomte;
import io.github.ageuxo.TomteMod.entity.ModEntities;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEvents {
    @SubscribeEvent
    public static void sleepEvent(SleepFinishedTimeEvent event){
        LevelAccessor level = event.getLevel();
        var sleepingPlayers = level.players().stream().filter(Player::isSleepingLongEnough).toList();
        Player player = sleepingPlayers.get(level.getRandom().nextInt(sleepingPlayers.size()));

        if (level.getEntitiesOfClass(BaseTomte.class, player.getBoundingBox().inflate(16)).isEmpty()){
            BaseTomte tomte = new BaseTomte(ModEntities.TOMTE.get(), player.level());
            ForgeEventFactory.onFinalizeSpawn(tomte, (ServerLevelAccessor) level, level.getCurrentDifficultyAt(player.blockPosition()), MobSpawnType.EVENT, null, null);
            level.addFreshEntity(tomte);
            tomte.setPos(player.position());
        }
    }
}
