package net.anvian.chiseledbookshelfvisualizer;

import net.anvian.chiseledbookshelfvisualizer.network.BookShelfInventoryPayload;
import net.anvian.chiseledbookshelfvisualizer.network.BookShelfInventoryRequestPayload;
import net.anvian.chiseledbookshelfvisualizer.network.ModCheckPayload;
import net.anvian.chiseledbookshelfvisualizer.util.BookshelfTools;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChiseledBookshelfVisualizer implements ModInitializer {
    public final static String MOD_ID = "chiseled-bookshelf-visualizer";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static MinecraftServer serverInstance;

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(BookShelfInventoryRequestPayload.ID,BookShelfInventoryRequestPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(BookShelfInventoryPayload.ID,BookShelfInventoryPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ModCheckPayload.ID, ModCheckPayload.CODEC);


        ServerPlayNetworking.registerGlobalReceiver(BookShelfInventoryRequestPayload.ID,((payload, context) -> context.server().execute(() -> {
            if(ChiseledBookshelfVisualizer.serverInstance == null) return;

            ItemStack stack = BookshelfTools.getItemById(payload.pos(),payload.slotNum(),context.player());
            if(stack == null){
                ServerPlayNetworking.send(context.player(), new BookShelfInventoryPayload(Items.AIR.getDefaultStack(), payload.pos(), payload.slotNum()));
                return;
            }
            ServerPlayNetworking.send(context.player(), new BookShelfInventoryPayload(stack, payload.pos(), payload.slotNum()));
        })));

        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
            serverInstance = server;
            ServerPlayNetworking.send(handler.player, new ModCheckPayload(true));
        }));
    }
}
