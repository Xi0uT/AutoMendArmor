package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Priomend implements ModInitializer {
    public static final String MOD_ID = "priomend";
    public static final int TARGET_DURABILITY = 400;

    private static final Map<UUID, Boolean> ENABLED = new ConcurrentHashMap<>();

    public record TogglePayload(boolean enabled) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<TogglePayload> TYPE =
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MOD_ID, "toggle"));

        public static final PacketCodec<RegistryByteBuf, TogglePayload> CODEC =
                PacketCodec.tuple(ByteBufCodecs.BOOL, TogglePayload::enabled, TogglePayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(TogglePayload.TYPE, TogglePayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TogglePayload.TYPE, (payload, context) -> {
            ENABLED.put(context.player().getUUID(), payload.enabled());
        });

        ServerPlayConnectionEvents.DISCONNECT.register(
                (handler, server) -> ENABLED.remove(handler.player.getUUID())
        );
    }

    public static boolean isEnabled(ServerPlayer player) {
        return ENABLED.getOrDefault(player.getUUID(), false);
    }
}
