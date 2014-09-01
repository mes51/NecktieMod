package com.mes51.minecraft.mods.necktie.item.necktie;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraftforge.common.MinecraftForge;

/**
 * Package: com.mes51.minecraft.mods.necktie.item
 * Date: 2014/05/31
 * Time: 15:31
 */
public abstract class ItemArmorCastedNecktieBase extends ItemArmorNecktieBase
{
    protected static void register(ItemArmorCastedNecktieBase item, String unlocalizedName, String displayName)
    {
        GameRegistry.registerItem(item, unlocalizedName);
        LanguageRegistry.addName(item, displayName);
        MinecraftForge.setToolClass(item, "pickaxe", item.getToolMaterial().getHarvestLevel());
    }

    public ItemArmorCastedNecktieBase(int par1, EnumArmorMaterial material, int index, EnumToolMaterial toolMaterial, int potionDurationLevel, String suffix)
    {
        super(par1, material, index, toolMaterial, potionDurationLevel, "Casted" + suffix);
        setNoRepair();
    }
}
