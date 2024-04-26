package dev.aaronhowser.mods.geneticsresequenced.blocks.base

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.Containers
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleContainer
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.IEnergyStorage

/**
 * Base class for all crafting machines.
 * Note that this is only for basic crafting machines, ie, those that turn one thing into one other thing.
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class CraftingMachineBlockEntity(
    pType: BlockEntityType<*>,
    pPos: BlockPos,
    pBlockState: BlockState
) : InventoryAndEnergyBlockEntity(
    pType,
    pPos,
    pBlockState
), MenuProvider {

    companion object {
        const val SIMPLE_CONTAINER_SIZE = 2
        const val ITEMSTACK_HANDLER_SIZE = 3

        const val INPUT_SLOT_INDEX = 0
        const val OUTPUT_SLOT_INDEX = 1
        const val OVERCLOCK_SLOT_INDEX = 2
    }

    abstract val energyCostPerTick: Int

    val amountOfOverclockers: Int
        get() = itemHandler.getStackInSlot(OVERCLOCK_SLOT_INDEX).count

    protected var progress = 0
    protected var maxProgress = 20 * 4
    protected fun resetProgress() {
        progress = 0
    }

    protected val containerData = object : ContainerData {

        private val PROGRESS_INDEX = 0
        private val MAX_PROGRESS_INDEX = 1

        override fun get(pIndex: Int): Int {
            return when (pIndex) {
                PROGRESS_INDEX -> progress
                MAX_PROGRESS_INDEX -> maxProgress
                else -> 0
            }
        }

        override fun set(pIndex: Int, pValue: Int) {
            when (pIndex) {
                PROGRESS_INDEX -> progress = pValue
                MAX_PROGRESS_INDEX -> maxProgress = pValue
            }
        }

        override fun getCount(): Int {
            return SIMPLE_CONTAINER_SIZE
        }
    }

}