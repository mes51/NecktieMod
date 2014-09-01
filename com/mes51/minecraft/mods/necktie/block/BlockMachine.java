package com.mes51.minecraft.mods.necktie.block;

import com.mes51.minecraft.mods.necktie.tileentity.TileEntityBase;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityMachineBlock;
import com.mes51.minecraft.mods.necktie.tileentity.machine.MachineTileEntityBase;
import com.mes51.minecraft.mods.necktie.tileentity.machine.MachineTileEntitySquatGenerator;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.GuiId;
import com.mes51.minecraft.mods.necktie.util.NecktieCreativeTabs;
import com.mes51.minecraft.mods.necktie.util.Util;
import com.mes51.minecraft.mods.necktie.util.enumerable.Enumerable;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.Predicate;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.SingleArgFunc;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Package: com.mes51.minecraft.mods.necktie.block
 * Date: 2014/05/01
 * Time: 13:24
 */
public class BlockMachine extends BlockMachineBase
{
    public static enum SubType
    {
        SQUAT_GENERATOR(MachineTileEntitySquatGenerator.class, true, false, false);

        private Class<? extends MachineTileEntityBase> klass = null;
        private boolean topTexture = false;
        private boolean bottomTexture = false;
        private boolean sideTexture = false;

        public static SubType byClass(final Class<? extends MachineTileEntityBase> klass)
        {
            return Enumerable.from(values()).first(
                    new Predicate<SubType>()
                    {
                        @Override
                        public boolean predicate(SubType value)
                        {
                            return value.klass.equals(klass);
                        }
                    }
            );
        }

        public static SubType byDamage(int daamage)
        {
            SubType[] types = values();
            if (types.length <= daamage)
            {
                return SubType.SQUAT_GENERATOR;
            }
            else
            {
                return types[daamage];
            }
        }

        private SubType(Class<? extends MachineTileEntityBase> klass, boolean topTexture, boolean bottomTexture, boolean sideTexture)
        {
            this.klass = klass;
            this.topTexture = topTexture;
            this.bottomTexture = bottomTexture;
            this.sideTexture = sideTexture;
        }

        public String getUnlocalizedName()
        {
            return getDisplayName().replace(" ", "");
        }

        public String getDisplayName()
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
                    ),
                    " "
            );
        }

        public ItemStack getItemStack()
        {
            return new ItemStack(BlockMachine.getInstance(), 1, ordinal());
        }

        public Class<? extends MachineTileEntityBase> getMachineTileEntityClass()
        {
            return this.klass;
        }

        public void registerIcon(IconRegister register, Map<String, Icon> icons)
        {
            String unlocalizedName = getUnlocalizedName();
            if (this.topTexture)
            {
                icons.put(unlocalizedName + "Top", register.registerIcon(Const.DOMAIN + ":machineBlock" + unlocalizedName + "Top"));
            }
            else
            {
                icons.put(unlocalizedName + "Top", register.registerIcon(Const.Block.Texture.MACHINE_TOP));
            }
            if (this.bottomTexture)
            {
                icons.put(unlocalizedName + "Bottom", register.registerIcon(Const.DOMAIN + ":machineBlock" + unlocalizedName + "Bottom"));
            }
            else
            {
                icons.put(unlocalizedName + "Bottom", register.registerIcon(Const.Block.Texture.MACHINE_BOTTOM));
            }
            if (this.sideTexture)
            {
                icons.put(unlocalizedName + "Side", register.registerIcon(Const.DOMAIN + ":machineBlock" + unlocalizedName + "Side"));
            }
            else
            {
                icons.put(unlocalizedName + "Side", register.registerIcon(Const.Block.Texture.MACHINE_SIDE));
            }
            icons.put(unlocalizedName + "Front", register.registerIcon(Const.DOMAIN + ":machineBlock" + unlocalizedName + "Front"));
        }
    }

    private static Block instance = null;

    public static void register(int blockId)
    {
        if (Util.ic2Loaded())
        {
            instance = new BlockMachine(blockId);
            GameRegistry.registerBlock(instance, Const.Block.BlockName.MACHINE_BLOCK);

            GameRegistry.registerTileEntity(TileEntityMachineBlock.class, TileEntityMachineBlock.class.getSimpleName());
        }
    }

    public static Block getInstance()
    {
        return instance;
    }

    protected BlockMachine(int par1)
    {
        super(par1);
        setUnlocalizedName(Const.Block.BlockName.MACHINE_BLOCK);
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundStoneFootstep);
        setCreativeTab(NecktieCreativeTabs.instance);
    }

    @Override
    public void onNeighborTileChange(World world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
        super.onNeighborTileChange(world, x, y, z, tileX, tileY, tileZ);

        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileEntityMachineBlock)
        {
            ((TileEntityMachineBlock)tileEntity).onNeighborTileChange();
        }
    }

    @Override
    protected Class<? extends TileEntityBase> getTileEntityClass()
    {
        return TileEntityMachineBlock.class;
    }

    @Override
    protected GuiId getGuiId()
    {
        return GuiId.MACHINE_BASE;
    }

    private String getIconPrefix(TileEntityMachineBlock tileEntity)
    {
        if (tileEntity.getMachineTileEntity() != null)
        {
            return SubType.byClass(tileEntity.getMachineTileEntity().getClass()).getUnlocalizedName();
        }
        return "";
    }

    private Icon getIconWithTileEntity(TileEntityMachineBlock tileEntity, int side, int metadata)
    {
        String prefix = getIconPrefix(tileEntity);
        switch (metadata)
        {
            case 0:
                return icons.get(prefix + "Bottom");
            case 1:
                return icons.get(prefix + "Top");
            case ICON_FRONT:
                if (side != 0)
                {
                    return icons.get(prefix + "Side");
                }
                else
                {
                    return icons.get(prefix + "Front");
                }
            default:
                return icons.get(prefix + "Side");
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityMachineBlock();
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
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLivingBase, par6ItemStack);

        if (!par1World.isRemote && par5EntityLivingBase instanceof EntityPlayer)
        {
            ItemStack itemStack = ((EntityPlayer)par5EntityLivingBase).getCurrentEquippedItem();
            if (itemStack != null && itemStack.itemID == this.blockID)
            {
                TileEntity entity = par1World.getBlockTileEntity(par2, par3, par4);
                if (entity != null && entity instanceof TileEntityMachineBlock)
                {
                    ((TileEntityMachineBlock)entity).setMachineTileEntityClass(SubType.byDamage(par6ItemStack.getItemDamage()).getMachineTileEntityClass());
                }
            }
        }

        ForgeDirection dir = Util.getOrientation(MathHelper.wrapAngleTo180_double(par5EntityLivingBase.rotationYaw));
        par1World.setBlockMetadataWithNotify(par2, par3, par4, dir.getOpposite().ordinal(), 3);
    }

    @Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        if (!player.capabilities.isCreativeMode && !world.isRemote)
        {
            TileEntity entity = world.getBlockTileEntity(x, y, z);
            if (entity != null && entity instanceof TileEntityMachineBlock)
            {
                Util.dropItem(((TileEntityMachineBlock)entity).getWrenchDrop(null), world, x, y, z);
            }
        }

        return super.removeBlockByPlayer(world, player, x, y, z);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        // 通常ドロップはなし
        return new ArrayList<ItemStack>();
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);

        for (SubType type : SubType.values())
        {
            type.registerIcon(par1IconRegister, this.icons);
        }
    }

    @Override
    public Icon getIcon(int par1, int par2)
    {
        SubType type = SubType.byDamage(par2);
        switch (par1)
        {
            case 0:
                return icons.get(type.getUnlocalizedName() + "Bottom");
            case 1:
                return icons.get(type.getUnlocalizedName() + "Top");
            case ICON_FRONT:
                return icons.get(type.getUnlocalizedName() + "Front");
            default:
                return icons.get(type.getUnlocalizedName() + "Side");
        }
    }

    @Override
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (par1IBlockAccess != null)
        {
            int dir = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
            TileEntity tileEntity = par1IBlockAccess.getBlockTileEntity(par2, par3, par4);
            if (tileEntity instanceof TileEntityMachineBlock)
            {
                if (dir != 0 && par5 == dir)
                {
                    return icons.get(getIconPrefix((TileEntityMachineBlock)tileEntity) + "Front");
                }
                return getIconWithTileEntity((TileEntityMachineBlock)tileEntity, dir, par5);
            }
        }
        return getIcon(par5, 0);
    }
}
