package io.github.ageuxo.TomteMod;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(TomteMod.MODID)
public class TomteMod
{
    public static final String MODID = "rimfrost";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TomteMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


    }
}
