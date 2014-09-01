package com.mes51.minecraft.mods.necktie.item;

import com.mes51.minecraft.mods.necktie.item.necktie.*;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.NecktieCreativeTabs;
import com.mes51.minecraft.mods.necktie.util.Util;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.PatternBuilder;
import tconstruct.library.util.IPattern;

/**
 * Package: com.mes51.minecraft.mods.necktie.item
 * Date: 2014/05/31
 * Time: 0:20
 */
public class ItemNecktiePattern extends Item implements IPattern
{
    private static final int COST = 90;

    private static Item instance = null;

    public static void register(int itemId)
    {
        instance = new ItemNecktiePattern(itemId);
        GameRegistry.registerItem(instance, Const.Item.ItemName.NECKTIE_PATTERN);
        LanguageRegistry.addName(instance, "Necktie Pattern");
        PatternBuilder.instance.addToolPattern((IPattern)instance);
    }

    public static void registerRecipe()
    {
        if (Util.tConstructLoaded())
        {
            TConstructRegistry.addItemToDirectory("necktiePattern", instance);
            TConstructRegistry.addItemStackToDirectory("necktiePatternCast", new ItemStack(instance, 1));
            FluidStack aluminumbrass = new FluidStack(FluidRegistry.getFluid("aluminumbrass.molten"), 50);
            FluidStack gold = new FluidStack(FluidRegistry.getFluid("gold.molten"), 50);
            TConstructRegistry.instance.getTableCasting().addCastingRecipe(new ItemStack(instance, 1), aluminumbrass, new ItemStack(ItemArmorIronNecktie.getInstance(), 1, Short.MAX_VALUE), 80);
            TConstructRegistry.instance.getTableCasting().addCastingRecipe(new ItemStack(instance, 1), gold, new ItemStack(ItemArmorIronNecktie.getInstance(), 1, Short.MAX_VALUE), 80);

            FluidStack[] material = new FluidStack[] {
                    new FluidStack(FluidRegistry.getFluid("iron.molten"), COST * 500),
                    new FluidStack(FluidRegistry.getFluid("copper.molten"), COST * 500),
                    new FluidStack(FluidRegistry.getFluid("cobalt.molten"), COST * 500),
                    new FluidStack(FluidRegistry.getFluid("ardite.molten"), COST * 500),
                    new FluidStack(FluidRegistry.getFluid("manyullyn.molten"), COST * 500),
                    new FluidStack(FluidRegistry.getFluid("bronze.molten"), COST * 500),
                    new FluidStack(FluidRegistry.getFluid("alumite.molten"), COST * 500),
                    new FluidStack(FluidRegistry.getFluid("obsidian.molten"), COST * 500),
                    new FluidStack(FluidRegistry.getFluid("steel.molten"), COST * 500),
                    new FluidStack(FluidRegistry.getFluid("pigiron.molten"), COST * 500)
            };
            ItemStack[] output = new ItemStack[] {
                    new ItemStack(ItemArmorIronCastedNecktie.getInstance(), 1),
                    new ItemStack(ItemArmorCopperCastedNecktie.getInstance(), 1),
                    new ItemStack(ItemArmorCobaltCastedNecktie.getInstance(), 1),
                    new ItemStack(ItemArmorArditeCastedNecktie.getInstance(), 1),
                    new ItemStack(ItemArmorManyullynCastedNecktie.getInstance(), 1),
                    new ItemStack(ItemArmorBronzeCastedNecktie.getInstance(), 1),
                    new ItemStack(ItemArmorAlumiteCastedNecktie.getInstance(), 1),
                    new ItemStack(ItemArmorObsidianCastedNecktie.getInstance(), 1),
                    new ItemStack(ItemArmorSteelCastedNecktie.getInstance(), 1),
                    new ItemStack(ItemArmorPigIronCastedNecktie.getInstance(), 1)
            };
            for (int i = 0; i < material.length; i++)
            {
                TConstructRegistry.instance.getTableCasting().addCastingRecipe(output[i], material[i], new ItemStack(instance, 1), 80);
            }
        }
    }

    public ItemNecktiePattern(int par1)
    {
        super(par1);
        setMaxStackSize(1);
        setUnlocalizedName(Const.Item.ItemName.NECKTIE_PATTERN);
        setTextureName(Const.Item.ItemName.NECKTIE_PATTERN);
        setCreativeTab(NecktieCreativeTabs.instance);
    }

    @Override
    public int getPatternCost(ItemStack pattern)
    {
        return COST;
    }

    @Override
    public ItemStack getPatternOutput(ItemStack pattern, ItemStack input, PatternBuilder.MaterialSet set)
    {
        return new ItemStack(ItemArmorNecktie.getInstance(), 1);
    }
}
