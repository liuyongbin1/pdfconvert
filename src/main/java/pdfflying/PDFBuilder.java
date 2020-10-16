package pdfflying;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import dto.PdfDataDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @description:
 * @FileName: PDFBuilder
 * @Author: haxi
 * @Date: 2020/10/16 16:20
 **/
@Slf4j
public class PDFBuilder extends PdfPageEventHelper {

    private BaseFont bf;
    private Font fontDetail;
    private int fontSize;
    private PdfTemplate template;
    private PdfDataDTO pdfDataDTO;
    private HeaderFooterBuilder headerFooterBuilder;

    public PDFBuilder(PDFHeaderFooter headerFooterBuilder, PdfDataDTO pdfDataDTO) {
        this.headerFooterBuilder = headerFooterBuilder;
        this.pdfDataDTO = pdfDataDTO;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        template = writer.getDirectContent().createTemplate(50.0F, 50.0F);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        this.addPage(writer, document);
    }

    private void addPage(PdfWriter writer, Document document) {
        if (this.headerFooterBuilder != null) {
            this.initFont();
            this.headerFooterBuilder.writeHeader(writer, document, this.pdfDataDTO, this.fontDetail, this.bf, this.template);
            this.headerFooterBuilder.writeFooter(writer, document, this.pdfDataDTO, this.fontDetail, this.bf, this.template);
        }

    }

    @Override
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

    private void initFont() {
        try {
            if (this.bf == null) {
                this.bf = BaseFont.createFont("/fonts/simsun.ttc,0", "Identity-H", false);
            }

            if (this.fontDetail == null) {
                this.fontDetail = new Font(this.bf, (float) this.fontSize, 0);
            }
        } catch (DocumentException e) {
            log.error("字体初始化失败{}", e);
            System.out.println("字体初始化失败:" + e.getMessage());
        } catch (IOException e) {
            log.error("字体初始化失败{}", e);
        }

    }

    public BaseFont getBf() {
        return bf;
    }

    public void setBf(BaseFont bf) {
        this.bf = bf;
    }

    public Font getFontDetail() {
        return fontDetail;
    }

    public void setFontDetail(Font fontDetail) {
        this.fontDetail = fontDetail;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public PdfTemplate getTemplate() {
        return template;
    }

    public void setTemplate(PdfTemplate template) {
        this.template = template;
    }

    public PdfDataDTO getPdfDataDTO() {
        return pdfDataDTO;
    }

    public void setPdfDataDTO(PdfDataDTO pdfDataDTO) {
        this.pdfDataDTO = pdfDataDTO;
    }

    public HeaderFooterBuilder getHeaderFooterBuilder() {
        return headerFooterBuilder;
    }

    public void setHeaderFooterBuilder(HeaderFooterBuilder headerFooterBuilder) {
        this.headerFooterBuilder = headerFooterBuilder;
    }
}
