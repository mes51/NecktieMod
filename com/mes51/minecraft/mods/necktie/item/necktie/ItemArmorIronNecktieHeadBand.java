package com.mes51.minecraft.mods.necktie.item.necktie;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.recipe.RecipeSwitch;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;

/**
 * Package: com.mes51.minecraft.mods.necktie.item
 * Date: 2014/04/28
 * Time: 0:43
 */
public class ItemArmorIronNecktieHeadBand extends ItemArmorNecktieHeadBandBase
{
    private static final EnumToolMaterial TOOL_MATERIAL = EnumToolMaterial.IRON;
    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktieHeadBand");
        instance = new ItemArmorIronNecktieHeadBand(itemId, index);

        GameRegistry.registerItem(instance, Const.Item.ItemName.IRON_NECKTIE_HEAD_BAND);
        LanguageRegistry.addName(instance, "Iron Necktie Head Band");
    }

    public static void registerRecipe()
    {
        GameRegistry.addRecipe(new RecipeSwitch(ItemArmorIronNecktie.getInstance(), instance));
        GameRegistry.addRecipe(new RecipeSwitch(instance, ItemArmorIronNecktie.getInstance()));
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorIronNecktieHeadBand(int par1, int index)
    {
        super(par1, EnumArmorMaterial.IRON, index, TOOL_MATERIAL, "Iron");
        setUnlocalizedName(Const.Item.ItemName.IRON_NECKTIE_HEAD_BAND);
        setTextureName(Const.Item.ItemName.IRON_NECKTIE_HEAD_BAND);
    }
}
