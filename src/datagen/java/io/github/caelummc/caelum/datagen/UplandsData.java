package io.github.caelummc.caelum.datagen;

import io.github.caelummc.caelum.Uplands;
import io.github.caelummc.caelum.block.SkymossStalksBlock;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.mininglevel.v1.FabricMineableTags;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;

public class UplandsData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.addProvider(ModelProvider::new);
        generator.addProvider(BlockLootTableProvider::new);
        generator.addProvider(BlockTagProvider::new);
    }
}

class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        gen.registerSimpleCubeAll(Uplands.Blocks.AERRACK);
        gen.registerSimpleCubeAll(Uplands.Blocks.AERRACK_GRAVEL);
        gen.registerSimpleCubeAll(Uplands.Blocks.ROOTED_AERRACK);
        registerMossyAerrack(gen, Uplands.Blocks.MOSSY_AERRACK, TextureMap.getId(Uplands.Blocks.AERRACK));
        registerNetherrackLike(gen, Uplands.Blocks.SKYMOSS_BLOCK);
        registerSkymossStalks(gen, Uplands.Blocks.SKYMOSS_STALKS);

        gen.registerParentedItemModel(Uplands.Blocks.AERRACK, new Identifier("caelum:block/aerrack"));
        gen.registerParentedItemModel(Uplands.Blocks.AERRACK_GRAVEL, new Identifier("caelum:block/aerrack_gravel"));
        gen.registerParentedItemModel(Uplands.Blocks.ROOTED_AERRACK, new Identifier("caelum:block/rooted_aerrack"));
        gen.registerParentedItemModel(Uplands.Blocks.MOSSY_AERRACK, new Identifier("caelum:block/mossy_aerrack"));
        gen.registerParentedItemModel(Uplands.Blocks.SKYMOSS_BLOCK, new Identifier("caelum:block/skymoss_block"));
        gen.registerItemModel(Uplands.Blocks.SKYMOSS_STALKS);
    }

    private static void registerMossyAerrack(BlockStateModelGenerator gen, Block block, Identifier bottom) {
        TextureMap textureMap = new TextureMap().put(TextureKey.BOTTOM, bottom).put(TextureKey.TOP, TextureMap.getId(block)).put(TextureKey.SIDE, TextureMap.getSubId(block, "_side"));
        Identifier id = Models.CUBE_BOTTOM_TOP.upload(block, textureMap, gen.modelCollector);
        gen.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, BlockStateModelGenerator.createModelVariantWithRandomHorizontalRotations(id)));
    }

    private static void registerNetherrackLike(BlockStateModelGenerator gen, Block block) {
        Identifier id = TexturedModel.CUBE_ALL.upload(block, gen.modelCollector);
        gen.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, id), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.X, VariantSettings.Rotation.R90), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.X, VariantSettings.Rotation.R180), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.X, VariantSettings.Rotation.R270), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.Y, VariantSettings.Rotation.R90), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.Y, VariantSettings.Rotation.R90).put(VariantSettings.X, VariantSettings.Rotation.R90), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.Y, VariantSettings.Rotation.R90).put(VariantSettings.X, VariantSettings.Rotation.R180), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.Y, VariantSettings.Rotation.R90).put(VariantSettings.X, VariantSettings.Rotation.R270), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.Y, VariantSettings.Rotation.R180), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.Y, VariantSettings.Rotation.R180).put(VariantSettings.X, VariantSettings.Rotation.R90), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.Y, VariantSettings.Rotation.R180).put(VariantSettings.X, VariantSettings.Rotation.R180), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.Y, VariantSettings.Rotation.R180).put(VariantSettings.X, VariantSettings.Rotation.R270), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.Y, VariantSettings.Rotation.R270), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.Y, VariantSettings.Rotation.R270).put(VariantSettings.X, VariantSettings.Rotation.R90), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.Y, VariantSettings.Rotation.R270).put(VariantSettings.X, VariantSettings.Rotation.R180), BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.Y, VariantSettings.Rotation.R270).put(VariantSettings.X, VariantSettings.Rotation.R270)));
    }

    private static void registerSkymossStalks(BlockStateModelGenerator gen, Block block) {
        BlockStateVariantMap variants = BlockStateVariantMap.create(SkymossStalksBlock.AGE).register(age -> {
            Identifier id = gen.createSubModel(block, "_stage" + age, Models.CROSS, TextureMap::cross);
            return BlockStateVariant.create().put(VariantSettings.MODEL, id);
        });
        gen.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(variants));
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {

    }
}

class BlockLootTableProvider extends FabricBlockLootTableProvider {
    private static final LootCondition.Builder WITH_SILK_TOUCH = MatchToolLootCondition.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, NumberRange.IntRange.atLeast(1))));
    private static final LootCondition.Builder WITHOUT_SILK_TOUCH = WITH_SILK_TOUCH.invert();

    public BlockLootTableProvider(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    protected void generateBlockLootTables() {
        this.addDrop(Uplands.Blocks.AERRACK);
        this.addDrop(Uplands.Blocks.AERRACK_GRAVEL);
        // TODO change sticks to skyroot sticks when added
        this.addDrop(Uplands.Blocks.ROOTED_AERRACK, rootedAerrackDrop(Uplands.Blocks.ROOTED_AERRACK, Uplands.Blocks.AERRACK_GRAVEL, Items.STICK));
        this.addDrop(Uplands.Blocks.SKYMOSS_BLOCK);
        this.addDrop(Uplands.Blocks.SKYMOSS_STALKS, BlockLootTableGenerator::dropsWithShears);
    }

    public static LootTable.Builder rootedAerrackDrop(Block silkTouch, Block notSilkTouch, Item stick) {
        return dropsWithSilkTouch(silkTouch, addSurvivesExplosionCondition(silkTouch, ItemEntry.builder(notSilkTouch)))
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .conditionally(WITHOUT_SILK_TOUCH)
                        .with(applyExplosionDecay(silkTouch, ItemEntry.builder(stick)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))));
    }
}

class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public BlockTagProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateTags() {
        this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(Uplands.Blocks.AERRACK);
        this.getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE).add(Uplands.Blocks.AERRACK_GRAVEL);
        this.getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(Uplands.Blocks.AERRACK_GRAVEL, Uplands.Blocks.ROOTED_AERRACK);
        this.getOrCreateTagBuilder(BlockTags.HOE_MINEABLE).add(Uplands.Blocks.SKYMOSS_BLOCK);
        this.getOrCreateTagBuilder(FabricMineableTags.SHEARS_MINEABLE).add(Uplands.Blocks.SKYMOSS_STALKS);

    }
}