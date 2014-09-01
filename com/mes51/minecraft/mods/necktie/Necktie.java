package com.mes51.minecraft.mods.necktie;

import com.mes51.minecraft.mods.necktie.block.BlockAirStone;
import com.mes51.minecraft.mods.necktie.block.BlockMachine;
import com.mes51.minecraft.mods.necktie.block.BlockNecktie;
import com.mes51.minecraft.mods.necktie.block.BlockNecktieBeacon;
import com.mes51.minecraft.mods.necktie.entity.EntityAttackEffect;
import com.mes51.minecraft.mods.necktie.handler.EventHandler;
import com.mes51.minecraft.mods.necktie.handler.GuiHandler;
import com.mes51.minecraft.mods.necktie.item.*;
import com.mes51.minecraft.mods.necktie.handler.PacketHandler;
import com.mes51.minecraft.mods.necktie.item.necktie.*;
import com.mes51.minecraft.mods.necktie.potion.PotionFullyFed;
import com.mes51.minecraft.mods.necktie.potion.PotionMuscle;
import com.mes51.minecraft.mods.necktie.proxy.IProxy;
import com.mes51.minecraft.mods.necktie.util.Const;
import com.mes51.minecraft.mods.necktie.recipe.RecipeReplacer;
import com.mes51.minecraft.mods.necktie.util.Util;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.event.ForgeSubscribe;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Package: com.mes51.minecraft.mods.necktie
 * Date: 2014/04/15
 * Time: 21:49
 */
@Mod(name = "Necktie", modid = "Necktie", version = "1.0.1", dependencies="after:IC2; after:BuildCraft|Core; after:BuildCraft|Builders; after:BuildCraft|Energy; after:BuildCraft|Factory; after:BuildCraft|Silicon; after:BuildCraft|Transport;")
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = true,
        packetHandler = PacketHandler.class,
        channels = {
                PacketHandler.PACKET_ARMOR_COMMAND,
                PacketHandler.PACKET_SYNC_ITEM_NBT,
                PacketHandler.PACKET_SYNC_CONTAINER,
                PacketHandler.PACKET_SYNC_TILE_ENTITY
        },
        versionBounds = "[1.0.1]"
)
public class Necktie
{
    @Mod.Instance("Necktie")
    public static Necktie instance;

    @SidedProxy(
            clientSide = "com.mes51.minecraft.mods.necktie.proxy.ClientProxy",
            serverSide = "com.mes51.minecraft.mods.necktie.proxy.ServerProxy"
    )
    public static IProxy proxy;

    private HashMap<String, Object> properties;

    public String configurationDirectory = "";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);

        properties = new HashMap<String, Object>();
        configurationDirectory = event.getModConfigurationDirectory().getAbsolutePath();
        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());

        try
        {
            cfg.load();

            // block
            Property property = cfg.getBlock(Const.Block.CfgBlockId.MACHINE_BLOCK, 2101);
            properties.put(Const.Block.CfgBlockId.MACHINE_BLOCK, property.getInt());
            property = cfg.getBlock(Const.Block.CfgBlockId.AIR_STONE, 2102);
            properties.put(Const.Block.CfgBlockId.AIR_STONE, property.getInt());
            property = cfg.getBlock(Const.Block.CfgBlockId.NECKTIE_BLOCK, 2103);
            properties.put(Const.Block.CfgBlockId.NECKTIE_BLOCK, property.getInt());
            property = cfg.getBlock(Const.Block.CfgBlockId.NECKTIE_BEACON, 2104);
            properties.put(Const.Block.CfgBlockId.NECKTIE_BEACON, property.getInt());

            // item
            property = cfg.getItem(Const.Item.CfgItemId.NECKTIE, 12522);
            properties.put(Const.Item.CfgItemId.NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.IRON_NECKTIE, 12523);
            properties.put(Const.Item.CfgItemId.IRON_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.DIAMOND_NECKTIE, 12524);
            properties.put(Const.Item.CfgItemId.DIAMOND_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.DRAGON_NECKTIE, 12525);
            properties.put(Const.Item.CfgItemId.DRAGON_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.DOPING_FOOD, 12526);
            properties.put(Const.Item.CfgItemId.DOPING_FOOD, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.NECKTIE_HEAD_BAND, 12527);
            properties.put(Const.Item.CfgItemId.NECKTIE_HEAD_BAND, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.IRON_NECKTIE_HEAD_BAND, 12528);
            properties.put(Const.Item.CfgItemId.IRON_NECKTIE_HEAD_BAND, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.DIAMOND_NECKTIE_HEAD_BAND, 12529);
            properties.put(Const.Item.CfgItemId.DIAMOND_NECKTIE_HEAD_BAND, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.DRAGON_NECKTIE_HEAD_BAND, 12530);
            properties.put(Const.Item.CfgItemId.DRAGON_NECKTIE_HEAD_BAND, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.NUKE_NECKTIE, 12531);
            properties.put(Const.Item.CfgItemId.NUKE_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.AIR_NECKTIE, 12532);
            properties.put(Const.Item.CfgItemId.AIR_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.NECKTIE_PATTERN, 12533);
            properties.put(Const.Item.CfgItemId.NECKTIE_PATTERN, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.IRON_CASTED_NECKTIE, 12534);
            properties.put(Const.Item.CfgItemId.IRON_CASTED_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.COPPER_CASTED_NECKTIE, 12535);
            properties.put(Const.Item.CfgItemId.COPPER_CASTED_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.COBALT_CASTED_NECKTIE, 12536);
            properties.put(Const.Item.CfgItemId.COBALT_CASTED_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.ARDITE_CASTED_NECKTIE, 12537);
            properties.put(Const.Item.CfgItemId.ARDITE_CASTED_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.MANYULLYN_CASTED_NECKTIE, 12538);
            properties.put(Const.Item.CfgItemId.MANYULLYN_CASTED_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.BRONZE_CASTED_NECKTIE, 12539);
            properties.put(Const.Item.CfgItemId.BRONZE_CASTED_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.ALUMITE_CASTED_NECKTIE, 12540);
            properties.put(Const.Item.CfgItemId.ALUMITE_CASTED_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.OBSIDIAN_CASTED_NECKTIE, 12541);
            properties.put(Const.Item.CfgItemId.OBSIDIAN_CASTED_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.STEEL_CASTED_NECKTIE, 12542);
            properties.put(Const.Item.CfgItemId.STEEL_CASTED_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.PIG_IRON_CASTED_NECKTIE, 12543);
            properties.put(Const.Item.CfgItemId.PIG_IRON_CASTED_NECKTIE, property.getInt());
            property = cfg.getItem(Const.Item.CfgItemId.TAMBOURINE, 12544);
            properties.put(Const.Item.CfgItemId.TAMBOURINE, property.getInt());

            // potion
            property = cfg.get("potion", Const.Potion.CfgPotionId.FULLY_FED, 30);
            properties.put(Const.Potion.CfgPotionId.FULLY_FED, property.getInt());
            property = cfg.get("potion", Const.Potion.CfgPotionId.MUSCLE, 31);
            properties.put(Const.Potion.CfgPotionId.MUSCLE, property.getInt());

            // key
            property = cfg.get(Const.Key.CfgKeyConfig.CONFIG_CATEGORY_KEY, Const.Key.CfgKeyConfig.ITEM_ARMOR_NECKTIE_MUSCLE_MODE_KEY, 0x25); // K
            properties.put(Const.Key.CfgKeyConfig.ITEM_ARMOR_NECKTIE_MUSCLE_MODE_KEY, property.getInt());
            property = cfg.get(Const.Key.CfgKeyConfig.CONFIG_CATEGORY_KEY, Const.Key.CfgKeyConfig.ITEM_ARMOR_NECKTIE_BREAK_ALL_SWITCH_KEY, 0x32); // M
            properties.put(Const.Key.CfgKeyConfig.ITEM_ARMOR_NECKTIE_BREAK_ALL_SWITCH_KEY, property.getInt()); //22
            property = cfg.get(Const.Key.CfgKeyConfig.CONFIG_CATEGORY_KEY, Const.Key.CfgKeyConfig.ITEM_ARMOR_NECKTIE_GLIDE_KEY, 0x22); // G
            properties.put(Const.Key.CfgKeyConfig.ITEM_ARMOR_NECKTIE_GLIDE_KEY, property.getInt());

            // general
            property = cfg.get(Const.Item.CfgGeneralId.CATEGORY, Const.Item.CfgGeneralId.NECKTIE_BREAK_BLOCK_IDS, new int[] { 17, 14, 15, 16, 21, 49, 56, 73, 74, 89, 129, 153 });
            properties.put(Const.Item.CfgGeneralId.NECKTIE_BREAK_BLOCK_IDS, property.getIntList());
            property = cfg.get(Const.Item.CfgGeneralId.CATEGORY, Const.Item.CfgGeneralId.ITEM_PUDDING_MILK_ID, 335);
            properties.put(Const.Item.CfgGeneralId.ITEM_PUDDING_MILK_ID, property.getInt());
            property = cfg.get(Const.Item.CfgGeneralId.CATEGORY, Const.Item.CfgGeneralId.ENABLE_ATTACK_EFFECT, true);
            properties.put(Const.Item.CfgGeneralId.ENABLE_ATTACK_EFFECT, property.getBoolean(true));
            property = cfg.get(Const.Item.CfgGeneralId.CATEGORY, Const.Item.CfgGeneralId.MAX_ATTACK_EFFECT_ID, 4);
            properties.put(Const.Item.CfgGeneralId.MAX_ATTACK_EFFECT_ID, property.getInt());
            property = cfg.get(Const.Item.CfgGeneralId.CATEGORY, Const.Item.CfgGeneralId.REPLACE_TOOL_MATERIAL, true);
            properties.put(Const.Item.CfgGeneralId.REPLACE_TOOL_MATERIAL, property.getBoolean(true));
            property = cfg.get(Const.Item.CfgGeneralId.CATEGORY, Const.Item.CfgGeneralId.REPLACE_TOOL_RECIPE, true);
            properties.put(Const.Item.CfgGeneralId.REPLACE_TOOL_RECIPE, property.getBoolean(true));
        }
        catch (Exception e)
        {
            FMLLog.log(Level.WARNING, e, "can't load setting");
        }
        finally
        {
            cfg.save();
        }
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        PotionFullyFed.register((Integer)properties.get(Const.Potion.CfgPotionId.FULLY_FED));
        PotionMuscle.register((Integer)properties.get(Const.Potion.CfgPotionId.MUSCLE));

        BlockMachine.register((Integer)properties.get(Const.Block.CfgBlockId.MACHINE_BLOCK));
        BlockAirStone.register((Integer)properties.get(Const.Block.CfgBlockId.AIR_STONE));
        BlockNecktie.register((Integer)properties.get(Const.Block.CfgBlockId.NECKTIE_BLOCK));
        BlockNecktieBeacon.register((Integer)properties.get(Const.Block.CfgBlockId.NECKTIE_BEACON));

        ItemArmorNecktie.register((Integer) properties.get(Const.Item.CfgItemId.NECKTIE));
        ItemArmorIronNecktie.register((Integer)properties.get(Const.Item.CfgItemId.IRON_NECKTIE));
        ItemArmorDiamondNecktie.register((Integer)properties.get(Const.Item.CfgItemId.DIAMOND_NECKTIE));
        ItemArmorDragonNecktie.register((Integer)properties.get(Const.Item.CfgItemId.DRAGON_NECKTIE));
        ItemDopingFood.register((Integer)properties.get(Const.Item.CfgItemId.DOPING_FOOD));
        ItemArmorNecktieHeadBand.register((Integer)properties.get(Const.Item.CfgItemId.NECKTIE_HEAD_BAND));
        ItemArmorIronNecktieHeadBand.register((Integer)properties.get(Const.Item.CfgItemId.IRON_NECKTIE_HEAD_BAND));
        ItemArmorDiamondNecktieHeadBand.register((Integer)properties.get(Const.Item.CfgItemId.DIAMOND_NECKTIE_HEAD_BAND));
        ItemArmorDragonNecktieHeadBand.register((Integer) properties.get(Const.Item.CfgItemId.DRAGON_NECKTIE_HEAD_BAND));
        ItemArmorNukeNecktie.register((Integer) properties.get(Const.Item.CfgItemId.NUKE_NECKTIE));
        ItemArmorNecktieAir.register((Integer) properties.get(Const.Item.CfgItemId.AIR_NECKTIE));
        ItemNecktiePattern.register((Integer)properties.get(Const.Item.CfgItemId.NECKTIE_PATTERN));
        ItemArmorIronCastedNecktie.register((Integer) properties.get(Const.Item.CfgItemId.IRON_CASTED_NECKTIE));
        ItemArmorCopperCastedNecktie.register((Integer)properties.get(Const.Item.CfgItemId.COPPER_CASTED_NECKTIE));
        ItemArmorCobaltCastedNecktie.register((Integer)properties.get(Const.Item.CfgItemId.COBALT_CASTED_NECKTIE));
        ItemArmorArditeCastedNecktie.register((Integer)properties.get(Const.Item.CfgItemId.ARDITE_CASTED_NECKTIE));
        ItemArmorManyullynCastedNecktie.register((Integer)properties.get(Const.Item.CfgItemId.MANYULLYN_CASTED_NECKTIE));
        ItemArmorBronzeCastedNecktie.register((Integer)properties.get(Const.Item.CfgItemId.BRONZE_CASTED_NECKTIE));
        ItemArmorAlumiteCastedNecktie.register((Integer)properties.get(Const.Item.CfgItemId.ALUMITE_CASTED_NECKTIE));
        ItemArmorObsidianCastedNecktie.register((Integer)properties.get(Const.Item.CfgItemId.OBSIDIAN_CASTED_NECKTIE));
        ItemArmorSteelCastedNecktie.register((Integer)properties.get(Const.Item.CfgItemId.STEEL_CASTED_NECKTIE));
        ItemArmorPigIronCastedNecktie.register((Integer)properties.get(Const.Item.CfgItemId.PIG_IRON_CASTED_NECKTIE));
        ItemTambourine.register((Integer)properties.get(Const.Item.CfgItemId.TAMBOURINE));
        ItemMachineBlock.register();
        ItemNecktieBlock.register();

        EntityAttackEffect.register((Integer)properties.get(Const.Item.CfgGeneralId.MAX_ATTACK_EFFECT_ID));

        proxy.registerRender();
        proxy.registerKey();
        NetworkRegistry.instance().registerGuiHandler(instance, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new EventHandler((int[])properties.get(Const.Item.CfgGeneralId.NECKTIE_BREAK_BLOCK_IDS), (Boolean)properties.get(Const.Item.CfgGeneralId.ENABLE_ATTACK_EFFECT)));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void postLoad(FMLPostInitializationEvent event)
    {
        BlockNecktieBeacon.registerRecipe();

        ItemArmorNecktie.registerRecipe();
        ItemArmorIronNecktie.registerRecipe();
        ItemArmorDiamondNecktie.registerRecipe();
        ItemArmorDragonNecktie.registerRecipe();
        ItemArmorNecktieHeadBand.registerRecipe();
        ItemArmorIronNecktieHeadBand.registerRecipe();
        ItemArmorDiamondNecktieHeadBand.registerRecipe();
        ItemArmorDragonNecktieHeadBand.registerRecipe();
        ItemArmorNukeNecktie.registerRecipe();
        ItemArmorNecktieAir.registerRecipe();
        ItemDopingFood.registerRecipe((Integer)properties.get(Const.Item.CfgGeneralId.ITEM_PUDDING_MILK_ID));
        ItemMachineBlock.registerRecipe();
        ItemNecktieBlock.registerRecipe();
        ItemNecktiePattern.registerRecipe();
        ItemTambourine.registerRecipe();

        if ((Boolean)properties.get(Const.Item.CfgGeneralId.REPLACE_TOOL_MATERIAL))
        {
            replaceToolMaterial();
        }
        if ((Boolean)properties.get(Const.Item.CfgGeneralId.REPLACE_TOOL_RECIPE))
        {
            replaceToolRecipe();
            replaceToolUseRecipe();
        }

        Const.Material.registerRepairItem();

        ItemArmorNecktieBase.registerKey(
                (Integer)properties.get(Const.Key.CfgKeyConfig.ITEM_ARMOR_NECKTIE_MUSCLE_MODE_KEY),
                (Integer)properties.get(Const.Key.CfgKeyConfig.ITEM_ARMOR_NECKTIE_BREAK_ALL_SWITCH_KEY),
                (Integer)properties.get(Const.Key.CfgKeyConfig.ITEM_ARMOR_NECKTIE_GLIDE_KEY)
        );
    }

    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    public void onSoundLoad(SoundLoadEvent event)
    {
        try
        {
            event.manager.addSound(Const.Item.Sound.TAMBOURINE + ".ogg");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void replaceToolMaterial()
    {
        EnumToolMaterial fragileWood = EnumHelper.addToolMaterial("FRAGILE_" + EnumToolMaterial.WOOD.name(), EnumToolMaterial.WOOD.getHarvestLevel(), 1, EnumToolMaterial.WOOD.getEfficiencyOnProperMaterial(), EnumToolMaterial.WOOD.getDamageVsEntity(), EnumToolMaterial.WOOD.getEnchantability());
        EnumToolMaterial fragileStone = EnumHelper.addToolMaterial("FRAGILE_" + EnumToolMaterial.STONE.name(), EnumToolMaterial.STONE.getHarvestLevel(), 1, EnumToolMaterial.STONE.getEfficiencyOnProperMaterial(), EnumToolMaterial.STONE.getDamageVsEntity(), EnumToolMaterial.STONE.getEnchantability());
        EnumToolMaterial fragileIron = EnumHelper.addToolMaterial("FRAGILE_" + EnumToolMaterial.IRON.name(), EnumToolMaterial.IRON.getHarvestLevel(), 1, EnumToolMaterial.IRON.getEfficiencyOnProperMaterial(), EnumToolMaterial.IRON.getDamageVsEntity(), EnumToolMaterial.IRON.getEnchantability());
        EnumToolMaterial fragileGold = EnumHelper.addToolMaterial("FRAGILE_" + EnumToolMaterial.GOLD.name(), EnumToolMaterial.GOLD.getHarvestLevel(), 1, EnumToolMaterial.GOLD.getEfficiencyOnProperMaterial(), EnumToolMaterial.GOLD.getDamageVsEntity(), EnumToolMaterial.GOLD.getEnchantability());
        EnumToolMaterial fragileDiamond = EnumHelper.addToolMaterial("FRAGILE_" + EnumToolMaterial.EMERALD.name(), EnumToolMaterial.EMERALD.getHarvestLevel(), 1, EnumToolMaterial.EMERALD.getEfficiencyOnProperMaterial(), EnumToolMaterial.EMERALD.getDamageVsEntity(), EnumToolMaterial.EMERALD.getEnchantability());
        Map<EnumToolMaterial, EnumToolMaterial> map = new HashMap<EnumToolMaterial, EnumToolMaterial>();
        map.put(EnumToolMaterial.WOOD, fragileWood);
        map.put(EnumToolMaterial.STONE, fragileStone);
        map.put(EnumToolMaterial.IRON, fragileIron);
        map.put(EnumToolMaterial.GOLD, fragileGold);
        map.put(EnumToolMaterial.EMERALD, fragileDiamond);
        ItemTool[] tools = new ItemTool[] {
                (ItemTool)Item.axeWood,
                (ItemTool)Item.axeStone,
                (ItemTool)Item.axeIron,
                (ItemTool)Item.axeGold,
                (ItemTool)Item.axeDiamond,
                (ItemTool)Item.shovelWood,
                (ItemTool)Item.shovelStone,
                (ItemTool)Item.shovelIron,
                (ItemTool)Item.shovelGold,
                (ItemTool)Item.shovelDiamond,
                (ItemTool)Item.pickaxeWood,
                (ItemTool)Item.pickaxeStone,
                (ItemTool)Item.pickaxeIron,
                (ItemTool)Item.pickaxeGold,
                (ItemTool)Item.pickaxeDiamond,
        };
        Item[] swords = new Item[] {
                Item.swordWood,
                Item.swordStone,
                Item.swordIron,
                Item.swordGold,
                Item.swordDiamond
        };
        boolean remapped = ItemTool.class.getSimpleName().equals("ItemTool");
        for (ItemTool tool : tools)
        {
            Field toolMaterialField = Util.tryGetField(ItemTool.class, "toolMaterial", "field_77862_b");
            try
            {
                EnumToolMaterial material = map.get(toolMaterialField.get(tool));
                if (material != null)
                {
                    toolMaterialField.set(tool, material);
                }
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            tool.setMaxDamage(1);
        }
        // sword
        for (Item tool : swords)
        {
            Field toolMaterialField = Util.tryGetField(tool.getClass(), "toolMaterial", "field_77826_b");
            toolMaterialField.setAccessible(true);
            try
            {
                toolMaterialField.set(tool, map.get(toolMaterialField.get(tool)));
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            tool.setMaxDamage(1);
        }
    }

    private void replaceToolRecipe()
    {
        Item[] tools = new Item[] {
                Item.axeWood,
                Item.axeStone,
                Item.axeIron,
                Item.axeGold,
                Item.axeDiamond,
                Item.shovelWood,
                Item.shovelStone,
                Item.shovelIron,
                Item.shovelGold,
                Item.shovelDiamond,
                Item.pickaxeWood,
                Item.pickaxeStone,
                Item.pickaxeIron,
                Item.pickaxeGold,
                Item.pickaxeDiamond,
                Item.swordWood,
                Item.swordStone,
                Item.swordIron,
                Item.swordGold,
                Item.swordDiamond
        };
        Map<Item, Item> map = new HashMap<Item, Item>();
        map.put(Item.ingotIron, Item.itemsList[Block.blockIron.blockID]);
        map.put(Item.ingotGold, Item.itemsList[Block.blockGold.blockID]);
        map.put(Item.diamond, Item.itemsList[Block.blockDiamond.blockID]);

        for (Item tool : tools)
        {
            RecipeReplacer.replaceRecipe(tool, map);
        }
    }

    private void replaceToolUseRecipe()
    {
        Map<Item, Item> map = new HashMap<Item, Item>();
        map.put(Item.axeStone, ItemArmorNecktie.getInstance());
        map.put(Item.pickaxeStone, ItemArmorNecktie.getInstance());
        map.put(Item.shovelStone, ItemArmorNecktie.getInstance());
        map.put(Item.swordStone, ItemArmorNecktie.getInstance());
        map.put(Item.axeIron, ItemArmorIronNecktie.getInstance());
        map.put(Item.pickaxeIron, ItemArmorIronNecktie.getInstance());
        map.put(Item.shovelIron, ItemArmorIronNecktie.getInstance());
        map.put(Item.swordIron, ItemArmorIronNecktie.getInstance());
        map.put(Item.axeDiamond, ItemArmorDiamondNecktie.getInstance());
        map.put(Item.pickaxeDiamond, ItemArmorDiamondNecktie.getInstance());
        map.put(Item.shovelDiamond, ItemArmorDiamondNecktie.getInstance());
        map.put(Item.swordDiamond, ItemArmorDiamondNecktie.getInstance());

        RecipeReplacer.replaceAllRecipe(map);
    }
}
