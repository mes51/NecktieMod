package com.mes51.minecraft.mods.necktie.item.necktie;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.potion.PotionFullyFed;
import com.mes51.minecraft.mods.necktie.potion.PotionMuscle;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.Util;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import ic2.api.item.Items;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;

/**
 * Package: com.mes51.minecraft.mods.necktie.items
 * Date: 2014/04/15
 * Time: 21:18
 */
public class ItemArmorDragonNecktie extends ItemArmorNecktieBase
{
    private static final EnumToolMaterial TOOL_MATERIAL = Const.Material.Tool.DRAGON;

    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktie");
        instance = new ItemArmorDragonNecktie(itemId, index);
        MinecraftForge.setToolClass(instance, "pickaxe", TOOL_MATERIAL.getHarvestLevel());

        GameRegistry.registerItem(instance, Const.Item.ItemName.DRAGON_NECKTIE);
        LanguageRegistry.addName(instance, "Necktie of Dragon");
    }

    public static void registerRecipe()
    {
        ItemStack iridium = null;
        if (Util.ic2Loaded())
        {
            iridium = Items.getItem("iridiumPlate");
        }
        else
        {
            iridium = new ItemStack(Block.blockDiamond, 1);
        }

        GameRegistry.addRecipe(
                new ItemStack(instance, 1),
                new Object[] {
                        "INI",
                        "E#E",
                        " B ",
                        'I', iridium,
                        'N', new ItemStack(ItemArmorDiamondNecktie.getInstance(), 1, 32767),
                        'E', new ItemStack(eyeOfEnder, 1),
                        'B', new ItemStack(Block.blockEmerald, 1),
                        '#', new ItemStack(Block.dragonEgg, 1)
                }
        );
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorDragonNecktie(int par1, int index)
    {
        super(par1, Const.Material.Armor.DRAGON, index, TOOL_MATERIAL, 4, "Dragon");
        setUnlocalizedName(Const.Item.ItemName.DRAGON_NECKTIE);
        setTextureName(Const.Item.ItemName.DRAGON_NECKTIE);
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        if (par1ItemStack.getItemDamage() / (double)par1ItemStack.getMaxDamage() > 0.5)
        {
            return par2ItemStack.getItem().itemID == Block.dragonEgg.blockID;
        }
        else
        {
            return super.getIsRepairable(par1ItemStack, par2ItemStack);
        }
    }

    @Override
    public boolean canHarvestBlock(Block par1Block)
    {
        return true;
    }

    @Override
    public PotionEffect[] getBeaconEffect(EntityPlayer player)
    {
        return new PotionEffect[] {
                new PotionEffect(PotionMuscle.getInstance().getId(), 0, 0),
                new PotionEffect(Potion.resistance.getId(), 0, 1),
                new PotionEffect(PotionFullyFed.getInstance().getId(), 0, 0)
        };
    }
}
