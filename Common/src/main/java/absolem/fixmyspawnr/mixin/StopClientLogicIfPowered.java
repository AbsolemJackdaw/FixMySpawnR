package absolem.fixmyspawnr.mixin;

import absolem.fixmyspawnr.FixrGetr;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.blockentity.state.SpawnerRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpawnerRenderer.class)
public abstract class StopClientLogicIfPowered {

    @Inject(at = @At("HEAD"), method = "extractRenderState(Lnet/minecraft/world/level/block/entity/SpawnerBlockEntity;Lnet/minecraft/client/renderer/blockentity/state/SpawnerRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V", cancellable = true)
    public void stopRendering(SpawnerBlockEntity sbe, SpawnerRenderState p_447179_, float p_446518_, Vec3 p_446190_, ModelFeatureRenderer.CrumblingOverlay p_445699_, CallbackInfo ci) {
        var spawnr = sbe.getSpawner();
        if (spawnr instanceof FixrGetr fixr) {
            int signal = sbe.getLevel().getBestNeighborSignal(sbe.getBlockPos());
            if (fixr.getLocked() && signal == 0)
                ci.cancel();
        }
    }
}
