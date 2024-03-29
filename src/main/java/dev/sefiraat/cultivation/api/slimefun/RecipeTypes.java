package dev.sefiraat.cultivation.api.slimefun;

import dev.sefiraat.cultivation.api.utils.CultivationThemes;
import dev.sefiraat.cultivation.implementation.listeners.CustomDropListener;
import dev.sefiraat.cultivation.implementation.listeners.MobDropListener;
import dev.sefiraat.cultivation.implementation.slimefun.items.Machines;
import dev.sefiraat.cultivation.implementation.slimefun.machines.KitchenRecipeMachineSimple;
import dev.sefiraat.cultivation.implementation.utils.Keys;
import dev.sefiraat.sefilib.entity.LivingEntityDefinition;
import dev.sefiraat.sefilib.string.Theme;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is used to store and initialise the {@link RecipeType}s used in the addon.
 */
public final class RecipeTypes {
    private RecipeTypes() {
        throw new IllegalStateException("Utility class");
    }

    @Nonnull
    public static final RecipeType BUSH = new RecipeType(
        Keys.newKey("bush_harvest"),
        Theme.themedItemStack(
            Material.TRIPWIRE_HOOK,
            CultivationThemes.RECIPE_TYPE,
            "Bush Harvesting",
            List.of(
                "This item can be harvested from a",
                "Cultivation Bush"
            )
        )
    );

    @Nonnull
    public static final RecipeType TREE = new RecipeType(
        Keys.newKey("tree_harvest"),
        Theme.themedItemStack(
            Material.ACACIA_SAPLING,
            CultivationThemes.RECIPE_TYPE,
            "Tree Harvesting",
            List.of(
                "This item can be harvested from a",
                "Cultivation Tree"
            )
        )
    );

    @Nonnull
    public static final RecipeType TRADING_FARMER = new RecipeType(
        Keys.newKey("trading_farmer"),
        Theme.themedItemStack(
            Material.VILLAGER_SPAWN_EGG,
            CultivationThemes.RECIPE_TYPE,
            "Villager Trading",
            List.of(
                "This item can be bought from a",
                "farming villager."
            )
        )
    );

    @Nonnull
    public static final RecipeType TRADING_FLETCHER = new RecipeType(
        Keys.newKey("trading_fletcher"),
        Theme.themedItemStack(
            Material.VILLAGER_SPAWN_EGG,
            CultivationThemes.RECIPE_TYPE,
            "Villager Trading",
            List.of(
                "This item can be bought from a",
                "fletching villager."
            )
        )
    );

    @Nonnull
    public static final RecipeType MOB = new RecipeType(
        Keys.newKey("mob"),
        Theme.themedItemStack(
            Material.ZOMBIE_HEAD,
            CultivationThemes.RECIPE_TYPE,
            "Mob Drop",
            List.of(
                "This item has a chance to drop from",
                "the following mob(s)."
            )
        )
    );

    @Nonnull
    public static final RecipeType PLANT_BREEDING = new RecipeType(
        Keys.newKey("plant_breeding"),
        Theme.themedItemStack(
            Material.FLOWER_POT,
            CultivationThemes.RECIPE_TYPE,
            "Plant Breeding",
            List.of(
                "This seed is found via breeding.",
                "You breed plants by placing",
                "two plants down with an air block",
                "between them.",
                "",
                "The breeding dictionary can be used",
                "to track your discoveries.",
                "",
                "Hint: Black particles mean the",
                "two plants CANNOT breed."
            )
        )
    );

    @Nonnull
    public static final RecipeType VANILLA_DROP = new RecipeType(
        Keys.newKey("vanilla_block_drop"),
        Theme.themedItemStack(
            Material.BROWN_DYE,
            CultivationThemes.RECIPE_TYPE,
            "World Drop",
            List.of("Drops from blocks when broken in the world.")
        )
    );

    @Nonnull
    public static final RecipeType BUCKETING = new RecipeType(
        Keys.newKey("bucketing"),
        Theme.themedItemStack(
            Material.BUCKET,
            CultivationThemes.RECIPE_TYPE,
            "World Drop",
            List.of("Drops from the world when using a bucket.")
        )
    );

    @Nonnull
    public static final RecipeType CHOPPED = new RecipeType(
        Keys.newKey("chopped"),
        Theme.themedItemStack(
            Material.IRON_SWORD,
            CultivationThemes.RECIPE_TYPE,
            "Chopped",
            List.of(
                "Simply chop with a knife!"
            )
        ),
        (itemStacks, itemStack) -> addRecipeToSimpleMachine(Machines.COUNTER_CHOPPING, itemStacks, itemStack)
    );

    @Nonnull
    public static final RecipeType MASHED = new RecipeType(
        Keys.newKey("mashed"),
        Theme.themedItemStack(
            Material.COMPOSTER,
            CultivationThemes.RECIPE_TYPE,
            "Mashed",
            List.of(
                "Mash Mash Mash"
            )
        ),
        (itemStacks, itemStack) -> addRecipeToSimpleMachine(Machines.COUNTER_MASHER, itemStacks, itemStack)
    );

    @Nonnull
    public static final RecipeType BLENDED = new RecipeType(
        Keys.newKey("blended"),
        Theme.themedItemStack(
            Material.GLASS,
            CultivationThemes.RECIPE_TYPE,
            "Blended",
            List.of(
                "Is made by putting an item in the blender."
            )
        ),
        (itemStacks, itemStack) -> addRecipeToSimpleMachine(Machines.COUNTER_BLENDER, itemStacks, itemStack)
    );

    @Nonnull
    public static final RecipeType SLICED = new RecipeType(
        Keys.newKey("sliced"),
        Theme.themedItemStack(
            Material.SHEARS,
            CultivationThemes.RECIPE_TYPE,
            "Sliced",
            List.of(
                "So thin and delicate"
            )
        ),
        (itemStacks, itemStack) -> addRecipeToSimpleMachine(Machines.COUNTER_SLICING, itemStacks, itemStack)
    );

    @Nonnull
    public static final RecipeType GROUND = new RecipeType(
        Keys.newKey("ground"),
        Theme.themedItemStack(
            Material.BONE,
            CultivationThemes.RECIPE_TYPE,
            "Ground",
            List.of(
                "To a fine degree"
            )
        ),
        (itemStacks, itemStack) -> addRecipeToSimpleMachine(Machines.COUNTER_GRINDER, itemStacks, itemStack)
    );

    @Nonnull
    public static final RecipeType BOILED = new RecipeType(
        Keys.newKey("boiled"),
        Theme.themedItemStack(
            Material.CAULDRON,
            CultivationThemes.RECIPE_TYPE,
            "Boiled",
            List.of(
                "Hot hot hot!"
            )
        ),
        (itemStacks, itemStack) -> addRecipeToSimpleMachine(Machines.COUNTER_BOILING, itemStacks, itemStack)
    );

    @Nonnull
    public static final RecipeType FRIED = new RecipeType(
        Keys.newKey("fried"),
        Theme.themedItemStack(
            Material.BLACK_CONCRETE,
            CultivationThemes.RECIPE_TYPE,
            "Fried",
            List.of(
                "Crispy Perfection."
            )
        ),
        (itemStacks, itemStack) -> addRecipeToSimpleMachine(Machines.COUNTER_FRYER, itemStacks, itemStack)
    );

    @Nonnull
    public static final RecipeType GRILLED = new RecipeType(
        Keys.newKey("grilled"),
        Theme.themedItemStack(
            Material.POLISHED_BLACKSTONE,
            CultivationThemes.RECIPE_TYPE,
            "Grilled",
            List.of(
                "Juicy and Tender."
            )
        ),
        (itemStacks, itemStack) -> addRecipeToSimpleMachine(Machines.COUNTER_GRILL, itemStacks, itemStack)
    );

    @Nonnull
    public static final RecipeType FINISHING = new RecipeType(
        Keys.newKey("finishing"),
        Theme.themedItemStack(
            Material.POLISHED_DIORITE,
            CultivationThemes.RECIPE_TYPE,
            "Finishing",
            List.of(
                "Use the kitchen's finishing counter to make this item."
            )
        ),
        RecipeTypes::createFoodFinishingRecipe
    );

    @Nonnull
    public static final RecipeType BAKING = new RecipeType(
        Keys.newKey("baking"),
        Theme.themedItemStack(
            Material.FURNACE,
            CultivationThemes.RECIPE_TYPE,
            "Baking",
            List.of(
                "Use the kitchen's oven counter to make this item."
            )
        ),
        RecipeTypes::createFoodBakingRecipe
    );

    private static void addRecipeToSimpleMachine(KitchenRecipeMachineSimple machine, ItemStack[] recipe, ItemStack output) {
        ItemStack input = recipe[4];
        if (input == null) {
            return;
        }
        SlimefunItem slimefunItem = SlimefunItem.getByItem(input);
        machine.addRecipe(
            slimefunItem != null ? slimefunItem.getId() : input.getType().name(),
            output
        );
    }

    /**
     * This method both registers the drop and returns an ItemStack array that can be used
     * for Slimefun's recipe system. {@link RecipeTypes#VANILLA_DROP}
     *
     * @param stackToDrop The {@link ItemStack} to drop in the world
     * @param dropFrom    The {@link ItemStack} to drop from (#getType() is used) and the stack is used in the recipe.
     * @param dropChance  The chance (0-1) for the drop to occur
     * @return A {@link ItemStack[]} used for Slimefun's Recipe registration with the dropFrom item in the middle.
     */
    @Nonnull
    public static ItemStack[] createBlockDropRecipe(@Nonnull ItemStack stackToDrop,
                                                    @Nonnull ItemStack dropFrom,
                                                    double dropChance
    ) {
        return createBlockDropRecipe(stackToDrop, Set.of(dropFrom.getType()), dropChance);
    }

    /**
     * This method both registers the drop and returns an ItemStack array that can be used
     * for Slimefun's recipe system. {@link RecipeTypes#VANILLA_DROP}
     *
     * @param stackToDrop The {@link ItemStack} to drop in the world
     * @param dropFrom    The {@link ItemStack}'s to drop from (#getType() for the first is used) and the stack is used in the recipe.
     * @param dropChance  The chance (0-1) for the drop to occur
     * @return A {@link ItemStack[]} used for Slimefun's Recipe registration with the dropFrom item in the middle.
     */
    @Nonnull
    public static ItemStack[] createBlockDropRecipe(@Nonnull ItemStack stackToDrop,
                                                    @Nonnull Set<Material> dropFrom,
                                                    double dropChance
    ) {
        Material material = dropFrom.stream().findFirst().orElse(Material.DIRT);
        List<String> lore = dropFrom.stream()
            .map(material1 -> Theme.CLICK_INFO.apply(WordUtils.capitalize(material1.name().toLowerCase(Locale.ROOT))))
            .toList();
        ItemStack itemStack = Theme.themedItemStack(material, CultivationThemes.RECIPE_TYPE, "Drops From", lore);
        CustomDropListener.addBlockDrop(new CustomDropListener.BlockDrop(stackToDrop, dropFrom, dropChance));
        return new ItemStack[]{
            null, null, null,
            null, itemStack, null,
            null, null, null
        };
    }

    /**
     * Creates and registers the given recipe for Bucketing up blocks in the world. {@link RecipeTypes#BUCKETING}
     *
     * @param stackToDrop The {@link ItemStack} to be dropped when bucketing
     * @param dropFrom    The {@link Material} to drop from
     * @param dropChance  The chance (0-1) for the drop to occur
     * @return A {@link ItemStack[]} used for Slimefun's Recipe registration, blank.
     */
    @Nonnull
    public static ItemStack[] createBucketingRecipe(@Nonnull ItemStack stackToDrop,
                                                    @Nonnull Material dropFrom,
                                                    double dropChance
    ) {
        return createBucketingRecipe(stackToDrop, Set.of(dropFrom), dropChance);
    }

    /**
     * Creates and registers the given recipe for Bucketing up blocks in the world. {@link RecipeTypes#BUCKETING}
     *
     * @param stackToDrop The {@link ItemStack} to be dropped when bucketing
     * @param dropFrom    The set of {@link Material} to drop from
     * @param dropChance  The chance (0-1) for the drop to occur
     * @return A {@link ItemStack[]} used for Slimefun's Recipe registration, blank.
     */
    @Nonnull
    public static ItemStack[] createBucketingRecipe(@Nonnull ItemStack stackToDrop,
                                                    @Nonnull Set<Material> dropFrom,
                                                    double dropChance
    ) {
        CustomDropListener.addBucketingDrop(new CustomDropListener.BlockDrop(stackToDrop, dropFrom, dropChance));
        return new ItemStack[]{
            null, null, null,
            null, null, null,
            null, null, null
        };
    }

    /**
     * This method both registers the drop and returns an ItemStack array that can be used
     * for Slimefun's recipe system. {@link RecipeTypes#VANILLA_DROP}
     *
     * @param stackToDrop The {@link ItemStack} to drop in the world
     * @param dropFrom    The {@link ItemStack} to drop from (#getType() is used) and the stack is used in the recipe.
     * @param dropChance  The chance (0-1) for the drop to occur
     * @return A {@link ItemStack[]} used for Slimefun's Recipe registration with the dropFrom item in the middle.
     */
    @Nonnull
    public static ItemStack[] createMobDropRecipe(@Nonnull ItemStack stackToDrop,
                                                  @Nonnull LivingEntityDefinition dropFrom,
                                                  double dropChance
    ) {
        return createMobDropRecipe(stackToDrop, Set.of(dropFrom), dropChance);
    }

    /**
     * This method both registers the drop and returns an ItemStack array that can be used
     * for Slimefun's recipe system. {@link RecipeTypes#VANILLA_DROP}
     *
     * @param stackToDrop The {@link ItemStack} to drop in the world
     * @param dropFrom    The {@link LivingEntityDefinition}'s to drop from.
     * @param dropChance  The chance (0-1) for the drop to occur
     * @return A {@link ItemStack[]} used for Slimefun's Recipe registration with the dropFrom item in the middle.
     */
    @Nonnull
    public static ItemStack[] createMobDropRecipe(@Nonnull ItemStack stackToDrop,
                                                  @Nonnull Set<LivingEntityDefinition> dropFrom,
                                                  double dropChance
    ) {
        Set<EntityType> types = dropFrom.stream().map(LivingEntityDefinition::getType).collect(Collectors.toSet());
        List<String> lore = types.stream()
            .map(type -> Theme.CLICK_INFO.apply(WordUtils.capitalize(type.name().toLowerCase(Locale.ROOT))))
            .toList();
        final ItemStack itemStack = Theme.themedItemStack(
            Material.SKELETON_SKULL,
            CultivationThemes.RECIPE_TYPE,
            "Drops From",
            lore
        );
        MobDropListener.addDrop(new MobDropListener.MobDrop(stackToDrop, types, dropChance));
        return new ItemStack[]{
            null, null, null,
            null, itemStack, null,
            null, null, null
        };
    }

    public static void createFoodFinishingRecipe(@Nonnull ItemStack[] recipe, @Nonnull ItemStack result) {
        Machines.COUNTER_FINISHING.addRecipe(recipe, result);
    }

    public static void createFoodBakingRecipe(@Nonnull ItemStack[] recipe, @Nonnull ItemStack result) {
        Machines.COUNTER_OVEN.addRecipe(recipe, result);
    }
}
