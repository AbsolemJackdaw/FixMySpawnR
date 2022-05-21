package absolem.fixmyspawnr;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.InteractionResult;

public class FixMySpawnR implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
        AutoConfig.getConfigHolder(ModConfig.class).registerSaveListener((configHolder, config) -> {
            CommonConfig.timer_time_out = config.timer_time_out;
            return InteractionResult.PASS;
        });
    }
}
