package io.github.ageuxo.TomteMod.block.entity.workstations;

import com.mojang.authlib.GameProfile;
import io.github.ageuxo.TomteMod.block.entity.ModBlockEntities;
import io.github.ageuxo.TomteMod.gui.ShearingWorkStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShearingWorkStationBE extends AbstractAnimalWorkStation<Sheep> {

    public static final int SHEARS_SLOT = 15;

    protected FakePlayer fakePlayer;

    public ShearingWorkStationBE(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SHEARING_STATION.get(), pPos, pBlockState, Sheep::readyForShearing, 3, 5, 1);
        this.wrappedHandler.setInsertFilter((slot, stack) -> {
            if (slot == SHEARS_SLOT){
                return stack.getItem() instanceof ShearsItem;
            } else {
                return stack.is(ItemTags.WOOL);
            }
        });
    }

    @Override
    public List<Sheep> getWorkableAnimals() {
        this.trimIdMap();
        return this.getOrFindAnimals(Sheep.class);
    }

    @Override
    public boolean canBeWorkedAt() {
        return this.wrappedHandler.getStackInSlot(SHEARS_SLOT).getItem() instanceof ShearsItem;
    }

    @Override
    public ItemStack getDisplayItem() {
        return Items.SHEARS.getDefaultInstance();
    }

    @Override
    public void doAction(Sheep sheep) {
        if (!this.level.isClientSide){
            ItemStack shearsStack = getShearsSlot();
            if (sheep.isShearable(shearsStack, this.level, this.worldPosition)){
                this.idToCooldownMap.put(sheep.getId(), sheep.level().getGameTime()); // Maybe switch to UUIDs instead?
                List<ItemStack> drops = sheep.onSheared(this.fakePlayer, shearsStack, this.level, BlockPos.containing(sheep.position()), shearsStack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE));
                for (ItemStack drop : drops){
                    ItemStack remainder = ItemHandlerHelper.insertItemStacked(this.wrappedHandler, drop, false);
                    if (!remainder.isEmpty()){ // Drop on ground if it doesn't fit into the inventory
                        sheep.spawnAtLocation(remainder);
                    }
                }
            }
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("tomtemod.gui.workstation.shearing");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ShearingWorkStationMenu(pContainerId, pPlayerInventory, this);
    }

    protected ItemStack getShearsSlot(){
        return this.wrappedHandler.getStackInSlot(SHEARS_SLOT);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!this.level.isClientSide){
            this.fakePlayer = new FakePlayer((ServerLevel) this.level, new GameProfile(null, "tomtemod:shearingworkstation"));
        }
    }
}
