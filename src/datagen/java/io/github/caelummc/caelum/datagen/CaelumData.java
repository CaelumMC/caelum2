package io.github.caelummc.caelum.datagen;

import io.github.caelummc.caelum.Caelum;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.condition.TableBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;

public class CaelumData implements DataGeneratorEntrypoint {
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
        gen.registerSimpleCubeAll(Caelum.Blocks.AERRACK);
        gen.registerSimpleCubeAll(Caelum.Blocks.AERRACK_GRAVEL);
        gen.registerSimpleCubeAll(Caelum.Blocks.ROOTED_AERRACK);

        gen.registerParentedItemModel(Caelum.Blocks.AERRACK, new Identifier("caelum:block/aerrack"));
        gen.registerParentedItemModel(Caelum.Blocks.AERRACK_GRAVEL, new Identifier("caelum:block/aerrack_gravel"));
        gen.registerParentedItemModel(Caelum.Blocks.ROOTED_AERRACK, new Identifier("caelum:block/rooted_aerrack"));
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
        this.addDrop(Caelum.Blocks.AERRACK);
        this.addDrop(Caelum.Blocks.AERRACK_GRAVEL);
        this.addDrop(Caelum.Blocks.AERRACK_GRAVEL);
        this.addDrop(Caelum.Blocks.ROOTED_AERRACK, rootedAerrackDrop(Caelum.Blocks.ROOTED_AERRACK, Caelum.Blocks.AERRACK_GRAVEL, Items.STICK)); // TODO change when adding skyroot sticks
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
        this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(Caelum.Blocks.AERRACK);
        this.getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE).add(Caelum.Blocks.AERRACK_GRAVEL);
        this.getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(Caelum.Blocks.AERRACK_GRAVEL, Caelum.Blocks.ROOTED_AERRACK);
    }
}