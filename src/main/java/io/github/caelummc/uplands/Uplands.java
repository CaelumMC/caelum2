package io.github.caelummc.uplands;

import io.github.caelummc.uplands.block.AerrackBlock;
import io.github.caelummc.uplands.block.MossyAerrackBlock;
import io.github.caelummc.uplands.block.SkymossBlock;
import io.github.caelummc.uplands.block.SkymossStalksBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Uplands implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("uplands");

    @Override
    public void onInitialize() {
        Blocks.register();
        Items.register();
    }

    public static class Blocks {
        public static final Block AERRACK = register("aerrack", new AerrackBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3F, 9F)));
        public static final Block AERRACK_GRAVEL = register("aerrack_gravel", new FallingBlock(FabricBlockSettings.of(Material.AGGREGATE).sounds(BlockSoundGroup.NETHER_BRICKS).requiresTool().strength(3F)));
        public static final Block ROOTED_AERRACK = register("rooted_aerrack", new Block(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.WOOD).requiresTool().strength(5F, 12F)));
        public static final Block MOSSY_AERRACK = register("mossy_aerrack", new MossyAerrackBlock(AbstractBlock.Settings.of(Material.STONE, MapColor.GREEN).requiresTool().strength(2.5f, 7.5f).ticksRandomly()));
        public static final Block SKYMOSS_BLOCK = register("skymoss_block", new SkymossBlock(AbstractBlock.Settings.of(Material.MOSS_BLOCK, MapColor.GREEN).strength(0.5F).sounds(BlockSoundGroup.MOSS_BLOCK)));
        public static final Block SKYMOSS_STALKS = register("skymoss_stalks", new SkymossStalksBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).offsetType(AbstractBlock.OffsetType.XZ).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));

        private static void register() {

        }

        private static Block register(String name, Block block) {
            return Registry.register(Registry.BLOCK, new Identifier("uplands", name), block);
        }
    }

    public static class Items {
        private static final ItemGroup MAIN = FabricItemGroupBuilder.build(new Identifier("uplands:main"), () -> new ItemStack(Items.AERRACK));

        public static final Item AERRACK = register("aerrack", new BlockItem(Blocks.AERRACK, new Item.Settings().group(MAIN)));
        public static final Item AERRACK_GRAVEL = register("aerrack_gravel", new BlockItem(Blocks.AERRACK_GRAVEL, new Item.Settings().group(MAIN)));
        public static final Item ROOTED_AERRACK = register("rooted_aerrack", new BlockItem(Blocks.ROOTED_AERRACK, new Item.Settings().group(MAIN)));
        public static final Item MOSSY_AERRACK = register("mossy_aerrack", new BlockItem(Blocks.MOSSY_AERRACK, new Item.Settings().group(MAIN)));
        public static final Item SKYMOSS_BLOCK = register("skymoss_block", new BlockItem(Blocks.SKYMOSS_BLOCK, new Item.Settings().group(MAIN)));
        public static final Item SKYMOSS_STALKS = register("skymoss_stalks", new BlockItem(Blocks.SKYMOSS_STALKS, new Item.Settings().group(ItemGroup.DECORATIONS)));

        private static void register() {

        }

        private static Item register(String name, Item block) {
            return Registry.register(Registry.ITEM, new Identifier("uplands", name), block);
        }
    }
}
