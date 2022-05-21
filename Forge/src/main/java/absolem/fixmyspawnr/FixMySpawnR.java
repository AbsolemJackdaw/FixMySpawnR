package absolem.fixmyspawnr;

import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;

@Mod("fixmyspawnr")
public class FixMySpawnR {

    public FixMySpawnR() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (ver, remote) -> true));

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::modConfig);
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, ConfigData.SERVER_SPEC);
    }

    public void modConfig(ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == ConfigData.SERVER_SPEC)
            ConfigData.refreshServer();
    }
}
