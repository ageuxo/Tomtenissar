package io.github.ageuxo.TomteMod.gui;

import io.github.ageuxo.TomteMod.block.entity.SimplePresentBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class PresentMenu extends SimpleContainerMenu<SimplePresentBlockEntity> {

    public PresentMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, (SimplePresentBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public PresentMenu(int id, Inventory inv, SimplePresentBlockEntity present){
        super(ModMenuTypes.PRESENT.get(), id, inv, present, 3, 3);

        present.startOpen(player);
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.blockEntity.stopOpen(pPlayer);
    }


}
