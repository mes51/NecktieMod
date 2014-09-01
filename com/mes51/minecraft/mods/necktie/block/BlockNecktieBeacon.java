package com.mes51.minecraft.mods.necktie.block;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityBase;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityNecktieBeacon;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.GuiId;
import com.mes51.minecraft.mods.necktie.util.Util;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

/**
 * Package: com.mes51.minecraft.mods.necktie.block
 * Date: 2014/05/20
 * Time: 0:56
 */
public class BlockNecktieBeacon extends BlockDisplayableBase
{
    private static Block instance = null;

    public static void register(int blockId)
    {
        instance = new BlockNecktieBeacon(blockId);

        GameRegistry.registerBlock(instance, Const.Block.BlockName.NECKTIE_BEACON);
        LanguageRegistry.addName(instance, "Necktie Beacon");
        GameRegistry.registerTileEntity(TileEntityNecktieBeacon.class, TileEntityNecktieBeacon.class.getSimpleName());
    }

    public static void registerRecipe()
    {
        GameRegistry.addRecipe(
                new ItemStack(instance, 1),
                new Object[] {
                        "GGG",
                        "G G",
                        "EBE",
                        'G', new ItemStack(Block.glass, 1),
                        'E', new ItemStack(Block.blockEmerald, 1),
                        'B', new ItemStack(Block.beacon, 1)
                }
        );
    }

    public static Block getInstance()
    {
        return instance;
    }

    protected BlockNecktieBeacon(int par1)
    {
        super(par1, Material.glass);
        setHardness(1.0F);
        setUnlocalizedName(Const.Block.BlockName.NECKTIE_BEACON);
    }

    @Override
    protected Class<? extends TileEntityBase> getTileEntityClass()
    {
        return TileEntityNecktieBeacon.class;
    }

    @Override
    protected GuiId getGuiId()
    {
        return GuiId.NECKTIE_BEACON;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityNecktieBeacon();
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return Necktie.proxy.getBlockRenderId(this);
    }

    @Override
    public Icon getIcon(int par1, int par2)
    {
        return Block.glass.getIcon(par1, par2);
    }

    @Override
    public void breakBlock(World world, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntity entity = world.getBlockTileEntity(par2, par3, par4);
        if (entity != null && entity.getClass().equals(getTileEntityClass()))
        {
            IInventory tTileEntity = (IInventory)entity;
            for (int i = 0; i < tTileEntity.getSizeInventory(); i++)
            {
                ItemStack itemStack = tTileEntity.getStackInSlot(i);
                if (itemStack != null)
                {
                    Util.dropItem(itemStack, world, par2, par3, par4);
                }
            }
        }
        super.breakBlock(world, par2, par3, par4, par5, par6);
    }
}
