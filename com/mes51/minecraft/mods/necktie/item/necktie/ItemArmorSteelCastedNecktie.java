package com.mes51.minecraft.mods.necktie.item.necktie;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Package: com.mes51.minecraft.mods.necktie.item.necktie
 * Date: 2014/06/01
 * Time: 14:00
 */
public class ItemArmorSteelCastedNecktie extends ItemArmorCastedNecktieBase
{
    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktie");
        instance = new ItemArmorSteelCastedNecktie(itemId, index);
        register((ItemArmorCastedNecktieBase)instance, Const.Item.ItemName.STEEL_CASTED_NECKTIE, "Steel Casted Necktie");
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorSteelCastedNecktie(int par1, int index)
    {
        super(par1, Const.Material.Armor.CASTED_STEEL, index, Const.Material.Tool.CASTED_STEEL, 3, "Steel");
        setUnlocalizedName(Const.Item.ItemName.STEEL_CASTED_NECKTIE);
        setTextureName(Const.Item.ItemName.STEEL_CASTED_NECKTIE);
    }

    @Override
    public PotionEffect[] getBeaconEffect(EntityPlayer player)
    {
        return new PotionEffect[] {
                new PotionEffect(Potion.damageBoost.getId(), 0, 1)
        };
    }
}
