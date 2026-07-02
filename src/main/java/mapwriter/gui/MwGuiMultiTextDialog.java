package mapwriter.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.*;

public class MwGuiMultiTextDialog extends MwGuiTextDialog {
	protected final List<GuiTextField> textFields = new ArrayList<>();
	private final List<String> texts = new ArrayList<>();
	protected final List<String> errors = new ArrayList<>();
	protected final List<Boolean> showErrors = new ArrayList<>();
	private final List<Boolean> inputValids = new ArrayList<>();
	protected final List<String> fieldLabels = new ArrayList<>();
	protected int fieldSize;
	protected int fieldSpacing = 16;
	private int labelWidth = 40;
	private int fieldWidth = 100;
	protected int colorPreviewIndex = -1;
	protected int previewColor = 0xFFFFFFFF;
	protected boolean colorPreviewEnabled = false;
	
	public MwGuiMultiTextDialog(GuiScreen parentScreen, String title, List<String> texts, String error) {
		super(parentScreen, title, texts.isEmpty() ? "" : texts.get(0), error);
		this.texts.clear();
		this.texts.addAll(texts);
		this.fieldSize = texts.size();
		this.errors.clear();
		for (int i = 0; i < this.fieldSize; i++) {
			this.errors.add(error);
			this.showErrors.add(false);
			this.inputValids.add(false);
			this.fieldLabels.add("");
		}
	}
	
	public MwGuiMultiTextDialog(GuiScreen parentScreen, String title, List<String> texts, List<String> errors) {
		super(parentScreen, title, texts.isEmpty() ? "" : texts.get(0), errors.isEmpty() ? "" : errors.get(0));
		this.texts.clear();
		this.texts.addAll(texts);
		this.fieldSize = texts.size();
		this.errors.clear();
		this.errors.addAll(errors);
		while (this.errors.size() < this.fieldSize) {
			this.errors.add("");
		}
		this.showErrors.clear();
		this.inputValids.clear();
		this.fieldLabels.clear();
		for (int i = 0; i < this.fieldSize; i++) {
			this.showErrors.add(false);
			this.inputValids.add(false);
			this.fieldLabels.add("");
		}
	}
	
	public void setFieldLabels(List<String> labels) {
		for (int i = 0; i < Math.min(labels.size(), this.fieldLabels.size()); i++) {
			this.fieldLabels.set(i, labels.get(i));
		}
	}
	
	public void setFieldLabel(int index, String label) {
		if (index >= 0 && index < this.fieldLabels.size()) {
			this.fieldLabels.set(index, label);
		}
	}
	
	public void setLabelWidth(int width) {
		this.labelWidth = width;
	}
	
	public void setFieldWidth(int width) {
		this.fieldWidth = width;
	}
	
	public void setFieldSpacing(int spacing) {
		this.fieldSpacing = spacing;
	}
	
	public void enableColorPreview(int index, int color) {
		this.colorPreviewEnabled = true;
		this.colorPreviewIndex = index;
		this.previewColor = color;
	}
	
	private void newTextFields() {
		int w = this.width * textDialogWidthPercent / 100;
		int actualFieldWidth = Math.min(this.fieldWidth, w - this.labelWidth - 15);
		int startX = (this.width - w) / 2 + 5;
		this.textFields.clear();
		for (int i = 0; i < this.fieldSize; i++) {
			GuiTextField field = new GuiTextField(this.fontRenderer, startX + this.labelWidth + 5, textDialogY + i * fieldSpacing, actualFieldWidth, 12);
			field.setFocused(i == 0);
			field.setCanLoseFocus(true);
			if (i < this.texts.size()) {
				field.setText(this.texts.get(i));
			}
			this.textFields.add(field);
		}
	}
	
	public void setText(int index, String s) {
		if (index >= 0 && index < this.textFields.size()) {
			this.textFields.get(index).setText(s);
			if (index < this.texts.size()) {
				this.texts.set(index, s);
			}
			if (this.colorPreviewEnabled && index == this.colorPreviewIndex) {
				this.updatePreviewColor(s);
			}
		}
	}
	
	public void setTexts(List<String> newTexts) {
		for (int i = 0; i < Math.min(newTexts.size(), this.textFields.size()); i++) {
			this.textFields.get(i).setText(newTexts.get(i));
			this.texts.set(i, newTexts.get(i));
		}
	}
	
	private void updatePreviewColor(String text) {
		try {
			this.previewColor = 0xFF000000 | Integer.parseInt(text.trim(), 16);
		} catch (NumberFormatException ignored) {
		}
	}
	
	public String getInputAsString(int index) {
		String s = this.textFields.get(index).getText().trim();
		this.inputValids.set(index, !s.isEmpty());
		this.showErrors.set(index, !this.inputValids.get(index));
		if (this.colorPreviewEnabled && index == this.colorPreviewIndex) {
			this.updatePreviewColor(s);
		}
		return s;
	}
	
	public int getInputAsInt(int index) {
		String s = this.textFields.get(index).getText().trim();
		int value = 0;
		try {
			value = Integer.parseInt(s);
			this.inputValids.set(index, true);
			this.showErrors.set(index, false);
		} catch (NumberFormatException e) {
			this.inputValids.set(index, false);
			this.showErrors.set(index, true);
		}
		return value;
	}
	
	public int getInputAsHexInt(int index) {
		String s = this.textFields.get(index).getText().trim();
		int value = 0;
		try {
			value = 0xFF000000 | Integer.parseInt(s, 16);
			this.inputValids.set(index, true);
			this.showErrors.set(index, false);
			if (this.colorPreviewEnabled && index == this.colorPreviewIndex) {
				this.previewColor = value;
			}
		} catch (NumberFormatException e) {
			this.inputValids.set(index, false);
			this.showErrors.set(index, true);
		}
		return value;
	}
	
	public List<String> getAllInputsAsStrings() {
		List<String> result = new ArrayList<>();
		for (int i = 0; i < this.textFields.size(); i++) {
			String s = this.textFields.get(i).getText().trim();
			result.add(s);
			this.inputValids.set(i, !s.isEmpty());
			this.showErrors.set(i, !this.inputValids.get(i));
		}
		return result;
	}
	
	public boolean isAllInputValid() {
		for (Boolean valid : this.inputValids) {
			if (!valid) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean submit() {
		boolean allValid = true;
		for (int i = 0; i < this.textFields.size(); i++) {
			String s = this.textFields.get(i).getText().trim();
			this.inputValids.set(i, !s.isEmpty());
			this.showErrors.set(i, !this.inputValids.get(i));
			if (!this.inputValids.get(i)) {
				allValid = false;
			}
		}
		return allValid;
	}
	
	@Override
	public void initGui() {
		this.newTextFields();
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float f) {
		if (this.parentScreen != null) {
			this.parentScreen.drawScreen(mouseX, mouseY, f);
		} else {
			this.drawDefaultBackground();
		}
		int w = this.width * textDialogWidthPercent / 100;
		int h = 20 + this.fieldSize * fieldSpacing + 10;
		drawRect(
				(this.width - w) / 2,
				textDialogTitleY - 4,
				(this.width - w) / 2 + w,
				textDialogTitleY + h,
				0x80000000);
		this.drawCenteredString(
				this.fontRenderer,
				this.title,
				this.width / 2,
				textDialogTitleY,
				0xffffff);
		for (int i = 0; i < this.textFields.size(); i++) {
			GuiTextField field = this.textFields.get(i);
			if (i < this.fieldLabels.size() && !this.fieldLabels.get(i).isEmpty()) {
				this.drawString(
						this.fontRenderer,
						this.fieldLabels.get(i),
						(this.width - w) / 2 + 5,
						textDialogY + i * fieldSpacing + 2,
						0xffffff);
			}
			field.drawTextBox();
			if (this.colorPreviewEnabled && i == this.colorPreviewIndex) {
				int colourX = field.xPos + field.getWidth() + 12;
				int ColourY = field.yPos;
				int ColourH = 12, ColourW = 12;
				drawRect(colourX, ColourY, colourX + ColourW, ColourY + ColourH, this.previewColor);
				// draw 1-pixel border
				drawRect(colourX, ColourY, colourX + ColourW, ColourY + 1, 0xFF000000);
				drawRect(colourX, ColourY + ColourH - 1, colourX + ColourW, ColourY + ColourH, 0xFF000000);
				drawRect(colourX, ColourY, colourX + 1, ColourY + ColourH, 0xFF000000);
				drawRect(colourX + ColourW - 1, ColourY, colourX + ColourW, ColourY + ColourH, 0xFF000000);
			}
			
			if (this.showErrors.get(i)) {
				String errorMsg = i < this.errors.size() ? this.errors.get(i) : this.error;
				this.drawString(
						this.fontRenderer,
						errorMsg,
						(field.xPos + field.getWidth()) + 28,
						textDialogY + i * fieldSpacing + 2,
						0xff6666);
			}
		}
		//super.drawScreen(mouseX, mouseY, f);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
		for (GuiTextField field : this.textFields) {
			field.mouseClicked(x, y, button);
		}
	}
	
	@Override
	protected void keyTyped(char c, int key) {
		switch (key) {
		case Keyboard.KEY_ESCAPE:
			this.mc.displayGuiScreen(this.parentScreen);
			break;
			
		case Keyboard.KEY_RETURN:
			if (this.submit()) {
				if (!this.backToGameOnSubmit) {
					this.mc.displayGuiScreen(this.parentScreen);
				} else {
					this.mc.displayGuiScreen(null);
				}
			}
			break;
			
		case Keyboard.KEY_TAB:
			int currentIndex = -1;
			for (int i = 0; i < this.textFields.size(); i++) {
				if (this.textFields.get(i).isFocused()) {
					currentIndex = i;
					break;
				}
			}
			if (currentIndex >= 0) {
				this.textFields.get(currentIndex).setFocused(false);
				int nextIndex = (currentIndex + 1) % this.textFields.size();
				this.textFields.get(nextIndex).setFocused(true);
			} else if (!this.textFields.isEmpty()) {
				this.textFields.get(0).setFocused(true);
			}
			break;
			
		default:
			for (GuiTextField field : this.textFields) {
				if (field.isFocused()) {
					field.textboxKeyTyped(c, key);
					int idx = this.textFields.indexOf(field);
					if (idx >= 0 && idx < this.texts.size()) {
						this.texts.set(idx, field.getText());
						if (this.colorPreviewEnabled && idx == this.colorPreviewIndex) {
							this.updatePreviewColor(field.getText());
						}
					}
				}
			}
			break;
		}
	}
}