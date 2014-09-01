package com.mes51.minecraft.mods.necktie.item.necktie;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.recipe.RecipeSwitch;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;

/**
 * Package: com.mes51.minecraft.mods.necktie.item
 * Date: 2014/04/28
 * Time: 0:51
 */
public class ItemArmorDiamondNecktieHeadBand extends ItemArmorNecktieHeadBandBase
{
    private static final EnumToolMaterial TOOL_MATERIAL = EnumToolMaterial.EMERALD;
    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktieHeadBand");
        instance = new ItemArmorDiamondNecktieHeadBand(itemId, index);

        GameRegistry.registerItem(instance, Const.Item.ItemName.DIAMOND_NECKTIE_HEAD_BAND);
        LanguageRegistry.addName(instance, "Diamond Necktie Head Band");
    }

    public static void registerRecipe()
    {
        GameRegistry.addRecipe(new RecipeSwitch(ItemArmorDiamondNecktie.getInstance(), instance));
        GameRegistry.addRecipe(new RecipeSwitch(instance, ItemArmorDiamondNecktie.getInstance()));
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorDiamondNecktieHeadBand(int par1, int index)
    {
        super(par1, Const.Material.Armor.REINFORCED_DIAMOND, index, TOOL_MATERIAL, "Diamond");
        setUnlocalizedName(Const.Item.ItemName.DIAMOND_NECKTIE_HEAD_BAND);
        setTextureName(Const.Item.ItemName.DIAMOND_NECKTIE_HEAD_BAND);
    }
}
