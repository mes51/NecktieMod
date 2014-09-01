package com.mes51.minecraft.mods.necktie.util;

import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorNecktieBase;
import com.mes51.minecraft.mods.necktie.potion.PotionMuscle;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;

/**
 * Package: com.mes51.minecraft.mods.necktie.util
 * Date: 2014/04/15
 * Time: 23:17
 */
public class Util
{
    private static final Random rand = new Random();

    public static boolean ic2Loaded()
    {
        return Loader.isModLoaded("IC2");
    }

    public static boolean tConstructLoaded()
    {
        return Loader.isModLoaded("TConstruct");
    }

    public static NBTTagCompound getOrCreateTag(ItemStack itemStack)
    {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if (tagCompound == null)
        {
            tagCompound = new NBTTagCompound("tag");
            itemStack.setTagCompound(tagCompound);
        }
        return tagCompound;
    }

    public static String pascalize(String value)
    {
        return Character.toUpperCase(value.charAt(0)) + value.substring(1).toLowerCase();
    }

    public static String joinString(Iterable<String> lines, String separator)
    {
        StringBuilder sb = new StringBuilder();
        for (String line : lines)
        {
            if (line != null)
            {
                sb.append(line);
                sb.append(separator);
            }
        }

        if (sb.length() > 0)
        {
            if (separator.length() > 0)
            {
                sb.deleteCharAt(sb.length() - separator.length());
            }
            return sb.toString();
        }
        else
        {
            return "";
        }
    }

    public static Field tryGetField(Class<?> klass, String ... candidateFieldNames)
    {
        for (String fieldName : candidateFieldNames)
        {
            try
            {
                return ReflectionHelper.findField(klass, fieldName);
            }
            catch (RuntimeException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void dropItem(ItemStack itemStack, World world, int x, int y, int z)
    {
        double posX = x + rand.nextDouble() * 0.8 + 0.1;
        double posY = y + rand.nextDouble() * 0.8 + 0.1;
        double posZ = z + rand.nextDouble() * 0.8 + 0.1;

        while (itemStack.stackSize > 0)
        {
            int size = Math.min(itemStack.stackSize, rand.nextInt(21) + 10);
            itemStack.stackSize -= size;
            EntityItem entity = new EntityItem(world, posX, posY, posZ, new ItemStack(itemStack.itemID, size, itemStack.getItemDamage()));
            if (itemStack.hasTagCompound())
            {
                entity.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
            }

            entity.motionX = rand.nextGaussian() * 0.05;
            entity.motionY = rand.nextGaussian() * 0.05 + 0.2;
            entity.motionZ = rand.nextGaussian() * 0.05;
            world.spawnEntityInWorld(entity);
        }
    }

    public static ForgeDirection getDirectionFromBlockMetadata(World world, int x, int y, int z)
    {
        return getDirectionFromBlockMetadata(world, x, y, z, false);
    }

    public static ForgeDirection getDirectionFromBlockMetadata(World world, int x, int y, int z, boolean useUpDown)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
        if (!useUpDown && (dir == ForgeDirection.DOWN || dir == ForgeDirection.UP))
        {
            dir = ForgeDirection.NORTH;
        }

        return dir;
    }

    public static ForgeDirection getOrientation(double angle)
    {
        while (angle < 0)
        {
            angle += 360.0;
        }
        if (angle < 45.0)
        {
            return ForgeDirection.SOUTH;
        }
        else if (angle < 135.0)
        {
            return ForgeDirection.WEST;
        }
        else if (angle < 225.0)
        {
            return ForgeDirection.NORTH;
        }
        else if (angle < 315.0)
        {
            return ForgeDirection.EAST;
        }
        return ForgeDirection.SOUTH;
    }

    public static boolean playerIsMuscle(EntityPlayer player)
    {
        ItemStack armor = player.inventory.armorItemInSlot(Const.Item.INVENTORY_SLOT_CHEST);
        return armor != null && armor.getItem() instanceof ItemArmorNecktieBase && player.isPotionActive(PotionMuscle.getInstance());
    }

    // serverとclientで視点補正がかかっていたりいなかったりするため、その差を吸収する
    // see: http://forum.minecraftuser.jp/viewtopic.php?f=21&t=7907#p63907
    public static Vec3 getEntityPosition(EntityLivingBase entity)
    {
        Vec3 pos = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
        if (entity.yOffset == 0.0)
        {
            pos.yCoord += entity.getEyeHeight();
        }
        return pos;
    }

    public static void writeItemStacksToNBT(NBTTagCompound tagCompound, ItemStack[] itemStacks, String key)
    {
        NBTTagCompound itemListNBT = new NBTTagCompound();
        itemListNBT.setInteger("length", itemStacks.length);
        for (int i = 0; i < itemStacks.length; i++)
        {
            if (itemStacks[i] != null)
            {
                NBTTagCompound itemNBT = new NBTTagCompound();
                itemStacks[i].writeToNBT(itemNBT);
                itemListNBT.setTag(i + "", itemNBT);
            }
        }
        tagCompound.setTag(key, itemListNBT);
    }

    public static ItemStack[] readItemStacksFromNBT(NBTTagCompound tagCompound, String key)
    {
        if (tagCompound.hasKey(key))
        {
            NBTTagCompound itemListNBT = (NBTTagCompound)tagCompound.getTag(key);
            ItemStack[] itemStacks = new ItemStack[itemListNBT.getInteger("length")];
            for (int i = 0; i < itemStacks.length; i++)
            {
                String n = i + "";
                if (itemListNBT.hasKey(n))
                {
                    itemStacks[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound)itemListNBT.getTag(n));
                }
            }
            return itemStacks;
        }
        else
        {
            return null;
        }
    }

    public static void writeItemStacksToStream(DataOutputStream stream, ItemStack[] itemStacks) throws IOException
    {
        stream.writeInt(itemStacks.length);
        for (ItemStack itemStack : itemStacks)
        {
            if (itemStack != null)
            {
                stream.writeBoolean(true);
                NBTTagCompound tagCompound = new NBTTagCompound();
                itemStack.writeToNBT(tagCompound);
                NBTTagCompound.writeNamedTag(tagCompound, stream);
            }
            else
            {
                stream.writeBoolean(false);
            }
        }
    }

    public static ItemStack[] readItemStacksFromStream(DataInputStream stream) throws IOException
    {
        ItemStack[] itemStacks = new ItemStack[stream.readInt()];
        for (int i = 0; i < itemStacks.length; i++)
        {
            if (stream.readBoolean())
            {
                NBTTagCompound tagCompound = (NBTTagCompound) NBTTagCompound.readNamedTag(stream);
                itemStacks[i] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
        return itemStacks;
    }
}
