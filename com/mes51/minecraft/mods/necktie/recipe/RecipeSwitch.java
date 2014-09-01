package com.mes51.minecraft.mods.necktie.recipe;

import com.mes51.minecraft.mods.necktie.util.enumerable.operator.Predicate;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

/**
 * Package: com.mes51.minecraft.mods.necktie.recipe
 * Date: 2014/04/24
 * Time: 23:51
 */
public class RecipeSwitch implements IRecipe
{
    private Item targetItem = null;
    private Item outputItem = null;

    public RecipeSwitch(Item targetItem, Item outputItem)
    {
        this.targetItem = targetItem;
        this.outputItem = outputItem;
    }

    @Override
    public boolean matches(InventoryCrafting inventorycrafting, World world)
    {
        return getCraftingResult(inventorycrafting) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting)
    {
        ItemStack target = null;
        for (int i = inventorycrafting.getSizeInventory(); i > -1; i--)
        {
            ItemStack itemStack = inventorycrafting.getStackInSlot(i);
            if (itemStack != null)
            {
                if (target == null && itemStack.getItem() == this.targetItem)
                {
                    target = itemStack;
                }
                else
                {
                    return null;
                }
            }
        }
        if (target != null)
        {
            return new ItemStack(this.outputItem, 1, target.getItemDamage());
        }
        else
        {
            return null;
        }
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
