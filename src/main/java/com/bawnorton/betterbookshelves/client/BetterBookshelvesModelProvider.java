package com.bawnorton.betterbookshelves.client;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import static com.bawnorton.betterbookshelves.BetterBookshelves.LOGGER;

public class BetterBookshelvesModelProvider implements ModelResourceProvider {

    @Override
    public @Nullable UnbakedModel loadModelResource(Identifier resourceId, ModelProviderContext context) {
        if(resourceId.toString().contains("chiseled_bookshelf")) {
            return new ChiseledBookshelfModel();
        }
        return null;
    }
}
