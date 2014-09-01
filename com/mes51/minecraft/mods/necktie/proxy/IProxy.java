package com.mes51.minecraft.mods.necktie.proxy;

import com.mes51.minecraft.mods.necktie.util.Vec3i;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Package: com.mes51.minecraft.mods.necktie.proxy
 * Date: 2014/04/28
 * Time: 22:43
 */
public interface IProxy
{
    void registerRender();

    void registerKey();

    boolean forwardKeyIsPressed();

    boolean backKeyIsPressed();

    boolean leftKeyIsPressed();

    boolean rightKeyIsPressed();

    void addKey(String keyName, int keyCode);

    boolean keyPressed(String keyName);

    boolean keyPushed(String keyName);

    int getArmorRenderPrefix(String name);

    int getBlockRenderId(Block block);

    World getWorld();

    Object createGui(int id, InventoryPlayer playerInventory, Vec3i tileEntityPos);
}
