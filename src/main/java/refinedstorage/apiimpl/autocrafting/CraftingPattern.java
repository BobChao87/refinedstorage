package refinedstorage.apiimpl.autocrafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import refinedstorage.api.autocrafting.ICraftingPattern;
import refinedstorage.api.autocrafting.ICraftingPatternContainer;
import refinedstorage.api.storage.CompareUtils;
import refinedstorage.apiimpl.autocrafting.registry.CraftingTaskFactoryNormal;
import refinedstorage.apiimpl.autocrafting.registry.CraftingTaskFactoryProcessing;
import refinedstorage.item.ItemPattern;

import java.util.ArrayList;
import java.util.List;

public class CraftingPattern implements ICraftingPattern {
    private World world;
    private ICraftingPatternContainer container;
    private ItemStack stack;
    private List<ItemStack> inputs = new ArrayList<>();
    private List<ItemStack> outputs = new ArrayList<>();
    private List<ItemStack> byproducts = new ArrayList<>();

    public CraftingPattern(World world, ICraftingPatternContainer container, ItemStack stack) {
        this.world = world;
        this.container = container;
        this.stack = stack;

        InventoryCrafting inv = new InventoryCrafting(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, 3, 3);

        for (int i = 0; i < 9; ++i) {
            ItemStack slot = ItemPattern.getSlot(stack, i);

            if (slot != null) {
                for (int j = 0; j < slot.stackSize; ++j) {
                    inputs.add(ItemHandlerHelper.copyStackWithSize(slot, 1));
                }

                inv.setInventorySlotContents(i, slot);
            }
        }

        if (!ItemPattern.isProcessing(stack)) {
            ItemStack output = CraftingManager.getInstance().findMatchingRecipe(inv, world);

            if (output != null) {
                outputs.add(output.copy());

                for (ItemStack remaining : CraftingManager.getInstance().getRemainingItems(inv, world)) {
                    if (remaining != null) {
                        byproducts.add(remaining.copy());
                    }
                }
            }
        } else {
            outputs = ItemPattern.getOutputs(stack);
        }
    }

    @Override
    public ICraftingPatternContainer getContainer() {
        return container;
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public boolean isValid() {
        return !inputs.isEmpty() && !outputs.isEmpty();
    }

    @Override
    public List<ItemStack> getInputs() {
        return inputs;
    }

    @Override
    public List<ItemStack> getOutputs() {
        return outputs;
    }

    @Override
    public List<ItemStack> getByproducts() {
        return byproducts;
    }

    @Override
    public String getId() {
        return ItemPattern.isProcessing(stack) ? CraftingTaskFactoryProcessing.ID : CraftingTaskFactoryNormal.ID;
    }

    @Override
    public int getQuantityPerRequest(ItemStack requested) {
        int quantity = 0;

        for (ItemStack output : outputs) {
            if (CompareUtils.compareStackNoQuantity(requested, output)) {
                quantity += output.stackSize;

                if (!ItemPattern.isProcessing(stack)) {
                    break;
                }
            }
        }

        return quantity;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CraftingPattern)) {
            return false;
        }

        CraftingPattern otherPattern = (CraftingPattern) other;

        if (inputs.size() != otherPattern.inputs.size() || outputs.size() != otherPattern.outputs.size()) {
            return false;
        }

        for (int i = 0; i < inputs.size(); ++i) {
            if (!CompareUtils.compareStack(inputs.get(i), otherPattern.inputs.get(i))) {
                return false;
            }
        }

        for (int i = 0; i < outputs.size(); ++i) {
            if (!CompareUtils.compareStack(outputs.get(i), otherPattern.outputs.get(i))) {
                return false;
            }
        }

        return true;
    }
}
