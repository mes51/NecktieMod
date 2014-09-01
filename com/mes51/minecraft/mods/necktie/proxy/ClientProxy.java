package com.mes51.minecraft.mods.necktie.proxy;

import com.mes51.minecraft.mods.necktie.block.BlockNecktie;
import com.mes51.minecraft.mods.necktie.block.BlockNecktieBeacon;
import com.mes51.minecraft.mods.necktie.gui.GuiBase;
import com.mes51.minecraft.mods.necktie.gui.GuiMachineBlock;
import com.mes51.minecraft.mods.necktie.gui.GuiNecktieBeacon;
import com.mes51.minecraft.mods.necktie.render.RenderEntityAttackEffect;
import com.mes51.minecraft.mods.necktie.render.RenderNecktieBeacon;
import com.mes51.minecraft.mods.necktie.render.RenderNecktieBlock;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityBase;
import com.mes51.minecraft.mods.necktie.util.GuiId;
import com.mes51.minecraft.mods.necktie.util.Vec3i;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Package: com.mes51.minecraft.mods.necktie.proxy
 * Date: 2014/04/28
 * Time: 22:55
 */
public class ClientProxy implements IProxy
{
    private static Map<Integer, Class<? extends GuiBase<? extends TileEntityBase>>> GUI_MAP = null;

    private KeyBinding forwardKey = null;
    private KeyBinding backKey = null;
    private KeyBinding leftKey = null;
    private KeyBinding rightKey = null;
    private Map<String, KeyBinding> keys = new HashMap<String, KeyBinding>();
    private Map<Block, Integer> blockRenderIds = new HashMap<Block, Integer>();

    static
    {
        GUI_MAP = new HashMap<Integer, Class<? extends GuiBase<? extends TileEntityBase>>>();

        GUI_MAP.put(GuiId.MACHINE_BASE.getGuiId(), GuiMachineBlock.class);
        GUI_MAP.put(GuiId.NECKTIE_BEACON.getGuiId(), GuiNecktieBeacon.class);
    }

    @Override
    public void registerRender()
    {
        RenderEntityAttackEffect.register();

        this.blockRenderIds.put(BlockNecktie.getInstance(), RenderNecktieBlock.register());
        this.blockRenderIds.put(BlockNecktieBeacon.getInstance(), RenderNecktieBeacon.register());
    }

    @Override
    public void registerKey()
    {
        this.forwardKey = Minecraft.getMinecraft().gameSettings.keyBindForward;
        this.backKey = Minecraft.getMinecraft().gameSettings.keyBindBack;
        this.leftKey = Minecraft.getMinecraft().gameSettings.keyBindLeft;
        this.rightKey = Minecraft.getMinecraft().gameSettings.keyBindRight;
    }

    @Override
    public boolean forwardKeyIsPressed()
    {
        return this.forwardKey.pressed;
    }

    @Override
    public boolean backKeyIsPressed()
    {
        return this.backKey.pressed;
    }

    @Override
    public boolean leftKeyIsPressed()
    {
        return this.leftKey.pressed;
    }

    @Override
    public boolean rightKeyIsPressed()
    {
        return this.rightKey.pressed;
    }

    @Override
    public void addKey(String keyName, int keyCode)
    {
        this.keys.put(keyName, new KeyBinding(keyName, keyCode));
    }

    @Override
    public boolean keyPressed(String keyName)
    {
        return this.keys.get(keyName).pressed;
    }

    @Override
    public boolean keyPushed(String keyName)
    {
        return this.keys.get(keyName).isPressed();
    }

    @Override
    public int getArmorRenderPrefix(String name)
    {
        return RenderingRegistry.addNewArmourRendererPrefix(name);
    }

    @Override
    public int getBlockRenderId(Block block)
    {
        return this.blockRenderIds.get(block);
    }

    @Override
    public World getWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public Object createGui(int id, InventoryPlayer playerInventory, Vec3i tileEntityPos)
    {
        try
        {
            Constructor constructor = GUI_MAP.get(id).getConstructor(IInventory.class, Vec3i.class);
            return constructor.newInstance(playerInventory, tileEntityPos);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
