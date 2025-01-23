package io.github.ageuxo.TomteMod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class DescriptionItem extends Item {
    private final List<Component> lines;

    public DescriptionItem(Properties properties, Component... lines) {
        super(properties);
        this.lines = List.of(lines);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.addAll(this.lines);
    }
}
