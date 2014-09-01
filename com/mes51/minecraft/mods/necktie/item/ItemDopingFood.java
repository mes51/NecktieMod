package com.mes51.minecraft.mods.necktie.item;

import com.mes51.minecraft.mods.necktie.potion.PotionMuscle;
import com.mes51.minecraft.mods.necktie.recipe.RecipeAir;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.util.NecktieCreativeTabs;
import com.mes51.minecraft.mods.necktie.util.Util;
import com.mes51.minecraft.mods.necktie.util.enumerable.Enumerable;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.Predicate;
import com.mes51.minecraft.mods.necktie.util.enumerable.operator.SingleArgFunc;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import java.util.List;

/**
 * Package: com.mes51.minecraft.mods.necktie.item
 * Date: 2014/04/20
 * Time: 18:16
 */
public class ItemDopingFood extends Item
{
    public static enum SubType
    {
        RAW_CONSOMME_SOUP(1, 0.6F, 1.0F, new PotionEffect[] { new PotionEffect(Potion.harm.getId(), 0, 1), new PotionEffect(Potion.poison.getId(), 200), new PotionEffect(Potion.hunger.getId(), 200) }, Item.bowlEmpty),
        DOPING_CONSOMME_SOUP(3, 2.4F, 1.0F, new PotionEffect[] { new PotionEffect(Potion.harm.getId(), 0, 2), new PotionEffect(Potion.poison.getId(), 600, 1), new PotionEffect(PotionMuscle.getInstance().getId(), 1200), new PotionEffect(Potion.resistance.getId(), 1200, 1) }, Item.bowlEmpty),
        PUDDING_INGREDIENTS(1, 0.6F, 1.0F, new PotionEffect[] { }, null),
        PUDDING(2, 1.5F, 1.0F, new PotionEffect[] { new PotionEffect(Potion.regeneration.getId(), 100, 0) }, null),
        BUCKET_PUDDING_INGREDIENTS(3, 1.5F, 1.0F, new PotionEffect[] { }, Item.bucketEmpty),
        BUCKET_PUDDING(10, 3.0F, 1.0F, new PotionEffect[] { new PotionEffect(Potion.regeneration.getId(), 800, 0) }, Item.bucketEmpty),
        AIR_MISO_SOUP(-1, 15.0F, 0.1F, new PotionEffect[] { new PotionEffect(PotionMuscle.getInstance().getId(), 600) }, null),
        PROTEIN(3, 10.0F, 1.0F, new PotionEffect[] { new PotionEffect(Potion.resistance.getId(), 1200, 1) }, null);

        public static SubType byDamage(int damage)
        {
            SubType[] types = values();
            if (types.length <= damage)
            {
                damage = 0;
            }
            return types[damage];
        }

        private int amount = 0;
        private float saturation = 0.0F;
        private float potionEffectProbability = 0.0F;
        private PotionEffect[] effects = null;
        private Item container = null;

        private SubType(int amount, float saturation, float potionEffectProbability, PotionEffect[] effects, Item container)
        {
            this.amount = amount;
            this.saturation = saturation;
            this.potionEffectProbability = potionEffectProbability;
            this.effects = effects;
            this.container = container;
        }

        public int getAmount()
        {
            return this.amount;
        }

        public float getSaturation()
        {
            return this.saturation;
        }

        public float getPotionEffectProbability()
        {
            return this.potionEffectProbability;
        }

        public PotionEffect[] getEffects()
        {
            return this.effects;
        }

        public Item getContainer()
        {
            return this.container;
        }

        public String getUnlocalizedName()
        {
            return Util.joinString(
                    Enumerable.from(name().split("_")).select(
                            new SingleArgFunc<String, String>()
                            {
                                @Override
                                public String func(String value)
                                {
                                    return Util.pascalize(value);
                                }
                            }
                    ).insert(0, Const.DOMAIN).insert(1, ":item"),
                    ""
            );
        }

        public String getDisplayName()
        {
            return Util.joinString(
                    Enumerable.from(name().split("_")).select(
                            new SingleArgFunc<String, String>()
                            {
                                @Override
                                public String func(String value)
                                {
                                    return Util.pascalize(value);
                                }
                            }
                    ),
                    " "
            );
        }

        public ItemStack getItemStack()
        {
            return new ItemStack(ItemDopingFood.getInstance(), 1, ordinal());
        }
    }

    private static Item instance = null;

    private Icon[] icons = new Icon[SubType.values().length];

    public static void register(int itemId)
    {
        instance = new ItemDopingFood(itemId);

        GameRegistry.registerItem(instance, Const.Item.ItemName.DOPING_FOOD);
        for (SubType type : SubType.values())
        {
            LanguageRegistry.addName(type.getItemStack(), type.getDisplayName());
        }
    }

    public static void registerRecipe(int milkId)
    {
        GameRegistry.addShapelessRecipe(
                SubType.RAW_CONSOMME_SOUP.getItemStack(),
                new Object[] {
                        new ItemStack(Item.fermentedSpiderEye, 1),
                        new ItemStack(Item.rottenFlesh, 1),
                        new ItemStack(Item.beefCooked, 1),
                        new ItemStack(Item.potato, 1),
                        new ItemStack(Item.carrot, 1),
                        new ItemStack(Item.redstone, 1),
                        new ItemStack(Item.bowlEmpty, 1)
                }
        );

        FurnaceRecipes.smelting().addSmelting(ItemDopingFood.getInstance().itemID, SubType.RAW_CONSOMME_SOUP.ordinal(), SubType.DOPING_CONSOMME_SOUP.getItemStack(), 0.0F);

        GameRegistry.addRecipe(
                SubType.PUDDING_INGREDIENTS.getItemStack(),
                new Object[] {
                        "S",
                        "E",
                        "M",
                        'S', new ItemStack(Item.sugar, 1),
                        'E', new ItemStack(Item.egg, 1),
                        'M', new ItemStack(milkId, 1, 0)
                }
        );
        FurnaceRecipes.smelting().addSmelting(ItemDopingFood.getInstance().itemID, SubType.PUDDING_INGREDIENTS.ordinal(), SubType.PUDDING.getItemStack(), 0.0F);

        GameRegistry.addRecipe(
                SubType.BUCKET_PUDDING_INGREDIENTS.getItemStack(),
                new Object[] {
                        "PPP",
                        "PPP",
                        " B ",
                        'P', SubType.PUDDING_INGREDIENTS.getItemStack(),
                        'B', new ItemStack(Item.bucketEmpty, 1)
                }
        );
        FurnaceRecipes.smelting().addSmelting(ItemDopingFood.getInstance().itemID, SubType.BUCKET_PUDDING_INGREDIENTS.ordinal(), SubType.BUCKET_PUDDING.getItemStack(), 0.0F);

        GameRegistry.addRecipe(
                new RecipeAir(
                        SubType.AIR_MISO_SOUP.getItemStack(),
                        new Predicate<ItemStack>()
                        {
                            @Override
                            public boolean predicate(ItemStack value)
                            {
                                return value.getItem() instanceof ItemFood || value.getItem() instanceof ItemDopingFood;
                            }
                        }
                )
        );

        GameRegistry.addShapelessRecipe(
                SubType.PROTEIN.getItemStack(),
                new ItemStack(Item.potato, 1),
                new ItemStack(Item.wheat, 1),
                new ItemStack(Item.beefRaw, 1),
                new ItemStack(Item.porkRaw, 1),
                new ItemStack(Item.chickenRaw, 1),
                new ItemStack(Item.egg, 1),
                new ItemStack(Item.bucketMilk, 1)
        );
    }

    public static Item getInstance()
    {
        return instance;
    }

    public ItemDopingFood(int itemId)
    {
        super(itemId);
        setHasSubtypes(true);
        setMaxDamage(0);
        setMaxStackSize(1);
        setCreativeTab(NecktieCreativeTabs.instance);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return SubType.byDamage(par1ItemStack.getItemDamage()).getUnlocalizedName();
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (SubType s : SubType.values())
        {
            par3List.add(s.getItemStack());
        }
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (SubType s : SubType.values())
        {
            this.icons[s.ordinal()] = par1IconRegister.registerIcon(s.getUnlocalizedName());
        }
    }

    @Override
    public Icon getIconFromDamage(int par1)
    {
        return this.icons[par1 % this.icons.length];
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.eat;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par3EntityPlayer.canEat(true))
        {
            par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        }

        return par1ItemStack;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        SubType type = SubType.byDamage(par1ItemStack.getItemDamage());
        if (type.getAmount() > 0)
        {
            par3EntityPlayer.getFoodStats().addStats(type.getAmount(), type.getSaturation());
        }
        else
        {
            par3EntityPlayer.getFoodStats().addExhaustion(type.getSaturation());
        }
        par2World.playSoundAtEntity(par3EntityPlayer, "random.burp", 0.5F, par2World.rand.nextFloat() * 0.1F + 0.9F);

        if (!par2World.isRemote && type.getPotionEffectProbability() >= par2World.rand.nextFloat())
        {
            for (PotionEffect effect : type.getEffects())
            {
                par3EntityPlayer.addPotionEffect(new PotionEffect(effect));
            }
        }

        par1ItemStack.stackSize--;
        if (type.getContainer() != null)
        {
            return new ItemStack(type.getContainer(), 1);
        }
        else
        {
            return par1ItemStack;
        }
    }
}
