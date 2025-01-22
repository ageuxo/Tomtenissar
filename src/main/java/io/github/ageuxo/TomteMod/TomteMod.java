package io.github.ageuxo.TomteMod;

import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import io.github.ageuxo.TomteMod.block.entity.ModBlockEntities;
import io.github.ageuxo.TomteMod.block.entity.render.AnimalWorkStationItemRenderer;
import io.github.ageuxo.TomteMod.block.entity.render.AnimalWorkStationRenderer;
import io.github.ageuxo.TomteMod.client.SteamParticleProvider;
import io.github.ageuxo.TomteMod.entity.ModEntities;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import io.github.ageuxo.TomteMod.entity.brain.ModSensors;
import io.github.ageuxo.TomteMod.entity.client.BaseTomteRenderer;
import io.github.ageuxo.TomteMod.entity.client.ModModelLayers;
import io.github.ageuxo.TomteMod.entity.client.TomteModel;
import io.github.ageuxo.TomteMod.gui.ModMenuTypes;
import io.github.ageuxo.TomteMod.gui.ShearingStationScreen;
import io.github.ageuxo.TomteMod.gui.SimpleContainerScreen;
import io.github.ageuxo.TomteMod.item.ModCreativeTabs;
import io.github.ageuxo.TomteMod.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.slf4j.Logger;

@Mod(TomteMod.MODID)
public class TomteMod {
    public static final String MODID = "tomtemod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TomteMod(IEventBus eventBus) {
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModEntities.register(eventBus);
        ModMemoryTypes.register(eventBus);
        ModSensors.register(eventBus);
        ModPoiTypes.register(eventBus);
        ModMenuTypes.register(eventBus);
        ModCreativeTabs.register(eventBus);
        ModParticles.register(eventBus);

        eventBus.register(ModEvents.class);
        NeoForge.EVENT_BUS.register(ForgeEvents.class);

        NeoForgeMod.enableMilkFluid();
    }

    public static ResourceLocation modRL(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    @EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents{
        @SubscribeEvent
        public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
            event.registerLayerDefinition(ModModelLayers.TOMTE_LAYER, TomteModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
            event.registerBlockEntityRenderer(ModBlockEntities.MILKING_STATION.get(), AnimalWorkStationRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.SHEARING_STATION.get(), AnimalWorkStationRenderer::new);
            event.registerEntityRenderer(ModEntities.TOMTE.get(), BaseTomteRenderer::new);
        }

        @SubscribeEvent
        public static void registerMenuScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.WORK_STATION.get(), SimpleContainerScreen::new);
            event.register(ModMenuTypes.SHEARING_STATION.get(), ShearingStationScreen::new);
        }

        public static final IClientItemExtensions WORK_STATION_EXTENSIONS = new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return AnimalWorkStationItemRenderer.INSTANCE;
            }
        };

        @SubscribeEvent
        public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
            event.registerItem(WORK_STATION_EXTENSIONS, ModBlocks.MILKING_WORK_STATION.asItem(), ModBlocks.SHEARING_WORK_STATION.asItem());
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event){
            EntityRenderers.register(ModEntities.TOMTE.get(), BaseTomteRenderer::new);

            Minecraft minecraft = Minecraft.getInstance();
            AnimalWorkStationItemRenderer.INSTANCE = new AnimalWorkStationItemRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        }

        @SubscribeEvent
        public static void particleProviders(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.STEAM.get(), SteamParticleProvider::new);
        }

    }
}
