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
 * Time: 3:06
 */
public class ItemArmorAlumiteCastedNecktie extends ItemArmorCastedNecktieBase
{
    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktie");
        instance = new ItemArmorAlumiteCastedNecktie(itemId, index);
        register((ItemArmorCastedNecktieBase)instance, Const.Item.ItemName.ALUMITE_CASTED_NECKTIE, "Alumite Casted Necktie");
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorAlumiteCastedNecktie(int par1, int index)
    {
        super(par1, Const.Material.Armor.CASTED_ALUMITE, index, Const.Material.Tool.CASTED_ALUMITE, 3, "Alumite");
        setUnlocalizedName(Const.Item.ItemName.ALUMITE_CASTED_NECKTIE);
        setTextureName(Const.Item.ItemName.ALUMITE_CASTED_NECKTIE);
    }

    @Override
    public PotionEffect[] getBeaconEffect(EntityPlayer player)
    {
        return new PotionEffect[] {
                new PotionEffect(Potion.resistance.getId(), 0, 1)
        };
    }
}
