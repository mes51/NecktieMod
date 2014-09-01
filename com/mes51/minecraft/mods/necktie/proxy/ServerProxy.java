package com.mes51.minecraft.mods.necktie.proxy;

import com.mes51.minecraft.mods.necktie.util.Vec3i;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Package: com.mes51.minecraft.mods.necktie.proxy
 * Date: 2014/04/28
 * Time: 22:42
 */
public class ServerProxy implements IProxy
{
    @Override
    public void registerRender() { }

    @Override
    public void registerKey() { }

    @Override
    public boolean forwardKeyIsPressed()
    {
        return false;
    }

    @Override
    public boolean backKeyIsPressed()
    {
        return false;
    }

    @Override
    public boolean leftKeyIsPressed()
    {
        return false;
    }

    @Override
    public boolean rightKeyIsPressed()
    {
        return false;
    }

    @Override
    public void addKey(String keyName, int keyCode) { }

    @Override
    public boolean keyPressed(String keyName)
    {
        return false;
    }

    @Override
    public boolean keyPushed(String keyName)
    {
        return false;
    }

    @Override
    public int getArmorRenderPrefix(String name)
    {
        return 0;
    }

    @Override
    public int getBlockRenderId(Block block)
    {
        return -1;
    }

    @Override
    public World getWorld()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
    }

    @Override
    public Object createGui(int id, InventoryPlayer playerInventory, Vec3i tileEntityPos)
    {
        return null;
    }
}
