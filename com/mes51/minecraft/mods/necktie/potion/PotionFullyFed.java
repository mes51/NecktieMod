package com.mes51.minecraft.mods.necktie.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;

import java.awt.*;

/**
 * Package: com.mes51.minecraft.mods.necktie.potion
 * Date: 2014/05/23
 * Time: 1:08
 */
public class PotionFullyFed extends Potion
{
    private static Potion instance = null;

    public static void register(int potionId)
    {
        instance = new PotionFullyFed(potionId);
    }

    public static Potion getInstance()
    {
        return instance;
    }

    protected PotionFullyFed(int par1)
    {
        super(par1, false, new Color(25, 179, 60).getRGB());
        setPotionName("Fully Fed");
    }
}
