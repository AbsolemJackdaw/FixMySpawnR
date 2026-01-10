package absolem.fixmyspawnr.mixin;

import absolem.fixmyspawnr.FixrGetr;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpawnerBlockEntity.class)
public class UpdateChangedIfPowered {

    @Shadow
    @Final
    private BaseSpawner spawner;

    @Inject(at = @At("HEAD"), method = "serverTick")
    private static void sync(Level level, BlockPos pos, BlockState state, SpawnerBlockEntity blockEntity, CallbackInfo ci) {
        var spawnr = blockEntity.getSpawner();
        if (spawnr instanceof FixrGetr fixr) {
            if (fixr.getLocked() && !fixr.getPrevLocked())
                blockEntity.setChanged();
        }
    }
}
