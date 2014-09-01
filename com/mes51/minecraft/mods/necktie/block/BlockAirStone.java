package com.mes51.minecraft.mods.necktie.block;

import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.NecktieCreativeTabs;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Random;

/**
 * Package: com.mes51.minecraft.mods.necktie.block
 * Date: 2014/05/09
 * Time: 23:24
 */
public class BlockAirStone extends Block
{
    private static final double DROP_RATE = 0.05;
    private static final int EXIST_TIME = 10;

    private static Block instance = null;

    public static void register(int blockId)
    {
        instance = new BlockAirStone(blockId);

        GameRegistry.registerBlock(instance, Const.Block.BlockName.AIR_STONE);
        LanguageRegistry.addName(instance, "Air Block");
        MinecraftForge.setBlockHarvestLevel(instance, "pickaxe", 1);
    }

    public static void setInWorld(World world, int x, int y, int z)
    {
        world.setBlock(x, y, z, instance.blockID);
        world.scheduleBlockUpdate(x, y, z, instance.blockID, instance.tickRate(world));
    }

    public static Block getInstance()
    {
        return instance;
    }

    public BlockAirStone(int par1)
    {
        super(par1, Material.rock);
        setHardness(2.0F);
        setCreativeTab(NecktieCreativeTabs.instance);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Const.Block.BlockName.AIR_STONE);
        setTextureName(Const.Block.BlockName.AIR_STONE);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
    {
        return false;
    }

    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        int metadata = par1World.getBlockMetadata(par2, par3, par4);
        if (metadata < EXIST_TIME)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, metadata + 1, 3);
            par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
        }
        else
        {
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);

        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
    }

    @Override
    public int tickRate(World par1World)
    {
        return 8;
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> result = new ArrayList<ItemStack>();

        if (world.rand.nextDouble() < DROP_RATE)
        {
            String[] ores = OreDictionary.getOreNames();
            ArrayList<ItemStack> items = OreDictionary.getOres(ores[world.rand.nextInt(ores.length)]);
            if (items.size() > 0)
            {
                ItemStack target = items.get(world.rand.nextInt(items.size()));
                if (target != null)
                {
                    result.add(new ItemStack(target.getItem(), 1, target.getItemDamage()));
                }
            }
        }

        return result;
    }
}
