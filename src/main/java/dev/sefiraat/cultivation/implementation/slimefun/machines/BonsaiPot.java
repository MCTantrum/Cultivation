package dev.sefiraat.cultivation.implementation.slimefun.machines;

import dev.sefiraat.cultivation.Cultivation;
import dev.sefiraat.cultivation.implementation.slimefun.CultivationStacks;
import dev.sefiraat.cultivation.implementation.slimefun.items.Machines;
import dev.sefiraat.cultivation.implementation.utils.DisplayGroupGenerators;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import dev.sefiraat.sefilib.string.Theme;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class BonsaiPot extends GardenCloche {

    private static final String KEY_TREE = "tree";
    private static final String KEY_DIRT = "dirt";
    private static final String KEY_FERTILIZED_DIRT = "fertilized_dirt";
    private static final String KEY_SUPER_DIRT = "super_dirt";
    private static final String KEY_UUID = "display-uuid";

    private static final int DIRT_SLOT = 1;
    private static final int TREE_SLOT = 4;
    private static final int FERTILIZER_SLOT = 7;

    private static final int[] DIRT_SLOT_BACKGROUND = new int[]{
        0, 2
    };
    private static final int[] TREE_SLOT_BACKGROUND = new int[]{
        3, 5
    };
    private static final int[] FERTILIZER_SLOT_BACKGROUND = new int[]{
        6, 8
    };

    private static final int[] OUTPUT_SLOTS = new int[]{
        19, 20, 21, 22, 23, 24, 25
    };
    private static final int[] BACKGROUND = new int[]{
        9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35
    };


    public BonsaiPot(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        addItemHandler(
            new BlockPlaceHandler(false) {
                @Override
                public void onPlayerPlace(@NotNull BlockPlaceEvent e) {
                    e.getBlock().setType(Material.BARRIER);
                    setupDisplay(e.getBlock().getLocation());
                }
            },
            new BlockBreakHandler(false, false) {
                @Override
                @ParametersAreNonnullByDefault
                public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                    Location location = e.getBlock().getLocation();
                    removeDisplay(location);
                    e.getBlock().setType(Material.AIR);
                    BlockMenu blockMenu = BlockStorage.getInventory(location);
                    if (blockMenu != null) {
                        blockMenu.dropItems(location, TREE_SLOT);
                        blockMenu.dropItems(location, DIRT_SLOT);
                        blockMenu.dropItems(location, FERTILIZER_SLOT);
                        blockMenu.dropItems(location, OUTPUT_SLOTS);
                    }
                }
            },
            new BlockTicker() {
                @Override
                public boolean isSynchronized() {
                    return false;
                }

                @Override
                public void tick(Block block, SlimefunItem item, Config data) {
                    BlockMenu blockMenu = BlockStorage.getInventory(block);
                    Location location = block.getLocation();
                    ItemStack dirt = blockMenu.getItemInSlot(DIRT_SLOT);


                    if (dirt == null) {
                        handleNullDirt(location);
                    } else {
                        if (isValidDirtType(dirt.getType())) {

                            ItemStack tree = blockMenu.getItemInSlot(TREE_SLOT);
                            ItemStack fertilizer = blockMenu.getItemInSlot(FERTILIZER_SLOT);
                            SlimefunItem infernalBonemeal = SlimefunItem.getByItem(fertilizer);
                            double growthRate = 0.05;
                            double rand = ThreadLocalRandom.current().nextDouble();

                            if (!hasDisplayDirt(location) && fertilizer == null) {
                                Bukkit.getScheduler().runTask(
                                    Cultivation.getInstance(), () -> addDirtToDisplay(location)
                                );
                            }

                            if (tree == null) {
                                Bukkit.getScheduler().runTask(
                                    Cultivation.getInstance(), () -> removeTreeFromDisplay(location)
                                );
                            } else {
                                if (isValidTreeType(tree.getType())) {
                                    if (!hasDisplayTree(location)) {
                                        Bukkit.getScheduler().runTask(
                                            Cultivation.getInstance(), () -> addTreeToDisplay(location)
                                        );
                                    }
                                    if (fertilizer == null) {
                                        handleFertilizer(location);
                                        //TIER 1: NO FERTILIZER
                                    } else {
                                        if (infernalBonemeal != null) {
                                            if (infernalBonemeal.isItem(SlimefunItems.INFERNAL_BONEMEAL)) {
                                                if (!hasDisplaySuperDirt(location)) {
                                                    handleSuperDirt(location);
                                                }
                                                //TIER 3: INFERNAL BONEMEAL TURBO MODE
                                                growthRate = growthRate + 0.75; // 80% Total
                                            }
                                        } else {
                                            if (fertilizer.getType() != null) {
                                                if (fertilizer.getType() == Material.BONE_MEAL) {
                                                    if (!hasDisplayFertilizedDirt(location)) {
                                                        handleFertilizedDirt(location);
                                                    }
                                                    //TIER 2: BASIC FERTILIZER
                                                    growthRate = growthRate + 0.25; //30% Total
                                                }
                                            }
                                        }
                                    }

                                    handleTreeGrowth(location, tree.getType(), rand, growthRate, blockMenu);
                                }
                            }
                        }
                    }
                }
            }
        );
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(this.getId(), this.getItemName()) {

            @Override
            public void init() {
                ItemStack backgroundInputSoil = new CustomItemStack(
                    Material.BROWN_STAINED_GLASS_PANE,
                    Theme.PASSIVE.apply("Insert Soil"),
                    Theme.CLICK_INFO.asTitle("Accepts", "Plant-able soils")
                );
                ItemStack backgroundInputTree = new CustomItemStack(
                    Material.LIME_STAINED_GLASS_PANE,
                    Theme.PASSIVE.apply("Plant Tree"),
                    Theme.CLICK_INFO.asTitle("Accepts", "Saplings & Fungi")
                );
                ItemStack backgroundInputFertilizer = new CustomItemStack(
                    Material.CYAN_STAINED_GLASS_PANE,
                    Theme.PASSIVE.apply("Add Fertilizer"),
                    Theme.CLICK_INFO.asTitle("Accepts", "Bonemeal & Infernal Bonemeal"),
                    Theme.ERROR.apply("Consumes fertilizer no matter what!")
                );
                drawBackground(BACKGROUND);
                drawBackground(backgroundInputSoil, DIRT_SLOT_BACKGROUND);
                drawBackground(backgroundInputTree, TREE_SLOT_BACKGROUND);
                drawBackground(backgroundInputFertilizer, FERTILIZER_SLOT_BACKGROUND);
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return Machines.BONSAI_POT.canUse(player, false)
                    && Slimefun.getProtectionManager()
                    .hasPermission(player, block.getLocation(), Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow == ItemTransportFlow.INSERT) {
                    return new int[]{FERTILIZER_SLOT};
                } else if (flow == ItemTransportFlow.WITHDRAW) {
                    return OUTPUT_SLOTS;
                }
                return new int[0];
            }
        };
    }

    private void handleTreeGrowth(Location location,
                                  Material treeType,
                                  double rand,
                                  double growthRate,
                                  BlockMenu blockMenu
    ) {
        if (rand < growthRate) {
            if (blockMenu.getItemInSlot(FERTILIZER_SLOT) != null) {
                blockMenu.consumeItem(FERTILIZER_SLOT, 1);
            }
            blockMenu.pushItem(new ItemStack(Material.STICK), OUTPUT_SLOTS);

            SlimefunItem slimefunItem = SlimefunItem.getByItem(blockMenu.getItemInSlot(TREE_SLOT));

            if (slimefunItem != null) {
                if (slimefunItem.isItem(CultivationStacks.TREE_BANANA)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.BANANA), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.MANGROVE_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.OAK_LOG), OUTPUT_SLOTS);

                } else if (slimefunItem.isItem(CultivationStacks.TREE_APRICOT)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.APRICOT), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.ACACIA_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.DARK_OAK_LOG), OUTPUT_SLOTS);

                } else if (slimefunItem.isItem(CultivationStacks.TREE_CHERRY)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.CHERRY), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.FLOWERING_AZALEA_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.STRIPPED_BIRCH_LOG), OUTPUT_SLOTS);

                } else if (slimefunItem.isItem(CultivationStacks.TREE_CHESTNUT)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.CHESTNUT), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.JUNGLE_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.OAK_WOOD), OUTPUT_SLOTS);

                } else if (slimefunItem.isItem(CultivationStacks.TREE_HAZELNUT)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.HAZELNUT), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.SPRUCE_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.JUNGLE_WOOD), OUTPUT_SLOTS);

                } else if (slimefunItem.isItem(CultivationStacks.TREE_GREEN_APPLE)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.GREEN_APPLE), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.ACACIA_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.JUNGLE_WOOD), OUTPUT_SLOTS);

                } else if (slimefunItem.isItem(CultivationStacks.TREE_KIWI)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.KIWI), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.SPRUCE_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.JUNGLE_WOOD), OUTPUT_SLOTS);

                } else if (slimefunItem.isItem(CultivationStacks.TREE_LEMON)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.LEMON), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.OAK_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.OAK_WOOD), OUTPUT_SLOTS);

                } else if (slimefunItem.isItem(CultivationStacks.TREE_LIME)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.LIME), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.JUNGLE_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.OAK_WOOD), OUTPUT_SLOTS);

                } else if (slimefunItem.isItem(CultivationStacks.TREE_MANGO)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.MANGO), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.BIRCH_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.BIRCH_WOOD), OUTPUT_SLOTS);

                } else if (slimefunItem.isItem(CultivationStacks.TREE_ORANGE)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.ORANGE), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.OAK_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.OAK_WOOD), OUTPUT_SLOTS);

                } else if (slimefunItem.isItem(CultivationStacks.TREE_PEACH)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.PEACH), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.MANGROVE_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.BIRCH_WOOD), OUTPUT_SLOTS);

                } else if (slimefunItem.isItem(CultivationStacks.TREE_PEAR)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.PEAR), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.JUNGLE_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.JUNGLE_LOG), OUTPUT_SLOTS);

                } else if (slimefunItem.isItem(CultivationStacks.TREE_PECAN)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.PECAN), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.JUNGLE_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.JUNGLE_LOG), OUTPUT_SLOTS);

                } else if (slimefunItem.isItem(CultivationStacks.TREE_PINEAPPLE)) {
                    blockMenu.pushItem(new CustomItemStack(CultivationStacks.PINEAPPLE), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.JUNGLE_LEAVES), OUTPUT_SLOTS);
                    blockMenu.pushItem(new ItemStack(Material.OAK_WOOD), OUTPUT_SLOTS);

                }

            } else {

                switch (treeType) {
                    case OAK_SAPLING -> {
                        blockMenu.pushItem(new ItemStack(Material.APPLE), OUTPUT_SLOTS);
                        blockMenu.pushItem(new ItemStack(Material.OAK_LEAVES), OUTPUT_SLOTS);
                        blockMenu.pushItem(new ItemStack(Material.OAK_LOG), OUTPUT_SLOTS);
                    }
                    case SPRUCE_SAPLING -> {
                        blockMenu.pushItem(new ItemStack(Material.SPRUCE_LEAVES), OUTPUT_SLOTS);
                        blockMenu.pushItem(new ItemStack(Material.SPRUCE_LOG), OUTPUT_SLOTS);
                    }
                    case ACACIA_SAPLING -> {
                        blockMenu.pushItem(new ItemStack(Material.ACACIA_LEAVES), OUTPUT_SLOTS);
                        blockMenu.pushItem(new ItemStack(Material.ACACIA_LOG), OUTPUT_SLOTS);
                    }
                    case BIRCH_SAPLING -> {
                        blockMenu.pushItem(new ItemStack(Material.BIRCH_LEAVES), OUTPUT_SLOTS);
                        blockMenu.pushItem(new ItemStack(Material.BIRCH_LOG), OUTPUT_SLOTS);
                    }
                    case DARK_OAK_SAPLING -> {
                        blockMenu.pushItem(new ItemStack(Material.DARK_OAK_LEAVES), OUTPUT_SLOTS);
                        blockMenu.pushItem(new ItemStack(Material.DARK_OAK_LOG), OUTPUT_SLOTS);
                    }
                    case CHERRY_SAPLING -> {
                        blockMenu.pushItem(new ItemStack(Material.CHERRY_LEAVES), OUTPUT_SLOTS);
                        blockMenu.pushItem(new ItemStack(Material.CHERRY_LOG), OUTPUT_SLOTS);
                    }
                    case JUNGLE_SAPLING -> {
                        blockMenu.pushItem(new ItemStack(Material.COCOA_BEANS), OUTPUT_SLOTS);
                        blockMenu.pushItem(new ItemStack(Material.JUNGLE_LEAVES), OUTPUT_SLOTS);
                        blockMenu.pushItem(new ItemStack(Material.JUNGLE_LOG), OUTPUT_SLOTS);
                    }
                    case MANGROVE_PROPAGULE -> {
                        blockMenu.pushItem(new ItemStack(Material.MANGROVE_LEAVES), OUTPUT_SLOTS);
                        blockMenu.pushItem(new ItemStack(Material.MANGROVE_LOG), OUTPUT_SLOTS);
                    }
                    case WARPED_FUNGUS -> blockMenu.pushItem(new ItemStack(Material.WARPED_STEM), OUTPUT_SLOTS);
                    case CRIMSON_FUNGUS -> blockMenu.pushItem(new ItemStack(Material.CRIMSON_STEM), OUTPUT_SLOTS);
                    case RED_MUSHROOM -> blockMenu.pushItem(new ItemStack(Material.RED_MUSHROOM_BLOCK), OUTPUT_SLOTS);
                    case BROWN_MUSHROOM -> blockMenu.pushItem(
                        new ItemStack(Material.BROWN_MUSHROOM_BLOCK),
                        OUTPUT_SLOTS
                    );
                    default -> {
                    }
                }
            }
        }
    }


    private void handleNullDirt(Location location) {
        Bukkit.getScheduler().runTask(Cultivation.getInstance(), () -> removeTreeFromDisplay(location));
        Bukkit.getScheduler().runTask(Cultivation.getInstance(), () -> removeDirtFromDisplay(location));
        Bukkit.getScheduler().runTask(Cultivation.getInstance(), () -> removeFertilizedDirtFromDisplay(location));
        Bukkit.getScheduler().runTask(Cultivation.getInstance(), () -> removeSuperDirtFromDisplay(location));
    }

    private void handleFertilizedDirt(Location location) {
        Bukkit.getScheduler().runTask(Cultivation.getInstance(), () -> removeDirtFromDisplay(location));
        Bukkit.getScheduler().runTask(Cultivation.getInstance(), () -> removeSuperDirtFromDisplay(location));
        Bukkit.getScheduler().runTask(Cultivation.getInstance(), () -> addFertilizedDirtToDisplay(location));
    }

    private void handleSuperDirt(Location location) {
        Bukkit.getScheduler().runTask(Cultivation.getInstance(), () -> removeDirtFromDisplay(location));
        Bukkit.getScheduler().runTask(Cultivation.getInstance(), () -> removeFertilizedDirtFromDisplay(location));
        Bukkit.getScheduler().runTask(Cultivation.getInstance(), () -> addSuperDirtToDisplay(location));
    }

    private void handleFertilizer(Location location) {
        Bukkit.getScheduler().runTask(Cultivation.getInstance(), () -> removeFertilizedDirtFromDisplay(location));
        Bukkit.getScheduler().runTask(Cultivation.getInstance(), () -> removeSuperDirtFromDisplay(location));
    }

    private boolean isValidDirtType(Material dirtType) {
        return Set.of(Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT, Material.PODZOL,
                      Material.MYCELIUM, Material.FARMLAND
        ).contains(dirtType);
    }

    private boolean isValidTreeType(Material treeType) {
        return Set.of(
            Material.OAK_SAPLING,
            Material.SPRUCE_SAPLING,
            Material.ACACIA_SAPLING,
            Material.BIRCH_SAPLING,
            Material.DARK_OAK_SAPLING,
            Material.CHERRY_SAPLING,
            Material.JUNGLE_SAPLING,
            Material.BROWN_MUSHROOM,
            Material.RED_MUSHROOM,
            Material.WARPED_FUNGUS,
            Material.CRIMSON_FUNGUS,
            Material.MANGROVE_PROPAGULE
        ).contains(treeType);
    }

    private boolean hasDisplayTree(@Nonnull Location location) {
        String hasTree = BlockStorage.getLocationInfo(location, KEY_TREE);
        return Boolean.parseBoolean(hasTree);
    }


    private boolean hasDisplayDirt(@Nonnull Location location) {
        String hasDirt = BlockStorage.getLocationInfo(location, KEY_DIRT);
        return Boolean.parseBoolean(hasDirt);
    }

    private boolean hasDisplayFertilizedDirt(@Nonnull Location location) {
        String hasFertilizer = BlockStorage.getLocationInfo(location, KEY_FERTILIZED_DIRT);
        return Boolean.parseBoolean(hasFertilizer);
    }

    private boolean hasDisplaySuperDirt(@Nonnull Location location) {
        String hasSuperFertilizer = BlockStorage.getLocationInfo(location, KEY_SUPER_DIRT);
        return Boolean.parseBoolean(hasSuperFertilizer);
    }


    private void setupDisplay(@Nonnull Location location) {
        DisplayGroup displayGroup = DisplayGroupGenerators.generateBonsaiPot(location.clone().add(0.5, 0, 0.5));
        BlockStorage.addBlockInfo(location, KEY_UUID, displayGroup.getParentUUID().toString());
    }

    private void removeDisplay(@Nonnull Location location) {
        DisplayGroup group = getDisplayGroup(location);
        if (group != null) {
            group.remove();
        }
    }

    private void addDirtToDisplay(@Nonnull Location location) {
        BlockStorage.addBlockInfo(location, KEY_DIRT, "true");
        DisplayGroup group = getDisplayGroup(location);
        if (group != null) {
            DisplayGroupGenerators.addDirtToBonsaiPot(group);
        }
    }

    private void removeDirtFromDisplay(@Nonnull Location location) {
        DisplayGroup displayGroup = getDisplayGroup(location);
        if (displayGroup != null) {
            DisplayGroupGenerators.removeDirtFromBonsaiPot(displayGroup);
            BlockStorage.addBlockInfo(location, KEY_DIRT, null);
        }
    }

    private void addTreeToDisplay(@Nonnull Location location) {
        BlockStorage.addBlockInfo(location, KEY_TREE, "true");
        DisplayGroup group = getDisplayGroup(location);
        if (group != null) {
            DisplayGroupGenerators.addTreeToBonsaiPot(group);
        }
    }

    private void removeTreeFromDisplay(@Nonnull Location location) {
        DisplayGroup displayGroup = getDisplayGroup(location);
        if (displayGroup != null) {
            DisplayGroupGenerators.removeTreeFromBonsaiPot(displayGroup);
            BlockStorage.addBlockInfo(location, KEY_TREE, null);
        }
    }

    private void addFertilizedDirtToDisplay(@Nonnull Location location) {
        BlockStorage.addBlockInfo(location, KEY_FERTILIZED_DIRT, "true");
        DisplayGroup group = getDisplayGroup(location);
        if (group != null) {
            DisplayGroupGenerators.addFertilizedDirtToBonsaiPot(group);
        }
    }

    private void removeFertilizedDirtFromDisplay(@Nonnull Location location) {
        DisplayGroup displayGroup = getDisplayGroup(location);
        if (displayGroup != null) {
            DisplayGroupGenerators.removeFertilizedDirtFromBonsaiPot(displayGroup);
            BlockStorage.addBlockInfo(location, KEY_FERTILIZED_DIRT, null);
        }
    }

    private void addSuperDirtToDisplay(@Nonnull Location location) {
        BlockStorage.addBlockInfo(location, KEY_SUPER_DIRT, "true");
        DisplayGroup group = getDisplayGroup(location);
        if (group != null) {
            DisplayGroupGenerators.addSuperDirtToBonsaiPot(group);
        }
    }

    private void removeSuperDirtFromDisplay(@Nonnull Location location) {
        DisplayGroup displayGroup = getDisplayGroup(location);
        if (displayGroup != null) {
            DisplayGroupGenerators.removeSuperDirtFromBonsaiPot(displayGroup);
            BlockStorage.addBlockInfo(location, KEY_SUPER_DIRT, null);
        }
    }

    @Nullable
    private UUID getDisplayGroupUUID(@Nonnull Location location) {
        String uuid = BlockStorage.getLocationInfo(location, KEY_UUID);
        if (uuid == null) {
            return null;
        }
        return UUID.fromString(uuid);
    }

    @Nullable
    private DisplayGroup getDisplayGroup(@Nonnull Location location) {
        UUID uuid = getDisplayGroupUUID(location);
        if (uuid == null) {
            return null;
        }
        return DisplayGroup.fromUUID(uuid);
    }


    @NotNull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return 5;
    }
}