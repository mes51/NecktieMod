package com.mes51.minecraft.mods.necktie.item.necktie;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.util.Const;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
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
 * Time: 17:29
 */
public class ItemArmorNecktie extends ItemArmorNecktieBase
{
    private static final EnumToolMaterial TOOL_MATERIAL = EnumToolMaterial.STONE;
    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktie");
        instance = new ItemArmorNecktie(itemId, index);
        MinecraftForge.setToolClass(instance, "pickaxe", TOOL_MATERIAL.getHarvestLevel());

        GameRegistry.registerItem(instance, Const.Item.ItemName.NECKTIE);
        LanguageRegistry.addName(instance, "Necktie");
    }

    public static void registerRecipe()
    {
        GameRegistry.addRecipe(
                new ItemStack(instance, 1),
                new Object[] {
                        "###",
                        "# #",
                        " # ",
                        '#', new ItemStack(Block.cloth, 1, 32767)
                }
        );
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorNecktie(int par1, int index)
    {
        super(par1, EnumArmorMaterial.CHAIN, index, TOOL_MATERIAL, 1, "");
        setUnlocalizedName(Const.Item.ItemName.NECKTIE);
        setTextureName(Const.Item.ItemName.NECKTIE);
    }

    @Override
    public PotionEffect[] getBeaconEffect(EntityPlayer player)
    {
        return new PotionEffect[] {
                new PotionEffect(Potion.digSpeed.getId(), 0, 0)
        };
    }
}
