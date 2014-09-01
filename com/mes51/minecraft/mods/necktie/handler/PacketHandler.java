package com.mes51.minecraft.mods.necktie.handler;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.container.ContainerBase;
import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorNecktieBase;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityBase;
import com.mes51.minecraft.mods.necktie.util.Util;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Package: com.mes51.minecraft.mods.necktie.handler
 * Date: 2014/04/19
 * Time: 1:05
 */
public class PacketHandler implements IPacketHandler
{
    public static final String PACKET_ARMOR_COMMAND = "NCArmorCmd";
    public static final String PACKET_SYNC_ITEM_NBT = "NC_SyncItemNBT";
    public static final String PACKET_SYNC_CONTAINER = "NC_SyncContainer";
    public static final String PACKET_SYNC_TILE_ENTITY = "NC_SyncTEntity";

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        if (PACKET_ARMOR_COMMAND.equals(packet.channel) && player instanceof EntityPlayer)
        {
            processArmorCommand((EntityPlayer)player, packet);
        }
        else if (PACKET_SYNC_ITEM_NBT.equals(packet.channel) && player instanceof EntityPlayer)
        {
            syncItemNBT((EntityPlayer)player, packet);
        }
        else if (PACKET_SYNC_CONTAINER.equals(packet.channel) && player instanceof EntityPlayer)
        {
            loadContainerPacket((EntityPlayer)player, packet);
        }
        else if (PACKET_SYNC_TILE_ENTITY.equals(packet.channel))
        {
            World world = getWorld(player, true);
            if (world != null)
            {
                loadTileEntityPacket(packet, world);
            }
        }
    }

    private World getWorld(Player player, boolean preferentClientWorld)
    {
        World world = null;
        if (preferentClientWorld)
        {
            world = Necktie.proxy.getWorld();
            if (world == null)
            {
                world = ((EntityPlayer)player).worldObj;
            }
        }
        else
        {
            world = ((EntityPlayer)player).worldObj;
            if (world == null)
            {
                world = Necktie.proxy.getWorld();
            }
        }
        return world;
    }

    private void processArmorCommand(EntityPlayer player, Packet250CustomPayload packet)
    {
        ByteArrayInputStream stream = null;
        DataInputStream dataInputStream = null;
        try
        {
            stream = new ByteArrayInputStream(packet.data);
            dataInputStream = new DataInputStream(stream);
            ItemArmorNecktieBase.execCommand((NBTTagCompound) NBTTagCompound.readNamedTag(dataInputStream), player);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            IOUtils.closeQuietly(stream);
        }
    }

    public static void issueArmorCommand(NBTTagCompound tagCompound)
    {
        ByteArrayOutputStream stream = null;
        DataOutputStream dataOutputStream = null;
        try
        {
            stream = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(stream);
            NBTTagCompound.writeNamedTag(tagCompound, dataOutputStream);
            PacketDispatcher.sendPacketToServer(new Packet250CustomPayload(PACKET_ARMOR_COMMAND, stream.toByteArray()));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            IOUtils.closeQuietly(stream);
        }
    }

    private void syncItemNBT(EntityPlayer player, Packet250CustomPayload packet)
    {
        if (packet.data.length < 1)
        {
            return;
        }

        ByteArrayInputStream stream = new ByteArrayInputStream(packet.data);
        DataInputStream dataInputStream = new DataInputStream(stream);

        try
        {
            ItemStack itemStack = player.inventory.getStackInSlot(dataInputStream.readInt());
            if (itemStack == null)
            {
                return;
            }

            NBTTagCompound tagCompound = Util.getOrCreateTag(itemStack);
            if (dataInputStream.readBoolean())
            {
                NBTBase nbt = null;
                while ((nbt = NBTBase.readNamedTag(dataInputStream)).getId() != 0)
                {
                    tagCompound.setTag(nbt.getName(), nbt);
                }
            }
            else
            {
                itemStack.setTagCompound(null);
            }
        }
        catch (Exception e)
        {
            // ここに来ることはない...はず
            throw new RuntimeException(e);
        }
    }

    public static void issueSyncItemNBT(World world, EntityPlayer player, int slot)
    {
        ItemStack itemStack = player.inventory.getStackInSlot(slot);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(stream);

        try
        {
            if (itemStack != null)
            {
                NBTTagCompound tagCompound = itemStack.getTagCompound();
                dataOutputStream.writeInt(slot);
                if (tagCompound == null)
                {
                    dataOutputStream.writeBoolean(false);
                }
                else
                {
                    dataOutputStream.writeBoolean(true);
                    for (Object nbt : tagCompound.getTags())
                    {
                        NBTBase.writeNamedTag((NBTBase)nbt, dataOutputStream);
                    }
                    dataOutputStream.writeByte(0);
                }
            }

            Packet250CustomPayload packet = new Packet250CustomPayload(PACKET_SYNC_ITEM_NBT, stream.toByteArray());

            if (!world.isRemote)
            {
                PacketDispatcher.sendPacketToPlayer(packet, getPlayer(player));
            }
            else
            {
                PacketDispatcher.sendPacketToServer(packet);
            }
        }
        catch(Exception e)
        {
            // ここに来ることはない...はず
            throw new RuntimeException(e);
        }
        finally
        {
            IOUtils.closeQuietly(stream);
        }
    }

    public static void issueContainerSyncPacket(ContainerBase container, EntityPlayerMP player)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(stream);

        try
        {
            String className = container.getClass().getName();
            dataOutputStream.writeUTF(className);
            container.writePacketData(dataOutputStream);

            Packet250CustomPayload packet = new Packet250CustomPayload(PACKET_SYNC_CONTAINER, stream.toByteArray());
            PacketDispatcher.sendPacketToPlayer(packet, getPlayer(player));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            IOUtils.closeQuietly(stream);
        }
    }

    private void loadContainerPacket(EntityPlayer player, Packet250CustomPayload packet)
    {
        ByteArrayInputStream stream = new ByteArrayInputStream(packet.data);
        DataInputStream dataInputStream = new DataInputStream(stream);

        try
        {
            if (player.openContainer.getClass().getName().equals(dataInputStream.readUTF()))
            {
                ((ContainerBase)player.openContainer).readPacketData(dataInputStream);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private static Player getPlayer(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            return (Player)((EntityPlayerMP)player).playerNetServerHandler.getPlayer();
        }
        else if (player instanceof Player)
        {
            return (Player)player;
        }
        return null;
    }

    public static Packet250CustomPayload createTileEntityPacket(TileEntityBase entity)
    {
        String channel = entity.getClass().getSimpleName();
        byte[] channelData = channel.getBytes();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(stream);
        try
        {
            dataOutputStream.writeInt(channelData.length);
            dataOutputStream.write(channelData);
            dataOutputStream.writeInt(entity.xCoord);
            dataOutputStream.writeInt(entity.yCoord);
            dataOutputStream.writeInt(entity.zCoord);
            entity.writePacketData(dataOutputStream);
            dataOutputStream.flush();
        }
        catch(Exception e)
        {
            // ここに来ることはない...はず
            throw new RuntimeException(e);
        }

        return new Packet250CustomPayload(PACKET_SYNC_TILE_ENTITY, stream.toByteArray());
    }

    private void loadTileEntityPacket(Packet250CustomPayload packet, World world)
    {
        ByteArrayInputStream stream = new ByteArrayInputStream(packet.data);
        DataInputStream dataInputStream = new DataInputStream(stream);

        try
        {
            byte[] channelNameData = new byte[dataInputStream.readInt()];
            dataInputStream.read(channelNameData);
            String channel = new String(channelNameData);
            int x = dataInputStream.readInt();
            int y = dataInputStream.readInt();
            int z = dataInputStream.readInt();
            TileEntity entity = world.getBlockTileEntity(x, y, z);
            if (entity != null && entity instanceof TileEntityBase && entity.getClass().getSimpleName().equals(channel))
            {
                ((TileEntityBase)entity).readPacketData(dataInputStream);
            }
        }
        catch (Exception e)
        {
            // ここに来ることはない...はず
            throw new RuntimeException(e);
        }
    }
}
