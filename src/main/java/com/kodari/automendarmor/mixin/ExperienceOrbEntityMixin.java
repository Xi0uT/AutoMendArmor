package com.kodari.automendarmor.mixin;

import com.kodari.automendarmor.ArmorRepairer;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {
    @Inject(
            method = "repairPlayerGears(Lnet/minecraft/server/network/ServerPlayerEntity;I)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void repairArmorFirst(
            ServerPlayerEntity player,
            int experience,
            CallbackInfoReturnable<Integer> cir
    ) {
        if (experience > 0 && ArmorRepairer.hasEligibleArmor(player)) {
            cir.setReturnValue(ArmorRepairer.repair(player, experience));
        }
    }
}