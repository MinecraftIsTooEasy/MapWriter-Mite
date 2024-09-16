package mapwriter.compat;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import mapwriter.event.MwHotkeyConfig;

public class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> MwHotkeyConfig.getInstance().getConfigScreen(screen);
    }
}
