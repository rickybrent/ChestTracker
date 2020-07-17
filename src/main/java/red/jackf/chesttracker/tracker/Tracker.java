package red.jackf.chesttracker.tracker;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import red.jackf.chesttracker.ChestTracker;

import java.util.List;
import java.util.stream.Collectors;

public class Tracker {
    private static final Tracker TRACKER = new Tracker();

    public static Tracker getInstance() {
        return TRACKER;
    }

    public <T extends ScreenHandler> void handleScreen(HandledScreen<T> screen) {
        ScreenHandler handler = screen.getScreenHandler();
        List<ItemStack> items = handler.slots.stream()
                .filter(slot -> !(slot.inventory instanceof PlayerInventory))
                .filter(Slot::hasStack)
                .map(Slot::getStack)
                .collect(Collectors.toList());

        if (ChestTracker.CONFIG.trackedScreens.debugPrint && MinecraftClient.getInstance().player != null) {
            String className = screen.getClass().getSimpleName();
            Text message = validScreenToTrack(className) ?
                    new TranslatableText("chesttracker.gui_class_name_tracked", className).formatted(Formatting.GREEN) :
                    new TranslatableText("chesttracker.gui_class_name_not_tracked", className).formatted(Formatting.RED);
            MinecraftClient.getInstance().player.sendSystemMessage(message, Util.NIL_UUID);
        }
        System.out.println(items);
    }

    public <T extends ScreenHandler> boolean validScreenToTrack(HandledScreen<T> screen) {
        return validScreenToTrack(screen.getClass().getSimpleName());
    }

    public boolean validScreenToTrack(String screenClass) {
        return !ChestTracker.CONFIG.trackedScreens.blocklist.contains(screenClass);
    }
}
