package com.mes51.minecraft.mods.necktie.item.necktie;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.block.BlockAirStone;
import com.mes51.minecraft.mods.necktie.handler.PacketHandler;
import com.mes51.minecraft.mods.necktie.potion.PotionMuscle;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.NecktieCreativeTabs;
import com.mes51.minecraft.mods.necktie.util.Util;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

/**
 * Package: com.mes51.minecraft.mods.necktie.items
 * Date: 2014/04/15
 * Time: 21:18
 */
public abstract class ItemArmorNecktieBase extends ItemArmor implements INecktie
{
    private static final String TEXTURE_ARMOR_PATH = "com.mes51.minecraft.mods.necktie:textures/armor/armorNecktie";
    private static final long SWITCH_INTERVAL = 500; // ms

    public static final String NBT_BREAK_ALL_ENABLE_KEY = "enableBreakAll";
    private static final String NBT_SWITCH_INTERVAL_KEY = "switchInterval";
    private static final String NBT_ACTION_KEY = "action";
    private static final String NBT_POTION_INTERVAL_KEY = "interval";
    private static final String NBT_POTION_DURATION_LEVEL_KEY = "potionDurationLevel";
    private static final String NBT_SET_BLOCK_TARGET_X = "setBlockTargetX";
    private static final String NBT_SET_BLOCK_TARGET_Y = "setBlockTargetY";
    private static final String NBT_SET_BLOCK_TARGET_Z = "setBlockTargetZ";

    private static final String KEY_NAME_MUSCLE_MODE = "Muscle Mode";
    private static final String KEY_NAME_SWITCH_BREAK_ALL = "Break All Switch";
    private static final String KEY_NAME_GLIDE = "Glide";

    private static final int ACTION_MUSCLE_MODE = 1;
    private static final int ACTION_BREAK_ALL_SWITCH = 2;
    private static final int ACTION_SET_AIR_STONE = 3;

    private static final int POTION_DURATION_BASE = 100; // tick
    private static final int POTION_DURATION_BASE_MS = POTION_DURATION_BASE * 50; // ms
    private static final long POTION_INTERVAL = 60000; // ms

    private static final double GLIDE_FALL_DOWN_MOTION_Y = -0.1;
    private static final double GLIDE_MOVE_ASSIST = 0.1;
    private static final float GLIDE_USE_FOOD = 0.1F;

    private EnumToolMaterial toolMaterial = null;
    private String suffix = "";
    private int potionDurationLevel = 0;

    public synchronized static void registerKey(int muscleModeKey, int breakAllSwitchKey, int glideKey)
    {
        Necktie.proxy.addKey(KEY_NAME_MUSCLE_MODE, muscleModeKey);
        Necktie.proxy.addKey(KEY_NAME_SWITCH_BREAK_ALL, breakAllSwitchKey);
        Necktie.proxy.addKey(KEY_NAME_GLIDE, glideKey);
    }

    public static boolean playerIsMuscle(EntityPlayer player)
    {
        ItemStack armor = player.inventory.armorItemInSlot(2);
        return  armor != null &&
                armor.getItem() instanceof ItemArmorNecktieBase &&
                ((EntityPlayer)player).isPotionActive(PotionMuscle.getInstance());
    }

    public static void execCommand(NBTTagCompound tagCompound, EntityPlayer player)
    {
        switch (tagCompound.getInteger(NBT_ACTION_KEY))
        {
            case ACTION_MUSCLE_MODE:
                applyMuscle(tagCompound, player);
                break;
            case ACTION_BREAK_ALL_SWITCH:
                switchBreakAll(tagCompound, player);
                break;
            case ACTION_SET_AIR_STONE:
                setAirBlock(tagCompound, player.worldObj);
                break;
        }
        PacketHandler.issueSyncItemNBT(player.worldObj, player, player.inventory.mainInventory.length + 2);
    }

    public static void issueSetAirBlock(Vec3 pos)
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger(NBT_ACTION_KEY, ACTION_SET_AIR_STONE);
        tagCompound.setDouble(NBT_SET_BLOCK_TARGET_X, pos.xCoord);
        tagCompound.setDouble(NBT_SET_BLOCK_TARGET_Y, pos.yCoord);
        tagCompound.setDouble(NBT_SET_BLOCK_TARGET_Z, pos.zCoord);
        PacketHandler.issueArmorCommand(tagCompound);
    }

    private static void applyMuscle(NBTTagCompound tagCompound, EntityPlayer player)
    {
        ItemStack armor = player.inventory.armorItemInSlot(Const.Item.INVENTORY_SLOT_CHEST);
        if (!player.isPotionActive(PotionMuscle.getInstance()) && armor != null && armor.getItem() instanceof ItemArmorNecktieBase)
        {
            NBTTagCompound armorTag = Util.getOrCreateTag(armor);
            int potionDurationLevel = tagCompound.getInteger(NBT_POTION_DURATION_LEVEL_KEY);
            if (System.currentTimeMillis() - armorTag.getLong(NBT_POTION_INTERVAL_KEY) > POTION_INTERVAL + (potionDurationLevel * POTION_DURATION_BASE_MS))
            {
                player.addPotionEffect(
                        new PotionEffect(PotionMuscle.getInstance().getId(), potionDurationLevel * POTION_DURATION_BASE)
                );
                player.addPotionEffect(
                        new PotionEffect(Potion.resistance.getId(), potionDurationLevel * POTION_DURATION_BASE, 1)
                );

                if (!player.capabilities.isCreativeMode)
                {
                    armorTag.setLong(NBT_POTION_INTERVAL_KEY, System.currentTimeMillis());
                }
            }
        }
    }

    private static void switchBreakAll(NBTTagCompound tagCompound, EntityPlayer player)
    {
        ItemStack armor = player.inventory.armorItemInSlot(Const.Item.INVENTORY_SLOT_CHEST);
        if (armor != null && armor.getItem() instanceof ItemArmorNecktieBase)
        {
            NBTTagCompound armorTag = Util.getOrCreateTag(armor);
            if (System.currentTimeMillis() - armorTag.getLong(NBT_SWITCH_INTERVAL_KEY) > SWITCH_INTERVAL)
            {
                armorTag.setBoolean(NBT_BREAK_ALL_ENABLE_KEY, !armorTag.getBoolean(NBT_BREAK_ALL_ENABLE_KEY));
                armorTag.setLong(NBT_SWITCH_INTERVAL_KEY, System.currentTimeMillis());
                player.addChatMessage("Necktie Break All: " + armorTag.getBoolean(NBT_BREAK_ALL_ENABLE_KEY));
            }
        }
    }

    private static void setAirBlock(NBTTagCompound tagCompound, World world)
    {
        int x = (int)Math.round(tagCompound.getDouble(NBT_SET_BLOCK_TARGET_X));
        int y = (int)Math.round(tagCompound.getDouble(NBT_SET_BLOCK_TARGET_Y));
        int z = (int)Math.round(tagCompound.getDouble(NBT_SET_BLOCK_TARGET_Z));
        BlockAirStone.setInWorld(world, x, y, z);
    }

    public ItemArmorNecktieBase(int par1, EnumArmorMaterial material, int index, EnumToolMaterial toolMaterial, int potionDurationLevel, String suffix)
    {
        super(par1, material, index, 1);
        this.toolMaterial = toolMaterial;
        this.potionDurationLevel = potionDurationLevel;
        this.suffix = suffix;
        setMaxStackSize(1);
        setCreativeTab(NecktieCreativeTabs.instance);
    }

    protected EnumToolMaterial getToolMaterial()
    {
        return this.toolMaterial;
    }

    public float getEfficiencyOnProperMaterial(EntityPlayer player)
    {
        return this.toolMaterial.getEfficiencyOnProperMaterial();
    }

    public boolean canReflectArrow(EntityPlayer player)
    {
        return true;
    }

    protected int getPotionDurationLevel(EntityPlayer player)
    {
        return this.potionDurationLevel;
    }

    @Override
    public float getDamageVsEntity(EntityPlayer player)
    {
        return this.toolMaterial.getDamageVsEntity();
    }

    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
    {
        if (entity instanceof EntityPlayer && ((EntityPlayer)entity).isPotionActive(PotionMuscle.getInstance()))
        {
            return TEXTURE_ARMOR_PATH + this.suffix + "Muscle.png";
        }
        else
        {
            return TEXTURE_ARMOR_PATH + this.suffix +  ".png";
        }
    }

    @Override
    public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack)
    {
        super.onArmorTickUpdate(world, player, itemStack);

        boolean muscleNow = player.isPotionActive(PotionMuscle.getInstance());
        if (Necktie.proxy.keyPushed(KEY_NAME_MUSCLE_MODE) && !muscleNow)
        {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setInteger(NBT_ACTION_KEY, ACTION_MUSCLE_MODE);
            tagCompound.setInteger(NBT_POTION_DURATION_LEVEL_KEY, getPotionDurationLevel(player));
            PacketHandler.issueArmorCommand(tagCompound);
        }
        if (Necktie.proxy.keyPushed(KEY_NAME_SWITCH_BREAK_ALL))
        {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setInteger(NBT_ACTION_KEY, ACTION_BREAK_ALL_SWITCH);
            PacketHandler.issueArmorCommand(tagCompound);
        }
        // KeyBinding#isPressedは一定時間後にfalseになるため、KeyEventWatcherの方で押し続けているかどうか判定する
        if (Necktie.proxy.keyPressed(KEY_NAME_GLIDE) && !player.onGround && player.getFoodStats().getFoodLevel() >= 5)
        {
            player.motionY = Math.max(player.motionY, GLIDE_FALL_DOWN_MOTION_Y);
            player.fallDistance = 0.0F;
            moveAssist(player);
            if (!player.capabilities.isCreativeMode)
            {
                player.addExhaustion(GLIDE_USE_FOOD);
            }
        }
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
        NBTTagCompound tagCompound = Util.getOrCreateTag(par1ItemStack);
        long remaining = (POTION_INTERVAL + getPotionDurationLevel(par2EntityPlayer) * POTION_DURATION_BASE_MS) - (System.currentTimeMillis() - tagCompound.getLong(NBT_POTION_INTERVAL_KEY));
        if (remaining > 0)
        {
            remaining /= 1000;
            par3List.add("Muscle Mode Interval: " + String.format("%d:%02d", remaining / 60, remaining % 60));
        }
    }

    @Override
    public boolean canHarvestBlock(Block par1Block)
    {
        if (this.toolMaterial == EnumToolMaterial.EMERALD)
        {
            return Item.pickaxeDiamond.canHarvestBlock(par1Block);
        }
        else if (this.toolMaterial == EnumToolMaterial.IRON)
        {
            return Item.pickaxeIron.canHarvestBlock(par1Block);
        }
        else
        {
            return Item.pickaxeStone.canHarvestBlock(par1Block);
        }
    }

    public boolean canSetAirBlock()
    {
        return false;
    }

    public abstract PotionEffect[] getBeaconEffect(EntityPlayer player);

    protected void moveAssist(EntityPlayer player)
    {
        double forward = 0;
        double side = 0;
        if (Necktie.proxy.forwardKeyIsPressed())
        {
            forward = 1.0;
        }
        else if (Necktie.proxy.backKeyIsPressed())
        {
            forward = -1.0;
        }
        if (Necktie.proxy.leftKeyIsPressed())
        {
            side = 1.0;
        }
        else if (Necktie.proxy.rightKeyIsPressed())
        {
            side = -1.0;
        }
        addMotion(player, forward * GLIDE_MOVE_ASSIST, side * GLIDE_MOVE_ASSIST);
    }

    protected void addMotion(EntityPlayer player, double forward, double side)
    {
        double rad = player.rotationYaw / 180.0F * (float)Math.PI;
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        player.motionX += cos * side - sin * forward;
        player.motionZ += sin * side + cos * forward;
        if (player.motionY < 0.0)
        {
            player.motionY = Math.max(player.motionY, -0.1);
            player.fallDistance = 0.0F;
        }
    }
}
