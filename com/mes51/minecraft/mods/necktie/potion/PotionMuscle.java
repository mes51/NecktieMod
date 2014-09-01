package com.mes51.minecraft.mods.necktie.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Potion;

import java.awt.*;

/**
 * Package: com.mes51.minecraft.mods.necktie.potion
 * Date: 2014/04/18
 * Time: 22:39
 */
public class PotionMuscle extends Potion
{
    private static Potion instance = null;

    public static void register(int potionId)
    {
        instance = new PotionMuscle(potionId);
    }

    public static Potion getInstance()
    {
        return instance;
    }

    protected PotionMuscle(int par1)
    {
        super(par1, false, new Color(255, 179, 60).getRGB());
        setPotionName("Muscle");
        setIconIndex(0, 0);
        func_111184_a(SharedMonsterAttributes.movementSpeed, "D5424A47-A39B-4221-9026-04FEA64E7F23", 0.2, 2);
    }

    public double func_111183_a(int par1, AttributeModifier par2AttributeModifier)
    {
        return 1.3 * (par1 + 1);
    }
}
