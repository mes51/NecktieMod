package com.mes51.minecraft.mods.necktie.tileentity.machine;

import com.mes51.minecraft.mods.necktie.block.BlockMachine;
import com.mes51.minecraft.mods.necktie.gui.window.WindowBase;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityMachineBlock;
import com.mes51.minecraft.mods.necktie.util.Vec3i;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

import java.lang.reflect.Constructor;

/**
 * Package: com.mes51.minecraft.mods.necktie.tileentity.machine
 * Date: 2014/05/01
 * Time: 21:15
 */
public abstract class MachineTileEntityBase implements IWrenchable
{
    protected TileEntityMachineBlock tileEntity = null;
    protected Vec3i pos = null;

    public static MachineTileEntityBase newInstance(Class<? extends MachineTileEntityBase> klass, TileEntityMachineBlock tileEntity)
    {
        try
        {
            Constructor<? extends MachineTileEntityBase> ctor = klass.getDeclaredConstructor(TileEntityMachineBlock.class);
            return ctor.newInstance(tileEntity);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public MachineTileEntityBase(TileEntityMachineBlock tileEntity)
    {
        this.tileEntity = tileEntity;
        this.pos = new Vec3i(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
    }

    public void update() { }

    public void updateBlock() { }

    public void onNeighborTileChange() { }

    public abstract Class<? extends WindowBase<TileEntityMachineBlock>> getWindowClass();

    public abstract void writeToNBT(NBTTagCompound tagCompound);

    public abstract void readFromNBT(NBTTagCompound tagCompound);

    // IWrenchable

    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        ForgeDirection now = ForgeDirection.getOrientation(getFacing());
        return dir != ForgeDirection.UP && dir != ForgeDirection.DOWN && now != dir;
    }

    @Override
    public short getFacing()
    {
        return (short)this.tileEntity.worldObj.getBlockMetadata(this.pos.x, this.pos.y, this.pos.z);
    }

    @Override
    public void setFacing(short facing)
    {
        this.tileEntity.worldObj.setBlockMetadataWithNotify(this.pos.x, this.pos.y, this.pos.z, facing, 3);
    }

    @Override
    public boolean wrenchCanRemove(EntityPlayer entityPlayer)
    {
        return false;
    }

    @Override
    public float getWrenchDropRate()
    {
        return 1.0F;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
    {
        ItemStack result = new ItemStack(BlockMachine.getInstance(), 1);
        result.setItemDamage(BlockMachine.SubType.byClass(this.getClass()).ordinal());
        return result;
    }
}