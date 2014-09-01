package com.mes51.minecraft.mods.necktie.item.necktie;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.potion.PotionFullyFed;
import com.mes51.minecraft.mods.necktie.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Package: com.mes51.minecraft.mods.necktie.item.necktie
 * Date: 2014/06/01
 * Time: 14:08
 */
public class ItemArmorPigIronCastedNecktie extends ItemArmorCastedNecktieBase
{
    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktie");
        instance = new ItemArmorPigIronCastedNecktie(itemId, index);
        register((ItemArmorCastedNecktieBase)instance, Const.Item.ItemName.PIG_IRON_CASTED_NECKTIE, "Pig Iron Casted Necktie");
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorPigIronCastedNecktie(int par1, int index)
    {
        super(par1, Const.Material.Armor.CASTED_PIG_IRON, index, Const.Material.Tool.CASTED_PIG_IRON, 3, "PigIron");
        setUnlocalizedName(Const.Item.ItemName.PIG_IRON_CASTED_NECKTIE);
        setTextureName(Const.Item.ItemName.PIG_IRON_CASTED_NECKTIE);
    }

    @Override
    public PotionEffect[] getBeaconEffect(EntityPlayer player)
    {
        return new PotionEffect[] {
                new PotionEffect(PotionFullyFed.getInstance().getId(), 0, 0)
        };
    }
}
