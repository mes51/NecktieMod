package com.mes51.minecraft.mods.necktie.recipe;

import com.mes51.minecraft.mods.necktie.util.enumerable.operator.Predicate;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

/**
 * Package: com.mes51.minecraft.mods.necktie.recipe
 * Date: 2014/05/01
 * Time: 11:07
 */
public class RecipeAir implements IRecipe
{
    private ItemStack output = null;
    private Predicate<ItemStack> predicate;

    public RecipeAir(ItemStack output, Predicate<ItemStack> predicate)
    {
        this.output = output;
        this.predicate = predicate;
    }

    @Override
    public boolean matches(InventoryCrafting inventorycrafting, World world)
    {
        return getCraftingResult(inventorycrafting) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting)
    {
        boolean hasItem = false;
        for (int i = inventorycrafting.getSizeInventory() - 1; i > -1; i--)
        {
            ItemStack itemStack = inventorycrafting.getStackInSlot(i);
            if (itemStack != null)
            {
                if (!this.predicate.predicate(itemStack))
                {
                    return null;
                }
                else
                {
                    hasItem = true;
                }
            }
        }
        if (hasItem)
        {
            return getRecipeOutput();
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the size of the recipe area
     */
    @Override
    public int getRecipeSize()
    {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return this.output.copy();
    }
}
