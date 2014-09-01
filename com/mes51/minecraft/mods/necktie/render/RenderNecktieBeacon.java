package com.mes51.minecraft.mods.necktie.render;

import com.mes51.minecraft.mods.necktie.tileentity.TileEntityNecktieBeacon;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Package: com.mes51.minecraft.mods.necktie.render
 * Date: 2014/05/23
 * Time: 0:00
 */
public class RenderNecktieBeacon extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler
{
    private static int RENDER_ID = -1;

    private long start = 0;
    private Random rand = null;
    private EntityItem dummyItem = null;
    private RenderItem renderer = null;

    public static int register()
    {
        RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
        RenderNecktieBeacon renderer = new RenderNecktieBeacon();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNecktieBeacon.class, renderer);
        RenderingRegistry.registerBlockHandler(renderer);
        return RENDER_ID;
    }

    public RenderNecktieBeacon()
    {
        this.start = System.currentTimeMillis();
        this.rand = new Random();
        this.dummyItem = new EntityItem(null);
        this.renderer = new RenderItem();
        this.renderer.setRenderManager(RenderManager.instance);
        this.dummyItem.hoverStart = 0.0F;
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
    {
        if (RenderManager.instance.renderEngine != null && tileentity != null && tileentity instanceof TileEntityNecktieBeacon)
        {
            TileEntityNecktieBeacon tileEntityNecktieBeacon = (TileEntityNecktieBeacon)tileentity;
            if (tileEntityNecktieBeacon.getStackInSlot(0) != null)
            {
                Item item = tileEntityNecktieBeacon.getStackInSlot(0).getItem();
                this.dummyItem.setEntityItemStack(new ItemStack(item, 1));

                this.rand.setSeed(tileEntityNecktieBeacon.hashCode());
                GL11.glPushMatrix();
                GL11.glTranslated(d0, d1, d2);
                GL11.glTranslated(0.5, 0.0, 0.5);
                GL11.glRotated((System.currentTimeMillis() - this.start + rand.nextInt(1000)) * 0.09, 0.0, 1.0, 0.0);
                GL11.glScaled(1.5, 1.5, 1.5);
                GL11.glTranslated(0.0, 0.2, 0.0);

                this.renderer.doRender(this.dummyItem, 0.0, 0.0, 0.0, 0.0F, 0.0F);
                GL11.glPopMatrix();

                if (tileEntityNecktieBeacon.isValidStructure())
                {
                    this.rand.setSeed(tileEntityNecktieBeacon.hashCode());
                    GL11.glPushMatrix();
                    GL11.glTranslated(d0, d1, d2);
                    GL11.glTranslated(0.5, 0.0, 0.5);
                    GL11.glRotated((System.currentTimeMillis() - this.start + rand.nextInt(1000)) * 0.09, 0.0, 1.0, 0.0);
                    GL11.glScaled(15.0, 15.0, 15.0);
                    GL11.glTranslated(0.0, 0.1, 0.0);

                    this.renderer.doRender(this.dummyItem, 0.0, 0.0, 0.0, 0.0F, 0.0F);
                    GL11.glPopMatrix();
                }
            }
        }
    }

    // ISimpleBlockRenderingHandler

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        if (modelID == RENDER_ID)
        {
            GL11.glPushMatrix();

            GL11.glScaled(0.75, 0.25, 0.75);
            GL11.glTranslated(0.0, -1.5, 0.0);
            renderer.renderBlockAsItem(Block.blockEmerald, 0, 1.0F);

            GL11.glPopMatrix();

            renderer.renderBlockAsItem(Block.glass, 0, 1.0F);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if (modelId == RENDER_ID)
        {
            renderer.setRenderBounds(0.125, 0.0005, 0.125, 0.875, 0.25, 0.875);
            renderer.renderStandardBlock(Block.blockEmerald, x, y, z);

            renderer.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            renderer.renderStandardBlock(Block.glass, x, y, z);
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean shouldRender3DInInventory()
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return RENDER_ID;
    }
}
