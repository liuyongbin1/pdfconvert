package pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import dto.PdfDataDTO;

/**
 * @description: 处理页眉页脚
 * @FileName: HeaderFooterBuilder
 * @Author: haxi
 * @Date: 2020/09/29 14:50
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
    void writeHeader(PdfWriter pdfWriter, Document document, PdfDataDTO pdfDataDTO, Font font, PdfTemplate pdfTemplate);

    /**
     * 写页脚
     *
     * @param pdfWriter
     * @param document
     * @param pdfDataDTO
     * @param font
     * @param pdfTemplate
     */
    void writeFooter(PdfWriter pdfWriter, Document document, PdfDataDTO pdfDataDTO, Font font, PdfTemplate pdfTemplate);

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
