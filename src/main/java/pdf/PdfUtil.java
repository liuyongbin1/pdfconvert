package pdf;

import com.google.common.collect.Maps;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import dto.PdfDataDTO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @description: pdf处理工具
 * @FileName: PdfUtil
 * @Author: haxi
 * @Date: 2020/09/29 14:55
 **/
@Slf4j
public class PdfUtil {

    public static final String START_TABLE_LIST = "<#list";
    public static final String END_TABLE_LIST = "</#list>";
    private static final String templateName = "template";
    private static final int interval = 50;

    private static final Map<String, Configuration> configurationCache = Maps.newConcurrentMap();

    static {
        Configuration config = new Configuration(Configuration.VERSION_2_3_0);
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setLogTemplateExceptions(false);
        configurationCache.put(templateName, config);
    }

    /**
     * 加盖骑缝章
     *
     * @param pdfByte
     * @param output
     * @param picPath
     * @throws IOException
     * @throws DocumentException
     */
    public static void stamperCheckMarkPDF(byte[] pdfByte, OutputStream output, String picPath) throws IOException, DocumentException {
        try {
            PdfReader reader = new PdfReader(pdfByte);
            PdfStamper stamp = new PdfStamper(reader, output);
            int nums = reader.getNumberOfPages();
            Image[] nImage = ImageUtil.subImages(picPath, nums);

            for (int n = 1; n <= nums; ++n) {
                PdfContentByte over = stamp.getOverContent(n);
                Image img = nImage[n - 1];
                float width = over.getPdfWriter().getPageSize().getWidth();
                img.setAbsolutePosition(width - img.getWidth() - 1.0F, 200.0F);
                over.addImage(img);
            }

            stamp.close();
            reader.close();
        } catch (Exception e) {
            log.error("盖骑缝章出错", e);
        }

    }

    /**
     * 加水印
     *
     * @param pdfByte
     * @param output
     * @param waterMarkName
     */
    public static void waterMark(byte[] pdfByte, OutputStream output, String waterMarkName) {
        try {
            PdfReader reader = new PdfReader(pdfByte);
            PdfStamper stamper = new PdfStamper(reader, output);
            Rectangle pageRect = null;
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.3F);
            gs.setStrokeOpacity(0.4F);
            int total = reader.getNumberOfPages() + 1;
            JLabel label = new JLabel();
            label.setText(waterMarkName);
            FontMetrics metrics = label.getFontMetrics(label.getFont());
            int textH = metrics.getHeight();
            int textW = metrics.stringWidth(label.getText());

            for (int i = 1; i < total; ++i) {
                pageRect = reader.getPageSizeWithRotation(i);
                PdfContentByte under = stamper.getOverContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                under.setFontAndSize((new PdfFontProvider()).getBaseFont(), 20.0F);

                for (int height = 50 + textH; (float) height < pageRect.getHeight(); height += textH * 14) {
                    for (int width = 50 + textW; (float) width < pageRect.getWidth() + (float) textW; width += textW * 6) {
                        under.showTextAligned(0, waterMarkName, (float) (width - textW), (float) (height - textH), 30.0F);
                    }
                }

                under.endText();
            }

            stamper.close();
            reader.close();
        } catch (Exception var16) {
            log.error("pdf加水印出错", var16);
        }

    }

    /**
     * 盖总章
     *
     * @param pdfWriter
     * @param picPath
     * @throws DocumentException
     * @throws IOException
     */
    public static void stampChapter(PdfWriter pdfWriter, String picPath) throws DocumentException, IOException {
        PdfContentByte directContent = pdfWriter.getDirectContent();
        Image image = ImageUtil.getImage(picPath);
        image.scaleAbsolute(120.0F, 120.0F);
        image.setAbsolutePosition(150.0F, 250.0F);
        directContent.addImage(image);
    }

    public static byte[] createOrderContractPdf(String templateData, PdfDataDTO pdfDataDTO) throws IOException {
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

    /**
     * 填充html模板数据
     *
     * @param content
     * @param pdfDataDTO
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public static String fillTemplateData(String content, PdfDataDTO pdfDataDTO) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        Configuration configuration = getConfiguration(templateName);
        Template template = new Template(templateName, new StringReader(content), configuration);
        template.process(pdfDataDTO, writer);
        writer.flush();
        return writer.toString();
    }

    private static Configuration getConfiguration(String templateName) {
        return configurationCache.get(templateName);
    }
}
