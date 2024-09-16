package mapwriter.event;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.TickHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import mapwriter.Mw;
import mapwriter.api.MwAPI;
import mapwriter.overlay.OverlayGrid;
import mapwriter.overlay.OverlaySlime;
import net.minecraft.I18n;
import net.minecraft.Minecraft;

import java.io.File;

public class InitHandler implements IInitializationHandler {
    @Override
    public void registerModHandlers() {
        MwConfig config = new MwConfig(new File("config" + File.separator + Mw.MOD_ID + "_Legacy" + ".json"));
        new Mw(config);
        MwHotkeyConfig.getInstance().load();
        ConfigManager.getInstance().registerConfig(MwHotkeyConfig.getInstance());
        Callbacks.init(Mw.getInstance(), Minecraft.getMinecraft());
        TickHandler.getInstance().registerClientTickHandler(Mw.getInstance());
        MwAPI.registerDataProvider(I18n.getString("mv.button.overlay.slime"), new OverlaySlime());
        MwAPI.registerDataProvider(I18n.getString("mv.button.overlay.grid"), new OverlayGrid());
    }
}
