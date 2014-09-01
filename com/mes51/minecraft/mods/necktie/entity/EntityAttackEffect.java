package com.mes51.minecraft.mods.necktie.entity;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.render.RenderEntityAttackEffect;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Package: com.mes51.minecraft.mods.necktie.entity
 * Date: 2014/04/28
 * Time: 16:35
 */
public class EntityAttackEffect extends Entity
{
    private static final int DATA_WATCHER_ID_TEXTURE_ID = 18;
    private static final int MAX_LIFE_TICK = 20;
    private static final double RANDOMNESS = 1.0;
    private static final Random rand = new Random();

    private static int maxTextureId = 0;

    private int textureId = 0;

    public static void register(int maxTextureId)
    {
        EntityAttackEffect.maxTextureId = maxTextureId;
        EntityRegistry.registerModEntity(
                EntityAttackEffect.class,
                "AttackEffect",
                EntityRegistry.findGlobalUniqueEntityId(),
                Necktie.instance,
                256,
                1,
                false
        );
    }

    public EntityAttackEffect(World par1World)
    {
        this(par1World, Vec3.createVectorHelper(0.0, 0.0, 0.0), 0.0F);
    }

    public EntityAttackEffect(World world, Vec3 pos, float yaw)
    {
        super(world);
        this.posX = pos.xCoord + rand.nextDouble() * RANDOMNESS;
        this.posY = pos.yCoord + rand.nextDouble() * RANDOMNESS;
        this.posZ = pos.zCoord + rand.nextDouble() * RANDOMNESS;
        this.rotationYaw = yaw;
        setTextureId(rand.nextInt(maxTextureId));
    }

    @Override
    protected void entityInit()
    {
        this.dataWatcher.addObject(DATA_WATCHER_ID_TEXTURE_ID, this.textureId);
    }

    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        if (this.isDead)
        {
            return;
        }

        if (this.ticksExisted > MAX_LIFE_TICK)
        {
            setDead();
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        setTextureId(nbttagcompound.getInteger("textureId"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setInteger("textureId", this.textureId);
    }

    public int getTextureId()
    {
        this.textureId = this.dataWatcher.getWatchableObjectInt(DATA_WATCHER_ID_TEXTURE_ID);
        return this.textureId;
    }

    private void setTextureId(int id)
    {
        this.textureId = id;
        this.dataWatcher.updateObject(DATA_WATCHER_ID_TEXTURE_ID, id);
    }
}
