package pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @description: 设置字体类
 * @FileName: PdfFontProvider
 * @Author: haxi
 * @Date: 2020/09/29 14:53
 **/
@Slf4j
public class PdfFontProvider extends XMLWorkerFontProvider {

    public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color) {
        BaseFont bf = null;

        try {
            bf = this.getBaseFont();
        } catch (DocumentException e) {
            log.error("获取基础字体格式失败", e);
        } catch (IOException e) {
            log.error("获取基础字体格式失败", e);
        }

        Font font = new Font(bf, size, style, color);
        font.setColor(color);
        return font;
    }

    public BaseFont getBaseFont() throws IOException, DocumentException {
        return BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    }
}
