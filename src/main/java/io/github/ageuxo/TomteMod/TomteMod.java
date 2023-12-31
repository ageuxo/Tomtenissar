package io.github.ageuxo.TomteMod;

import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import io.github.ageuxo.TomteMod.block.entity.ModBlockEntities;
import io.github.ageuxo.TomteMod.block.entity.render.SimplePresentBERenderer;
import io.github.ageuxo.TomteMod.entity.ModEntities;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import io.github.ageuxo.TomteMod.entity.brain.ModSensors;
import io.github.ageuxo.TomteMod.entity.client.BaseTomteRenderer;
import io.github.ageuxo.TomteMod.entity.client.ModModelLayers;
import io.github.ageuxo.TomteMod.entity.client.TomteModel;
import io.github.ageuxo.TomteMod.gui.ModMenuTypes;
import io.github.ageuxo.TomteMod.gui.SimpleContainerScreen;
import io.github.ageuxo.TomteMod.item.ModItems;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
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
        ModMenuTypes.register(modEventBus);

        modEventBus.register(ModEvents.class);

        MinecraftForge.EVENT_BUS.register(ForgeEvents.class);
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

            event.enqueueWork( ()-> {
                MenuScreens.register(ModMenuTypes.PRESENT.get(), SimpleContainerScreen::new);
                MenuScreens.register(ModMenuTypes.WORK_STATION.get(), SimpleContainerScreen::new);
            });
        }

        @SubscribeEvent
        public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event){
//            event.registerBlockEntityRenderer(ModBlockEntities.PRESENT_TYPE.get(), context -> new PresentBERenderer(context));
            event.registerBlockEntityRenderer(ModBlockEntities.SIMPLE_PRESENT.get(), SimplePresentBERenderer::new);
        }

        @SubscribeEvent
        public static void registerAdditionalModels(ModelEvent.RegisterAdditional event){
            event.register(SimplePresentBERenderer.PRESENT_LID);
        }

    }
}
