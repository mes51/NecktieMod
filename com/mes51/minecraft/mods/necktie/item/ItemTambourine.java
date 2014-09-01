package com.mes51.minecraft.mods.necktie.item;

import com.mes51.minecraft.mods.necktie.item.necktie.ItemArmorNecktieBase;
import com.mes51.minecraft.mods.necktie.recipe.RecipeUpgrade;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.NecktieCreativeTabs;
import com.mes51.minecraft.mods.necktie.util.Util;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

/**
 * Package: com.mes51.minecraft.mods.necktie.item
 * Date: 2014/07/07
 * Time: 12:49
 */
public class ItemTambourine extends Item
{
    private static final double SEARCH_RANGE = 20.0;
    private static final double EFFECT_RANGE = 8.0;

    private static Item instance = null;

    public static void register(int itemId)
    {
        instance = new ItemTambourine(itemId);

        GameRegistry.registerItem(instance, Const.Item.ItemName.TAMBOURINE);
        LanguageRegistry.addName(instance, "Tambourine");
    }

    public static void registerRecipe()
    {
        GameRegistry.addRecipe(
                new ItemStack(instance, 1),
                new Object[] {
                        "SIS",
                        "ILI",
                        "SIS",
                        'S', new ItemStack(Item.stick, 1),
                        'I', new ItemStack(Item.ingotIron, 1),
                        'L', new ItemStack(Item.leather, 1)
                }
        );

        GameRegistry.addRecipe(new RecipeUpgrade(instance));
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemTambourine(int par1)
    {
        super(par1);
        setUnlocalizedName(Const.Item.ItemName.TAMBOURINE);
        setTextureName(Const.Item.ItemName.TAMBOURINE);
        setMaxStackSize(1);
        setNoRepair();
        setHasSubtypes(true);
        setCreativeTab(NecktieCreativeTabs.instance);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par3EntityPlayer.playSound(Const.Item.Sound.TAMBOURINE, 1.0F, 1.0F);
        if (par1ItemStack.getItemDamage() > 0 && ItemArmorNecktieBase.playerIsMuscle(par3EntityPlayer))
        {
            AxisAlignedBB aabb = par3EntityPlayer.boundingBox.expand(SEARCH_RANGE, SEARCH_RANGE, SEARCH_RANGE);
            List entities = par2World.getEntitiesWithinAABBExcludingEntity(par3EntityPlayer, aabb);
            for (Entity entity : (List<Entity>)entities)
            {
                if (entity instanceof EntityLivingBase)
                {
                    Vec3 from = Util.getEntityPosition(par3EntityPlayer);
                    Vec3 to = Util.getEntityPosition((EntityLivingBase)entity);
                    double dist = from.distanceTo(to);
                    if (dist < EFFECT_RANGE)
                    {
                        entity.attackEntityFrom(DamageSource.magic, (float) ((EFFECT_RANGE - dist) / EFFECT_RANGE * 3.0));
                    }
                }
            }
        }
        return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
    }
}
