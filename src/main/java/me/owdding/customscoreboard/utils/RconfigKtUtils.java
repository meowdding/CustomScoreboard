package me.owdding.customscoreboard.utils;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import com.teamresourceful.resourcefulconfigkt.api.builders.CategoryBuilder;
import com.teamresourceful.resourcefulconfigkt.api.builders.EntriesBuilder;
import com.teamresourceful.resourcefulconfigkt.api.builders.TypeBuilder;
import me.owdding.customscoreboard.mixins.compat.TypeBuilderAccessor;

import java.util.LinkedHashMap;
import java.util.List;

public sealed interface RconfigKtUtils permits RconfigKtUtils.Impl {
    static LinkedHashMap<String, CategoryBuilder> getCategories(CategoryBuilder builder) {
        return builder.getCategories$ResourcefulConfigKt();
    }

    static List<ResourcefulConfigElement> getElements(EntriesBuilder builder) {
        return builder.getElements$ResourcefulConfigKt();
    }

    static String getId(TypeBuilder builder) {
        return ((TypeBuilderAccessor) builder).customscoreboard$getId();
    }

    final class Impl implements RconfigKtUtils {
    }
}
