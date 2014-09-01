package com.mes51.minecraft.mods.necktie.power;

import java.io.Serializable;

/**
 * Package: com.mes51.minecraft.mods.necktie.power
 * Date: 2014/05/03
 * Time: 13:32
 */
public abstract class EnergyBase<T extends EnergyBase> implements Serializable
{
    protected final double energy;

    public EnergyBase(double energy)
    {
        this.energy = energy;
    }

    public double getValue()
    {
        return this.energy;
    }

    public boolean isEmpty()
    {
        return this.energy <= 0.0;
    }

    public abstract T add(T energy);

    public abstract T add(double energy);
}
