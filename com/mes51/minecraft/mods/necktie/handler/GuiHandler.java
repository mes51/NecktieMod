package com.mes51.minecraft.mods.necktie.handler;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.util.GuiId;
import com.mes51.minecraft.mods.necktie.util.Vec3i;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Package: com.mes51.minecraft.mods.necktie.handler
 * Date: 2014/05/02
 * Time: 0:14
 */
public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID > 0)
        {
            TileEntity entity = world.getBlockTileEntity(x, y, z);
            return GuiId.byId(ID).createContainer(player.inventory, entity);
        }
        else
        {
            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID > 0)
        {
            Vec3i pos = new Vec3i(x, y, z);
            return Necktie.proxy.createGui(ID, player.inventory, pos);
        }
        else
        {
            return null;
        }
    }
}
