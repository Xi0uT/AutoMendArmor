package com.example.mixin;

import com.example.Priomend;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Comparator;

@Mixin(ExperienceOrb.class)
public class MendingPriorityMixin {
    @Inject(method = "repairPlayerGears", at = @At("HEAD"), cancellable = true)
    private void priomend$prioritizeLowest(
            ServerPlayer player,
            int amount,
            CallbackInfoReturnable<Integer> cir
    ) {
        if (!Priomend.isEnabled(player) || amount <= 0) {
            return;
        }

        Holder<Enchantment> mending = player.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.MENDING);

        Candidate candidate = List.of(
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        ).stream()
                .map(slot -> new Candidate(slot, player.getItemBySlot(slot)))
                .filter(c -> c.stack.isDamageableItem())
                .filter(c -> c.stack.getEnchantmentLevel(mending) > 0)
                .filter(c -> c.currentDurability() <
                        Math.min(Priomend.TARGET_DURABILITY, c.stack.getMaxDamage()))
                .min(Comparator.comparingInt(Candidate::currentDurability))
                .orElse(null);

        if (candidate == null) {
            return; // let vanilla Mending handle non-priority cases
        }

        int target = Math.min(Priomend.TARGET_DURABILITY, candidate.stack.getMaxDamage());
        int needed = target - candidate.currentDurability();

        // Vanilla Mending is 2 durability per XP.
        int repair = Math.min(needed, amount * 2);

        // Preserve vanilla XP rounding: each repair point costs ceil(repair / 2).
        int xpUsed = (repair + 1) / 2;

        candidate.stack.setDamageValue(candidate.stack.getDamageValue() - repair);

        cir.setReturnValue(amount - xpUsed);
    }

    private record Candidate(EquipmentSlot slot, ItemStack stack) {
        int currentDurability() {
            return stack.getMaxDamage() - stack.getDamageValue();
        }
    }
}
