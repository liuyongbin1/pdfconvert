package pdfflying;

import com.lowagie.text.pdf.PdfWriter;
import dto.PdfDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.xhtmlrenderer.pdf.DefaultPDFCreationListener;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * @description:
 * @FileName: HtmlDToPDFCreationListener
 * @Author: haxi
 * @Date: 2020/10/16 15:32
 **/
@Slf4j
public class HtmlToPdfCreationListener extends DefaultPDFCreationListener {

    private PdfDataDTO pdfDataDTO;

    public HtmlToPdfCreationListener(PdfDataDTO pdfDataDTO) {
        this.pdfDataDTO = pdfDataDTO;
    }


    @Override
    public void preOpen(ITextRenderer iTextRenderer) {
        System.out.println("打开前");
        PdfWriter pdfWriter = iTextRenderer.getWriter();
        PDFHeaderFooter headerFooterBuilder = new PDFHeaderFooter();
        PDFBuilder builder = new PDFBuilder(headerFooterBuilder, pdfDataDTO);
        builder.setFontSize(10);
        pdfWriter.setPageEvent(builder);
    }

    @Override
    public void preWrite(ITextRenderer iTextRenderer, int pageCount) {
        System.out.println("写入前，共" + pageCount + "页");
    }

    @Override
    public void onClose(ITextRenderer iTextRenderer) {
        System.out.println("关闭");
    }
}
