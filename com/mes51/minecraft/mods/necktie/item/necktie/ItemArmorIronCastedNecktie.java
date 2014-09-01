package com.mes51.minecraft.mods.necktie.item.necktie;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Package: com.mes51.minecraft.mods.necktie.item
 * Date: 2014/05/31
 * Time: 15:49
 */
public class ItemArmorIronCastedNecktie extends ItemArmorCastedNecktieBase
{
    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktie");
        instance = new ItemArmorIronCastedNecktie(itemId, index);
        register((ItemArmorCastedNecktieBase)instance, Const.Item.ItemName.IRON_CASTED_NECKTIE, "Iron Casted Necktie");
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorIronCastedNecktie(int par1, int index)
    {
        super(par1, Const.Material.Armor.CASTED_IRON, index, Const.Material.Tool.CASTED_IRON, 2, "Iron");
        setUnlocalizedName(Const.Item.ItemName.IRON_CASTED_NECKTIE);
        setTextureName(Const.Item.ItemName.IRON_CASTED_NECKTIE);
    }

    @Override
    public PotionEffect[] getBeaconEffect(EntityPlayer player)
    {
        return new PotionEffect[] {
                new PotionEffect(Potion.moveSpeed.getId(), 0, 0)
        };
    }
}
