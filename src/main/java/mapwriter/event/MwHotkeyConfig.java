package mapwriter.event;

import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import mapwriter.Mw;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class MwHotkeyConfig extends SimpleConfigs {

    private static final MwHotkeyConfig Instance;

    public MwHotkeyConfig(String name, List<ConfigHotkey> hotkeys, List<?> values) {
        super(name, hotkeys, values);
    }

    public static MwHotkeyConfig getInstance() {
        return Instance;
    }

    public static final List<ConfigHotkey> hotkeys;

    // hotkey
    public static ConfigHotkey keyMapGui = new ConfigHotkey("key.mw_open_gui", KeybindMulti.fromStorageString("K", KeybindSettings.INGAME_BOTH), null);
    public static ConfigHotkey keyNewMarker = new ConfigHotkey("key.mw_new_marker", Keyboard.KEY_INSERT, null);
    public static ConfigHotkey keyMapMode = new ConfigHotkey("key.mw_next_map_mode", Keyboard.KEY_I, null);
    public static ConfigHotkey keyNextGroup = new ConfigHotkey("key.mw_next_marker_group", KeybindMulti.fromStorageString("COMMA", KeybindSettings.INGAME_BOTH), null);
    public static ConfigHotkey keyTeleport = new ConfigHotkey("key.mw_teleport", Keyboard.KEY_PERIOD, null);
    public static ConfigHotkey keyZoomIn = new ConfigHotkey("key.mw_zoom_in", KeybindMulti.fromStorageString("PRIOR", KeybindSettings.INGAME_BOTH), null);
    public static ConfigHotkey keyZoomOut = new ConfigHotkey("key.mw_zoom_out", KeybindMulti.fromStorageString("NEXT", KeybindSettings.INGAME_BOTH), null);
    public static ConfigHotkey keyUndergroundMode = new ConfigHotkey("key.mw_underground_mode", Keyboard.KEY_U, null);
    //public static KeyBinding keyQuickLargeMap = new KeyBinding("key.mw_quick_large_map", Keyboard.KEY_NONE);


    static {
        hotkeys = List.of(keyMapGui, keyNewMarker, keyMapMode, keyNextGroup, keyTeleport, keyZoomIn, keyZoomOut, keyUndergroundMode);
        Instance = new MwHotkeyConfig(Mw.MOD_ID, hotkeys, null);
    }
}
