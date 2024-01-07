package io.github.ageuxo.TomteMod.gui;

import io.github.ageuxo.TomteMod.TomteMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SimpleContainerScreen extends AbstractContainerScreen<SimpleContainerMenu<?>> {
    public static final ResourceLocation CONTAINER_PLATE = TomteMod.modRL("textures/gui/plate_sliced.png");
    public static final ResourceLocation SLOT_TEXTURE = TomteMod.modRL("textures/gui/slot.png");
    private int containerSlotX;
    private int containerSlotY;
    private int inventorySlotX;
    private int inventorySlotY;
    private int backplateX;
    private int backplateY;
    private int backplateEndX;
    private int backplateEndY;

    public SimpleContainerScreen(SimpleContainerMenu<?> pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 176;
    }

    @Override
    protected void init() {
        super.init();

        this.inventoryLabelX = this.menu.inventoryXOffset;
        this.inventoryLabelY = this.menu.inventoryLabelY;

        this.titleLabelX = this.menu.inventoryXOffset;
        this.titleLabelY = this.menu.yOrig - 12;

        this.containerSlotX = this.menu.xOrig + this.leftPos;
        this.containerSlotY = this.menu.yOrig + this.topPos;

        this.inventorySlotX = this.menu.inventoryXOffset + this.leftPos - 1;
        this.inventorySlotY = this.menu.inventoryYOffset + this.topPos - 1;

        this.backplateX = this.leftPos;
        this.backplateY = this.containerSlotY - 16;
        this.backplateEndX = inventorySlotX + (9 * SimpleContainerMenu.SLOT_SIZE) + 7;
        this.backplateEndY = inventorySlotY + (4 * SimpleContainerMenu.SLOT_SIZE) + 11;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blitNineSliced(CONTAINER_PLATE, backplateX, backplateY, backplateEndX - backplateX, backplateEndY - backplateY, 4, 4, 64, 64, 0, 0);
        if (this.menu.columns > 9){
            pGuiGraphics.blitNineSliced(CONTAINER_PLATE, containerSlotX - 4, containerSlotY - 4, SimpleContainerMenu.SLOT_SIZE * this.menu.columns + 8, SimpleContainerMenu.SLOT_SIZE + 7, 8, 8, 64, 64, 0, 0);
        }
        renderContainerSlots(pGuiGraphics);
        renderInventorySlots(pGuiGraphics);
    }

    public void renderContainerSlots(GuiGraphics guiGraphics){
        guiGraphics.blitRepeating(SLOT_TEXTURE, containerSlotX -1, containerSlotY -1,this.menu.columns * SimpleContainerMenu.SLOT_SIZE, this.menu.rows * SimpleContainerMenu.SLOT_SIZE, 0, 0, SimpleContainerMenu.SLOT_SIZE, SimpleContainerMenu.SLOT_SIZE, SimpleContainerMenu.SLOT_SIZE, SimpleContainerMenu.SLOT_SIZE);

    }

    public void renderInventorySlots(GuiGraphics guiGraphics){
        guiGraphics.blitRepeating(SLOT_TEXTURE, inventorySlotX, inventorySlotY,9 * SimpleContainerMenu.SLOT_SIZE, 3 * SimpleContainerMenu.SLOT_SIZE, 0, 0, SimpleContainerMenu.SLOT_SIZE, SimpleContainerMenu.SLOT_SIZE, SimpleContainerMenu.SLOT_SIZE, SimpleContainerMenu.SLOT_SIZE);
        guiGraphics.blitRepeating(SLOT_TEXTURE, inventorySlotX, inventorySlotY + (3 * SimpleContainerMenu.SLOT_SIZE) + 4,9 * SimpleContainerMenu.SLOT_SIZE, SimpleContainerMenu.SLOT_SIZE, 0, 0, SimpleContainerMenu.SLOT_SIZE, SimpleContainerMenu.SLOT_SIZE, SimpleContainerMenu.SLOT_SIZE, SimpleContainerMenu.SLOT_SIZE);
    }
}
