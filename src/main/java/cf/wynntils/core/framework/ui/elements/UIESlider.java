package cf.wynntils.core.framework.ui.elements;

import cf.wynntils.core.framework.enums.MouseButton;
import cf.wynntils.core.framework.rendering.colors.CommonColors;
import cf.wynntils.core.framework.rendering.colors.CustomColor;
import cf.wynntils.core.framework.rendering.textures.Texture;
import cf.wynntils.core.framework.ui.UI;
import net.minecraft.util.math.MathHelper;

public abstract class UIESlider extends UIEClickZone {
    public float min, max, progress, precision;
    public UIEButton sliderButton;
    public CustomColor backColor;
    boolean moving = false; public boolean isMoving() {return moving;}
    int mouseOffset = 0;

    public UIESlider(CustomColor backColor, Texture sliderButton, float anchorX, float anchorY, int offsetX, int offsetY, int width, int height, boolean active, float min, float max, float precision, float progress) {
        super(anchorX, anchorY, offsetX, offsetY, width, height, active, null);
        this.backColor = backColor;
        this.sliderButton = new UIEButton("",sliderButton,anchorX,anchorY,offsetX,offsetY,0,active,null);
        this.sliderButton.clickSound = null;
        this.min = min;
        this.max = max;
        this.progress = progress;
        this.precision = precision;
    }

    public float getValue() {
        float m = 1f / precision;
        return Math.round((max - min) * progress * m) / m + min;
    }

    @Override
    public void release(int mouseX, int mouseY, MouseButton button, UI ui) {
        super.release(mouseX, mouseY, button, ui);
        if(button == MouseButton.LEFT)
            this.moving = false;
    }

    public static class Horizontal extends UIESlider {
        public Horizontal(CustomColor backColor, Texture sliderButton, float anchorX, float anchorY, int offsetX, int offsetY, int width, boolean active, float min, float max, float precision, float progress) {
            super(backColor, sliderButton, anchorX, anchorY, offsetX, offsetY, width, MathHelper.fastFloor(sliderButton.height/3), active, min, max, precision, progress);
        }

        @Override
        public void render(int mouseX, int mouseY) {
            super.render(mouseX, mouseY);
            if(this.backColor != null) drawRect(this.backColor, this.position.getDrawingX(), this.position.getDrawingY(), this.position.getDrawingX() + width, this.position.getDrawingY() + height);
            this.sliderButton.active = this.active;
            this.sliderButton.position.copy(this.position);
            this.sliderButton.position.offsetX = MathHelper.fastFloor(progress * (width - sliderButton.width)) + this.position.offsetX;
            this.sliderButton.position.refresh(screen);
            this.sliderButton.render(this.hovering || this.moving ? this.sliderButton.position.getDrawingX() : mouseX, this.hovering || this.moving ? this.sliderButton.position.getDrawingY() : mouseY);
        }

        @Override
        public void clickMove(int mouseX, int mouseY, MouseButton button, long timeSinceLastClick, UI ui) {
            super.clickMove(mouseX, mouseY, button, timeSinceLastClick, ui);
            if(moving && button == MouseButton.LEFT)
                this.progress = Math.min(1f,Math.max(0f,((float)(mouseX - mouseOffset - this.position.getDrawingX())) / ((float)this.width - this.sliderButton.width)));
        }

        @Override
        public void click(int mouseX, int mouseY, MouseButton button, UI ui) {
            super.click(mouseX, mouseY, button, ui);
            if(button == MouseButton.LEFT && active && hovering) {
                this.moving = true;
                if(mouseX >= this.sliderButton.position.getDrawingX() && mouseX <= this.sliderButton.position.getDrawingX() + this.sliderButton.width) {
                    this.mouseOffset = mouseX - this.sliderButton.position.getDrawingX();
                } else {
                    this.mouseOffset = this.sliderButton.width/2;
                }
                clickMove(mouseX,mouseY,button,-1,ui);
            }
        }
    }

    public static class Vertical extends UIESlider {
        public Vertical(CustomColor backColor, Texture sliderButton, float anchorX, float anchorY, int offsetX, int offsetY, int height, boolean active, float min, float max, float precision, float progress) {
            super(backColor, sliderButton, anchorX, anchorY, offsetX, offsetY, MathHelper.fastFloor(sliderButton.width), height, active, min, max, precision, progress);
        }

        @Override
        public void render(int mouseX, int mouseY) {
            super.render(mouseX, mouseY);
            if(this.backColor != null) drawRect(this.backColor, this.position.getDrawingX(), this.position.getDrawingY(), this.position.getDrawingX() + width, this.position.getDrawingY() + height);
            this.sliderButton.active = this.active;
            this.sliderButton.position.copy(this.position);
            this.sliderButton.position.offsetY = MathHelper.fastFloor(progress * (height - sliderButton.height)) + this.position.offsetY;
            this.sliderButton.position.refresh(screen);
            this.sliderButton.render(this.hovering || this.moving ? this.sliderButton.position.getDrawingX() : mouseX, this.hovering || this.moving ? this.sliderButton.position.getDrawingY() : mouseY);
        }

        @Override
        public void clickMove(int mouseX, int mouseY, MouseButton button, long timeSinceLastClick, UI ui) {
            super.clickMove(mouseX, mouseY, button, timeSinceLastClick, ui);
            if(moving && button == MouseButton.LEFT)
                this.progress = Math.min(1f,Math.max(0f,((float)(mouseY - mouseOffset - this.position.getDrawingY())) / ((float)this.height - this.sliderButton.height)));
        }

        @Override
        public void click(int mouseX, int mouseY, MouseButton button, UI ui) {
            super.click(mouseX, mouseY, button, ui);
            if(button == MouseButton.LEFT && active && hovering) {
                this.moving = true;
                if(mouseY >= this.sliderButton.position.getDrawingY() && mouseY <= this.sliderButton.position.getDrawingY() + this.sliderButton.height) {
                    this.mouseOffset = mouseY - this.sliderButton.position.getDrawingY();
                } else {
                    this.mouseOffset = this.sliderButton.height/2;
                }
                clickMove(mouseX,mouseY,button,-1,ui);
            }
        }
    }
}