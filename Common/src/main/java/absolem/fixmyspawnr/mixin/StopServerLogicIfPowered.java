package absolem.fixmyspawnr.mixin;

import absolem.fixmyspawnr.Config;
import absolem.fixmyspawnr.FixrGetr;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseSpawner.class)
public abstract class StopServerLogicIfPowered implements FixrGetr {

    private BaseSpawner self = (BaseSpawner) (Object) this;
    private int blockExistsTick = 0;
    private boolean blockLockedByTime = false;
    private boolean prevBlockLockedByTime = false;

    @Override
    public boolean getLocked() {
        return blockLockedByTime;
    }

    @Override
    public boolean getPrevLocked() {
        return prevBlockLockedByTime;
    }

    /**
     * isNearPlayer is called both client and serverside, so we dont have to mixin to both client and server tick.
     * when no player is near, spawners also dont do logic
     */
    @Inject(at = @At("HEAD"), method = "isNearPlayer", cancellable = true)
    public void stopLogicIfPowered(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (blockLockedByTime) {
            if (!prevBlockLockedByTime)
                prevBlockLockedByTime = true;
        } else {
            if (blockExistsTick > Config.timer_time_out) {
                blockLockedByTime = true;
            } else {
                blockExistsTick++;
            }
        }
        int signal = level.getBestNeighborSignal(pos);
        if (blockLockedByTime && signal == 0) {
            cir.setReturnValue(false);
        }
    }

    @Inject(at = @At("RETURN"), method = "save")
    public void addTickToSave(ValueOutput output, CallbackInfo ci) {
        output.putInt("fixmyspawnrTicks", blockExistsTick);
        output.putBoolean("fixMySpawnerLocked", blockLockedByTime);
    }

    @Inject(at = @At("HEAD"), method = "load")
    public void loadTickFromSave(Level level, BlockPos pos, ValueInput tag, CallbackInfo ci) {
        blockExistsTick = tag.getIntOr("fixmyspawnrTicks", 0);
        blockLockedByTime = tag.getBooleanOr("fixMySpawnerLocked", false);
    }
}
