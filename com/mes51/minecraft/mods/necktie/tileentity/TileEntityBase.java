package com.mes51.minecraft.mods.necktie.tileentity;

import com.mes51.minecraft.mods.necktie.handler.PacketHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Package: com.mes51.minecraft.mods.necktie.tileentity
 * Date: 2014/05/02
 * Time: 0:33
 */
public class TileEntityBase extends TileEntity
{
    protected static final int DEFAULT_BLOCK_UPDATE_COUNT_INTERVAL = 60;

    private int blockUpdateCount = 0;
    private int blockUpdateCountInterval = DEFAULT_BLOCK_UPDATE_COUNT_INTERVAL;
    private boolean enableAutoBlockUpdate = false;

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (this.enableAutoBlockUpdate)
        {
            this.blockUpdateCount++;
            if (this.blockUpdateCount > this.blockUpdateCountInterval)
            {
                updateBlock();
            }
        }
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return PacketHandler.createTileEntityPacket(this);
    }

    public void setEnableAutoBlockUpdate(boolean enableAutoBlockUpdate)
    {
        this.enableAutoBlockUpdate = enableAutoBlockUpdate;
    }

    protected void updateBlock()
    {
        this.blockUpdateCount = 0;
        if (this.worldObj != null)
        {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    public void readPacketData(DataInputStream stream) throws IOException { }

    public void writePacketData(DataOutputStream stream) throws IOException { }
}
