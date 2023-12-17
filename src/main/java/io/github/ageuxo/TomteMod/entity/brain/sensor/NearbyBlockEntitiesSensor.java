package io.github.ageuxo.TomteMod.entity.brain.sensor;

import com.mojang.datafixers.util.Pair;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import io.github.ageuxo.TomteMod.entity.brain.ModSensors;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor;
import net.tslat.smartbrainlib.object.SquareRadius;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class NearbyBlockEntitiesSensor<E extends LivingEntity> extends PredicateSensor<Pair<BlockState, BlockEntity>, E> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(ModMemoryTypes.NEARBY_BLOCK_ENTITIES.get());

    protected Predicate<E> scanPredicate = (entity) -> true;
    protected SquareRadius radius = new SquareRadius(1,1);

    public NearbyBlockEntitiesSensor(){
        setPredicate((stateBlockEntityPair, e)-> stateBlockEntityPair.getFirst().hasBlockEntity());
    }

    public NearbyBlockEntitiesSensor<E> setRadius(double horizontalRadius, double verticalRadius){
        this.radius = new SquareRadius(horizontalRadius, verticalRadius);

        return this;
    }

    /**
     * Helper for setting the predicate to filter for BEs with Inventory Capabilities
     * @return this
     */
    public NearbyBlockEntitiesSensor<E> setInventoryCapabilityPredicate(){
        this.setPredicate((pair, e) -> capabilityPredicate(pair, ForgeCapabilities.ITEM_HANDLER));
        return this;
    }

    /**
     * Helper for setting the predicate to filter for BEs with Energy Capabilities
     * @return this
     */
    public NearbyBlockEntitiesSensor<E> setEnergyCapabilityPredicate(){
        this.setPredicate((pair, e) -> capabilityPredicate(pair, ForgeCapabilities.ENERGY));
        return this;
    }

    /**
     * Helper for setting the predicate to filter for BEs with Fluid Capabilities
     * @return this
     */
    public NearbyBlockEntitiesSensor<E> setFluidCapabilityPredicate(){
        this.setPredicate((pair, e) -> capabilityPredicate(pair, ForgeCapabilities.FLUID_HANDLER));
        return this;
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {
        List<Pair<BlockPos, BlockEntityType<?>>> pairList = new ObjectArrayList<>();

        if (this.scanPredicate.test(entity)){
            for (BlockPos pos : BlockPos.betweenClosed(entity.blockPosition().subtract(this.radius.toVec3i()), entity.blockPosition().offset(this.radius.toVec3i()))) {
                BlockState state = level.getBlockState(pos);
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null && this.predicate().test(Pair.of(state, blockEntity), entity)) {
                    pairList.add(Pair.of(pos.immutable(), blockEntity.getType()));
                }
            }
        }
        if (pairList.isEmpty()){
            BrainUtils.clearMemory(entity, ModMemoryTypes.NEARBY_BLOCK_ENTITIES.get());
        } else {
            BrainUtils.setMemory(entity, ModMemoryTypes.NEARBY_BLOCK_ENTITIES.get(), pairList);
        }
    }

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensors.NEARBY_BLOCK_ENTITIES.get();
    }

    @Override
    public NearbyBlockEntitiesSensor<E> setPredicate(BiPredicate<Pair<BlockState, BlockEntity>, E> predicate) {
        super.setPredicate(predicate);
        return this;
    }

    public NearbyBlockEntitiesSensor<E> scanPredicate(Predicate<E> predicate){
        this.scanPredicate = predicate;

        return this;
    }

    protected static boolean capabilityPredicate(Pair<BlockState, BlockEntity> stateBlockEntityPair, Capability<?> capability){
        return stateBlockEntityPair.getFirst().hasBlockEntity() && stateBlockEntityPair.getSecond().getCapability(capability).isPresent();
    }
}
