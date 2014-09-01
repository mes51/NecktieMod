package com.mes51.minecraft.mods.necktie.item.necktie;

import com.mes51.minecraft.mods.necktie.util.NecktieCreativeTabs;
import com.mes51.minecraft.mods.necktie.util.enumerable.Enumerable;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.Action;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.Predicate;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.SingleArgFunc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

/**
 * Package: com.mes51.minecraft.mods.necktie.item
 * Date: 2014/04/24
 * Time: 22:13
 */
public abstract class ItemArmorNecktieHeadBandBase extends ItemArmor implements INecktie
{
    private static final String TEXTURE_ARMOR_PATH = "com.mes51.minecraft.mods.necktie:textures/armor/armorNecktieHeadBand";
    private static final double OVERAWE_LENGTH = 5.0;
    private static final double OVERAWE_SEARCH_RANGE = 32.0;
    private static final int POTION_EFFECT_TICK = 100;

    private EnumToolMaterial toolMaterial = null;
    private String suffix = "";

    public ItemArmorNecktieHeadBandBase(int par1, EnumArmorMaterial material, int index, EnumToolMaterial toolMaterial, String suffix)
    {
        super(par1, material, index, 0);
        this.toolMaterial = toolMaterial;
        this.suffix = suffix;
        setMaxStackSize(1);
        setCreativeTab(NecktieCreativeTabs.instance);
    }

    @Override
    public float getDamageVsEntity(EntityPlayer player)
    {
        return this.toolMaterial.getDamageVsEntity();
    }

    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
    {
        return TEXTURE_ARMOR_PATH + this.suffix +  ".png";
    }

    @Override
    public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack)
    {
        super.onArmorTickUpdate(world, player, itemStack);

        player.addPotionEffect(new PotionEffect(Potion.blindness.getId(), POTION_EFFECT_TICK, 0));
        player.addPotionEffect(new PotionEffect(Potion.confusion.getId(), POTION_EFFECT_TICK, 0));

        final Vec3 pos = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
        List entities = world.getEntitiesWithinAABBExcludingEntity(
                player,
                player.boundingBox.expand(OVERAWE_SEARCH_RANGE, OVERAWE_SEARCH_RANGE, OVERAWE_SEARCH_RANGE)
        );
        Enumerable.<Object>from(entities).where(
                new Predicate<Object>()
                {
                    @Override
                    public boolean predicate(Object value)
                    {
                        return value instanceof EntityLivingBase;
                    }
                }
        ).select(
                new SingleArgFunc<Object, EntityLivingBase>()
                {
                    @Override
                    public EntityLivingBase func(Object value)
                    {
                        return (EntityLivingBase)value;
                    }
                }
        ).each(
                new Action<EntityLivingBase>()
                {
                    @Override
                    public void action(EntityLivingBase value)
                    {
                        Vec3 sub = pos.subtract(Vec3.createVectorHelper(value.posX, value.posY, value.posZ));
                        double dist = sub.lengthVector();
                        if (dist < OVERAWE_LENGTH)
                        {
                            Vec3 normal = sub.normalize();
                            dist *= 0.1;
                            value.motionX += normal.xCoord * dist;
                            value.motionY += normal.yCoord * dist;
                            value.motionZ += normal.zCoord * dist;
                        }
                    }
                }
        ).toList();
    }
}
