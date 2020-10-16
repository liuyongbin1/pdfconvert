package pdfflying;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.*;
import dto.PdfDataDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @FileName: PDFHeaderFooter
 * @Author: haxi
 * @Date: 2020/10/16 16:36
 **/
@Slf4j
public class PDFHeaderFooter implements HeaderFooterBuilder {

    @Override
    public void writeHeader(PdfWriter pdfWriter, Document document, PdfDataDTO pdfDataDTO, Font font, BaseFont baseFont, PdfTemplate pdfTemplate) {
        if (pdfDataDTO != null) {
            int pageNumber = pdfWriter.getPageNumber();
            log.info("开始创建第{}页", pageNumber);
            System.out.println("开始创建第" + pageNumber + "页");
            PdfContentByte canvas = pdfWriter.getDirectContent();

            canvas.setFontAndSize(baseFont, 10.0F);

            Phrase lineFooter = new Phrase("_______________________________________________________________________________________", font);
            Phrase footer1 = new Phrase(String.format("第  %d 页", pageNumber) + "共", font);
            Phrase footer2 = new Phrase("法务部：11111-11111", font);
            ColumnText.showTextAligned(canvas, 0, lineFooter, document.left(), document.top() + 5.0F, 0.0F);
            ColumnText.showTextAligned(canvas, 0, lineFooter, document.left(), document.bottom() - 25.0F, 0.0F);
            ColumnText.showTextAligned(canvas, 1, footer1, 270.0F, document.bottom() - 45.0F, 0.0F);
            ColumnText.showTextAligned(canvas, 2, footer2, document.right(), document.bottom() - 45.0F, 0.0F);
            canvas.addTemplate(pdfTemplate, 295.0F, 35.0F);

        }
    }

    @Override
    public void writeFooter(PdfWriter pdfWriter, Document document, PdfDataDTO pdfDataDTO, Font font, BaseFont baseFont, PdfTemplate pdfTemplate) {
        if (pdfDataDTO != null) {
            PdfContentByte canvas = pdfWriter.getDirectContent();
            canvas.setFontAndSize(baseFont, 10.0F);

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

    @Override
    public String getReplaceOfTemplate(PdfWriter pdfWriter, Document document, PdfDataDTO pdfDataDTO) {
        int total = pdfWriter.getPageNumber() - 2;
        System.out.println("总页数：" + total);
        return total + " 页";
    }
}
