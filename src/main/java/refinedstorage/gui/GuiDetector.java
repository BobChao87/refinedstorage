package refinedstorage.gui;

import com.google.common.primitives.Ints;
import net.minecraft.client.gui.GuiTextField;
import refinedstorage.api.storage.CompareUtils;
import refinedstorage.container.ContainerDetector;
import refinedstorage.gui.sidebutton.SideButtonCompare;
import refinedstorage.gui.sidebutton.SideButtonDetectorMode;
import refinedstorage.gui.sidebutton.SideButtonType;
import refinedstorage.tile.TileDetector;
import refinedstorage.tile.data.TileDataManager;

import java.io.IOException;

public class GuiDetector extends GuiBase {
    public static GuiTextField AMOUNT;

    public GuiDetector(ContainerDetector container) {
        super(container, 176, 137);
    }

    @Override
    public void init(int x, int y) {
        addSideButton(new SideButtonType(this, TileDetector.TYPE));

        addSideButton(new SideButtonDetectorMode(this));

        addSideButton(new SideButtonCompare(this, TileDetector.COMPARE, CompareUtils.COMPARE_DAMAGE));
        addSideButton(new SideButtonCompare(this, TileDetector.COMPARE, CompareUtils.COMPARE_NBT));

        AMOUNT = new GuiTextField(0, fontRendererObj, x + 62 + 1, y + 23 + 1, 29, fontRendererObj.FONT_HEIGHT);
        AMOUNT.setText(String.valueOf(TileDetector.AMOUNT.getValue()));
        AMOUNT.setEnableBackgroundDrawing(false);
        AMOUNT.setVisible(true);
        AMOUNT.setTextColor(16777215);
        AMOUNT.setCanLoseFocus(true);
        AMOUNT.setFocused(false);
    }

    @Override
    public void update(int x, int y) {
    }

    @Override
    public void drawBackground(int x, int y, int mouseX, int mouseY) {
        bindTexture("gui/detector.png");

        drawTexture(x, y, 0, 0, width, height);

        AMOUNT.drawTextBox();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        drawString(7, 7, t("gui.refinedstorage:detector"));
        drawString(7, 43, t("container.inventory"));
    }

    @Override
    protected void keyTyped(char character, int keyCode) throws IOException {
        if (!checkHotbarKeys(keyCode) && AMOUNT.textboxKeyTyped(character, keyCode)) {
            Integer result = Ints.tryParse(AMOUNT.getText());

            if (result != null) {
                TileDataManager.setParameter(TileDetector.AMOUNT, result);
            }
        } else {
            super.keyTyped(character, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        AMOUNT.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
