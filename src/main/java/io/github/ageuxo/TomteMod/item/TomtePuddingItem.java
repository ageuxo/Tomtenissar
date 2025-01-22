package io.github.ageuxo.TomteMod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class TomtePuddingItem extends BlockItem {

    public TomtePuddingItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getDescription() {
        return Component.translatable("info.tomtemod.pudding.desc").withStyle(ChatFormatting.GRAY);
    }
}
