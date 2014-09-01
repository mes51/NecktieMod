package com.mes51.minecraft.mods.necktie.tileentity.machine;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile;
import cofh.api.energy.IEnergyHandler;
import com.mes51.minecraft.mods.necktie.power.EnergyUnit;
import com.mes51.minecraft.mods.necktie.power.InternalEnergy;
import com.mes51.minecraft.mods.necktie.power.MinecraftJoule;
import com.mes51.minecraft.mods.necktie.power.RedstoneFlux;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityMachineBlock;
import com.mes51.minecraft.mods.necktie.util.Vec3i;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.HashMap;
import java.util.Map;

/**
 * Package: com.mes51.minecraft.mods.necktie.tileentity.machine
 * Date: 2014/05/03
 * Time: 18:16
 */
public abstract class MachineTileEntityGeneratorBase extends MachineTileEntityBase implements IEnergySource, IPipeConnection, IPowerEmitter, IEnergyHandler
{
    protected InternalEnergy stored = new InternalEnergy(0.0);
    private Map<ForgeDirection, IPowerReceptor> connectedIPowerReceptor = null;
    private Map<ForgeDirection, IEnergyHandler> connectedIEnergyHandler = null;

    public MachineTileEntityGeneratorBase(TileEntityMachineBlock tileEntity)
    {
        super(tileEntity);
        this.tileEntity.setEnableAutoBlockUpdate(true);
        this.connectedIPowerReceptor = new HashMap<ForgeDirection, IPowerReceptor>();
        this.connectedIEnergyHandler = new HashMap<ForgeDirection, IEnergyHandler>();
    }

    @Override
    public void update()
    {
        super.update();

        transferEnergyByMJ();
        transferEnergyByRF();
    }

    @Override
    public void updateBlock()
    {
        super.updateBlock();
        if (this.tileEntity.worldObj != null)
        {
            onNeighborTileChange();
        }
    }

    @Override
    public void onNeighborTileChange()
    {
        super.onNeighborTileChange();

        this.connectedIPowerReceptor.clear();
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            Vec3i np = this.pos.add(dir);
            TileEntity neighborTileEntity = this.tileEntity.worldObj.getBlockTileEntity(np.x, np.y, np.z);
            if (neighborTileEntity instanceof IEnergyHandler)
            {
                if (((IEnergyHandler)neighborTileEntity).canInterface(dir.getOpposite()))
                {
                    this.connectedIEnergyHandler.put(dir.getOpposite(), (IEnergyHandler)neighborTileEntity);
                }
            }
            else if (neighborTileEntity instanceof IPowerReceptor)
            {
                if (((IPowerReceptor)neighborTileEntity).getPowerReceiver(dir.getOpposite()) != null)
                {
                    this.connectedIPowerReceptor.put(dir.getOpposite(), (IPowerReceptor)neighborTileEntity);
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setDouble("stored", this.stored.getValue());
        tagCompound.setBoolean("internalEnergy", true);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        if (tagCompound.getBoolean("internalEnergy"))
        {
            this.stored = new InternalEnergy(tagCompound.getDouble("stored"));
        }
        else
        {
            this.stored = InternalEnergy.from(new EnergyUnit(tagCompound.getDouble("stored")));
        }
    }

    private void transferEnergyByMJ()
    {
        double offered = getOfferedEnergyInternal();
        MinecraftJoule mj = new InternalEnergy(offered).toMJ();
        for (Map.Entry<ForgeDirection, IPowerReceptor> entry : this.connectedIPowerReceptor.entrySet())
        {
            ForgeDirection dir = entry.getKey();
            PowerHandler.PowerReceiver pr = entry.getValue().getPowerReceiver(dir);
            if (pr != null)
            {
                float require = Math.min(pr.getMaxEnergyReceived(), pr.getMaxEnergyStored() - pr.getEnergyStored());
                if (require > 0.0)
                {
                    float used = Math.min(require, (float)mj.getValue());
                    mj =  mj.add(-pr.receiveEnergy(PowerHandler.Type.GATE, used, dir));
                }
            }
            if (mj.isEmpty())
            {
                break;
            }
        }
        this.stored = this.stored.add(InternalEnergy.from(mj).getValue() - offered);
    }

    private void transferEnergyByRF()
    {
        double offered = getOfferedEnergyInternal();
        RedstoneFlux rf = new InternalEnergy(offered).toRF();
        for (Map.Entry<ForgeDirection, IEnergyHandler> entry : this.connectedIEnergyHandler.entrySet())
        {
            ForgeDirection dir = entry.getKey();
            rf = rf.add(-entry.getValue().receiveEnergy(dir, (int)rf.getValue(), false));
            if (rf.isEmpty())
            {
                break;
            }
        }
        this.stored = this.stored.add(InternalEnergy.from(rf).getValue() - offered);
    }

    protected abstract double getOfferedEnergyInternal();

    protected abstract double getMaxStorageSize();

    // IEnergySource

    @Override
    public double getOfferedEnergy()
    {
        return InternalEnergy.convertToEUValue(getOfferedEnergyInternal());
    }

    @Override
    public void drawEnergy(double amount)
    {
        this.stored = this.stored.add(InternalEnergy.from(new EnergyUnit(-amount)));
    }

    @Override
    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction)
    {
        return true;
    }

    // IPipeConnection

    @Override
    public IPipeConnection.ConnectOverride overridePipeConnection(IPipeTile.PipeType type, ForgeDirection with)
    {
        switch (type)
        {
            case POWER:
            case STRUCTURE:
                return IPipeConnection.ConnectOverride.CONNECT;
            default:
                return IPipeConnection.ConnectOverride.DISCONNECT;
        }
    }

    // IPowerEmitter

    @Override
    public boolean canEmitPowerFrom(ForgeDirection side)
    {
        return true;
    }

    // IEnergyHandler

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        return 0;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        return 0;
    }

    @Override
    public boolean canInterface(ForgeDirection from)
    {
        return true;
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        return (int)this.stored.getRFValue();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        return (int)InternalEnergy.convertToRFValue(getMaxStorageSize());
    }
}
