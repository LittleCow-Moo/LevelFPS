package xyz.cowgl.levelfps.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;

import java.lang.reflect.Field;

public class LevelFPSClient implements ClientModInitializer {
    private static final String SERVER_HOST = "smp.cowgl.xyz".toLowerCase(); // Server hostname or IP address (converted to lowercase)
    private int currentFPSLimit = 60; // Default FPS limit

    @Override
    public void onInitializeClient() {
        System.out.println("Hi there, LevelFPSClient mod has been initialized.");
        // Register tick event handler
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(MinecraftClient client) {
        if (isOnAllowedServer(client)) {
            // Access player level and calculate target FPS
            int playerLevel = getPlayerLevel(client);
            int targetFPS = playerLevel + 1;

            // Update FPS limit if it has changed
            if (targetFPS != currentFPSLimit) {
                System.out.println("Hi there, I'm trying to change your FPS limit.");
                setFPSLimit(targetFPS);
                currentFPSLimit = targetFPS;
            }
        }
    }

    // Method to check if the client is connected to the allowed server
    private boolean isOnAllowedServer(MinecraftClient client) {
        boolean connectedToServer = client.getCurrentServerEntry() != null && client.getCurrentServerEntry().address.toLowerCase().contains(SERVER_HOST);
        if (connectedToServer) {
            System.out.println("Hi there, you are connected to the allowed server: " + SERVER_HOST);
        } else {
            System.out.println("Hi there, you are not connected to the allowed server: " + SERVER_HOST);
        }
        return connectedToServer;
    }

    // Method to retrieve player level from the game
    private int getPlayerLevel(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player != null) {
            // You may need to adjust this depending on how player levels are stored in your modded environment
            // For instance, you may need to access a player's experience or attributes to determine their level.
            return player.experienceLevel;
        }
        return 1; // Default value
    }

    // Method to set FPS limit in the game
    private void setFPSLimit(int fpsLimit) {
        System.out.println("Hi there, I'm setting FPS limit to: " + fpsLimit);
        try {
            Field maxFpsField = GameOptions.class.getDeclaredField("maxFps");
            maxFpsField.setAccessible(true);
            maxFpsField.setInt(MinecraftClient.getInstance().options, fpsLimit);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
