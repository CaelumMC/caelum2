package io.github.caelummc.caelum;

import io.github.caelummc.caelum.block.AerrackBlock;
import io.github.caelummc.caelum.block.MossyAerrackBlock;
import io.github.caelummc.caelum.block.SkymossBlock;
import io.github.caelummc.caelum.block.SkymossStalksBlock;
import io.github.caelummc.caelum.world.CaelumChunkGenerator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Caelum implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("caelum");

    @Override
    public void onInitialize() {
        Blocks.register();
        Items.register();
        Misc.register();

    }

    public static class Blocks {
        public static final Block AERRACK = register("aerrack", new AerrackBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3F, 9F)));
        public static final Block AERRACK_GRAVEL = register("aerrack_gravel", new FallingBlock(FabricBlockSettings.of(Material.AGGREGATE).sounds(BlockSoundGroup.NETHER_BRICKS).requiresTool().strength(3F)));
        public static final Block ROOTED_AERRACK = register("rooted_aerrack", new Block(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.WOOD).requiresTool().strength(5F, 12F)));
        public static final Block MOSSY_AERRACK = register("mossy_aerrack", new MossyAerrackBlock(AbstractBlock.Settings.of(Material.STONE, MapColor.GREEN).requiresTool().strength(2.5f, 7.5f).ticksRandomly()));
        public static final Block SKYMOSS = register("skymoss", new SkymossBlock(AbstractBlock.Settings.of(Material.MOSS_BLOCK, MapColor.GREEN).strength(0.5F).sounds(BlockSoundGroup.MOSS_BLOCK)));
        public static final Block SKYMOSS_STALKS = register("skymoss_stalks", new SkymossStalksBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).offsetType(AbstractBlock.OffsetType.XZ).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));

        private static void register() {

        }

        private static Block register(String name, Block block) {
            return Registry.register(Registry.BLOCK, new Identifier("caelum", name), block);
        }
    }

    @SuppressWarnings("unused")
    public static class Items {
        private static final ItemGroup MAIN = FabricItemGroupBuilder.build(new Identifier("caelum:main"), () -> new ItemStack(Items.MOSSY_AERRACK));

        public static final Item AERRACK = register("aerrack", new BlockItem(Blocks.AERRACK, new Item.Settings().group(MAIN)));
        public static final Item AERRACK_GRAVEL = register("aerrack_gravel", new BlockItem(Blocks.AERRACK_GRAVEL, new Item.Settings().group(MAIN)));
        public static final Item ROOTED_AERRACK = register("rooted_aerrack", new BlockItem(Blocks.ROOTED_AERRACK, new Item.Settings().group(MAIN)));
        public static final Item MOSSY_AERRACK = register("mossy_aerrack", new BlockItem(Blocks.MOSSY_AERRACK, new Item.Settings().group(MAIN)));
        public static final Item SKYMOSS = register("skymoss", new BlockItem(Blocks.SKYMOSS, new Item.Settings().group(MAIN)));
        public static final Item SKYMOSS_STALKS = register("skymoss_stalks", new BlockItem(Blocks.SKYMOSS_STALKS, new Item.Settings().group(ItemGroup.DECORATIONS)));

        private static void register() {

        }

        private static Item register(String name, Item block) {
            return Registry.register(Registry.ITEM, new Identifier("caelum", name), block);
        }
    }

    @SuppressWarnings("unused")
    public static class Misc {
        public static final Biome BIOME = createBiome();

        private static void register() {
            register(BuiltinRegistries.BIOME, "caelum", BIOME);
            register(Registry.CHUNK_GENERATOR, "caelum", CaelumChunkGenerator.CODEC);
        }

        private static <T> T register(Registry<T> registry, String name, T entry) {
            Identifier id = new Identifier("caelum", name);
            return Registry.register(registry, id, entry);
        }

        public static Biome createBiome() {
            SpawnSettings.Builder builder = new SpawnSettings.Builder();
            GenerationSettings.Builder generation = new GenerationSettings.Builder();

            return new Biome.Builder()
                    .precipitation(Biome.Precipitation.RAIN)
                    .temperature(0.25F).downfall(0.25F)
                    .effects(new BiomeEffects.Builder()
                            .waterColor(0x3D57D6)
                            .waterFogColor(0x050533)
                            .fogColor(12638463)
                            .skyColor(getSkyColor(0.25F))
                            .moodSound(BiomeMoodSound.CAVE).build())
                    .spawnSettings(builder.build())
                    .generationSettings(generation.build()).build();
        }

        private static int getSkyColor(float temperature) {
            float f = temperature / 3.0F;
            f = MathHelper.clamp(f, -1.0F, 1.0F);
            return MathHelper.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
        }
    }
}
