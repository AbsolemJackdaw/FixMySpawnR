package absolem.fixmyspawnr;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigData {

    public static final ServerConfig SERVER;
    public static final ModConfigSpec SERVER_SPEC;

    static {
        final Pair<ServerConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static void refreshServer() {

        CommonConfig.timer_time_out = SERVER.timer_time_out.get();

    }

    public static class ServerConfig {

        public final ModConfigSpec.IntValue timer_time_out;

        ServerConfig(ModConfigSpec.Builder builder) {

            builder.push("general");
            timer_time_out = builder.
                    comment("Mobspawner timer before being locked. time in ticks. use redstone signal on top of block to circumvent lock.").
                    defineInRange("spawner_lock_after", 24000, 1, Integer.MAX_VALUE);
            builder.pop();
        }
    }
}
