package pdfflying;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import dto.PdfDataDTO;

/**
 * @description: 处理页眉页脚
 * @FileName: HeaderFooterBuilder
 * @Author: haxi
 * @Date: 2020/10/16 16:35
 **/
public interface HeaderFooterBuilder {

    /**
     * 写页眉
     *
     * @param pdfWriter
     * @param document
     * @param pdfDataDTO
     * @param font
     * @param pdfTemplate
     */
    void writeHeader(PdfWriter pdfWriter, Document document, PdfDataDTO pdfDataDTO, Font font, BaseFont baseFont, PdfTemplate pdfTemplate);

    /**
     * 写页脚
     *
     * @param pdfWriter
     * @param document
     * @param pdfDataDTO
     * @param font
     * @param pdfTemplate
     */
    void writeFooter(PdfWriter pdfWriter, Document document, PdfDataDTO pdfDataDTO, Font font, BaseFont baseFont, PdfTemplate pdfTemplate);

    /**
     * 关闭文档前,获取替换页眉页脚处设置模板的文本
     *
     * @param pdfWriter
     * @param document
     * @param pdfDataDTO
     * @return
     */
    String getReplaceOfTemplate(PdfWriter pdfWriter, Document document, PdfDataDTO pdfDataDTO);
}
