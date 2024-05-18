package xyz.nucleoid.stimuli.mixin.world;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.nucleoid.stimuli.Stimuli;
import xyz.nucleoid.stimuli.event.block.FluidRandomTickEvent;

@Mixin(FluidState.class)
public class FluidStateMixin {
    @WrapWithCondition(method = "onRandomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/Fluid;onRandomTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/fluid/FluidState;Lnet/minecraft/util/math/random/Random;)V"))
    private boolean applyFluidRandomTickEvent(Fluid fluid, World world, BlockPos pos, FluidState state, Random random) {
        ServerWorld serverWorld = (ServerWorld) world;

        try (var invokers = Stimuli.select().at(world, pos)) {
            var result = invokers.get(FluidRandomTickEvent.EVENT).onFluidRandomTick(serverWorld, pos, state);
            if (result == ActionResult.FAIL) {
                return false;
            }
        }

        return true;
    }
}
