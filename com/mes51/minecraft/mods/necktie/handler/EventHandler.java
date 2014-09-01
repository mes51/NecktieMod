package com.mes51.minecraft.mods.necktie.handler;

import com.mes51.minecraft.mods.necktie.entity.EntityAttackEffect;
import com.mes51.minecraft.mods.necktie.item.necktie.INecktie;
import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorNecktieAir;
import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorNecktieBase;
import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorNukeNecktie;
import com.mes51.minecraft.mods.necktie.potion.PotionFullyFed;
import com.mes51.minecraft.mods.necktie.potion.PotionMuscle;
import com.mes51.minecraft.mods.necktie.util.Util;
import com.mes51.minecraft.mods.necktie.util.Vec3i;
import com.mes51.minecraft.mods.necktie.util.enumerable.Enumerable;
import com.mes51.minecraft.mods.necktie.world.NecktieExplosion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.StepSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Package: com.mes51.minecraft.mods.necktie.handler
 * Date: 2014/04/19
 * Time: 14:28
 */
public class EventHandler
{
    private static final int INVENTORY_ARMOR_SLOT = 2;
    private static final float EXHAUSTION_BIAS = 1.01875F;
    private static final float LV_BONUS_ATTACK = 0.1F;
    private static final float LV_BONUS_DAMAGE = 0.99F;
    private static final float LV_BONUS_JUMP = 0.005F;
    private static final float LV_BONUS_FALL_DOWN = 0.99F;
    private static final float LV_BONUS_EXHAUSTION = 0.9999F;

    private static final Vec3i[] MOVE = new Vec3i[] {
            new Vec3i(-1, 0, 0),
            new Vec3i(1, 0, 0),
            new Vec3i(0, -1, 0),
            new Vec3i(0, 1, 0),
            new Vec3i(0, 0, -1),
            new Vec3i(0, 0, 1),
    };

    private Set<Integer> breakAllBlockIds = null;
    private boolean enableAttackEffect = false;

    public EventHandler(int[] breakAllBlockIds, boolean enableAttackEffect)
    {
        this.enableAttackEffect = enableAttackEffect;
        this.breakAllBlockIds = new HashSet<Integer>();
        this.breakAllBlockIds.addAll(Enumerable.from(breakAllBlockIds).toList());
    }

    @ForgeSubscribe
    public void checkHarvestEvent(final PlayerEvent.HarvestCheck event)
    {
        InventoryPlayer inventory = event.entityPlayer.inventory;
        ItemStack armor = inventory.armorItemInSlot(INVENTORY_ARMOR_SLOT);
        ItemStack equipped = inventory.getCurrentItem();
        event.success |= event.block.blockMaterial.isToolNotRequired();
        if (!event.success && armor != null)
        {
            if (equipped == null)
            {
                event.success = ForgeHooks.canToolHarvestBlock(event.block, 0, armor) ||
                        armor.getItem().canHarvestBlock(event.block) ||
                        Item.swordDiamond.canHarvestBlock(event.block) ||
                        (armor.getItem() instanceof ItemArmorNecktieAir && ((ItemArmorNecktieAir)armor.getItem()).canHarvestBlockByPlayer(event.entityPlayer, event.block));
            }
        }
    }

    @ForgeSubscribe
    public void breakSpeed(PlayerEvent.BreakSpeed event)
    {
        ItemStack armor = event.entityPlayer.inventory.armorItemInSlot(INVENTORY_ARMOR_SLOT);
        if (armor != null && armor.getItem() instanceof ItemArmorNecktieBase)
        {
            boolean canBreak = event.block.blockMaterial.isToolNotRequired() || ForgeHooks.canToolHarvestBlock(event.block, 0, armor) || armor.getItem().canHarvestBlock(event.block) || Item.swordDiamond.canHarvestBlock(event.block);
            if (canBreak && event.entityPlayer.inventory.getCurrentItem() == null)
            {
                event.newSpeed = event.originalSpeed + ((ItemArmorNecktieBase)armor.getItem()).getEfficiencyOnProperMaterial(event.entityPlayer);
            }
        }
    }

    @ForgeSubscribe
    public void livingUpdate(LivingUpdateEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            ItemStack armor = player.inventory.armorItemInSlot(INVENTORY_ARMOR_SLOT);
            if (!player.capabilities.isCreativeMode && armor != null && armor.getItem() instanceof INecktie)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                player.getFoodStats().writeNBT(tagCompound);
                double foodExhaustionLevel = tagCompound.getFloat("foodExhaustionLevel") * EXHAUSTION_BIAS * Math.pow(LV_BONUS_EXHAUSTION, player.experienceLevel);
                if (player.isPotionActive(PotionFullyFed.getInstance()))
                {
                    foodExhaustionLevel *= 0.5;
                }
                tagCompound.setFloat("foodExhaustionLevel", (float)foodExhaustionLevel);
                player.getFoodStats().readNBT(tagCompound);
            }
        }
    }

    @ForgeSubscribe
    public void livingDeath(LivingDeathEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            ItemStack armor = player.inventory.armorItemInSlot(INVENTORY_ARMOR_SLOT);
            if (armor != null && armor.getItem() instanceof ItemArmorNukeNecktie)
            {
                new NecktieExplosion(((EntityPlayer) event.entityLiving).worldObj, null, player.posX, player.posY, player.posZ, ItemArmorNukeNecktie.EXPLOSION_RADIUS).explosion();
            }
        }
    }

    @ForgeSubscribe
    public void livingAttack(LivingAttackEvent event)
    {
        EntityLivingBase target = event.entityLiving;
        if (target instanceof EntityPlayer)
        {
            EntityPlayer targetPlayer = (EntityPlayer)target;
            ItemStack targetArmor = targetPlayer.inventory.armorItemInSlot(INVENTORY_ARMOR_SLOT);
            if (targetArmor != null &&
                    targetArmor.getItem() instanceof ItemArmorNecktieBase &&
                    event.source instanceof EntityDamageSourceIndirect &&
                    event.source.getSourceOfDamage() instanceof EntityArrow &&
                    event.source.getEntity() instanceof EntityLivingBase &&
                    event.source.getEntity() != targetPlayer &&
                    ((ItemArmorNecktieBase)targetArmor.getItem()).canReflectArrow(targetPlayer))
            {
                EntityLivingBase sourceEntity = (EntityLivingBase) event.source.getEntity();
                EntityArrow returnArrow = null;
                if (targetPlayer.isPotionActive(PotionMuscle.getInstance()))
                {
                    event.source.getSourceOfDamage().setDead();
                    returnArrow = new EntityArrow(targetPlayer.worldObj, targetPlayer, sourceEntity, 1.6F, 0.0F);
                }
                else
                {
                    Vec3 lookAt = targetPlayer.getLookVec();
                    Vec3 targetVec = Util.getEntityPosition(targetPlayer).subtract(Util.getEntityPosition(sourceEntity)).normalize();
                    if (lookAt.dotProduct(targetVec) > 0.9)
                    {
                        event.source.getSourceOfDamage().setDead();
                        returnArrow = new EntityArrow(targetPlayer.worldObj, targetPlayer, sourceEntity, 1.6F, 14.0F);
                    }
                }
                if (returnArrow != null && !targetPlayer.worldObj.isRemote)
                {
                    targetPlayer.worldObj.spawnEntityInWorld(returnArrow);
                    event.setCanceled(true);
                }
            }
        }
    }

    @ForgeSubscribe
    public void livingHurt(LivingHurtEvent event)
    {
        if (event.source == null)
        {
            return;
        }

        EntityLivingBase target = event.entityLiving;
        boolean hit = event.source.getDamageType().equals("player");
        boolean eggHit = event.source instanceof EntityDamageSourceIndirect && event.source.getSourceOfDamage() instanceof EntityEgg;
        if ((hit || eggHit) && event.source.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.source.getEntity();
            ItemStack armor = player.inventory.armorItemInSlot(INVENTORY_ARMOR_SLOT);
            if (armor != null && armor.getItem() instanceof INecktie && (eggHit || player.inventory.getCurrentItem() == null))
            {
                // 4 = Sword base damage
                event.ammount += ((INecktie)armor.getItem()).getDamageVsEntity(player) + 4;
                event.ammount += player.experienceLevel * LV_BONUS_ATTACK;

                if (player.isPotionActive(PotionMuscle.getInstance()))
                {
                    event.ammount *= 1.5F;
                }

                if (this.enableAttackEffect && target != null)
                {
                    player.worldObj.spawnEntityInWorld(
                            new EntityAttackEffect(
                                    player.worldObj,
                                    Util.getEntityPosition(target),
                                    player.rotationYaw
                            )
                    );
                }
            }
        }

        if (target != null && target instanceof EntityPlayer)
        {
            EntityPlayer targetPlayer = (EntityPlayer)target;
            ItemStack targetArmor = targetPlayer.inventory.armorItemInSlot(INVENTORY_ARMOR_SLOT);
            if (targetArmor != null && targetArmor.getItem() instanceof INecktie)
            {
                event.ammount = (float)(event.ammount * Math.pow(LV_BONUS_DAMAGE, targetPlayer.experienceLevel));
            }
        }
    }

    @ForgeSubscribe
    public void livingJump(LivingJumpEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer)
        {
            ItemStack armor = ((EntityPlayer) event.entityLiving).inventory.armorItemInSlot(INVENTORY_ARMOR_SLOT);
            if (armor != null && armor.getItem() instanceof ItemArmorNecktieBase)
            {
                event.entityLiving.motionY += ((EntityPlayer)event.entityLiving).experienceLevel * LV_BONUS_JUMP;
                if (event.entityLiving.isPotionActive(PotionMuscle.getInstance()))
                {
                    event.entityLiving.motionY *= 1.5;
                }
            }
        }
    }

    @ForgeSubscribe
    public void livingFall(LivingFallEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer)
        {
            ItemStack armor = ((EntityPlayer) event.entityLiving).inventory.armorItemInSlot(INVENTORY_ARMOR_SLOT);
            if (armor != null && armor.getItem() instanceof ItemArmorNecktieBase)
            {
                event.distance *= (float)Math.pow(LV_BONUS_FALL_DOWN, ((EntityPlayer)event.entityLiving).experienceLevel);
                if (event.entityLiving.isPotionActive(PotionMuscle.getInstance()))
                {
                    event.distance *= 0.5F;
                }
            }
        }
    }

    @ForgeSubscribe
    public void playerInteract(PlayerInteractEvent event)
    {
        EntityPlayer player = event.entityPlayer;
        ItemStack armor = player.inventory.armorItemInSlot(INVENTORY_ARMOR_SLOT);
        if (player.capabilities.allowEdit && player.inventory.getCurrentItem() == null && armor != null && armor.getItem() instanceof ItemArmorNecktieBase)
        {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            {
                if (event.face != 1)
                {
                    return;
                }

                World world = player.worldObj;
                int x = event.x;
                int y = event.y;
                int z = event.z;

                UseHoeEvent useHoeEvent = new UseHoeEvent(player, null, world, x, y, z);
                MinecraftForge.EVENT_BUS.post(useHoeEvent);
                if (useHoeEvent.isCanceled())
                {
                    return;
                }

                int blockId = world.getBlockId(x, y, z);
                boolean isAir = world.isAirBlock(x, y + 1, z);
                if (isAir && (blockId == Block.grass.blockID || blockId == Block.dirt.blockID))
                {
                    StepSound stepSound = Block.tilledField.stepSound;
                    world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, stepSound.getStepSound(), (stepSound.getVolume() + 1.0F) / 2.0F, stepSound.getPitch() * 0.8F);

                    if (!world.isRemote)
                    {
                        world.setBlock(x, y, z, Block.tilledField.blockID);
                    }
                }
            }
            else if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR && ((ItemArmorNecktieBase)armor.getItem()).canSetAirBlock())
            {
                Vec3 pos = Util.getEntityPosition(player);
                Vec3 look = player.getLook(1.0F);
                ItemArmorNecktieBase.issueSetAirBlock(pos.addVector(look.xCoord * 2.0, look.yCoord * 2.0, look.zCoord * 2.0));
            }
        }
    }

    @ForgeSubscribe
    public void breakBlock(BlockEvent.BreakEvent event)
    {
        EntityPlayer player = event.getPlayer();
        if (event.block == null || event.world.isRemote || player == null || !this.breakAllBlockIds.contains(event.block.blockID))
        {
            return;
        }

        ItemStack armor = player.inventory.armorItemInSlot(INVENTORY_ARMOR_SLOT);
        if (armor == null || !(armor.getItem() instanceof ItemArmorNecktieBase) || !Util.getOrCreateTag(armor).getBoolean(ItemArmorNecktieBase.NBT_BREAK_ALL_ENABLE_KEY))
        {
            return;
        }

        World world = event.world;
        Queue<Vec3i> queue = new LinkedList<Vec3i>();
        queue.add(new Vec3i(event.x, event.y, event.z));
        boolean skipFirst = false;
        while (queue.size() > 0)
        {
            Vec3i pos = queue.poll();
            Block block = Block.blocksList[world.getBlockId(pos.x, pos.y, pos.z)];
            boolean hit = block != null &&
                    (block == event.block || (event.block instanceof BlockRedstoneOre && (block == Block.oreRedstone || block == Block.oreRedstoneGlowing))) &&
                    world.getBlockMetadata(pos.x, pos.y, pos.z) == event.blockMetadata;
            if (hit)
            {
                if (skipFirst)
                {
                    event.block.dropBlockAsItem(world, pos.x, pos.y, pos.z, event.blockMetadata, 0);
                    event.block.dropXpOnBlockBreak(world, pos.x, pos.y, pos.z, event.block.getExpDrop(world, event.blockMetadata, 0));
                    world.setBlockToAir(pos.x, pos.y, pos.z);
                }
                skipFirst = true;
                for (Vec3i m : MOVE)
                {
                    queue.add(pos.add(m));
                }
            }
        }
    }
}
