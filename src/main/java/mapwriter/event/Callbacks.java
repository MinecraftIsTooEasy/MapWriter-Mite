package mapwriter.event;

import mapwriter.Mw;
import mapwriter.gui.MwGui;
import mapwriter.gui.MwGuiMarkerDialog;
import mapwriter.map.Marker;
import net.minecraft.Minecraft;

public class Callbacks {

    public static boolean ready() {
        return Mw.getInstance().ready;
    }

    public static void init(Mw mw, Minecraft mc) {

        MwHotkeyConfig.keyMapGui.getKeybind().setCallback((keyAction, iKeybind) -> {
            if (!ready()) return false;
            if (mc.currentScreen == null) {
                mc.displayGuiScreen(new MwGui(mw));
                return true;
            }
            if (mc.currentScreen instanceof MwGui mwGui) {
                mwGui.exit = 1;
                return true;
            }
            return false;
        });

        MwHotkeyConfig.keyNewMarker.getKeybind().setCallback((keyAction, iKeybind) -> {
            if (!ready()) return false;
            String group = mw.markerManager.getVisibleGroupName();
            if (group.equals("none")) {
                group = "group";
            }
            mc.displayGuiScreen(
                    new MwGuiMarkerDialog(
                            null,
                            mw.markerManager,
                            "",
                            group,
                            mw.playerXInt,
                            mw.playerYInt,
                            mw.playerZInt,
                            mw.playerDimension
                    )
            );
            return true;
        });

        MwHotkeyConfig.keyMapMode.getKeybind().setCallback((keyAction, iKeybind) -> {
            if (!ready()) return false;
            mw.miniMap.nextOverlayMode(1);
            return true;
        });

        MwHotkeyConfig.keyNextGroup.getKeybind().setCallback((keyAction, iKeybind) -> {
            if (!ready()) return false;
            if (mc.currentScreen == null || mc.currentScreen instanceof MwGui) {
                mw.markerManager.nextGroup();
                mw.markerManager.update();
                if (mc.currentScreen == null) {
                    mc.thePlayer.addChatMessage("group " + mw.markerManager.getVisibleGroupName() + " selected");
                }
                return true;
            }
            return false;
        });

        MwHotkeyConfig.keyTeleport.getKeybind().setCallback((keyAction, iKeybind) -> {
            if (!ready()) return false;
            // set or remove marker
            Marker marker = mw.markerManager.getNearestMarkerInDirection(
                    mw.playerXInt,
                    mw.playerZInt,
                    mw.playerHeading);
            if (marker != null) {
                mw.teleportToMarker(marker);
            }
            return true;
        });

        MwHotkeyConfig.keyZoomIn.getKeybind().setCallback((keyAction, iKeybind) -> {
            if (!ready()) return false;
            if (mc.currentScreen == null || mc.currentScreen instanceof MwGui) {
                mw.miniMap.view.adjustZoomLevel(-1);
                return true;
            }
            return false;
        });

        MwHotkeyConfig.keyZoomOut.getKeybind().setCallback((keyAction, iKeybind) -> {
            if (!ready()) return false;
            if (mc.currentScreen == null || mc.currentScreen instanceof MwGui) {
                mw.miniMap.view.adjustZoomLevel(1);
                return true;
            }
            return false;
        });

        MwHotkeyConfig.keyUndergroundMode.getKeybind().setCallback((keyAction, iKeybind) -> {
            if (!ready()) return false;
            if (mc.currentScreen == null || mc.currentScreen instanceof MwGui) {
                mw.toggleUndergroundMode();
                if (mc.currentScreen instanceof MwGui mwGui) mwGui.mapView.setUndergroundMode(mw.undergroundMode);
                return true;
            }
            return false;
        });

    }
}
