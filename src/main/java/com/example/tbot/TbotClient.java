package com.example.tbot;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;

public class TbotClient implements ClientModInitializer {
    public static boolean enabled = false;
    public static boolean attackPlayers = true;
    public static boolean attackMobs = true;
    public static float minCooldown = 0.9f;

    private static KeyBinding toggleKey;
    private static KeyBinding guiKey;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tbot.toggle", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R, KeyBinding.Category.MISC));
        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tbot.gui", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT, KeyBinding.Category.MISC));

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        while (guiKey.wasPressed()) {
            client.setScreen(new TbotScreen());
        }
        while (toggleKey.wasPressed()) {
            enabled = !enabled;
            client.player.sendMessage(
                    Text.literal("Tbot: " + (enabled ? "ACTIVADO" : "DESACTIVADO")), true);
        }

        if (!enabled || client.currentScreen != null || client.interactionManager == null) return;

        if (client.crosshairTarget instanceof EntityHitResult hit) {
            Entity target = hit.getEntity();
            if (!isValidTarget(target)) return;
            if (client.player.getAttackCooldownProgress(0.5f) < minCooldown) return;

            client.interactionManager.attackEntity(client.player, target);
            client.player.swingHand(Hand.MAIN_HAND);
        }
    }

    private boolean isValidTarget(Entity e) {
        if (!(e instanceof LivingEntity) || !e.isAlive()) return false;
        if (e instanceof PlayerEntity) return attackPlayers;
        if (e instanceof MobEntity) return attackMobs;
        return false;
    }
}
