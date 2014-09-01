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
 * Date: 2014/04/24
 * Time: 23:35
 */
public class ItemArmorNecktieHeadBand extends ItemArmorNecktieHeadBandBase
{
    private static final EnumToolMaterial TOOL_MATERIAL = EnumToolMaterial.STONE;
    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktieHeadBand");
        instance = new ItemArmorNecktieHeadBand(itemId, index);

        GameRegistry.registerItem(instance, Const.Item.ItemName.NECKTIE_HEAD_BAND);
        LanguageRegistry.addName(instance, "Necktie Head Band");
    }

    public static void registerRecipe()
    {
        GameRegistry.addRecipe(new RecipeSwitch(ItemArmorNecktie.getInstance(), instance));
        GameRegistry.addRecipe(new RecipeSwitch(instance, ItemArmorNecktie.getInstance()));
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorNecktieHeadBand(int par1, int index)
    {
        super(par1, EnumArmorMaterial.CHAIN, index, TOOL_MATERIAL, "");
        setUnlocalizedName(Const.Item.ItemName.NECKTIE_HEAD_BAND);
        setTextureName(Const.Item.ItemName.NECKTIE_HEAD_BAND);
    }
}
