package mapwriter.gui;

import java.util.ArrayList;
import java.util.List;

import mapwriter.Mw;
import mapwriter.map.Marker;
import mapwriter.map.MarkerManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.GuiScreen;
import net.minecraft.GuiTextField;
import net.minecraft.I18n;
import org.lwjgl.input.Mouse;

@Environment(EnvType.CLIENT)
public class MwGuiMarkerDialog extends MwGuiMultiTextDialog {
	private final Mw mw;
	private final MarkerManager markerManager;
	private Marker editingMarker;
	private String markerName;
	private String markerGroup;
	private int markerX;
	private int markerY;
	private int markerZ;
	private int dimension;
	private int markerColor;
	private Marker localMarker;

	private int previewBoxX, previewBoxY, previewBoxW, previewBoxH;
	private boolean previewBoxVisible;

	private final List<MwTextButton> mwButtons = new ArrayList<>();

	public MwGuiMarkerDialog(GuiScreen parentScreen, Mw mw, MarkerManager markerManager, String markerName, String markerGroup, int x, int y, int z, int dimension) {
		super(parentScreen,
				I18n.getString("mw.title.marker.new"),
				List.of(markerName,
						markerGroup,
						String.valueOf(x),
						String.valueOf(y),
						String.valueOf(z),
						String.format("%06X", Marker.getCurrentColour() & 0x00FFFFFF)),
				List.of(I18n.getString("mw.text.marker.error"),
						I18n.getString("mw.title.marker.group.error"),
						I18n.getString("mw.title.marker.value.error"),
						I18n.getString("mw.title.marker.value.error"),
						I18n.getString("mw.title.marker.value.error"),
						I18n.getString("mw.title.marker.value.error")));
		this.mw = mw;
		this.markerManager = markerManager;
		this.markerName = markerName;
		this.markerGroup = markerGroup;
		this.markerX = x;
		this.markerY = y;
		this.markerZ = z;
		this.editingMarker = null;
		this.dimension = dimension;
		this.markerColor = Marker.getCurrentColour();
		this.localMarker = new Marker(markerName, markerGroup, x, y, z, dimension, this.markerColor);

		this.setFieldLabels(List.of(
				I18n.getString("mw.title.marker"),
				I18n.getString("mw.title.marker.group"),
				I18n.getString("mw.title.marker.value.x"),
				I18n.getString("mw.title.marker.value.y"),
				I18n.getString("mw.title.marker.value.z"),
				I18n.getString("mw.title.marker.value.color")
		));
		this.setLabelWidth(50);
		this.setFieldWidth(60);
		this.enableColorPreview(5, this.markerColor);
	}

	public MwGuiMarkerDialog(GuiScreen parentScreen, Mw mw, MarkerManager markerManager, Marker editingMarker) {
		super(parentScreen,
				I18n.getString("mw.title.marker.edit"),
				List.of(editingMarker.name,
						editingMarker.groupName,
						String.valueOf(editingMarker.x),
						String.valueOf(editingMarker.y),
						String.valueOf(editingMarker.z),
						String.format("%06X", editingMarker.colour & 0x00FFFFFF)),
				List.of(I18n.getString("mw.text.marker.error"),
						I18n.getString("mw.title.marker.group.error"),
						I18n.getString("mw.title.marker.value.error"),
						I18n.getString("mw.title.marker.value.error"),
						I18n.getString("mw.title.marker.value.error"),
						I18n.getString("mw.title.marker.value.error")));
		this.mw = mw;
		this.markerManager = markerManager;
		this.editingMarker = editingMarker;
		this.markerName = editingMarker.name;
		this.markerGroup = editingMarker.groupName;
		this.markerX = editingMarker.x;
		this.markerY = editingMarker.y;
		this.markerZ = editingMarker.z;
		this.dimension = editingMarker.dimension;
		this.markerColor = editingMarker.colour;
		this.localMarker = editingMarker;

		this.setFieldLabels(List.of(
				I18n.getString("mw.title.marker"),
				I18n.getString("mw.title.marker.group"),
				I18n.getString("mw.title.marker.value.x"),
				I18n.getString("mw.title.marker.value.y"),
				I18n.getString("mw.title.marker.value.z"),
				I18n.getString("mw.title.marker.value.color")
		));
		this.setLabelWidth(50);
		this.setFieldWidth(60);
		this.enableColorPreview(5, this.markerColor);
	}

	@Override
	public void initGui() {
		super.initGui();
		this.mwButtons.clear();
		int w = this.width * textDialogWidthPercent / 100;
		int firstField = textDialogY + 2;
		int lastField = textDialogY + (this.fieldSize - 1) * this.fieldSpacing + 2;
		int btnX = (this.width - w) / 2 + w * 3 / 4;
		List<MwTextButton> btns = new ArrayList<>();
		if (this.editingMarker != null) {
			addButtonDef(btns, 101, "mw.button.delete");
			addButtonDef(btns, 102, "mw.button.teleport");
		}
		for (int i = 0; i < btns.size(); i++) {
			MwTextButton def = btns.get(i);
			int btnY = (btns.size() == 1) ? firstField : firstField + (lastField - firstField) * i / (btns.size() - 1);
			this.mwButtons.add(new MwTextButton(def.id(), def.text(), btnX - def.width() / 2, btnY, def.width(), 10));
		}
		addButtonDef(btns, 100, "mw.button.done");
		MwTextButton done = btns.get(btns.size() - 1);
		this.mwButtons.add(new MwTextButton(100, done.text(), btnX - done.width() / 2, lastField + this.fieldSpacing, done.width(), 10));
		addButtonDef(btns, 99, "mw.button.cancel");
		MwTextButton cancel = btns.get(btns.size() - 1);
		int cancelX = (this.width + w) / 2 - w * 3 / 4;
		this.mwButtons.add(new MwTextButton(99, cancel.text(),  cancelX - cancel.width() / 2, lastField + this.fieldSpacing, cancel.width(), 10));
	}

	private void addButtonDef(List<MwTextButton> btns, int id, String key) {
		String text = I18n.getString(key);
		btns.add(new MwTextButton(id, text, 0, 0, this.fontRenderer.getStringWidth(text), 10));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float f) {
		super.drawScreen(mouseX, mouseY, f);
		if (this.colorPreviewEnabled && this.colorPreviewIndex >= 0 && this.colorPreviewIndex < this.textFields.size()) {
			GuiTextField field = this.textFields.get(this.colorPreviewIndex);
			this.previewBoxX = field.xPos + field.getWidth() + 12;
			this.previewBoxY = field.yPos;
			this.previewBoxH = this.previewBoxW = 12;
			this.previewBoxVisible = true;
		} else {
			this.previewBoxVisible = false;
		}
		for (MwTextButton btn : this.mwButtons) {
			this.drawCenteredString(this.fontRenderer, btn.text(), btn.x() + btn.width() / 2, btn.y(), 0xFFFFFF);
		}
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		if (!this.previewBoxVisible) return;
		if (Mouse.getEventDWheel() != 0) {
			int mx = Mouse.getEventX() * this.width / this.mc.displayWidth;
			int my = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			if (mx >= this.previewBoxX && mx < this.previewBoxX + this.previewBoxW && my >= this.previewBoxY && my < this.previewBoxY + this.previewBoxH) {
				if (Mouse.getEventDWheel() > 0) {
					this.localMarker.colourNext();
				} else {
					this.localMarker.colourPrev();
				}
				this.markerColor = this.localMarker.colour;
				this.previewColor = this.markerColor;
				this.textFields.get(this.colorPreviewIndex).setText(String.format("%06X", this.markerColor & 0x00FFFFFF));
			}
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
		for (MwTextButton btn : this.mwButtons) {
			if (btn.over(x, y)) {
				this.onTextButton(btn.id());
				return;
			}
		}
	}

	private void onTextButton(int id) {
		if (id == 99) {
			this.mc.displayGuiScreen(this.parentScreen);
			return;
		}
		if (id == 101 && this.editingMarker != null) {
			if (this.markerManager.selectedMarker == this.editingMarker) {
				this.markerManager.selectedMarker = null;
			}
			this.markerManager.delMarker(this.editingMarker);
			this.markerManager.update();
			this.editingMarker = null;
			this.mc.displayGuiScreen(this.parentScreen);
		}
		if (id == 100) {
			if (this.submit()) {
				this.mc.displayGuiScreen(this.parentScreen);
			}
		}
		if (id == 102 && this.editingMarker != null) {
			this.mw.teleportToMarker(this.editingMarker);
			this.mc.displayGuiScreen(null);
		}
	}

	@Override
	public boolean submit() {
		List<String> inputs = this.getAllInputsAsStrings();
		if (inputs.size() >= 6 && this.isAllInputValid()) {
			this.markerName = inputs.get(0);
			this.markerGroup = inputs.get(1);

			try {
				this.markerX = Integer.parseInt(inputs.get(2));
				this.markerY = Integer.parseInt(inputs.get(3));
				this.markerZ = Integer.parseInt(inputs.get(4));
			} catch (NumberFormatException ignored) {
				return false;
			}

			try {
				this.markerColor = 0xFF000000 | Integer.parseInt(inputs.get(5).trim(), 16);
			} catch (NumberFormatException ignored) {
			}

			if (this.editingMarker != null) {
				this.markerManager.delMarker(this.editingMarker);
				this.editingMarker = null;
			}
			this.markerManager.addMarker(this.markerName, this.markerGroup, this.markerX, this.markerY, this.markerZ, this.dimension, this.markerColor);
			this.markerManager.setVisibleGroupName(this.markerGroup);
			this.markerManager.update();
			return true;
		}
		return false;
	}
}
