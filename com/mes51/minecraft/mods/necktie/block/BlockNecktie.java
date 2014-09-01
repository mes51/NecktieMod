package com.mes51.minecraft.mods.necktie.block;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorNukeNecktie;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityNecktieBlock;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.NecktieCreativeTabs;
import com.mes51.minecraft.mods.necktie.util.Util;
import com.mes51.minecraft.mods.necktie.util.enumerable.Enumerable;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.SingleArgFunc;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import java.util.List;

/**
 * Package: com.mes51.minecraft.mods.necktie.block
 * Date: 2014/05/17
 * Time: 15:06
 */
public class BlockNecktie extends BlockContainer
{
    public static enum SubType
    {
        NECKTIE(0.5),
        IRON_NECKTIE(1.0),
        DIAMOND_NECKTIE(1.5),
        NUKE_NECKTIE(1.0);

        private static SubType[] types = null;
        private static boolean registeredNuke = false;
        private double additionalRange = 0.0;

        public static SubType byDamage(int damage)
        {
            if (types == null)
            {
                types = values();
                registeredNuke = ItemArmorNukeNecktie.getInstance() != null;
            }
            if (types.length <= damage)
            {
                return SubType.NECKTIE;
            }
            else
            {
                if (!registeredNuke && types[damage] == NUKE_NECKTIE)
                {
                    return NECKTIE;
                }
                else
                {
                    return types[damage];
                }
            }
        }

        private SubType(double additionalRange)
        {
            this.additionalRange = additionalRange;
        }

        public double getAdditionalRange()
        {
            return this.additionalRange;
        }

        public String getUnlocalizedName()
        {
            return Util.joinString(
                    Enumerable.from(name().split("_")).select(
                            new SingleArgFunc<String, String>()
                            {
                                @Override
                                public String func(String value)
                                {
                                    return Util.pascalize(value);
                                }
                            }
                    ).insert(0, Const.DOMAIN).insert(1, ":block"),
                    ""
            );
        }

        public String getDisplayName()
        {
            return "Block of " + Util.joinString(
                    Enumerable.from(name().split("_")).select(
                            new SingleArgFunc<String, String>()
                            {
                                @Override
                                public String func(String value)
                                {
                                    return Util.pascalize(value);
                                }
                            }
                    ),
                    " "
            );
        }

        public ItemStack getItemStack()
        {
            return new ItemStack(BlockNecktie.getInstance(), 1, ordinal());
        }
    }

    private static Block instance = null;

    public static void register(int blockId)
    {
        instance = new BlockNecktie(blockId);
        GameRegistry.registerBlock(instance, Const.Block.BlockName.NECKTIE_BLOCK);

        GameRegistry.registerTileEntity(TileEntityNecktieBlock.class, TileEntityNecktieBlock.class.getSimpleName());
    }

    public static Block getInstance()
    {
        return instance;
    }

    public BlockNecktie(int par1)
    {
        super(par1, Material.cloth);
        setUnlocalizedName(Const.Block.BlockName.NECKTIE_BLOCK);
        setHardness(1.0F);
        setResistance(5.0F);
        setStepSound(soundStoneFootstep);
        setCreativeTab(NecktieCreativeTabs.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityNecktieBlock();
    }

    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }

    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (SubType type : SubType.values())
        {
            par3List.add(type.getItemStack());
        }
    }

    @Override
    public int getRenderBlockPass()
    {
        return 0;
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
}
