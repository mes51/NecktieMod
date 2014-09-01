package com.mes51.minecraft.mods.necktie.block;

import com.mes51.minecraft.mods.necktie.util.Const;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

import java.util.HashMap;
import java.util.Map;

/**
 * Package: com.mes51.minecraft.mods.necktie.block
 * Date: 2014/05/01
 * Time: 13:07
 */
public abstract class BlockMachineBase extends BlockDisplayableBase
{
    protected static final int ICON_FRONT = 3;

    protected Map<String, Icon> icons = new HashMap<String, Icon>();

    protected BlockMachineBase(int par1)
    {
        super(par1, Material.iron);
        setHardness(2.0F);
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        icons.put("Top", par1IconRegister.registerIcon(Const.Block.Texture.MACHINE_TOP));
        icons.put("Bottom", par1IconRegister.registerIcon(Const.Block.Texture.MACHINE_BOTTOM));
        icons.put("Side", par1IconRegister.registerIcon(Const.Block.Texture.MACHINE_SIDE));
        icons.put("Front", par1IconRegister.registerIcon(getRealUnlocalizedName() + "Front"));
    }

    @Override
    public Icon getIcon(int par1, int par2)
    {
        switch (par1)
        {
            case 0:
                return icons.get("Bottom");
            case 1:
                return icons.get("Top");
            case ICON_FRONT:
                if (par2 != 0)
                {
                    return icons.get("Side");
                }
                else
                {
                    return icons.get("Front");
                }
            default:
                return icons.get("Side");
        }
    }

    @Override
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (par1IBlockAccess != null)
        {
            int dir = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
            if (dir != 0 && par5 == dir)
            {
                return icons.get("Front");
            }
            return getIcon(par5, dir);
        }
        return getIcon(par5, 0);
    }
}
