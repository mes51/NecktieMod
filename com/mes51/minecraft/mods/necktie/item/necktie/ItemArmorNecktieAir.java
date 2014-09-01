package com.mes51.minecraft.mods.necktie.item.necktie;

import com.mes51.minecraft.mods.necktie.Necktie;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.Util;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

/**
 * Package: com.mes51.minecraft.mods.necktie.item
 * Date: 2014/05/09
 * Time: 0:51
 */
public class ItemArmorNecktieAir extends ItemArmorNecktieBase
{
    public static enum Level
    {
        FIRST(5, null, 0, 0.0F, 1.0F, 0),
        SECOND(10, Item.pickaxeStone, 2, 1.0F, 4.0F, 1),
        THIRD(20, Item.pickaxeIron, 8, 2.0F, 6.0F, 2),
        FORTH(35, Item.pickaxeDiamond, 16, 3.0F, 8.0F, 3),
        FIFTH(50, Item.pickaxeDiamond, 33, 7.0F, 16.0F, 4),
        FINAL(Integer.MAX_VALUE, Item.pickaxeDiamond, 50, 15.0F, 32.0F, 5);

        private int thresholdLevel = 0;
        private Item harvestItem = null;
        private int armor = 0;
        private float vsDamage = 0.0F;
        private float efficiency = 0.0F;
        private int potionDurationLevel = 0;

        public static Level byLevel(int level)
        {
            for (Level l : values())
            {
                if (l.thresholdLevel > level)
                {
                    return l;
                }
            }
            return FINAL;
        }

        private Level(int thresholdLevel, Item harvestItem, int armor, float vsDamage, float efficiency, int potionDurationLevel)
        {
            this.thresholdLevel = thresholdLevel;
            this.harvestItem = harvestItem;
            this.armor = armor;
            this.vsDamage = vsDamage;
            this.efficiency = efficiency;
            this.potionDurationLevel = potionDurationLevel;
        }

        public Item getHarvestTargetTool()
        {
            return this.harvestItem;
        }

        public int getArmor()
        {
            return this.armor;
        }

        public float getVsDamage()
        {
            return this.vsDamage;
        }

        public float getEfficiency()
        {
            return this.efficiency;
        }

        public int getPotionDurationLevel()
        {
            return this.potionDurationLevel;
        }
    }

    private static Item instance = null;

    public static void register(int itemId)
    {
        int index = Necktie.proxy.getArmorRenderPrefix("armorNecktie");
        instance = new ItemArmorNecktieAir(itemId, index);

        GameRegistry.registerItem(instance, Const.Item.ItemName.AIR_NECKTIE);
        LanguageRegistry.addName(instance, "Air Necktie");
    }

    public static void registerRecipe()
    {
        GameRegistry.addShapelessRecipe(
                new ItemStack(instance, 1),
                new ItemStack(ItemArmorIronNecktie.getInstance(), 1),
                new ItemStack(Block.glass, 1)
        );
        GameRegistry.addShapelessRecipe(
                new ItemStack(instance, 1),
                new ItemStack(ItemArmorDiamondNecktie.getInstance(), 1),
                new ItemStack(Block.glass, 1)
        );
        if (Util.ic2Loaded())
        {
            GameRegistry.addShapelessRecipe(
                    new ItemStack(instance, 1),
                    new ItemStack(ItemArmorNukeNecktie.getInstance(), 1),
                    new ItemStack(Block.glass, 1)
            );
        }
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemArmorNecktieAir(int par1, int index)
    {
        super(par1, Const.Material.Armor.AIR, index, Const.Material.Tool.AIR, 0, "Air");
        setMaxDamage(0);
        setUnlocalizedName(Const.Item.ItemName.AIR_NECKTIE);
        setTextureName(Const.Item.ItemName.AIR_NECKTIE);
    }

    @Override
    public float getDamageVsEntity(EntityPlayer player)
    {
        return Level.byLevel(player.experienceLevel).getVsDamage();
    }

    @Override
    public boolean canReflectArrow(EntityPlayer player)
    {
        return Level.byLevel(player.experienceLevel) != Level.FIRST;
    }

    @Override
    protected int getPotionDurationLevel(EntityPlayer player)
    {
        return Level.byLevel(player.experienceLevel).getPotionDurationLevel();
    }

    @Override
    public float getEfficiencyOnProperMaterial(EntityPlayer player)
    {
        return Level.byLevel(player.experienceLevel).getEfficiency();
    }

    @Override
    public boolean canHarvestBlock(Block par1Block)
    {
        return false;
    }

    @Override
    public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack)
    {
        if (Level.byLevel(player.experienceLevel) != Level.FIRST)
        {
            super.onArmorTickUpdate(world, player, itemStack);
        }
    }

    @Override
    public boolean canSetAirBlock()
    {
        return true;
    }

    @Override
    public PotionEffect[] getBeaconEffect(EntityPlayer player)
    {
        return new PotionEffect[0];
    }

    public boolean canHarvestBlockByPlayer(EntityPlayer player, Block block)
    {
        Item tool = Level.byLevel(player.experienceLevel).getHarvestTargetTool();
        return tool != null && ForgeHooks.canToolHarvestBlock(block, 0, new ItemStack(tool, 1));
    }
}
