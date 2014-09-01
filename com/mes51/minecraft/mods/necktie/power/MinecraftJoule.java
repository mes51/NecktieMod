package com.mes51.minecraft.mods.necktie.power;

/**
 * Package: com.mes51.minecraft.mods.necktie.power
 * Date: 2014/05/03
 * Time: 13:24
 */
public class MinecraftJoule extends EnergyBase<MinecraftJoule>
{
    public MinecraftJoule(double energy)
    {
        super(energy);
    }

    @Override
    public MinecraftJoule add(MinecraftJoule energy)
    {
        return new MinecraftJoule(this.energy + energy.getValue());
    }

    @Override
    public MinecraftJoule add(double energy)
    {
        return new MinecraftJoule(this.energy + energy);
    }
}
