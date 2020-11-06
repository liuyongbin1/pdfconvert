package test;

import com.lowagie.text.pdf.BaseFont;
import dto.PdfDataDTO;
import dto.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final String filePath05 = "C:\\Users\\hjy20\\Desktop\\pdf\\ruanjianxukexiefu.html";
    private static final String filePath06 = "D:\\haxi\\pdfconvert\\src\\main\\resources\\test.html";
    private static final String filePath07 = "D:\\haxi\\pdfconvert\\src\\main\\resources\\jieyuexieyi.html";

    public static void main(String[] args) throws Exception {
        long l = System.currentTimeMillis();
        Student student = new Student();
        student.setName("小明");
        student.setSex("男");
        student.setPrice(BigDecimal.valueOf(8325.36).toString());
        PdfDataDTO<Student> pdfDataDTO = new PdfDataDTO<>();
        pdfDataDTO.setHeaderTitle("模板转PDF文件");
        pdfDataDTO.setContent("主要内容");
        pdfDataDTO.setLogoFile("D:\\haxi\\pdfconvert\\src\\main\\resources\\logo.png");
        pdfDataDTO.setCachetFile("D:\\haxi\\pdfconvert\\src\\main\\resources\\logo.png");
        pdfDataDTO.setData(student);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bf = new BufferedReader(new FileReader("ruanjianxukexie.html"))) {
            String s = null;
            while ((s = bf.readLine()) != null) {
                stringBuilder.append(s);
            }
            String content = stringBuilder.toString();
            content = subTemplate(content);
//            String greedPattern = greedPattern("<#list", "</#list>", content);
//            if (greedPattern != null) {
//                content = content.replace("${table}", greedPattern);
//            }
//            content = PdfUtil.fillTemplateData(content, pdfDataDTO);
            try (FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\hjy20\\Desktop\\hello.pdf")) {
//                fileOutputStream.write(PdfUtil.createOrderContractPdf(content, pdfDataDTO));

                fileOutputStream.write(itextHtmlToPDF(content, pdfDataDTO));
            }
        }
        System.out.println(System.currentTimeMillis() - l);

    }

    private static String greedPattern(String first, String second, String target) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\\").append(first).append("(.*)\\").append(second);
        Pattern p = Pattern.compile(stringBuilder.toString());
        Matcher m = p.matcher(target);
        return m.find() ? m.group() : null;
    }

    private static String subTemplate(String target) {
        if (StringUtils.isEmpty(target)) {
            return target;
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (target.contains("<#list") && target.contains("</#list>")) {
            stringBuilder.append(StringUtils.substringBefore(target, "<#list")).append("${table}").append(StringUtils.substringAfterLast(target, "</#list>"));
        } else {
            stringBuilder.append(target);
        }
        return stringBuilder.toString();
    }

    public static byte[] itextHtmlToPDF(String templateData, PdfDataDTO pdfDataDTO) {

        ByteArrayOutputStream pdfByte = null;
        try {
            Long startTime = System.currentTimeMillis();

            pdfByte = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            ITextFontResolver fontResolver = renderer.getFontResolver();
            // 设置中文字体/宋体
            fontResolver.addFont("fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            // 方式二/HTML代码字符串方式生成PDF
            // HTML代码字符串
            renderer.setDocumentFromString(templateData);
//            renderer.setListener(new HtmlToPdfCreationListener(pdfDataDTO));
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
