package pdf;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @description: 图片处理工具
 * @FileName: ImageUtil
 * @Author: haxi
 * @Date: 2020/09/29 14:52
 **/
@Slf4j
public class ImageUtil {

    /**
     * 根据图片路径创建Image
     *
     * @param imgPath
     * @return
     * @throws IOException
     * @throws BadElementException
     */
    public static Image getImage(String imgPath) throws IOException, BadElementException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedImage img = ImageIO.read(new File(imgPath));
        ImageIO.write(img, imgPath.substring(imgPath.lastIndexOf(46) + 1), out);
        Image image = Image.getInstance(out.toByteArray());
        out.flush();
        out.reset();
        return image;
    }

    /**
     * 切割图片
     *
     * @param imgPath
     * @param n
     * @return
     * @throws IOException
     * @throws BadElementException
     */
    public static Image[] subImages(String imgPath, int n) throws IOException, BadElementException {
        Image[] nImage = new Image[n];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedImage img = ImageIO.read(new File(imgPath));
        img = resizeBufferedImage(img, 0, 0, 120, 120);
        int h = img.getHeight();
        int w = img.getWidth();
        int sw = w / n;

        for (int i = 0; i < n; ++i) {
            BufferedImage subImg;
            if (i == n - 1) {
                subImg = img.getSubimage(i * sw, 0, w - i * sw, h);
            } else {
                subImg = img.getSubimage(i * sw, 0, sw, h);
            }

            ImageIO.write(subImg, imgPath.substring(imgPath.lastIndexOf(46) + 1), out);
            nImage[i] = Image.getInstance(out.toByteArray());
            out.flush();
            out.reset();
        }

        return nImage;
    }

    private static BufferedImage resizeBufferedImage(BufferedImage bufferedImage, int x, int y, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, 2);
        Graphics2D graphics = img.createGraphics();
        img = graphics.getDeviceConfiguration().createCompatibleImage(width, height, 3);
        graphics.dispose();
        graphics = img.createGraphics();
        graphics.setComposite(AlphaComposite.Src);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.fill(new RoundRectangle2D.Float((float) x, (float) y, (float) width, (float) height, (float) width, (float) height));
        graphics.setComposite(AlphaComposite.SrcAtop);
        graphics.drawImage(bufferedImage, x, y, width, height, (ImageObserver) null);
        graphics.dispose();
        return img;
    }
}
