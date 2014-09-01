package com.mes51.minecraft.mods.necktie.tileentity;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile;
import cofh.api.energy.IEnergyHandler;
import com.mes51.minecraft.mods.necktie.tileentity.machine.MachineTileEntityBase;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.NetworkHelper;
import ic2.api.tile.IEnergyStorage;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Package: com.mes51.minecraft.mods.necktie.tileentity
 * Date: 2014/05/01
 * Time: 13:03
 */
public class TileEntityMachineBlock extends TileEntityBase implements IWrenchable, IEnergySource, IEnergyStorage, IEnergySink, INetworkDataProvider, IPipeConnection, IPowerEmitter, IEnergyHandler
{
    private static List<String> field = new ArrayList<String>();

    private Class<? extends MachineTileEntityBase> machineTileEntityClass = null;
    private MachineTileEntityBase machineTileEntity = null;
    private boolean initialized = false;

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        initialize();
        if (this.machineTileEntity != null)
        {
            this.machineTileEntity.update();
        }
    }

    @Override
    public void invalidate()
    {
        EnergyTileUnloadEvent unloadEvent = new EnergyTileUnloadEvent(this);
        MinecraftForge.EVENT_BUS.post(unloadEvent);
        super.invalidate();
    }

    @Override
    protected void updateBlock()
    {
        super.updateBlock();
        if (this.machineTileEntity != null)
        {
            this.machineTileEntity.updateBlock();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        if (this.machineTileEntityClass != null)
        {
            par1NBTTagCompound.setString("machineTileEntityClass", this.machineTileEntityClass.getName());
            NBTTagCompound tagCompound = new NBTTagCompound();
            this.machineTileEntity.writeToNBT(tagCompound);
            par1NBTTagCompound.setTag("machineTileEntity", tagCompound);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        if (par1NBTTagCompound.hasKey("machineTileEntityClass"))
        {
            try
            {
                this.machineTileEntityClass = (Class<? extends MachineTileEntityBase>)Class.forName(par1NBTTagCompound.getString("machineTileEntityClass"));
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            initMachineTileEntity();
            if (this.machineTileEntity != null)
            {
                this.machineTileEntity.readFromNBT((NBTTagCompound)par1NBTTagCompound.getTag("machineTileEntity"));
            }
        }
    }

    @Override
    public void writePacketData(DataOutputStream stream) throws IOException
    {
        super.writePacketData(stream);
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        NBTTagCompound.writeNamedTag(tagCompound, stream);
    }

    @Override
    public void readPacketData(DataInputStream stream) throws IOException
    {
        super.readPacketData(stream);
        readFromNBT((NBTTagCompound)NBTTagCompound.readNamedTag(stream));
    }

    public void setMachineTileEntityClass(Class<? extends MachineTileEntityBase> klass)
    {
        this.machineTileEntityClass = klass;
        initMachineTileEntity();
    }

    public MachineTileEntityBase getMachineTileEntity()
    {
        return this.machineTileEntity;
    }

    public void onNeighborTileChange()
    {
        if (this.machineTileEntity != null)
        {
            this.machineTileEntity.onNeighborTileChange();
        }
    }

    private void initMachineTileEntity()
    {
        if (this.machineTileEntityClass != null && (this.machineTileEntity == null || !this.machineTileEntity.getClass().equals(this.machineTileEntityClass)))
        {
            this.machineTileEntity = MachineTileEntityBase.newInstance(this.machineTileEntityClass, this);
            updateBlock();
        }
    }

    private void initialize()
    {
        if (!this.initialized && this.worldObj != null)
        {
            updateBlock();
            if (this.worldObj.isRemote)
            {
                NetworkHelper.requestInitialData(this);
            }
            else
            {
                EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
                MinecraftForge.EVENT_BUS.post(loadEvent);
            }
            initialized = true;
        }
    }

    // IWrenchable

    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
    {
        return this.machineTileEntity.wrenchCanSetFacing(entityPlayer, side);
    }

    @Override
    public short getFacing()
    {
        return this.machineTileEntity.getFacing();
    }

    @Override
    public void setFacing(short facing) { this.machineTileEntity.setFacing(facing); }

    @Override
    public boolean wrenchCanRemove(EntityPlayer entityPlayer)
    {
        return this.machineTileEntity.wrenchCanRemove(entityPlayer);
    }

    @Override
    public float getWrenchDropRate()
    {
        return this.machineTileEntity.getWrenchDropRate();
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
    {
        return this.machineTileEntity.getWrenchDrop(entityPlayer);
    }

    // IEnergySource

    @Override
    public double getOfferedEnergy()
    {
        if (this.machineTileEntity instanceof IEnergySource)
        {
            return ((IEnergySource)this.machineTileEntity).getOfferedEnergy();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public void drawEnergy(double amount)
    {
        if (this.machineTileEntity instanceof IEnergySource)
        {
            ((IEnergySource)this.machineTileEntity).drawEnergy(amount);
        }
    }

    // IEnergyEmitter

    @Override
    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction)
    {
        return this.machineTileEntity instanceof IEnergyEmitter && ((IEnergyEmitter) this.machineTileEntity).emitsEnergyTo(receiver, direction);
    }

    // IEnergyStorage

    @Override
    public int getStored()
    {
        if (this.machineTileEntity instanceof IEnergyStorage)
        {
            return ((IEnergyStorage)this.machineTileEntity).getStored();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public void setStored(int energy)
    {
        if (this.machineTileEntity instanceof IEnergyStorage)
        {
            ((IEnergyStorage)this.machineTileEntity).setStored(energy);
        }
    }

    @Override
    public int addEnergy(int amount)
    {
        if (this.machineTileEntity instanceof IEnergyStorage)
        {
            return ((IEnergyStorage)this.machineTileEntity).addEnergy(amount);
        }
        else
        {
            return 0;
        }
    }

    @Override
    public int getCapacity()
    {
        if (this.machineTileEntity instanceof IEnergyStorage)
        {
            return ((IEnergyStorage)this.machineTileEntity).getCapacity();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public int getOutput()
    {
        if (this.machineTileEntity instanceof IEnergyStorage)
        {
            return ((IEnergyStorage)this.machineTileEntity).getOutput();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public double getOutputEnergyUnitsPerTick()
    {
        if (this.machineTileEntity instanceof IEnergyStorage)
        {
            return ((IEnergyStorage)this.machineTileEntity).getOutputEnergyUnitsPerTick();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public boolean isTeleporterCompatible(ForgeDirection side)
    {
        return this.machineTileEntity instanceof IEnergyStorage && ((IEnergyStorage) this.machineTileEntity).isTeleporterCompatible(side);
    }

    // IEnergySink

    @Override
    public double demandedEnergyUnits()
    {
        if (this.machineTileEntity instanceof IEnergySink)
        {
            return ((IEnergySink)this.machineTileEntity).demandedEnergyUnits();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public double injectEnergyUnits(ForgeDirection directionFrom, double amount)
    {
        if (this.machineTileEntity instanceof IEnergySink)
        {
            return ((IEnergySink) this.machineTileEntity).injectEnergyUnits(directionFrom, amount);
        }
        else
        {
            return 0;
        }
    }

    @Override
    public int getMaxSafeInput()
    {
        if (this.machineTileEntity instanceof IEnergySink)
        {
            return ((IEnergySink)this.machineTileEntity).getMaxSafeInput();
        }
        else
        {
            return 0;
        }
    }

    // IEnergyAcceptor

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
    {
        return this.machineTileEntity instanceof IEnergyAcceptor && ((IEnergyAcceptor) this.machineTileEntity).acceptsEnergyFrom(emitter, direction);
    }

    // INetworkDataProvider

    @Override
    public List<String> getNetworkedFields()
    {
        return field;
    }

    // IPipeConnection

    @Override
    public ConnectOverride overridePipeConnection(IPipeTile.PipeType type, ForgeDirection with)
    {
        if (this.machineTileEntity instanceof IPipeConnection)
        {
            return ((IPipeConnection)this.machineTileEntity).overridePipeConnection(type, with);
        }
        else
        {
            return ConnectOverride.DISCONNECT;
        }
    }

    // IPowerEmitter

    @Override
    public boolean canEmitPowerFrom(ForgeDirection side)
    {
        if (this.machineTileEntity instanceof IPowerEmitter)
        {
            return ((IPowerEmitter)this.machineTileEntity).canEmitPowerFrom(side);
        }
        else
        {
            return false;
        }
    }

    // IEnergyHandler

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        if (this.machineTileEntity instanceof IEnergyHandler)
        {
            return ((IEnergyHandler)this.machineTileEntity).receiveEnergy(from, maxReceive, simulate);
        }
        else
        {
            return 0;
        }
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        if (this.machineTileEntity instanceof IEnergyHandler)
        {
            return ((IEnergyHandler)this.machineTileEntity).extractEnergy(from, maxExtract, simulate);
        }
        else
        {
            return 0;
        }
    }

    @Override
    public boolean canInterface(ForgeDirection from)
    {
        if (this.machineTileEntity instanceof IEnergyHandler)
        {
            return ((IEnergyHandler)this.machineTileEntity).canInterface(from);
        }
        else
        {
            return false;
        }
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        if (this.machineTileEntity instanceof IEnergyHandler)
        {
            return ((IEnergyHandler)this.machineTileEntity).getEnergyStored(from);
        }
        else
        {
            return 0;
        }
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        if (this.machineTileEntity instanceof IEnergyHandler)
        {
            return ((IEnergyHandler)this.machineTileEntity).getMaxEnergyStored(from);
        }
        else
        {
            return 0;
        }
    }
}
