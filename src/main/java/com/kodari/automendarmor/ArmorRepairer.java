package com.kodari.automendarmor;

import java.util.Optional;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryEntry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class ArmorRepairer {
    private static final int TARGET_DURABILITY = 400;
    private static final Identifier MENDING_ID = Identifier.of("minecraft", "mending");

    private ArmorRepairer() {
    }

    public static boolean hasEligibleArmor(ServerPlayerEntity player) {
        RegistryEntry<Enchantment> mending = getMendingEntry(player);
        return mending != null && findLowestEligibleArmor(player, mending) != null;
    }

    public static int repair(ServerPlayerEntity player, int experience) {
        RegistryEntry<Enchantment> mending = getMendingEntry(player);
        if (mending == null) {
            return experience;
        }

        int remainingExperience = experience;
        while (remainingExperience > 0) {
            ItemStack armor = findLowestEligibleArmor(player, mending);
            if (armor == null) {
                break;
            }

            int currentDurability = getCurrentDurability(armor);
            int targetDurability = Math.min(TARGET_DURABILITY, armor.getMaxDamage());
            int durabilityNeeded = targetDurability - currentDurability;
            int durabilityToRepair = Math.min(2, Math.min(durabilityNeeded, armor.getDamage()));

            if (durabilityToRepair <= 0) {
                break;
            }

            armor.setDamage(armor.getDamage() - durabilityToRepair);
            remainingExperience--;
        }

        return remainingExperience;
    }

    private static ItemStack findLowestEligibleArmor(ServerPlayerEntity player, RegistryEntry<Enchantment> mending) {
        ItemStack lowest = null;
        int lowestDurability = Integer.MAX_VALUE;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmorSlot()) {
                continue;
            }

            ItemStack armor = player.getEquippedStack(slot);
            if (armor.isEmpty() || !armor.isDamaged()) {
                continue;
            }

            int currentDurability = getCurrentDurability(armor);
            int targetDurability = Math.min(TARGET_DURABILITY, armor.getMaxDamage());
            if (currentDurability >= targetDurability
                    || EnchantmentHelper.getLevel(mending, armor) <= 0
                    || currentDurability >= lowestDurability) {
                continue;
            }

            lowest = armor;
            lowestDurability = currentDurability;
        }

        return lowest;
    }

    private static int getCurrentDurability(ItemStack armor) {
        return armor.getMaxDamage() - armor.getDamage();
    }

    private static RegistryEntry<Enchantment> getMendingEntry(ServerPlayerEntity player) {
        Optional<? extends RegistryEntry<Enchantment>> entry = player.getEntityWorld()
                .getRegistryManager()
                .get(RegistryKeys.ENCHANTMENT)
                .getEntry(MENDING_ID);
        return entry.orElse(null);
    }
}