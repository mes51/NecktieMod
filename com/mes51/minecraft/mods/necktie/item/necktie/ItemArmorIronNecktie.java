package com.mes51.minecraft.mods.necktie.item.necktie;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.util.Const;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;

/**
 * Package: com.mes51.minecraft.mods.necktie.item
 * Date: 2014/04/20
 * Time: 17:28
 */
public class ItemArmorIronNecktie extends ItemArmorNecktieBase
{
    private static final EnumToolMaterial TOOL_MATERIAL = EnumToolMaterial.IRON;
    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktie");
        instance = new ItemArmorIronNecktie(itemId, index);
        MinecraftForge.setToolClass(instance, "pickaxe", TOOL_MATERIAL.getHarvestLevel());

        GameRegistry.registerItem(instance, Const.Item.ItemName.IRON_NECKTIE);
        LanguageRegistry.addName(instance, "Iron Necktie");
    }

    public static void registerRecipe()
    {
        GameRegistry.addRecipe(
                new ItemStack(instance, 1),
                new Object[] {
                        "INI",
                        "IGI",
                        " I ",
                        'N', new ItemStack(ItemArmorNecktie.getInstance(), 1, 32767),
                        'I', new ItemStack(ingotIron, 1),
                        'G', new ItemStack(ingotGold, 1)
                }
        );
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorIronNecktie(int par1, int index)
    {
        super(par1, EnumArmorMaterial.IRON, index, TOOL_MATERIAL, 2, "Iron");
        setUnlocalizedName(Const.Item.ItemName.IRON_NECKTIE);
        setTextureName(Const.Item.ItemName.IRON_NECKTIE);
    }

    @Override
    public PotionEffect[] getBeaconEffect(EntityPlayer player)
    {
        return new PotionEffect[] {
                new PotionEffect(Potion.moveSpeed.getId(), 0, 0)
        };
    }
}
