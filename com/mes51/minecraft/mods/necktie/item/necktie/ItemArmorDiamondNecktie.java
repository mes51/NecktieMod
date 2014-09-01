package com.mes51.minecraft.mods.necktie.item.necktie;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.util.Const;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;

/**
 * Package: com.mes51.minecraft.mods.necktie.item
 * Date: 2014/04/20
 * Time: 17:43
 */
public class ItemArmorDiamondNecktie extends ItemArmorNecktieBase
{
    private static final EnumToolMaterial TOOL_MATERIAL = EnumToolMaterial.EMERALD;
    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktie");
        instance = new ItemArmorDiamondNecktie(itemId, index);
        MinecraftForge.setToolClass(instance, "pickaxe", TOOL_MATERIAL.getHarvestLevel());

        GameRegistry.registerItem(instance, Const.Item.ItemName.DIAMOND_NECKTIE);
        LanguageRegistry.addName(instance, "Diamond Necktie");
    }

    public static void registerRecipe()
    {
        GameRegistry.addRecipe(
                new ItemStack(instance, 1),
                new Object[] {
                        "GNG",
                        "BEB",
                        " G ",
                        'N', new ItemStack(ItemArmorIronNecktie.getInstance(), 1, 32767),
                        'G', new ItemStack(ingotGold, 1),
                        'B', new ItemStack(Block.blockDiamond, 1),
                        'E', new ItemStack(emerald, 1)
                }
        );
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorDiamondNecktie(int par1, int index)
    {
        super(par1, Const.Material.Armor.REINFORCED_DIAMOND, index, TOOL_MATERIAL, 3, "Diamond");
        setUnlocalizedName(Const.Item.ItemName.DIAMOND_NECKTIE);
        setTextureName(Const.Item.ItemName.DIAMOND_NECKTIE);
    }

    @Override
    public PotionEffect[] getBeaconEffect(EntityPlayer player)
    {
        return new PotionEffect[] { new PotionEffect(Potion.resistance.getId(), 0, 1) };
    }
}
