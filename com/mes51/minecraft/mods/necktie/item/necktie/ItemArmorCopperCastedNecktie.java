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
 * Time: 1:18
 */
public class ItemArmorCopperCastedNecktie extends ItemArmorCastedNecktieBase
{
    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktie");
        instance = new ItemArmorCopperCastedNecktie(itemId, index);
        register((ItemArmorCastedNecktieBase)instance, Const.Item.ItemName.COPPER_CASTED_NECKTIE, "Copper Casted Necktie");
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorCopperCastedNecktie(int par1, int index)
    {
        super(par1, Const.Material.Armor.CASTED_COPPER, index, Const.Material.Tool.CASTED_COPPER, 2, "Copper");
        setUnlocalizedName(Const.Item.ItemName.COPPER_CASTED_NECKTIE);
        setTextureName(Const.Item.ItemName.COPPER_CASTED_NECKTIE);
    }

    @Override
    public PotionEffect[] getBeaconEffect(EntityPlayer player)
    {
        return new PotionEffect[] {
                new PotionEffect(Potion.damageBoost.getId(), 0, 0)
        };
    }
}
