package test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import dto.PdfDataDTO;
import lombok.extern.slf4j.Slf4j;
import pdf.PDFBuilder;
import pdf.PDFHeaderFooter;
import pdf.PdfFontProvider;
import pdf.PdfUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

/**
 * @description:
 * @FileName: GeneratePdf
 * @Author: haxi
 * @Date: 2020/09/29 14:57
 **/
@Slf4j
public class GeneratePdf {

    private static final DecimalFormat df = new DecimalFormat(",##0.00");

    public static void main(String[] args) throws Exception {
        long l = System.currentTimeMillis();
        PdfDataDTO pdfDataDTO = new PdfDataDTO();
        pdfDataDTO.setHeaderTitle("模板转PDF文件");
        pdfDataDTO.setContent("主要内容");
        pdfDataDTO.setLogoFile("");
        pdfDataDTO.setCachetFile("");
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bf = new BufferedReader(new FileReader("D:\\haxi\\pdfconvert\\src\\main\\resources\\test.html"))) {
            String s = null;
            while ((s = bf.readLine()) != null) {
                stringBuilder.append(s.trim());
            }
//            System.out.println(stringBuilder.toString());
            String templateData = PdfUtil.fillTemplateData(stringBuilder.toString(), pdfDataDTO);
            try (FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\hjy20\\Desktop\\hello.pdf")) {
                fileOutputStream.write(createOrderContractPdf(templateData, pdfDataDTO));
            }
        }
        System.out.println(System.currentTimeMillis() - l);

    }

    private static byte[] createOrderContractPdf(String templateData, PdfDataDTO pdfDataDTO) throws IOException {
        Document document = new Document(PageSize.A4);
        document.setMargins(78.0F, 80.0F, 72.0F, 80.0F);
        ByteArrayOutputStream pdfByte = null;
        PdfWriter pdfWriter = null;

        try {
            pdfByte = new ByteArrayOutputStream();
            pdfWriter = PdfWriter.getInstance(document, pdfByte);
            PDFHeaderFooter headerFooterBuilder = new PDFHeaderFooter();
            PDFBuilder builder = new PDFBuilder(headerFooterBuilder, pdfDataDTO);
            builder.setPresentFontSize(10);
            pdfWriter.setPageEvent(builder);
            convertToPDF(pdfWriter, document, templateData, pdfDataDTO);
            // 盖骑缝章
//            PdfUtil.stamperCheckMarkPDF(pdfByte.toByteArray(), pdfByte, pdfDataDTO.getCachetFile());
            // 加水印
//            PdfUtil.waterMark(pdfByte.toByteArray(), pdfByte, "加水印");
        } catch (DocumentException e) {
            log.error("生成文档异常", e);
        } finally {
            if (pdfByte != null) {
                pdfByte.close();
            }
            if (pdfWriter != null) {
                pdfWriter.close();
            }

        }

        return pdfByte.toByteArray();
    }

    private static void convertToPDF(PdfWriter pdfWriter, Document document, String templateData, PdfDataDTO pdfDataDTO) {
        document.open();
        try {
            XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, new ByteArrayInputStream(templateData.getBytes()), (InputStream) null, StandardCharsets.UTF_8, new PdfFontProvider());
            // 盖总章
//            PdfUtil.stampChapter(pdfWriter, pdfDataDTO.getCachetFile());
        } catch (IOException e) {
            log.error("convertToPDF -> IOException ->", e);
        } catch (Exception e) {
            log.error("convertToPDF -> Exception ->", e);
        } finally {
            document.close();
        }

    }
}
