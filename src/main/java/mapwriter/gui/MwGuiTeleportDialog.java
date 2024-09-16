package mapwriter.gui;

import mapwriter.Mw;
import mapwriter.map.MapView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.GuiScreen;
import net.minecraft.I18n;

@Environment(EnvType.CLIENT)
public class MwGuiTeleportDialog extends MwGuiTextDialog {

	final Mw mw;
	final MapView mapView;
	final int teleportX, teleportZ;
    
    public MwGuiTeleportDialog(GuiScreen parentScreen, Mw mw, MapView mapView, int x, int y, int z) {
        super(parentScreen, I18n.getString("mw.title.teleport"), "" + y, I18n.getString("mw.text.teleport.error"));
        this.mw = mw;
        this.mapView = mapView;
        this.teleportX = x;
        this.teleportZ = z;
        this.backToGameOnSubmit = true;
    }
    	
	@Override
	public boolean submit() {
		boolean done = false;
		int height = this.getInputAsInt();
		if (this.inputValid) {
    		height = Math.min(Math.max(0, height), 255);
    		this.mw.defaultTeleportHeight = height;
    		this.mw.teleportToMapPos(this.mapView, this.teleportX, height, this.teleportZ);
    		done = true;
		}
		return done;
	}
}
