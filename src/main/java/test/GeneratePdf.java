package test;

import com.lowagie.text.pdf.BaseFont;
import dto.PdfDataDTO;
import dto.Student;
import lombok.extern.slf4j.Slf4j;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import pdfflying.HtmlToPdfCreationListener;

import java.io.*;
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
//                fileOutputStream.write(PdfUtil.createOrderContractPdf(stringBuilder.toString(), pdfDataDTO));

                fileOutputStream.write(itextHtmlToPDF(stringBuilder.toString(), pdfDataDTO));
            }
        }
        System.out.println(System.currentTimeMillis() - l);

    }

    public static byte[] itextHtmlToPDF(String templateData, PdfDataDTO pdfDataDTO) {

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
            renderer.setListener(new HtmlToPdfCreationListener(pdfDataDTO));
            renderer.layout();
            renderer.createPDF(pdfByte);
//            renderer.finishPDF();


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
}
