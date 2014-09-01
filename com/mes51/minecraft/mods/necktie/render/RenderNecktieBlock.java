package com.mes51.minecraft.mods.necktie.render;

import com.mes51.minecraft.mods.necktie.block.BlockNecktie;
import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorDiamondNecktie;
import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorIronNecktie;
import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorNecktie;
import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorNukeNecktie;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityNecktieBlock;
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
 * Date: 2014/05/18
 * Time: 13:59
 */
public class RenderNecktieBlock extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler
{
    private static final double POSITION_ITEM_XZ = 0.5;
    private static final double POSITION_ITEM_Y = 0.05;
    private static final double SCALE_ITEM = 1.8;
    private static int RENDER_ID = -1;

    private long start = 0;
    private Random rand = null;
    private EntityItem dummyItem = null;
    private RenderItem renderer = null;

    public static int register()
    {
        RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
        RenderNecktieBlock renderer = new RenderNecktieBlock();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNecktieBlock.class, renderer);
        RenderingRegistry.registerBlockHandler(renderer);
        return RENDER_ID;
    }

    public RenderNecktieBlock()
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
        if (tileentity != null && tileentity instanceof TileEntityNecktieBlock)
        {
            this.rand.setSeed(tileentity.hashCode());
            GL11.glPushMatrix();
            GL11.glTranslated(d0, d1, d2);
            GL11.glTranslated(POSITION_ITEM_XZ, 0.0, POSITION_ITEM_XZ);
            GL11.glRotated((System.currentTimeMillis() - this.start + rand.nextInt(1000)) * 0.09, 0.0, 1.0, 0.0);
            GL11.glScaled(SCALE_ITEM, SCALE_ITEM, SCALE_ITEM);
            GL11.glTranslated(0.0, POSITION_ITEM_Y, 0.0);

            Item item = getInnerItem(tileentity.getBlockMetadata());
            this.dummyItem.setEntityItemStack(new ItemStack(item, 1));

            if (RenderManager.instance.renderEngine != null)
            {
                this.renderer.doRender(this.dummyItem, 0.0, 0.0, 0.0, 0.0F, 0.0F);
            }

            GL11.glPopMatrix();
        }
    }

    private Item getInnerItem(int metadata)
    {
        Item innerItem = null;
        switch (BlockNecktie.SubType.byDamage(metadata))
        {
            case NECKTIE:
                innerItem = ItemArmorNecktie.getInstance();
                break;
            case IRON_NECKTIE:
                innerItem = ItemArmorIronNecktie.getInstance();
                break;
            case DIAMOND_NECKTIE:
                innerItem = ItemArmorDiamondNecktie.getInstance();
                break;
            case NUKE_NECKTIE:
                innerItem = ItemArmorNukeNecktie.getInstance();
                break;
        }
        return innerItem;
    }

    // ISimpleBlockRenderingHandler

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        if (modelID == RENDER_ID)
        {
            renderer.renderBlockAsItem(Block.glass, 0, 1.0F);

            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glScaled(SCALE_ITEM, SCALE_ITEM, SCALE_ITEM);
            GL11.glTranslated(0.0, -POSITION_ITEM_Y * 4, 0.0);

            Item item = getInnerItem(metadata);
            this.dummyItem.setEntityItemStack(new ItemStack(item, 1));

            if (RenderManager.instance.renderEngine != null)
            {
                this.renderer.doRender(this.dummyItem, 0.0, 0.0, 0.0, 0.0F, 0.0F);
            }

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if (modelId == RENDER_ID)
        {
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
