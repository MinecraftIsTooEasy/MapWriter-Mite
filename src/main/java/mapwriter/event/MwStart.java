package mapwriter.event;

import java.io.File;
import java.util.logging.Logger;

import fi.dy.masa.malilib.event.InitializationHandler;
import mapwriter.Mw;
import mapwriter.api.MwAPI;
import mapwriter.overlay.OverlayGrid;
import mapwriter.overlay.OverlaySlime;
import net.fabricmc.api.ClientModInitializer;

public class MwStart implements ClientModInitializer {

    public static Logger logger;

    @Override
    public void onInitializeClient() {
        logger = Logger.getLogger("MapWriter");
        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());

        MwAPI.registerDataProvider("Slime", new OverlaySlime());
        MwAPI.registerDataProvider("Grid", new OverlayGrid());
        //MwAPI.registerDataProvider("Checker", new OverlayChecker());
        //MwAPI.setCurrentDataProvider("Slime");
    }
}
