package com.mes51.minecraft.mods.necktie.tileentity.machine;

import com.mes51.minecraft.mods.necktie.gui.window.WindowBase;
import com.mes51.minecraft.mods.necktie.gui.window.WindowSquatGenerator;
import com.mes51.minecraft.mods.necktie.power.InternalEnergy;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityMachineBlock;
import com.mes51.minecraft.mods.necktie.util.Util;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

/**
 * Package: com.mes51.minecraft.mods.necktie.tileentity.machine
 * Date: 2014/05/01
 * Time: 21:24
 */
public class MachineTileEntitySquatGenerator extends MachineTileEntityGeneratorBase
{
    private static final int MAX_STORAGE_SIZE = 400000;
    private static final int MAX_PRESSURE = 128;
    private static final double GENERATE_PER_STEP = 120.0;
    private static final double XP_GENERATE_RATE = 0.2;

    private int squatCount = 0;
    private boolean playerIsSneaking = false;

    public MachineTileEntitySquatGenerator(TileEntityMachineBlock tileEntity)
    {
        super(tileEntity);
    }

    @Override
    public void update()
    {
        super.update();

        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(this.pos.x, this.pos.y, this.pos.z, this.pos.x + 1.0, this.pos.y + 2.0, this.pos.z + 1.0);
        List players = this.tileEntity.worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, aabb);
        if (players.size() > 0)
        {
            EntityPlayerMP player = (EntityPlayerMP)players.get(0);

            if (player.isSneaking())
            {
                this.playerIsSneaking = true;
            }
            else if (this.playerIsSneaking)
            {
                this.playerIsSneaking = false;
                this.squatCount++;
                double generate = GENERATE_PER_STEP;
                if (Util.playerIsMuscle(player))
                {
                    generate *= 2.0;
                }
                this.stored = new InternalEnergy(Math.min(this.stored.getValue() + generate, MAX_STORAGE_SIZE));

                if (this.tileEntity.worldObj.rand.nextDouble() < XP_GENERATE_RATE)
                {
                    this.tileEntity.worldObj.spawnEntityInWorld(
                            new EntityXPOrb(this.tileEntity.worldObj, this.pos.x + 0.5, this.pos.y + 0.5, this.pos.z + 0.5, 1)
                    );
                }
            }
        }
        else
        {
            this.playerIsSneaking = false;
        }
    }

    @Override
    public Class<? extends WindowBase<TileEntityMachineBlock>> getWindowClass()
    {
        return WindowSquatGenerator.class;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("squatCount", this.squatCount);
        tagCompound.setBoolean("playerIsSneaking", this.playerIsSneaking);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        this.squatCount = tagCompound.getInteger("squatCount");
        this.playerIsSneaking = tagCompound.getBoolean("playerIsSneaking");
    }

    @Override
    protected double getOfferedEnergyInternal()
    {
        return Math.min(MAX_PRESSURE, this.stored.getValue());
    }

    @Override
    protected double getMaxStorageSize()
    {
        return MAX_STORAGE_SIZE;
    }

    public double getStored()
    {
        return this.stored.getEUValue();
    }

    public int getSquatCount()
    {
        return this.squatCount;
    }
}
