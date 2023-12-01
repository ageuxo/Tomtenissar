package io.github.ageuxo.Rimfrost;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Rimfrost.MODID)
public class Rimfrost
{
    public static final String MODID = "rimfrost";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Rimfrost()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


    }
}
