package io.github.ageuxo.TomteMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModLootTables {

    public static final ResourceKey<LootTable> MINESHAFT_ADDITIONS =
            ResourceKey.create(Registries.LOOT_TABLE, TomteMod.modRL("glm/mineshaft_additions"));

}
