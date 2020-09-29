package pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import dto.PdfDataDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @description: pdf构造类
 * @FileName: PDFBuilder
 * @Author: haxi
 * @Date: 2020/09/29 14:52
 **/
@Slf4j
public class PDFBuilder extends PdfPageEventHelper {

    private String fontFileName;
    private BaseFont bf;
    private Font fontDetail;
    private int fontSize;
    private PdfTemplate template;
    private PdfDataDTO pdfDataDTO;
    private HeaderFooterBuilder headerFooterBuilder;

    private PDFBuilder() {
        this.fontSize = 10;
    }

    public PDFBuilder(HeaderFooterBuilder headerFooterBuilder) {
        this(headerFooterBuilder, (PdfDataDTO) null);
    }

    public PDFBuilder(HeaderFooterBuilder headerFooterBuilder, PdfDataDTO pdfDataDTO) {
        this(headerFooterBuilder, pdfDataDTO, "ping_fang_regular.ttf");
    }

    public PDFBuilder(HeaderFooterBuilder headerFooterBuilder, PdfDataDTO pdfDataDTO, String fontFileName) {
        this(headerFooterBuilder, pdfDataDTO, fontFileName, 12);
    }

    public PDFBuilder(HeaderFooterBuilder headerFooterBuilder, PdfDataDTO pdfDataDTO, String fontFileName, int fontSize) {
        this.fontSize = 10;
        this.headerFooterBuilder = headerFooterBuilder;
        this.pdfDataDTO = pdfDataDTO;
        this.fontFileName = fontFileName;
        this.fontSize = fontSize;
    }

    public void onOpenDocument(PdfWriter writer, Document document) {
        this.template = writer.getDirectContent().createTemplate(50.0F, 50.0F);
    }

    public void onEndPage(PdfWriter writer, Document document) {
        this.addPage(writer, document);
    }

    private void addPage(PdfWriter writer, Document document) {
        if (this.headerFooterBuilder != null) {
            this.initFront();
            this.headerFooterBuilder.writeHeader(writer, document, this.pdfDataDTO, this.fontDetail, this.template);
            this.headerFooterBuilder.writeFooter(writer, document, this.pdfDataDTO, this.fontDetail, this.template);
        }

    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        if (this.headerFooterBuilder != null) {
            this.template.beginText();
            this.template.setFontAndSize(this.bf, (float) this.fontSize);
            String replace = this.headerFooterBuilder.getReplaceOfTemplate(writer, document, this.pdfDataDTO);
            this.template.showText(replace);
            this.template.endText();
            this.template.closePath();
        }

    }

    private void initFront() {
        try {
            if (this.bf == null) {
                this.bf = (new PdfFontProvider()).getBaseFont();
            }

            if (this.fontDetail == null) {
                this.fontDetail = new Font(this.bf, (float) this.fontSize, 0);
            }
        } catch (DocumentException e) {
            log.error("字体初始化失败{}", e);
        } catch (IOException e) {
            log.error("字体初始化失败{}", e);
        }

    }

    public int getPresentFontSize() {
        return this.fontSize;
    }

    public void setPresentFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontFileName() {
        return this.fontFileName;
    }

    public void setFontFileName(String fontFileName) {
        this.fontFileName = fontFileName;
    }
}
