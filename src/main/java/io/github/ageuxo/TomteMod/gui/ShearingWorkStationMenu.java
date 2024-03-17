package io.github.ageuxo.TomteMod.gui;

import io.github.ageuxo.TomteMod.block.entity.workstations.ShearingWorkStationBE;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.SlotItemHandler;

public class ShearingWorkStationMenu extends SimpleContainerMenu<ShearingWorkStationBE>{

    public ShearingWorkStationMenu(int pContainerId, Inventory inv, ShearingWorkStationBE blockEntity) {
        super(ModMenuTypes.SHEARING_STATION.get(), pContainerId, inv, blockEntity, blockEntity.type.rows, blockEntity.type.columns);
        addExtraSlots(
                new SlotItemHandler(this.blockEntity.getItemHandler(), ShearingWorkStationBE.SHEARS_SLOT, 0, 0)
        );
    }

    public ShearingWorkStationMenu(int id, Inventory inventory, FriendlyByteBuf buf){
        this(id, inventory, (ShearingWorkStationBE) inventory.player.level().getBlockEntity(buf.readBlockPos()));
    }
}
