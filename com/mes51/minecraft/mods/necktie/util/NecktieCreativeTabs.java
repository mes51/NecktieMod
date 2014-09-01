package com.mes51.minecraft.mods.necktie.util;

import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorNecktie;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * Package: com.mes51.minecraft.mods.necktie.util
 * Date: 2014/04/20
 * Time: 20:29
 */
public class NecktieCreativeTabs extends CreativeTabs
{
    public static CreativeTabs instance = new NecktieCreativeTabs();

    public NecktieCreativeTabs()
    {
        super("Necktie");
    }

    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(ItemArmorNecktie.getInstance());
    }
}
