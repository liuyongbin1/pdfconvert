package test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.lowagie.text.pdf.BaseFont;
import dto.PdfDataDTO;
import dto.Student;
import lombok.extern.slf4j.Slf4j;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.util.XHtmlMetaToPdfInfoAdapter;
import pdf.PDFBuilder;
import pdf.PDFHeaderFooter;
import pdf.PdfFontProvider;

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

    private static final String filePath01 = "C:\\Users\\hjy20\\Desktop\\pdf\\jieyuexieyi.html";
    private static final String filePath02 = "C:\\Users\\hjy20\\Desktop\\pdf\\ruanjianxukexie.html";
    private static final String filePath03 = "C:\\Users\\hjy20\\Desktop\\pdf\\fukuaiweituoshu.html";
    private static final String filePath04 = "C:\\Users\\hjy20\\Desktop\\pdf\\buchongxieyi.html";
    private static final String filePath05 = "C:\\Users\\hjy20\\Desktop\\pdf\\ruanjianxukexie(fu).html";
    private static final String filePath06 = "D:\\haxi\\pdfconvert\\src\\main\\resources\\test.html";
    private static final String filePath07 = "D:\\haxi\\pdfconvert\\src\\main\\resources\\jieyuexieyi.html";

    public static void main(String[] args) throws Exception {
        long l = System.currentTimeMillis();
        Student student = new Student();
        student.setName("小明");
        student.setSex("男");
        PdfDataDTO<Student> pdfDataDTO = new PdfDataDTO<>();
        pdfDataDTO.setHeaderTitle("模板转PDF文件");
        pdfDataDTO.setContent("主要内容");
        pdfDataDTO.setLogoFile("D:\\haxi\\pdfconvert\\src\\main\\resources\\logo.png");
        pdfDataDTO.setCachetFile("D:\\haxi\\pdfconvert\\src\\main\\resources\\logo.png");
        pdfDataDTO.setData(student);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bf = new BufferedReader(new FileReader(filePath02))) {
            String s = null;
            while ((s = bf.readLine()) != null) {
                stringBuilder.append(s);
            }
//            String templateData = PdfUtil.fillTemplateData(stringBuilder.toString(), pdfDataDTO);
            try (FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\hjy20\\Desktop\\hello.pdf")) {
//                fileOutputStream.write(createOrderContractPdf(stringBuilder.toString(), pdfDataDTO));

                fileOutputStream.write(itextHtmlToPDF(stringBuilder.toString()));
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

    public static byte[] itextHtmlToPDF(String templateData) {

        ByteArrayOutputStream pdfByte = null;
        try {
            Long startTime = System.currentTimeMillis();

            pdfByte = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            ITextFontResolver fontResolver = renderer.getFontResolver();
            // 设置中文字体/宋体
            fontResolver.addFont("/fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            // 方式二/HTML代码字符串方式生成PDF
            // HTML代码字符串
            renderer.setDocumentFromString(templateData);
            renderer.layout();
            renderer.createPDF(pdfByte, false);
            renderer.setListener(new XHtmlMetaToPdfInfoAdapter(renderer.getDocument()));
            renderer.finishPDF();


            Long endTime = System.currentTimeMillis();
            System.out.println("Itext parse Html to Pdf End -> " + (endTime - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != pdfByte) {
                try {
                    pdfByte.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
