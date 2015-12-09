
package org.trimps.images.analyzer.core.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;

import org.apache.commons.lang3.StringUtils;
import org.trimps.images.analyzer.core.font.FontManager;


public class FixDrawTextItem extends DrawTextItem {

    static final Color FONT_COLOR                 = new Color(255, 255, 255, 115);
    static final Color FONT_SHADOW_COLOR          = new Color(170, 170, 170, 77);
    static final Font  FONT                       = FontManager.getFont("方正黑体");
    static final float DEFAULT_TEXT_WIDTH_PERCENT = 0.85F;

    public enum Position {
        CENTER, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    protected float    textWidthPercent;
    protected Position position;
    
    
    public FixDrawTextItem(String text){
        super(text, FONT_COLOR, FONT_SHADOW_COLOR, FONT, MIN_FONT_SIZE);
        this.position = Position.CENTER;
        this.textWidthPercent = DEFAULT_TEXT_WIDTH_PERCENT;
    }

    
    public FixDrawTextItem(String text, Position position, float textWidthPercent){
        super(text, FONT_COLOR, FONT_SHADOW_COLOR, FONT, MIN_FONT_SIZE);
        this.position = position;
        this.textWidthPercent = textWidthPercent;
    }

    
    public FixDrawTextItem(String text, Color fontColor, Color fontShadowColor, Font font, int minFontSize,
                           Position position, float textWidthPercent){
        super(text, fontColor, fontShadowColor, font, minFontSize);
        this.position = position;
        this.textWidthPercent = textWidthPercent;
    }

    
    @Override
    public void drawText(Graphics2D graphics, int width, int height) {
        if(StringUtils.isBlank(text)) {
            return ;
        }
        
        int x = 0, y = 0;
        int fontsize = 1;
        if (position == Position.CENTER) {
            // 计算水印文字总长度
            int textLength = (int) (width * textWidthPercent);
            // 计算水印字体大小
            fontsize = textLength / text.length();
            // 太小了.....不显示
            if (fontsize < minFontSize) {
                return;
            }

            float fsize = (float)fontsize;
            Font font = defaultFont.deriveFont(fsize);
            graphics.setFont(font);
            FontRenderContext context = graphics.getFontRenderContext();
            int sw = (int) font.getStringBounds(text, context).getWidth();

            // 计算字体的坐标
            x = (width - sw) / 2;
            y = height / 2 + fontsize / 2;
        } else if (position == Position.TOP_LEFT) {
            fontsize = ((int)(width * textWidthPercent)) / text.length();
            if (fontsize < minFontSize) {
                return;
            }

            float fsize = (float)fontsize;
            Font font = defaultFont.deriveFont(fsize);
            graphics.setFont(font);

            x = fontsize;
            y = fontsize * 2;
        } else if (position == Position.TOP_RIGHT) {
            fontsize = ((int)(width * textWidthPercent)) / text.length();
            if (fontsize < minFontSize) {
                return;
            }

            float fsize = (float)fontsize;
            Font font = defaultFont.deriveFont(fsize);
            graphics.setFont(font);
            FontRenderContext context = graphics.getFontRenderContext();
            int sw = (int) font.getStringBounds(text, context).getWidth();

            x = width - sw - fontsize;
            y = fontsize * 2;
        } else if (position == Position.BOTTOM_LEFT) {
            fontsize = ((int)(width * textWidthPercent)) / text.length();
            if (fontsize < minFontSize) {
                return;
            }

            float fsize = (float)fontsize;
            Font font = defaultFont.deriveFont(fsize);
            graphics.setFont(font);

            x = fontsize / 2;
            y = height - fontsize;
        } else if (position == Position.BOTTOM_RIGHT) {
            fontsize = ((int)(width * textWidthPercent)) / text.length();
            if (fontsize < minFontSize) {
                return;
            }
            
            float fsize = (float)fontsize;
            Font font = defaultFont.deriveFont(fsize);
            graphics.setFont(font);
            FontRenderContext context = graphics.getFontRenderContext();
            int sw = (int) font.getStringBounds(text, context).getWidth();

            x = width - sw - fontsize;
            y = height - fontsize;
        } else {
            throw new IllegalArgumentException("Unknown position : " + position);
        }

        if(x <= 0 || y <= 0) {
            return ;
        }
        
        if (fontShadowColor != null) {
            graphics.setColor(fontShadowColor);
            graphics.drawString(text, x + getShadowTranslation(fontsize), y + getShadowTranslation(fontsize));
        }

        graphics.setColor(fontColor);
        graphics.drawString(text, x, y);
    }

    
    
    public float getTextWidthPercent() {
        return textWidthPercent;
    }

    
    
    public Position getPosition() {
        return position;
    }

    
    
    public void setTextWidthPercent(float textWidthPercent) {
        this.textWidthPercent = textWidthPercent;
    }

    
    
    public void setPosition(Position position) {
        this.position = position;
    }
}
