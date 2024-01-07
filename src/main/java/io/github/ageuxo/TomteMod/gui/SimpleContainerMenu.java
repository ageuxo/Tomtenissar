package io.github.ageuxo.TomteMod.gui;

import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.block.entity.SimpleContainerBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleContainerMenu<BE extends SimpleContainerBlockEntity> extends AbstractContainerMenu {
    public static final int SLOT_SIZE = 18;
    private static final Logger LOGGER = LogUtils.getLogger();

    protected BE blockEntity;
    protected Inventory inventory;
    protected Level level;
    protected Player player;
    protected final int rows;
    protected final int columns;
    protected List<SlotItemHandler> containerSlots = new ArrayList<>();
    protected int xOrig;
    protected int yOrig;
    protected int inventoryXOffset;
    protected int inventoryYOffset;
    protected int inventoryLabelY;

    public SimpleContainerMenu(MenuType<?> pMenuType, int pContainerId, Inventory inv, BE blockEntity, int rows, int columns) {
        super(pMenuType, pContainerId);
        this.inventory = inv;
        this.player = inv.player;
        this.level = player.level();
        this.blockEntity = blockEntity;
        this.rows = rows;
        this.columns = columns;
        this.TE_INVENTORY_SLOT_COUNT = rows * columns;

        this.blockEntity.getItemHandler().setSize(rows * columns);

        this.xOrig = ( (176/2) - (columns*SLOT_SIZE) / 2);
        this.yOrig = ( (176/4) - (rows*SLOT_SIZE) / 2);

        this.inventoryLabelY = yOrig + (rows * SLOT_SIZE) + 4;

        this.inventoryXOffset = 8;
        this.inventoryYOffset = inventoryLabelY + 10;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        addContainerSlots(rows, columns);
    }

    protected void addContainerSlots(int rows, int columns) {
        int slotIndex = 0;
        int x = this.xOrig;
        int y = yOrig;
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < columns; i++) {
                SlotItemHandler slot = new SlotItemHandler(blockEntity.getItemHandler(), slotIndex++, x, y);
                x += SLOT_SIZE;
                containerSlots.add(slot);
                this.addSlot(slot);
            }
            y += SLOT_SIZE;
            x = this.xOrig;
        }
    }

    protected void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 9; l++) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, this.inventoryXOffset + l * SLOT_SIZE, this.inventoryYOffset + i * SLOT_SIZE));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, this.inventoryXOffset + i * SLOT_SIZE, this.inventoryYOffset + (3 * SimpleContainerMenu.SLOT_SIZE) + 4));
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(this.level, this.blockEntity.getBlockPos()), pPlayer, blockEntity.getBlockState().getBlock());
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36+ = TileInventory slots, which map to our TileEntity slot numbers
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;

    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    // THIS YOU HAVE TO DEFINE!

    private final int TE_INVENTORY_SLOT_COUNT;  // must be the number of slots you have!

    @Override
    public @NotNull ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            LOGGER.error("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }
}
