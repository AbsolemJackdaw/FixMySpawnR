package absolem.fixmyspawnr.mixin;

import absolem.fixmyspawnr.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseSpawner.class)
public class StopServerLogicIfPowered {

    private int blockExistsTick = 0;
    private boolean blockLockedByTime = false;

    @Inject(at = @At("HEAD"), method = "serverTick", cancellable = true)
    public void stopLogicIfPowered(ServerLevel level, BlockPos pos, CallbackInfo ci) {

        if (!blockLockedByTime) {
            if (blockExistsTick > Config.timer_time_out) {
                blockLockedByTime = true;
            } else {
                blockExistsTick++;
            }
        }
        int signal = level.getBestNeighborSignal(pos);
        if (blockLockedByTime && signal == 0) {

            ci.cancel();
            return;
        }
    }

    @Inject(at = @At("RETURN"), method = "save")
    public void addTickToSave(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag ntag = new CompoundTag();
        ntag.putInt("fixmyspawnrTicks", blockExistsTick);
        ntag.putBoolean("fixMySpawnerLocked", blockLockedByTime);
        tag.put("FMS_data", ntag);
    }

    @Inject(at = @At("HEAD"), method = "load")
    public void loadTickFromSave(Level level, BlockPos pos, CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("FMS_data")) {
            blockLockedByTime = false;
            blockExistsTick = 0;
            CompoundTag ntag = tag.getCompound("FMS_data");
            if (ntag.contains("fixMySpawnerLocked"))
                blockLockedByTime = ntag.getBoolean("fixMySpawnerLocked");
            if (ntag.contains("fixmyspawnrTicks"))
                blockExistsTick = ntag.getInt("fixmyspawnrTicks");
        }
    }
}
