package com.mes51.minecraft.mods.necktie.container;

import com.mes51.minecraft.mods.necktie.handler.PacketHandler;
import com.mes51.minecraft.mods.necktie.tileentity.TileEntityBase;
import com.mes51.minecraft.mods.necktie.util.Vec3i;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Package: com.mes51.minecraft.mods.necktie.container
 * Date: 2014/05/02
 * Time: 0:29
 */
public abstract class ContainerBase<T extends TileEntityBase> extends Container
{
    protected static final int FAR = 10000;

    protected class SlotComparator implements Comparator<Slot>
    {
        @Override
        public int compare(Slot o1, Slot o2)
        {
            return o1.getSlotIndex() - o2.getSlotIndex();
        }
    }

    protected enum InventoryType
    {
        INSERTABLE,
        EJECT_ONLY,
        PLAYER_INVENTORY,
        PLAYER_INVENTORY_HOTBAR
    }

    private static final int PLAYER_INVENTORY_SIZE = 9 * 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;

    protected IInventory playerInventory = null;
    protected T entity = null;

    private Map<Slot, InventoryType> slots = new HashMap<Slot, InventoryType>();
    private int playerInventoryStartIndex = 0;
    private int playerInventoryHotBarStartIndex = 0;

    @SuppressWarnings("unchecked")
    public ContainerBase(IInventory playerInventory, TileEntity tileEntity)
    {
        this.playerInventory = playerInventory;
        if (tileEntity == null)
        {
            this.entity = getDefaultTileEntity();
            if (this.entity != null && playerInventory instanceof InventoryPlayer)
            {
                this.entity.worldObj = ((InventoryPlayer)playerInventory).player.worldObj;
            }
        }
        else
        {
            this.entity = (T)tileEntity;
        }

        layoutContainer();
    }

    public ContainerBase(IInventory playerInventory, Vec3i tileEntityPos)
    {
        this.playerInventory = playerInventory;
        if (playerInventory instanceof InventoryPlayer)
        {
            TileEntity tileEntity = ((InventoryPlayer)playerInventory).player.worldObj.getBlockTileEntity(
                    tileEntityPos.x,
                    tileEntityPos.y,
                    tileEntityPos.z
            );
            this.entity = (T)tileEntity;
        }
        if (this.entity == null)
        {
            this.entity = getDefaultTileEntity();
            this.entity.xCoord = tileEntityPos.x;
            this.entity.yCoord = tileEntityPos.y;
            this.entity.zCoord = tileEntityPos.z;
        }

        layoutContainer();
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        if (!FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            sendEntityPacket(par1ICrafting);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        if (!FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            for (ICrafting crafting : (List<ICrafting>)this.crafters)
            {
                sendEntityPacket(crafting);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);
        int slotCount = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            ItemStack tempStack = slot.getStack();
            itemStack = tempStack.copy();
            InventoryType type = this.slots.get(slot);
            if (type != null)
            {
                switch (type)
                {
                    case EJECT_ONLY:
                        if (!this.mergeItemStack(tempStack, this.playerInventoryStartIndex, slotCount, true))
                        {
                            return null;
                        }
                        slot.onSlotChanged();
                        break;
                    case INSERTABLE:
                        if (!this.mergeItemStack(tempStack, this.playerInventoryStartIndex, slotCount, false))
                        {
                            return null;
                        }
                        break;
                    case PLAYER_INVENTORY:
                    case PLAYER_INVENTORY_HOTBAR:
                    {
                        boolean processed = false;
                        int startIndex = -1;
                        int endIndex = 0;
                        for (Slot s : (List<Slot>)this.inventorySlots)
                        {
                            int index = this.inventorySlots.indexOf(s);
                            if (this.slots.get(s) == InventoryType.INSERTABLE)
                            {
                                if (s.isItemValid(tempStack))
                                {
                                    if (startIndex < 0)
                                    {
                                        startIndex = index;
                                    }
                                }
                                else if (startIndex > -1)
                                {
                                    endIndex = index;
                                    break;
                                }
                            }
                            else if (startIndex > -1)
                            {
                                endIndex = index;
                                break;
                            }
                        }
                        if (startIndex > -1)
                        {
                            if (endIndex < 0)
                            {
                                endIndex = this.playerInventoryStartIndex;
                            }
                            if (!this.mergeItemStack(tempStack, startIndex, endIndex, false))
                            {
                                return null;
                            }
                            processed = true;
                        }

                        if (!processed)
                        {
                            if (type == InventoryType.PLAYER_INVENTORY)
                            {
                                if (!this.mergeItemStack(tempStack, this.playerInventoryHotBarStartIndex, slotCount, false))
                                {
                                    return null;
                                }
                            }
                            else if (type == InventoryType.PLAYER_INVENTORY_HOTBAR)
                            {
                                if (!this.mergeItemStack(tempStack, this.playerInventoryStartIndex, this.playerInventoryHotBarStartIndex, false))
                                {
                                    return null;
                                }
                            }
                        }
                        break;
                    }
                }
            }

            if (tempStack.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (tempStack.stackSize == itemStack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, tempStack);
        }

        return itemStack;
    }

    public T getEntity()
    {
        return this.entity;
    }

    public IInventory getPlayerInventory()
    {
        return this.playerInventory;
    }

    protected void addPlayerInventory(int left, int top)
    {
        this.playerInventoryStartIndex = this.inventorySlots.size();
        for (int i = 0; i < PLAYER_INVENTORY_SIZE; i++)
        {
            addSlot(
                    new Slot(
                            this.playerInventory,
                            i + PLAYER_INVENTORY_COLUMN_COUNT,
                            0,
                            0
                    ),
                    InventoryType.PLAYER_INVENTORY
            );
        }
        this.playerInventoryHotBarStartIndex = this.inventorySlots.size();
        for (int hotbarSlot = 0; hotbarSlot < PLAYER_INVENTORY_COLUMN_COUNT; hotbarSlot++)
        {
            addSlot(
                    new Slot(
                            this.playerInventory,
                            hotbarSlot,
                            0,
                            0
                    ),
                    InventoryType.PLAYER_INVENTORY_HOTBAR
            );
        }
        movePlayerInventory(left, top);
    }

    public void movePlayerInventory(int left, int top)
    {
        List<Slot> playerInventrySlots = new ArrayList<Slot>();
        for (Map.Entry<Slot, InventoryType> entry : this.slots.entrySet())
        {
            if (entry.getValue() == InventoryType.PLAYER_INVENTORY)
            {
                playerInventrySlots.add(entry.getKey());
            }
        }
        Collections.sort(playerInventrySlots, new SlotComparator());
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < PLAYER_INVENTORY_COLUMN_COUNT; col++)
            {
                Slot slot = playerInventrySlots.get(col + row * 9);
                slot.xDisplayPosition = left + col * 18;
                slot.yDisplayPosition = top + row * 18;
            }
        }

        playerInventrySlots.clear();
        for (Map.Entry<Slot, InventoryType> entry : this.slots.entrySet())
        {
            if (entry.getValue() == InventoryType.PLAYER_INVENTORY_HOTBAR)
            {
                playerInventrySlots.add(entry.getKey());
            }
        }
        Collections.sort(playerInventrySlots, new SlotComparator());
        for (int hotbarSlot = 0; hotbarSlot < PLAYER_INVENTORY_COLUMN_COUNT; hotbarSlot++)
        {
            Slot slot = playerInventrySlots.get(hotbarSlot);
            slot.xDisplayPosition = left + hotbarSlot * 18;
            slot.yDisplayPosition = top + 4 + 54;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    public void readPacketData(DataInputStream stream) throws IOException
    {
        if (this.entity != null)
        {
            this.entity.readPacketData(stream);
        }
    }

    public void writePacketData(DataOutputStream stream) throws IOException
    {
        if (this.entity != null)
        {
            this.entity.writePacketData(stream);
        }
    }

    protected Slot addSlot(Slot slot, InventoryType type)
    {
        this.slots.put(slot, type);
        return addSlotToContainer(slot);
    }

    protected abstract T getDefaultTileEntity();

    protected abstract void layoutContainer();

    private void sendEntityPacket(ICrafting crafting)
    {
        if (crafting instanceof EntityPlayerMP)
        {
            PacketHandler.issueContainerSyncPacket(this, (EntityPlayerMP)crafting);
        }
    }
}
