package pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import dto.PdfDataDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @description:
 * @FileName: PDFHeaderFooter
 * @Author: haxi
 * @Date: 2020/09/29 14:55
 **/
@Slf4j
public class PDFHeaderFooter implements HeaderFooterBuilder {

    public void writeFooter(PdfWriter writer, Document document, PdfDataDTO pdfDataDTO, Font font, PdfTemplate template) {
        if (pdfDataDTO != null) {
            int pageS = writer.getPageNumber();
            PdfContentByte canvas = writer.getDirectContent();

            try {
                canvas.setFontAndSize((new PdfFontProvider()).getBaseFont(), 10.0F);
            } catch (DocumentException e) {
                log.error("writeFooter -> DocumentException ->", e);
            } catch (IOException e) {
                log.error("writeFooter -> IOException ->", e);
            }

            Phrase lineFooter = new Phrase("_______________________________________________________________________________________", font);
            Phrase footer1 = new Phrase(String.format("第  %d 页", pageS) + "共", font);
            Phrase footer2 = new Phrase("法务部：11111-11111", font);
            ColumnText.showTextAligned(canvas, 0, lineFooter, document.left(), document.top() + 5.0F, 0.0F);
            ColumnText.showTextAligned(canvas, 0, lineFooter, document.left(), document.bottom() - 25.0F, 0.0F);
            ColumnText.showTextAligned(canvas, 1, footer1, 270.0F, document.bottom() - 45.0F, 0.0F);
            ColumnText.showTextAligned(canvas, 2, footer2, document.right(), document.bottom() - 45.0F, 0.0F);
            canvas.addTemplate(template, 295.0F, 35.0F);
        }

    }

    public void writeHeader(PdfWriter writer, Document document, PdfDataDTO pdfDataDTO, Font font, PdfTemplate template) {
        if (pdfDataDTO != null) {
            int pageS = writer.getPageNumber();
            PdfContentByte canvas = writer.getDirectContent();

            try {
                canvas.setFontAndSize((new PdfFontProvider()).getBaseFont(), 10.0F);
            } catch (DocumentException e) {
                log.error("writeHeader -> DocumentException ->", e);
            } catch (IOException e) {
                log.error("writeHeader -> IOException ->", e);
            }

            try {
                Image image = Image.getInstance(pdfDataDTO.getLogoFile());
                image.scaleAbsolute(50.0F, 20.0F);
                image.setAbsolutePosition(80.0F, 775.0F);
                canvas.addImage(image);
            } catch (Exception e) {
                log.error("writeHeader -> Exception ->", e);
            }

            ColumnText.showTextAligned(canvas, 2, new Phrase(pdfDataDTO.getHeaderTitle(), font), document.right(), document.top() + 10.0F, 0.0F);
        }

    }

    public String getReplaceOfTemplate(PdfWriter writer, Document document, PdfDataDTO pdfDataDTO) {
        int total = writer.getPageNumber() - 2;
        return total + " 页";
    }
}
