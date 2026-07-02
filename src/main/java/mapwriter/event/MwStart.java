package mapwriter.event;

import java.util.logging.Logger;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;

public class MwStart implements ClientModInitializer {

    public static Logger logger;

    @Override
    public void onInitializeClient() {
        logger = Logger.getLogger("MapWriter");
        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
        MwEvents.register();


        //MwAPI.registerDataProvider("Checker", new OverlayChecker());
        //MwAPI.setCurrentDataProvider("Slime");
    }
}
