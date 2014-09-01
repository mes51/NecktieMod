package com.mes51.minecraft.mods.necktie.tileentity;

import com.mes51.minecraft.mods.necktie.block.BlockNecktie;
import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorNecktieBase;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Package: com.mes51.minecraft.mods.necktie.tileentity
 * Date: 2014/05/20
 * Time: 1:10
 */
public class TileEntityNecktieBeacon extends TileEntityBase implements IInventory
{
    private static final double MINIMUM_SEARCH_RANGE = 16.0;
    private static final int STRUCTURE_UPDATE_INTERVAL = 10;
    private static final int INVENTORY_SIZE = 1;

    private ItemStack[] inventory = new ItemStack[INVENTORY_SIZE];
    private int updateInterval = -1;
    private boolean validStructure = false;
    private double searchRange = 0.0;

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        this.updateInterval--;
        if (this.updateInterval < 0)
        {
            updateStructure();
            this.updateInterval = STRUCTURE_UPDATE_INTERVAL;
            this.searchRange += MINIMUM_SEARCH_RANGE;
        }

        if (this.validStructure && this.inventory[0] != null && this.inventory[0].getItem() instanceof ItemArmorNecktieBase)
        {
            List list = this.worldObj.getEntitiesWithinAABB(
                    EntityPlayer.class,
                    AxisAlignedBB.getBoundingBox(
                            this.xCoord - this.searchRange,
                            this.yCoord - this.searchRange,
                            this.zCoord - this.searchRange,
                            this.xCoord + this.searchRange,
                            this.yCoord + this.searchRange,
                            this.zCoord + this.searchRange
                    )
            );
            for (EntityPlayer player : (List<EntityPlayer>)list)
            {
                ItemStack armor = player.inventory.armorItemInSlot(Const.Item.INVENTORY_SLOT_CHEST);
                if (armor != null && armor.getItem() instanceof ItemArmorNecktieBase)
                {
                    for (PotionEffect potionEffect : ((ItemArmorNecktieBase)this.inventory[0].getItem()).getBeaconEffect(player))
                    {
                        player.addPotionEffect(new PotionEffect(potionEffect.getPotionID(), 20, potionEffect.getAmplifier()));
                    }
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        Util.writeItemStacksToNBT(par1NBTTagCompound, this.inventory, "inventory");
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.inventory = Util.readItemStacksFromNBT(par1NBTTagCompound, "inventory");
    }

    @Override
    public void writePacketData(DataOutputStream stream) throws IOException
    {
        super.writePacketData(stream);
        Util.writeItemStacksToStream(stream, this.inventory);
    }

    @Override
    public void readPacketData(DataInputStream stream) throws IOException
    {
        super.readPacketData(stream);
        this.inventory = Util.readItemStacksFromStream(stream);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }

    public boolean isValidStructure()
    {
        return this.validStructure;
    }

    private void updateStructure()
    {
        boolean invalidLayer = false;
        int layer = 0;
        int targetBlockId = BlockNecktie.getInstance().blockID;
        double layerAdditionalRange = 0.0;
        this.searchRange = 0.0;
        while (!invalidLayer && this.yCoord - layer > -1)
        {
            layer += 1;
            this.searchRange += layerAdditionalRange;
            layerAdditionalRange = 0.0;
            int y = this.yCoord - layer;
            for (int x = this.xCoord - layer, cx = -layer; cx <= layer && !invalidLayer; x++, cx++)
            {
                for (int z = this.zCoord - layer, cz = -layer; cz <= layer; z++, cz++)
                {
                    int blockId = this.worldObj.getBlockId(x, y, z);
                    if (blockId == targetBlockId)
                    {
                        layerAdditionalRange += BlockNecktie.SubType.byDamage(this.worldObj.getBlockMetadata(x, y, z)).getAdditionalRange();
                    }
                    else
                    {
                        invalidLayer = true;
                        break;
                    }
                }
            }
        }

        this.validStructure = layer > 1;
    }

    // IInventory

    @Override
    public int getSizeInventory()
    {
        return INVENTORY_SIZE;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return this.inventory[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (this.inventory[i] == null)
        {
            return null;
        }

        ItemStack stack = this.inventory[i];
        stack = stack.splitStack(Math.min(stack.stackSize, j));
        if (this.inventory[i].stackSize < 1)
        {
            this.inventory[i] = null;
        }
        onInventoryChanged();
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (this.inventory[i] != null)
        {
            ItemStack stack = this.inventory[i];
            this.inventory[i] = null;
            return stack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        this.inventory[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "Necktie Beacon";
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void openChest() { }

    @Override
    public void closeChest() { }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return itemstack != null && itemstack.getItem() instanceof ItemArmorNecktieBase;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && entityplayer.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0;
    }
}
