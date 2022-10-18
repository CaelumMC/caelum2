package io.github.caelummc.caelum;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Caelum implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("caelum");

    @Override
    public void onInitialize() {
        Blocks.register();
        Items.register();

        ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("caelum:caelum32"), FabricLoader.getInstance().getModContainer("caelum").orElseThrow(), "Caelum x32", ResourcePackActivationType.NORMAL);
    }

    public static class Blocks {
        public static final Block AERRACK = register("aerrack", new Block(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3F, 9F)));
        public static final Block AERRACK_GRAVEL = register("aerrack_gravel", new Block(FabricBlockSettings.of(Material.AGGREGATE).sounds(BlockSoundGroup.NETHER_BRICKS).requiresTool().strength(3F, 9F)));

        private static void register() {

        }

        private static Block register(String name, Block block) {
            return Registry.register(Registry.BLOCK, new Identifier("caelum", name), block);
        }
    }


    public static class Items {
        private static final ItemGroup MAIN = FabricItemGroupBuilder.build(new Identifier("caelum:main"), () -> new ItemStack(Items.AERRACK));

        public static final Item AERRACK = register("aerrack", new BlockItem(Blocks.AERRACK, new Item.Settings().group(MAIN)));
        public static final Item AERRACK_GRAVEL = register("aerrack_gravel", new BlockItem(Blocks.AERRACK_GRAVEL, new Item.Settings().group(MAIN)));

        private static void register() {

        }

        private static Item register(String name, Item block) {
            return Registry.register(Registry.ITEM, new Identifier("caelum", name), block);
        }
    }
}
