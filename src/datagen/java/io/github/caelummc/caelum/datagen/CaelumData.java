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

        gen.registerParentedItemModel(Caelum.Blocks.AERRACK, new Identifier("caelum:block/aerrack"));
        gen.registerParentedItemModel(Caelum.Blocks.AERRACK_GRAVEL, new Identifier("caelum:block/aerrack_gravel"));
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {

    }
}

class BlockLootTableProvider extends FabricBlockLootTableProvider {
    public BlockLootTableProvider(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    protected void generateBlockLootTables() {
        this.addDrop(Caelum.Blocks.AERRACK);
        this.addDrop(Caelum.Blocks.AERRACK_GRAVEL);
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
    }
}