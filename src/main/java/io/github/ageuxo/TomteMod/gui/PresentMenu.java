package io.github.ageuxo.TomteMod.gui;

import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.block.entity.SimplePresentBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class PresentMenu extends AbstractContainerMenu {
    Inventory inventory;
    Level level;
    Player player;
    SimplePresentBlockEntity present;

    private static final Logger LOGGER = LogUtils.getLogger();

    public PresentMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, (SimplePresentBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public PresentMenu(int id, Inventory inv, SimplePresentBlockEntity present){
        super(ModMenuTypes.PRESENT.get(), id);
        this.TE_INVENTORY_SLOT_COUNT = 9;
        this.inventory = inv;
        this.player = inv.player;
        this.level = player.level();
        this.present = present;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        addContainerSlots(TE_INVENTORY_SLOT_COUNT, 44, -1);

        present.startOpen(player);
    }

    protected void addContainerSlots(int count, int xOrig, int yOrig){
        int x = xOrig;
        int y = yOrig;
        int k = 0;
        for (int j = 0; j < count/3; j++) {
            int slotOffset = 18;
            y += slotOffset;
            this.addSlot(new SlotItemHandler(present.getItemHandler(), j, x+=slotOffset, y));
            this.addSlot(new SlotItemHandler(present.getItemHandler(), j, x+=slotOffset, y));
            this.addSlot(new SlotItemHandler(present.getItemHandler(), j, x+=slotOffset, y));
            x = xOrig;
        }
    }

    protected void addPlayerInventory(Inventory playerInventory){
        for (int i = 0; i<3;i++){
            for (int l = 0;l<9;l++){
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory){
        for (int i = 0;i<9;i++){
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(this.level, this.present.getBlockPos()), pPlayer, present.getBlockState().getBlock());
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.present.stopOpen(pPlayer);
    }

    public float getOpenness(float partialTick){
        return this.present.getOpenness(partialTick);
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
