package com.mes51.minecraft.mods.necktie.power;

/**
 * Package: com.mes51.minecraft.mods.necktie.power
 * Date: 2014/05/03
 * Time: 15:46
 */
public class InternalEnergy extends EnergyBase<InternalEnergy>
{
    public static final double TO_MJ_RATE = 10.0;
    public static final double TO_EU_RATE = 4.0;
    public static final double TO_RF_RATE = 1.0;

    public InternalEnergy(double energy)
    {
        super(energy);
    }

    @Override
    public InternalEnergy add(InternalEnergy energy)
    {
        return new InternalEnergy(this.energy + energy.getValue());
    }

    @Override
    public InternalEnergy add(double energy)
    {
        return new InternalEnergy(this.energy + energy);
    }

    public double getEUValue()
    {
        return this.energy / TO_EU_RATE;
    }

    public double getMJValue()
    {
        return this.energy / TO_MJ_RATE;
    }

    public double getRFValue()
    {
        return this.energy / TO_RF_RATE;
    }

    public EnergyUnit toEU()
    {
        return new EnergyUnit(getEUValue());
    }

    public MinecraftJoule toMJ()
    {
        return new MinecraftJoule(getMJValue());
    }

    public RedstoneFlux toRF()
    {
        return new RedstoneFlux(getRFValue());
    }

    public static double convertToEUValue(double eu)
    {
        return eu * TO_EU_RATE;
    }

    public static double convertToMJValue(double mj)
    {
        return mj * TO_MJ_RATE;
    }

    public static double convertToRFValue(double rf)
    {
        return rf * TO_RF_RATE;
    }

    public static InternalEnergy from(EnergyUnit eu)
    {
        return new InternalEnergy(convertToEUValue(eu.getValue()));
    }

    public static InternalEnergy from(MinecraftJoule mj)
    {
        return new InternalEnergy(convertToMJValue(mj.getValue()));
    }

    public static InternalEnergy from(RedstoneFlux rf)
    {
        return new InternalEnergy(convertToRFValue(rf.getValue()));
    }

    public static InternalEnergy from(EnergyBase energy)
    {
        if (energy instanceof EnergyUnit)
        {
            return from((EnergyUnit)energy);
        }
        else if (energy instanceof MinecraftJoule)
        {
            return from((MinecraftJoule)energy);
        }
        else if (energy instanceof RedstoneFlux)
        {
            return from((RedstoneFlux)energy);
        }
        else
        {
            return null;
        }
    }
}
