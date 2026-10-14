package com.example.tbot;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class TbotScreen extends Screen {
    public TbotScreen() {
        super(Text.literal("Tbot"));
    }

    private static Text label(String name, boolean on) {
        return Text.literal(name + ": " + (on ? "SÍ" : "NO"));
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 100;
        int y = this.height / 4;

        addDrawableChild(ButtonWidget.builder(label("Activado", TbotClient.enabled), b -> {
            TbotClient.enabled = !TbotClient.enabled;
            b.setMessage(label("Activado", TbotClient.enabled));
        }).dimensions(x, y, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(label("Atacar jugadores", TbotClient.attackPlayers), b -> {
            TbotClient.attackPlayers = !TbotClient.attackPlayers;
            b.setMessage(label("Atacar jugadores", TbotClient.attackPlayers));
        }).dimensions(x, y + 24, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(label("Atacar mobs", TbotClient.attackMobs), b -> {
            TbotClient.attackMobs = !TbotClient.attackMobs;
            b.setMessage(label("Atacar mobs", TbotClient.attackMobs));
        }).dimensions(x, y + 48, 200, 20).build());

        addDrawableChild(new SliderWidget(x, y + 72, 200, 20,
                Text.literal("Cooldown mínimo: " + Math.round(TbotClient.minCooldown * 100) + "%"),
                TbotClient.minCooldown) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Cooldown mínimo: " + Math.round(this.value * 100) + "%"));
            }

            @Override
            protected void applyValue() {
                TbotClient.minCooldown = (float) this.value;
            }
        });

        addDrawableChild(ButtonWidget.builder(Text.literal("Cerrar"), b -> close())
                .dimensions(x, y + 104, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title,
                this.width / 2, this.height / 4 - 20, 0xFFFFFFFF);
    }
                  }
