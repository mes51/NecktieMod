package com.mes51.minecraft.mods.necktie.recipe;

import com.mes51.minecraft.mods.necktie.util.enumerable.Enumerable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

/**
 * Package: com.mes51.minecraft.mods.necktie.recipe
 * Date: 2014/07/07
 * Time: 15:57
 */
public class RecipeUpgrade implements IRecipe
{
    private Item targetItem = null;

    public RecipeUpgrade(Item targetItem)
    {
        this.targetItem = targetItem;
    }

    @Override
    public boolean matches(InventoryCrafting inventorycrafting, World world)
    {
        return getCraftingResult(inventorycrafting) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting)
    {
        int damage = 0;
        int size = inventorycrafting.getSizeInventory();
        ItemStack reference = inventorycrafting.getStackInSlot(0);
        if (reference == null || reference.getItem() != this.targetItem)
        {
            return null;
        }

        for (int i = 0; i < size; i++)
        {
            ItemStack itemStack = inventorycrafting.getStackInSlot(i);
            if (itemStack == null || !reference.isItemEqual(itemStack))
            {
                return null;
            }
        }

        ItemStack result = reference.copy();
        result.setItemDamage(result.getItemDamage() + 1);
        return result;
    }

    @Override
    public int getRecipeSize()
    {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return null;
    }
}
