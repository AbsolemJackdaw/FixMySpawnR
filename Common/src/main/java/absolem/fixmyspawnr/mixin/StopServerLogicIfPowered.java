package absolem.fixmyspawnr.mixin;

import absolem.fixmyspawnr.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(BaseSpawner.class)
public class StopServerLogicIfPowered {

    private int blockExistsTick = 0;
    private boolean blockLockedByTime = false;

    @Inject(at = @At("HEAD"), method = "serverTick", cancellable = true)
    public void stopLogicIfPowered(ServerLevel level, BlockPos pos, CallbackInfo ci) {

        if (!blockLockedByTime) {
            if (blockExistsTick > CommonConfig.timer_time_out) {
                blockLockedByTime = true;
            } else {
                blockExistsTick++;
            }
        }
        int signal = level.getBestNeighborSignal(pos);
        if (blockLockedByTime && signal > 0) {
            ci.cancel();
            return;
        }
    }

    @Inject(at = @At("TAIL"), method = "save")
    public void addTickToSave(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        tag.putInt("fixmyspawnrTicks", blockExistsTick);
        tag.putBoolean("fixMySpawnerLocked", blockLockedByTime);
    }

    @Inject(at = @At("TAIL"), method = "load")
    public void loadTickFromSave(Level level, BlockPos pos, CompoundTag tag, CallbackInfo ci) {
        blockExistsTick = tag.getInt("fixmyspawnrTicks");
        blockLockedByTime = tag.getBoolean("fixMySpawnerLocked");
    }
}
