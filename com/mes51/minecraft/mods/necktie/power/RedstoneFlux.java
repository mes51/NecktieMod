package com.mes51.minecraft.mods.necktie.power;

/**
 * Package: com.mes51.minecraft.mods.necktie.power
 * Date: 2014/05/03
 * Time: 15:41
 */
public class RedstoneFlux extends EnergyBase<RedstoneFlux>
{
    public RedstoneFlux(double energy)
    {
        super(energy);
    }

    @Override
    public RedstoneFlux add(RedstoneFlux energy)
    {
        return new RedstoneFlux(this.energy + energy.getValue());
    }

    @Override
    public RedstoneFlux add(double energy)
    {
        return new RedstoneFlux(this.energy + energy);
    }

    @Override
    public boolean isEmpty()
    {
        return this.energy < 1.0 || super.isEmpty();
    }
}
