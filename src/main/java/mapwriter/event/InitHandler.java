package mapwriter.event;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.TickHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import mapwriter.Mw;
import net.minecraft.Minecraft;

import java.io.File;

public class InitHandler implements IInitializationHandler {
    @Override
    public void registerModHandlers() {
        MwConfig config = new MwConfig(new File("config" + File.separator + Mw.MOD_ID + "_Legacy" + ".json"));
        new Mw(config);
        NewMWConfig.getInstance().load();
        ConfigManager.getInstance().registerConfig(NewMWConfig.getInstance());
        Callbacks.init(Mw.getInstance(), Minecraft.getMinecraft());
        TickHandler.getInstance().registerClientTickHandler(Mw.getInstance());
    }
}
