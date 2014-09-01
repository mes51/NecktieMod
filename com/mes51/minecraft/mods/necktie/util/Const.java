package com.mes51.minecraft.mods.necktie.util;

import ic2.api.item.Items;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumHelper;

import java.text.DecimalFormat;

/**
 * Package: com.mes51.minecraft.mods.necktie.util
 * Date: 2014/04/15
 * Time: 21:51
 */
public class Const
{
    public static final String DOMAIN = "com.mes51.minecraft.mods.necktie";

    public static class Gui
    {
        public static final DecimalFormat STORAGE_FORMAT = new DecimalFormat("#,###,###,###.###");
    }

    public static class Material
    {
        public static class Tool
        {
            public static final EnumToolMaterial DRAGON = EnumHelper.addToolMaterial("DRAGON", 3, 10000, 16, 7, 22);
            public static final EnumToolMaterial NUKE = EnumHelper.addToolMaterial("NUKE", 3, 200, 10, 5, 22);
            public static final EnumToolMaterial AIR = EnumHelper.addToolMaterial("AIR", 1, 10000, 5, 1, 0);

            public static final EnumToolMaterial CASTED_IRON = EnumHelper.addToolMaterial("CASTED_IRON", 2, 250, 6.0F, 2.0F, 0);
            public static final EnumToolMaterial CASTED_COPPER = EnumHelper.addToolMaterial("CASTED_COPPER", 2, 180, 5.0F, 2.0F, 0);
            public static final EnumToolMaterial CASTED_COBALT = EnumHelper.addToolMaterial("CASTED_COBALT", 4, 800, 11.0F, 3.0F, 0);
            public static final EnumToolMaterial CASTED_ARDITE = EnumHelper.addToolMaterial("CASTED_ARDITE", 4, 600, 8.0F, 3.0F, 0);
            public static final EnumToolMaterial CASTED_MANYULLYN = EnumHelper.addToolMaterial("CASTED_MANYULLYN", 5, 1200, 9.0F, 4.0F, 0);
            public static final EnumToolMaterial CASTED_BRONZE = EnumHelper.addToolMaterial("CASTED_BRONZE", 4, 350, 7.0F, 2.0F, 0);
            public static final EnumToolMaterial CASTED_ALUMITE = EnumHelper.addToolMaterial("CASTED_ALUMITE", 4, 550, 8.0F, 3.0F, 0);
            public static final EnumToolMaterial CASTED_OBSIDIAN = EnumHelper.addToolMaterial("CASTED_OBSIDIAN", 4, 89, 7.0F, 3.0F, 0);
            public static final EnumToolMaterial CASTED_STEEL = EnumHelper.addToolMaterial("CASTED_STEEL", 4, 750, 8.0F, 3.0F, 0);
            public static final EnumToolMaterial CASTED_PIG_IRON = EnumHelper.addToolMaterial("CASTED_PIG_IRON", 3, 250, 6.0F, 2.0F, 0);
        }

        public static class Armor
        {
            public static final EnumArmorMaterial DRAGON = EnumHelper.addArmorMaterial("DRAGON", 2000, new int[] { 30, 30, 30, 30 }, 22);
            public static final EnumArmorMaterial NUKE = EnumHelper.addArmorMaterial("NUKE", 500, new int[] { 4, 10, 8, 6 }, 22);
            public static final EnumArmorMaterial REINFORCED_DIAMOND = EnumHelper.addArmorMaterial("REINFORCED_DIAMOND", 33, new int[]{ 6, 16, 12, 6 }, 10);
            public static final EnumArmorMaterial AIR = EnumHelper.addArmorMaterial("AIR", 2000, new int[] { 1, 1, 1, 1 }, 0);

            public static final EnumArmorMaterial CASTED_IRON = EnumHelper.addArmorMaterial("CASTED_IRON", 120, new int[] { 3, 8, 6, 6 }, 0);
            public static final EnumArmorMaterial CASTED_COPPER = EnumHelper.addArmorMaterial("CASTED_COPPER", 78, new int[] { 3, 8, 6, 6 }, 0);
            public static final EnumArmorMaterial CASTED_COBALT = EnumHelper.addArmorMaterial("CASTED_COBALT", 525, new int[] { 6, 14, 10, 6 }, 0);
            public static final EnumArmorMaterial CASTED_ARDITE = EnumHelper.addArmorMaterial("CASTED_ARDITE", 450, new int[] { 4, 12, 8, 6 }, 0);
            public static final EnumArmorMaterial CASTED_MANYULLYN = EnumHelper.addArmorMaterial("CASTED_MANYULLYN", 1125, new int[] { 8, 18, 10, 6 }, 0);
            public static final EnumArmorMaterial CASTED_BRONZE = EnumHelper.addArmorMaterial("CASTED_BRONZE", 171, new int[] { 4, 10, 8, 6 }, 0);
            public static final EnumArmorMaterial CASTED_ALUMITE = EnumHelper.addArmorMaterial("CASTED_ALUMITE", 268, new int[] { 5, 12, 8, 6 }, 0);
            public static final EnumArmorMaterial CASTED_OBSIDIAN = EnumHelper.addArmorMaterial("CASTED_OBSIDIAN", 34, new int[] { 6, 16, 12, 6 }, 0);
            public static final EnumArmorMaterial CASTED_STEEL = EnumHelper.addArmorMaterial("CASTED_STEEL", 367, new int[] { 6, 15, 12, 6 }, 0);
            public static final EnumArmorMaterial CASTED_PIG_IRON = EnumHelper.addArmorMaterial("CASTED_PIG_IRON", 122, new int[] { 3, 8, 6, 6 }, 0);
        }

        public static void registerRepairItem()
        {
            if (Util.ic2Loaded())
            {
                Tool.DRAGON.customCraftingMaterial = Items.getItem("iridiumPlate").getItem();
                Armor.DRAGON.customCraftingMaterial = Items.getItem("iridiumPlate").getItem();
            }
            else
            {
                Tool.DRAGON.customCraftingMaterial = net.minecraft.item.Item.itemsList[net.minecraft.block.Block.blockEmerald.blockID];
                Armor.DRAGON.customCraftingMaterial = net.minecraft.item.Item.itemsList[net.minecraft.block.Block.blockEmerald.blockID];
            }
            Armor.REINFORCED_DIAMOND.customCraftingMaterial = net.minecraft.item.Item.itemsList[net.minecraft.block.Block.blockDiamond.blockID];
            if (Util.ic2Loaded())
            {
                Tool.NUKE.customCraftingMaterial = Items.getItem("uraniumBlock").getItem();
                Armor.NUKE.customCraftingMaterial = Items.getItem("uraniumBlock").getItem();
            }
        }
    }

    public class Block
    {
        public static final int BLOCK_ID_OFFSET = 256;

        public class BlockName
        {
            public static final String MACHINE_BLOCK = "com.mes51.minecraft.mods.necktie:machineBlock";
            public static final String AIR_STONE = "com.mes51.minecraft.mods.necktie:airStone";
            public static final String NECKTIE_BLOCK = "com.mes51.minecraft.mods.necktie:necktieBlock";
            public static final String NECKTIE_BEACON = "com.mes51.minecraft.mods.necktie:necktieBeacon";
        }

        public class CfgBlockId
        {
            public static final String MACHINE_BLOCK = "BlockMachine_ID";
            public static final String AIR_STONE = "BlockAirStone_ID";
            public static final String NECKTIE_BLOCK = "BlockNecktie_ID";
            public static final String NECKTIE_BEACON = "BlockNecktieBeacon_ID";
        }

        public class Texture
        {
            public static final String MACHINE_TOP = "com.mes51.minecraft.mods.necktie:machineTop";
            public static final String MACHINE_BOTTOM = "com.mes51.minecraft.mods.necktie:machineBottom";
            public static final String MACHINE_SIDE = "com.mes51.minecraft.mods.necktie:machineSide";
        }
    }

    public class Item
    {
        public static final int INVENTORY_SLOT_CHEST = 2;

        public class ItemName
        {
            public static final String NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktie";
            public static final String IRON_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieIron";
            public static final String DIAMOND_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieDiamond";
            public static final String DRAGON_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieDragon";
            public static final String NUKE_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieNuke";
            public static final String AIR_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieAir";
            public static final String DOPING_FOOD = "com.mes51.minecraft.mods.necktie:dopingFood";
            public static final String NECKTIE_HEAD_BAND = "com.mes51.minecraft.mods.necktie:armorNecktieHeadBand";
            public static final String IRON_NECKTIE_HEAD_BAND = "com.mes51.minecraft.mods.necktie:armorNecktieHeadBandIron";
            public static final String DIAMOND_NECKTIE_HEAD_BAND = "com.mes51.minecraft.mods.necktie:armorNecktieHeadBandDiamond";
            public static final String DRAGON_NECKTIE_HEAD_BAND = "com.mes51.minecraft.mods.necktie:armorNecktieHeadBandDragon";
            public static final String MACHINE_BLOCK = "com.mes51.minecraft.mods.necktie:itemMachineBlock";
            public static final String NECKTIE_BLOCK = "com.mes51.minecraft.mods.necktie:itemNecktieBlock";
            public static final String NECKTIE_PATTERN = "com.mes51.minecraft.mods.necktie:necktiePattern";
            public static final String IRON_CASTED_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieCastedIron";
            public static final String COPPER_CASTED_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieCastedCopper";
            public static final String COBALT_CASTED_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieCastedCobalt";
            public static final String ARDITE_CASTED_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieCastedArdite";
            public static final String MANYULLYN_CASTED_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieCastedManyullyn";
            public static final String BRONZE_CASTED_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieCastedBronze";
            public static final String ALUMITE_CASTED_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieCastedAlumite";
            public static final String OBSIDIAN_CASTED_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieCastedObsidian";
            public static final String STEEL_CASTED_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieCastedSteel";
            public static final String PIG_IRON_CASTED_NECKTIE = "com.mes51.minecraft.mods.necktie:armorNecktieCastedPigIron";
            public static final String TAMBOURINE = "com.mes51.minecraft.mods.necktie:tambourine";
        }

        public class CfgItemId
        {
            public static final String NECKTIE = "ItemArmorNecktie_ID";
            public static final String IRON_NECKTIE = "Iron_ItemArmorNecktie_ID";
            public static final String DIAMOND_NECKTIE = "Diamond_ItemArmorNecktie_ID";
            public static final String DRAGON_NECKTIE = "Dragon_ItemArmorNecktie_ID";
            public static final String NUKE_NECKTIE = "Nuke_ItemArmorNecktie_ID";
            public static final String AIR_NECKTIE = "Air_ItemArmorNecktie_ID";
            public static final String DOPING_FOOD = "ItemDopingFood_ID";
            public static final String NECKTIE_HEAD_BAND = "ItemArmorNecktieHeadBand_ID";
            public static final String IRON_NECKTIE_HEAD_BAND = "ItemArmorIronNecktieHeadBand_ID";
            public static final String DIAMOND_NECKTIE_HEAD_BAND = "ItemArmorDiamondNecktieHeadBand_ID";
            public static final String DRAGON_NECKTIE_HEAD_BAND = "ItemArmorDragonNecktieHeadBand_ID";
            public static final String NECKTIE_PATTERN = "ItemNecktiePattern_ID";
            public static final String IRON_CASTED_NECKTIE = "ItemArmorIronCastedNecktie_ID";
            public static final String COPPER_CASTED_NECKTIE = "ItemArmorCopperCastedNecktie_ID";
            public static final String COBALT_CASTED_NECKTIE = "ItemArmorCobaltCastedNecktie_ID";
            public static final String ARDITE_CASTED_NECKTIE = "ItemArmorArditeCastedNecktie_ID";
            public static final String MANYULLYN_CASTED_NECKTIE = "ItemArmorManyullynCastedNecktie_ID";
            public static final String BRONZE_CASTED_NECKTIE = "ItemArmorBronzeCastedNecktie_ID";
            public static final String ALUMITE_CASTED_NECKTIE = "ItemArmorAlumiteCastedNecktie_ID";
            public static final String OBSIDIAN_CASTED_NECKTIE = "ItemArmorObsidianCastedNecktie_ID";
            public static final String STEEL_CASTED_NECKTIE = "ItemArmorSteelCastedNecktie_ID";
            public static final String PIG_IRON_CASTED_NECKTIE = "ItemArmorPigIronCastedNecktie_ID";
            public static final String TAMBOURINE = "ItemTambourine_ID";
        }

        public class CfgGeneralId
        {
            public static final String CATEGORY = "general";
            public static final String NECKTIE_BREAK_BLOCK_IDS = "Necktie_BreakBlockIds";
            public static final String ITEM_PUDDING_MILK_ID = "ItemPudding_MilkId";
            public static final String ENABLE_ATTACK_EFFECT = "EnableAttackEffect";
            public static final String MAX_ATTACK_EFFECT_ID = "MaxAttackEffectID";
            public static final String REPLACE_TOOL_MATERIAL = "ReplaceToolMaterial";
            public static final String REPLACE_TOOL_RECIPE = "ReplaceToolRecipe";
        }

        public class Sound
        {
            public static final String TAMBOURINE = "necktie:tambourine";
        }
    }

    public class Potion
    {
        public class CfgPotionId
        {
            public static final String FULLY_FED = "PotionFullyFed_ID";
            public static final String MUSCLE = "PotionMuscle_ID";
        }
    }

    public class Key
    {
        public class CfgKeyConfig
        {
            public static final String CONFIG_CATEGORY_KEY = "key";
            public static final String ITEM_ARMOR_NECKTIE_MUSCLE_MODE_KEY = "MuscleModeKey";
            public static final String ITEM_ARMOR_NECKTIE_BREAK_ALL_SWITCH_KEY = "BreakAllSwitchKey";
            public static final String ITEM_ARMOR_NECKTIE_GLIDE_KEY = "GlideKey";
        }
    }
}
