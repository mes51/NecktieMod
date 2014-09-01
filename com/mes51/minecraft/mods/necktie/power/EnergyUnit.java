package com.mes51.minecraft.mods.necktie.power;

/**
 * Package: com.mes51.minecraft.mods.necktie.power
 * Date: 2014/05/03
 * Time: 13:22
 */
public class EnergyUnit extends EnergyBase<EnergyUnit>
{
    public EnergyUnit(double energy)
    {
        super(energy);
    }

    @Override
    public EnergyUnit add(EnergyUnit energy)
    {
        return new EnergyUnit(this.energy + energy.getValue());
    }

    @Override
    public EnergyUnit add(double energy)
    {
        return new EnergyUnit(this.energy + energy);
    }
}
