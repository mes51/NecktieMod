package com.mes51.minecraft.mods.necktie.recipe;

import com.mes51.minecraft.mods.necktie.util.Util;
import com.mes51.minecraft.mods.necktie.util.enumerable.Enumerable;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.Predicate;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Package: com.mes51.minecraft.mods.necktie.recipe
 * Date: 2014/04/22
 * Time: 22:37
 */
public class RecipeReplacer
{
    private static class ListWrapper<T>
    {
        private Object wrappedObject = null;
        private boolean isArray = false;

        public ListWrapper(Object wrappedObject)
        {
            this.wrappedObject = wrappedObject;
            this.isArray = wrappedObject.getClass().isArray();
        }

        public T get(int index)
        {
            if (this.isArray)
            {
                return (T)Array.get(this.wrappedObject, index);
            }
            else
            {
                return (T)((List)this.wrappedObject).get(index);
            }
        }

        public void set(int index, T value)
        {
            if (this.isArray)
            {
                Array.set(this.wrappedObject, index, value);
            }
            else
            {
                ((List)this.wrappedObject).set(index, value);
            }
        }

        public int size()
        {
            if (this.isArray)
            {
                return Array.getLength(this.wrappedObject);
            }
            else
            {
                return ((List)this.wrappedObject).size();
            }
        }

        public Object getWappedObject()
        {
            return this.wrappedObject;
        }
    }

    private static Field shapedRecipeInputField = Util.tryGetField(ShapedRecipes.class, "recipeItems", "field_77574_d");
    private static Field shapedRecipeOutputField = Util.tryGetField(ShapedRecipes.class, "recipeOutput", "field_77575_e");
    private static Field shapelessRecipeInputField = Util.tryGetField(ShapelessRecipes.class, "recipeItems", "field_77579_b");
    private static Field shapelessRecipeOutputField = Util.tryGetField(ShapedRecipes.class, "recipeOutput", "field_77580_a");
    private static Field shapedOreRecipeInputField = Util.tryGetField(ShapedOreRecipe.class, "input");
    private static Field shapedOreRecipeOutputField = Util.tryGetField(ShapedOreRecipe.class, "output");
    private static Field shapelessOreRecipeInputField = Util.tryGetField(ShapelessOreRecipe.class, "input");
    private static Field shapelessOreRecipeOutputField = Util.tryGetField(ShapelessOreRecipe.class, "output");

    private static Enumerable<IRecipe> recipeCache = null;

    public static void replaceOutput(Item targetOutput, ItemStack replace)
    {
        IRecipe recipe = findRecipe(targetOutput);

        try
        {
            if (recipe instanceof ShapedRecipes)
            {
                shapedRecipeOutputField.set(recipe, replace);
            }
            else if (recipe instanceof ShapelessRecipes)
            {
                shapelessRecipeOutputField.set(recipe, replace);
            }
            else if (recipe instanceof ShapedOreRecipe)
            {
                shapedOreRecipeOutputField.set(recipe, replace);
            }
            else
            {
                shapelessOreRecipeOutputField.set(recipe, replace);
            }
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public static void replaceRecipe(final Item targetOutput, Map<Item, Item> inputMap)
    {
        IRecipe recipe = findRecipe(targetOutput);
        if (recipe instanceof ShapedRecipes)
        {
            replaceDefaultRecipe(recipe, shapedRecipeInputField, inputMap);
        }
        else if (recipe instanceof ShapelessRecipes)
        {
            replaceDefaultRecipe(recipe, shapelessRecipeInputField, inputMap);
        }
        else if (recipe instanceof ShapedOreRecipe)
        {
            replaceOreRecipe(recipe, shapedOreRecipeInputField, inputMap);
        }
        else
        {
            replaceOreRecipe(recipe, shapelessOreRecipeInputField, inputMap);
        }
    }

    public static void replaceAllRecipe(Map<Item, Item> inputMap)
    {
        cacheRecipe();
        for (IRecipe recipe : recipeCache)
        {
            if (recipe instanceof ShapedRecipes)
            {
                replaceDefaultRecipe(recipe, shapedRecipeInputField, inputMap);
            }
            else if (recipe instanceof ShapelessRecipes)
            {
                replaceDefaultRecipe(recipe, shapelessRecipeInputField, inputMap);
            }
            else if (recipe instanceof ShapedOreRecipe)
            {
                replaceOreRecipe(recipe, shapedOreRecipeInputField, inputMap);
            }
            else
            {
                replaceOreRecipe(recipe, shapelessOreRecipeInputField, inputMap);
            }
        }
    }

    private static void replaceDefaultRecipe(IRecipe recipe, Field inputField, Map<Item, Item> inputMap)
    {
        try
        {
            ListWrapper<ItemStack> inputItems = new ListWrapper<ItemStack>(inputField.get(recipe));
            for (int i = inputItems.size() - 1; i > -1; i--)
            {
                ItemStack itemStack = inputItems.get(i);
                if (itemStack == null)
                {
                    continue;
                }
                Item item = inputMap.get(itemStack.getItem());
                if (item != null)
                {
                    inputItems.set(i, new ItemStack(item, 1));
                }
            }
            inputField.set(recipe, inputItems.getWappedObject());
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    private static void replaceOreRecipe(IRecipe recipe, Field inputField, Map<Item, Item> inputMap)
    {
        try
        {
            ListWrapper inputItems = new ListWrapper(inputField.get(recipe));
            for (int i = inputItems.size() - 1; i > -1; i--)
            {
                Object obj = inputItems.get(i);
                if (obj == null)
                {
                    continue;
                }
                if (obj instanceof ItemStack)
                {
                    Item item = inputMap.get(((ItemStack)obj).getItem());
                    if (item != null)
                    {
                        inputItems.set(i, new ItemStack(item, 1));
                    }
                }
                else if (obj instanceof List)
                {
                    List list = (List)obj;
                    for (int n = list.size() - 1; n > -1; n--)
                    {
                        if (list.get(n) == null)
                        {
                            continue;
                        }
                        if (list.get(n) instanceof ItemStack)
                        {
                            Item item = inputMap.get(((ItemStack)list.get(n)).getItem());
                            if (item != null)
                            {
                                list.set(n, new ItemStack(item, 1));
                            }
                        }
                    }
                }
            }
            inputField.set(recipe, inputItems.getWappedObject());
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    private static IRecipe findRecipe(final Item targetOutput)
    {
        cacheRecipe();
        return recipeCache.first(
                new Predicate<IRecipe>()
                {
                    @Override
                    public boolean predicate(IRecipe value)
                    {
                        return value.getRecipeOutput().getItem() == targetOutput;
                    }
                }
        );
    }

    private static void cacheRecipe()
    {
        if (recipeCache == null)
        {
            recipeCache = Enumerable.from((List<IRecipe>) CraftingManager.getInstance().getRecipeList()).where(
                    new Predicate<IRecipe>()
                    {
                        @Override
                        public boolean predicate(IRecipe value)
                        {
                            return value.getRecipeOutput() != null;
                        }
                    }
            ).where(
                    new Predicate<IRecipe>()
                    {
                        @Override
                        public boolean predicate(IRecipe value)
                        {
                            return value instanceof ShapedRecipes ||
                                    value instanceof ShapelessRecipes ||
                                    value instanceof ShapedOreRecipe ||
                                    value instanceof ShapelessOreRecipe;
                        }
                    }
            ).cache();
        }
    }
}
