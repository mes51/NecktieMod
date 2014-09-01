package com.mes51.minecraft.mods.necktie.block;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityBase;
import com.mes51.minecraft.mods.necktie.util.GuiId;
import com.mes51.minecraft.mods.necktie.util.NecktieCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Package: com.mes51.minecraft.mods.necktie.block
 * Date: 2014/05/20
 * Time: 0:57
 */
public abstract class BlockDisplayableBase extends BlockContainer
{
    private String realUnlocalizedName = "";

    protected BlockDisplayableBase(int par1, Material par2Material)
    {
        super(par1, par2Material);
        setCreativeTab(NecktieCreativeTabs.instance);
    }

    protected abstract Class<? extends TileEntityBase> getTileEntityClass();

    protected abstract GuiId getGuiId();

    public void openGui(World world, EntityPlayer player, int x, int y, int z)
    {
        TileEntity entity = world.getBlockTileEntity(x, y, z);
        if (!world.isRemote && entity != null && entity.getClass().equals(getTileEntityClass()) && !player.isSneaking())
        {
            player.openGui(Necktie.instance, getGuiId().getGuiId(), world, x, y, z);
        }
    }

    @Override
    public Block setUnlocalizedName(String par1Str)
    {
        this.realUnlocalizedName = par1Str;
        return super.setUnlocalizedName(par1Str);
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        openGui(par1World, par5EntityPlayer, par2, par3, par4);
        return !par5EntityPlayer.isSneaking();
    }

    protected String getRealUnlocalizedName()
    {
        return this.realUnlocalizedName;
    }
}
