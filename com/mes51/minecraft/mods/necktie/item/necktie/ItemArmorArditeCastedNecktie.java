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
 * Time: 2:11
 */
public class ItemArmorArditeCastedNecktie extends ItemArmorCastedNecktieBase
{
    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktie");
        instance = new ItemArmorArditeCastedNecktie(itemId, index);
        register((ItemArmorCastedNecktieBase)instance, Const.Item.ItemName.ARDITE_CASTED_NECKTIE, "Ardite Casted Necktie");
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorArditeCastedNecktie(int par1, int index)
    {
        super(par1, Const.Material.Armor.CASTED_ARDITE, index, Const.Material.Tool.CASTED_ARDITE, 3, "Ardite");
        setUnlocalizedName(Const.Item.ItemName.ARDITE_CASTED_NECKTIE);
        setTextureName(Const.Item.ItemName.ARDITE_CASTED_NECKTIE);
    }

    @Override
    public PotionEffect[] getBeaconEffect(EntityPlayer player)
    {
        return new PotionEffect[] {
                new PotionEffect(Potion.nightVision.getId(), 0, 0)
        };
    }
}
