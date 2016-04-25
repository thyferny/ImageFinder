
package in.thyferny.images.core.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;

import org.apache.commons.lang3.StringUtils;


public class CornerDrawTextItem extends FixDrawTextItem {

    static final float DEFAULT_CORNER_TEXT_WIDTH_PER = 0.5f;

    public CornerDrawTextItem(String text){
        super(text, FONT_COLOR, FONT_SHADOW_COLOR, FONT, MIN_FONT_SIZE, null, DEFAULT_CORNER_TEXT_WIDTH_PER);
    }

    public CornerDrawTextItem(String text, float textWidthPercent){
        super(text, FONT_COLOR, FONT_SHADOW_COLOR, FONT, MIN_FONT_SIZE, null, textWidthPercent);
    }

    
    public CornerDrawTextItem(String text, Color fontColor, Color fontShadowColor, Font font, int minFontSize,
                              float textWidthPercent){
        super(text, fontColor, fontShadowColor, font, minFontSize, null, textWidthPercent);
    }

    
    @Override
    public void drawText(Graphics2D graphics, int width, int height) {
        if(StringUtils.isBlank(text)) {
            return ;
        }
        
        int x = 0, y = 0;
        // 计算水印文字总长度
        int textLength = (int) (width * textWidthPercent);
        // 计算水印字体大小
        int fontsize = textLength / text.length();
        // 太小了.....不显示
        if (fontsize < minFontSize) {
            return ;
        }

        float fsize = (float)fontsize;
        Font font = defaultFont.deriveFont(fsize);
        graphics.setFont(font);
        FontRenderContext context = graphics.getFontRenderContext();
        int sw = (int) font.getStringBounds(text, context).getWidth();

        // 计算字体的坐标
        if (width > height) {
            y = height / 4;
        } else {
            y = width / 4;
        }

        int halflen = sw / 2;
        if (halflen <= (y - fontsize)) {
            x = y - halflen;
        } else {
            x = fontsize;
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

        if (width > height) {
            y = height - (height / 4);
        } else {
            y = height - (width / 4);
        }

        halflen = sw / 2;
        if (halflen <= (height - y - fontsize)) {
            x = width - (height - y) - halflen;
        } else {
            x = width - sw - fontsize;
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

}
