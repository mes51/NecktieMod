package com.mes51.minecraft.mods.necktie.item.necktie;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.Util;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import ic2.api.item.Items;
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
 * Date: 2014/05/04
 * Time: 19:52
 */
public class ItemArmorNukeNecktie extends ItemArmorNecktieBase
{
    public static final float EXPLOSION_RADIUS = 128.0F;
    private static final EnumToolMaterial TOOL_MATERIAL = Const.Material.Tool.NUKE;

    private static Item instance = null;

    public static void register(int itemId)
    {
        if (Util.ic2Loaded())
        {
            int index = Necktie.proxy.getArmorRenderPrefix("armorNecktie");
            instance = new ItemArmorNukeNecktie(itemId, index);
            MinecraftForge.setToolClass(instance, "pickaxe", TOOL_MATERIAL.getHarvestLevel());

            GameRegistry.registerItem(instance, Const.Item.ItemName.NUKE_NECKTIE);
            LanguageRegistry.addName(instance, "Nuke Necktie");
        }
    }

    public static void registerRecipe()
    {
        if (Util.ic2Loaded())
        {
            GameRegistry.addRecipe(
                    new ItemStack(instance, 1),
                    new Object[] {
                            "PNP",
                            "PUP",
                            " C ",
                            'P', new ItemStack(Items.getItem("reactorReflectorThick").getItem(), 1, 32767),
                            'N', new ItemStack(ItemArmorIronNecktie.getInstance(), 1, 32767),
                            'U', Items.getItem("uraniumBlock"),
                            'C', Items.getItem("copperIngot")
                    }
            );
        }
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorNukeNecktie(int par1, int index)
    {
        super(par1, Const.Material.Armor.NUKE, index, TOOL_MATERIAL, 3, "Nuke");
        setUnlocalizedName(Const.Item.ItemName.NUKE_NECKTIE);
        setTextureName(Const.Item.ItemName.NUKE_NECKTIE);
    }

    @Override
    public boolean canHarvestBlock(Block par1Block)
    {
        return Item.pickaxeIron.canHarvestBlock(par1Block);
    }

    @Override
    public PotionEffect[] getBeaconEffect(EntityPlayer player)
    {
        return new PotionEffect[] {
                new PotionEffect(Potion.poison.getId(), 0, 0)
        };
    }
}
