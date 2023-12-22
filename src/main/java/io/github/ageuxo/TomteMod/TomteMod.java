package io.github.ageuxo.TomteMod;

import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import io.github.ageuxo.TomteMod.block.entity.ModBlockEntities;
import io.github.ageuxo.TomteMod.entity.ModEntities;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import io.github.ageuxo.TomteMod.entity.brain.ModSensors;
import io.github.ageuxo.TomteMod.entity.client.BaseTomteModel;
import io.github.ageuxo.TomteMod.entity.client.BaseTomteRenderer;
import io.github.ageuxo.TomteMod.entity.client.ModModelLayers;
import io.github.ageuxo.TomteMod.entity.client.TomteModel;
import io.github.ageuxo.TomteMod.item.ModItems;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(TomteMod.MODID)
public class TomteMod {
    public static final String MODID = "tomtemod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TomteMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModMemoryTypes.register(modEventBus);
        ModSensors.register(modEventBus);
        ModPoiTypes.register(modEventBus);

        modEventBus.register(ModEvents.class);

    }
    public static ResourceLocation modRL(String path){
        return new ResourceLocation(MODID, path);
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents{
        @SubscribeEvent
        public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
            event.registerLayerDefinition(ModModelLayers.TOMTE_LAYER, TomteModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event){
            EntityRenderers.register(ModEntities.TOMTE.get(), BaseTomteRenderer::new);
        }
    }
}
