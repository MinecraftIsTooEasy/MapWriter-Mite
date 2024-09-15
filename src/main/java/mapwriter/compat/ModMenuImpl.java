package mapwriter.compat;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import mapwriter.Mw;
import mapwriter.event.NewMWConfig;

public class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> NewMWConfig.getInstance().getConfigScreen(screen);
    }
}
