package mapwriter.event;

import java.io.File;
import java.util.logging.Logger;

import fi.dy.masa.malilib.event.InitializationHandler;
import mapwriter.Mw;
import mapwriter.api.MwAPI;
import mapwriter.overlay.OverlayGrid;
import mapwriter.overlay.OverlaySlime;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.I18n;

public class MwStart implements ClientModInitializer {

    public static Logger logger;

    @Override
    public void onInitializeClient() {
        logger = Logger.getLogger("MapWriter");
        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());


        //MwAPI.registerDataProvider("Checker", new OverlayChecker());
        //MwAPI.setCurrentDataProvider("Slime");
    }
}
