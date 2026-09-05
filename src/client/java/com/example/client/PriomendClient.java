package com.example.client;

import com.example.Priomend;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class PriomendClient implements ClientModInitializer {
    private static KeyMapping toggleKey;
    private static boolean enabled = false;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.priomend.toggle",
                GLFW.GLFW_KEY_G,
                "key.categories.priomend"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(PriomendClient::tick);
    }

    private static void tick(Minecraft client) {
        while (toggleKey.consumeClick()) {
            enabled = !enabled;

            if (ClientPlayNetworking.canSend(Priomend.TogglePayload.TYPE)) {
                ClientPlayNetworking.send(new Priomend.TogglePayload(enabled));
            }

            if (client.player != null) {
                client.player.displayClientMessage(
                        Component.literal("Mending Priority: " + (enabled ? "ON" : "OFF")),
                        true
                );
            }
        }
    }
}
