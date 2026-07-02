package mapwriter.gui;

import java.util.ArrayList;
import java.util.List;

import mapwriter.Mw;
import mapwriter.map.MapView;
import net.minecraft.*;

import org.lwjgl.input.Keyboard;

public class MwGuiMapContextMenu extends GuiScreen {

	private final GuiScreen parentScreen;
	private final Mw mw;
	private final MapView mapView;
	private final int bX, bY, bZ, dim;
	private final List<MwTextButton> buttons = new ArrayList<>();
	String title;
	static final int textDialogWidthPercent = 50;
	static final int textDialogTitleY = 80;
	static final int textDialogY = 92;
	static final int textDialogErrorY = 108;

	public MwGuiMapContextMenu(GuiScreen parentScreen, String title, Mw mw, MapView mapView, int x, int y, int z, int dimension) {
		this.parentScreen = parentScreen;
		this.title = title;
		this.mw = mw;
		this.mapView = mapView;
		this.bX = x;
		this.bY = y;
		this.bZ = z;
		this.dim = dimension;
	}

	public void initGui() {
		this.buttons.clear();
		String[] btnTexts = {
				I18n.getString("mw.button.map_context.new_marker"),
				I18n.getString("mw.button.map_context.teleport_here")
		};
		int btnW = 0;
		for (String text : btnTexts) {
			btnW = Math.max(btnW, this.fontRenderer.getStringWidth("[" + text + "]"));
		}
		int btnH = 14;
		int btnSpacing = 4;
		int totalH = btnTexts.length * btnH + (btnTexts.length - 1) * btnSpacing;
		int startY = this.height / 2 - totalH / 2 + 4;

		for (int i = 0; i < btnTexts.length; i++) {
			int btnX = this.width / 2 - btnW / 2;
			int btnY = startY + i * (btnH + btnSpacing);
			this.buttons.add(new MwTextButton(i, btnTexts[i], btnX, btnY, btnW, btnH));
		}
	}

	public void drawScreen(int mouseX, int mouseY, float f) {

		if (this.parentScreen != null) {
			this.parentScreen.drawScreen(mouseX, mouseY, f);
		} else {
			this.drawDefaultBackground();
		}
		
		int w = this.width * textDialogWidthPercent / 100;
		drawRect(
				(this.width - w) / 2,
				textDialogTitleY - 4,
				(this.width - w) / 2 + w,
				textDialogErrorY + (this.buttons.size() * 20),
				0x80000000);
		this.drawCenteredString(
				this.fontRenderer,
				this.title,
				this.width / 2,
				textDialogTitleY,
				0xffffff);
//		String s;
//		if (bY != 0) {
//			s = String.format(I18n.getString("mw.cursor.pos.ynonull"), bX, bY, bZ);
//		} else {
//			s = String.format(I18n.getString("mw.cursor.pos.ynull"), bX, bZ);
//		}
//		this.drawCenteredString(
//				this.fontRenderer,
//				s,
//				this.width / 2,
//				textDialogTitleY + 16,
//				0xffffff);

		for (MwTextButton btn : this.buttons) {
			this.drawCenteredString(this.fontRenderer, btn.text(), btn.x() + btn.width() / 2, btn.y(), 0xffffff);
		}

		super.drawScreen(mouseX, mouseY, f);
	}

	protected void mouseClicked(int x, int y, int button) {
		if (button != 0) return;
		for (MwTextButton btn : this.buttons) {
			if (btn.over(x, y)) {
				switch (btn.id()) {
					case 0 -> {
						String group = this.mw.markerManager.getVisibleGroupName();
						if (group.equals("none")) {
							group = "group";
						}
						this.mc.displayGuiScreen(new MwGuiMarkerDialog(
								this.parentScreen, this.mw, this.mw.markerManager,
								"", group, this.bX, this.bY, this.bZ, this.dim));
					}
					case 1 -> {
						this.mw.teleportToMapPos(this.mapView, this.bX, this.bY, this.bZ);
						this.mc.displayGuiScreen(null);
					}
				}
			}
		}
		super.mouseClicked(x, y, button);
	}

	protected void keyTyped(char c, int key) {
		if (key == Keyboard.KEY_ESCAPE) {
			this.mc.displayGuiScreen(this.parentScreen);
		}
	}
}
