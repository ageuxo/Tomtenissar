package io.github.ageuxo.TomteMod.block.entity.workstations;

import io.github.ageuxo.TomteMod.block.entity.SimpleContainerBlockEntity;
import io.github.ageuxo.TomteMod.item.ItemHandlerWrapper;
import it.unimi.dsi.fastutil.ints.Int2LongArrayMap;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractAnimalWorkStation<A extends Animal> extends SimpleContainerBlockEntity {

    protected Int2LongArrayMap idToCooldownMap = new Int2LongArrayMap();
    protected List<A> animalCache;
    protected ItemHandlerWrapper wrappedHandler;
    protected long lastCheck;
    protected Predicate<? super A> predicate;

    public AbstractAnimalWorkStation(BlockEntityType<?> blockEntityType, BlockPos pPos, BlockState pBlockState, Predicate<? super A> predicate, int rows, int columns, int extraSlots) {
        super(blockEntityType, pPos, pBlockState, rows, columns, extraSlots);
        this.predicate = predicate;
        this.wrappedHandler = new ItemHandlerWrapper(this.itemHandler);
    }

    public abstract void doAction(A animal);

    protected void trimIdMap(){
        IntArrayList ids = new IntArrayList();
        for (Int2LongMap.Entry entry : this.idToCooldownMap.int2LongEntrySet()){
            //noinspection DataFlowIssue
            if (entry.getLongValue() - this.level.getGameTime() >= 10000){
                ids.add(entry.getIntKey());
            }
        }
        for (int id : ids){
            this.idToCooldownMap.remove(id);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    protected List<A> getOrFindAnimals(Class<A> aClass){
        List<A> foundAnimals;
        if (this.animalCache == null || this.level.getGameTime() - this.lastCheck  < 100){
            AABB checkBox = new AABB(this.worldPosition);
            checkBox = checkBox.inflate(8);
            foundAnimals = this.level.getEntities(EntityTypeTest.forClass(aClass), checkBox, predicate);
            this.lastCheck = this.level.getGameTime();
        } else {
            foundAnimals = this.animalCache;
        }
        foundAnimals.removeIf(this::filterByCooldown);
        this.animalCache = foundAnimals;
        return foundAnimals;
    }

    private boolean filterByCooldown(A animal) {
        return this.idToCooldownMap.containsKey(animal.getId());
    }

    @Override
    public IItemHandlerModifiable getItemHandler() {
        return this.wrappedHandler;
    }

    public abstract List<A> getWorkableAnimals();

    public abstract boolean canBeWorkedAt();
}
