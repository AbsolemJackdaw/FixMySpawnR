package absolem.fixmyspawnr;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;

@Mod(Constants.MODID)
@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD)
public class FixMySpawnR {
    public FixMySpawnR(IEventBus modBus, ModContainer container) {
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.SERVER, ModConfig.SERVER_SPEC);
    }

    @SubscribeEvent
    public static void configLoad(ModConfigEvent.Loading event) {
        reloadConfig(event);
    }

    @SubscribeEvent
    public static void configReload(ModConfigEvent.Reloading event) {
        reloadConfig(event);
    }

    private static void reloadConfig(ModConfigEvent event) {
        net.neoforged.fml.config.ModConfig config = event.getConfig();
        if (config.getSpec() == ModConfig.SERVER_SPEC && ModConfig.SERVER != null)
            ModConfig.refreshServer();
    }
}
