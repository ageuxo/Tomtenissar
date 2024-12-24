package io.github.ageuxo.TomteMod.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ShearingStationScreen extends SimpleContainerScreen{
    public static final ResourceLocation SHEARS = ResourceLocation.withDefaultNamespace("textures/item/shears.png");

    public ShearingStationScreen(SimpleContainerMenu<?> pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void renderExtraSlot(GuiGraphics guiGraphics, Slot slot) {
        super.renderExtraSlot(guiGraphics, slot);
        guiGraphics.blit(RenderType.GUI_TEXTURED, SHEARS, slot.x + this.leftPos, slot.y + this.topPos, 0f, 0f, 16, 16, 16, 16, 0xAA000000);
    }
}
