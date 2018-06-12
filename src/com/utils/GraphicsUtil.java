package com.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * <li> 印章生成工具</li>
 * <p> 返回印章的base64 </p>
 *
 * @author zsf
 * @version 1.0
 */
public class GraphicsUtil {

    public static final int RADIUS_OF_CIRCLE = 26;//圆半径
    public static int WIDTH_OF_CANVAS = 165;//画布宽
    public static int HEIGHT_OF_CANVAS = 165;//画布高

    public static void main(String[] args) {
        String savepath = "C:\\Users\\Administrator\\Desktop\\seal.png";
        GraphicsUtil.generateSeal("深圳金鹰科技有限公司", savepath);
    }

    /**
     * 返回Base64有
     *
     * @param companyName
     * @return
     */
    public static String generateSeal(String companyName, String savepath) {

        int canvasWidth = WIDTH_OF_CANVAS;
        int canvasHeight = HEIGHT_OF_CANVAS;

        double lineArc = 80 * (Math.PI / 180);//角度转弧度 角度 * π/180 = 角度对应的弧度值

        String head = companyName;
        BufferedImage image = GraphicsUtil.getSeal(head, canvasWidth, canvasHeight, lineArc);
        makeImageTranslucentAndSave(image, savepath);//图片背景透明化处理
        return FileStreamTool.getImage(savepath);//图片Base64
    }

    /**
     * 图片背景透明化，并保存
     *
     * @return
     */
    public static void makeImageTranslucentAndSave(BufferedImage image, String filePath) {
        try {
            ImageIcon imageIcon = new ImageIcon(image);
            BufferedImage bufferedImage = new BufferedImage(imageIcon
                    .getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon
                    .getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);

                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    }
                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            ImageIO.write(bufferedImage, "png", new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static BufferedImage getSeal(String head/*,String center,String foot*/, int canvasWidth, int canvasHeight, double lineArc) {
        BufferedImage bi = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = bi.createGraphics();
        /* 消除java.awt.Font字体的锯齿 */
        bi = g2d.getDeviceConfiguration().createCompatibleImage(canvasWidth, canvasHeight, Transparency.TRANSLUCENT);

        g2d.dispose();
        g2d = bi.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //设置画笔
        g2d.fillRect(0, 0, canvasWidth, canvasWidth);

        int circleRadius = Math.min(canvasWidth, canvasHeight) / 2;
        /***********draw circle*************/
        g2d.setPaint(Color.red);
        g2d.setStroke(new BasicStroke(4));//设置画笔的粗度
        Shape circle = new Arc2D.Double(2, 2, circleRadius * 2 - 5, circleRadius * 2 - 5, 0, 360, Arc2D.OPEN);
        g2d.draw(circle);

        int x0 = (int) (canvasWidth / 2);
        int y0 = (int) (canvasHeight / 2);
        int r = RADIUS_OF_CIRCLE;
        double ch = 72 * Math.PI / 180;// 圆心角的弧度数
        int x1 = x0;//x0,y0为圆心， x1 y1等为圆上的点
        int x2 = (int) (x0 - Math.sin(ch) * r);
        int x3 = (int) (x0 + Math.sin(ch) * r);
        int x4 = (int) (x0 - Math.sin(ch / 2) * r);
        int x5 = (int) (x0 + Math.sin(ch / 2) * r);

        int y1 = y0 - r;
        int y2 = (int) (y0 - Math.cos(ch) * r);
        int y3 = y2;
        int y4 = (int) (y0 + Math.cos(ch / 2) * r);
        int y5 = y4;

        int bx = (int) (x0 + Math.cos(ch) * Math.tan(ch / 2) * r);
        int by = y2;

        Polygon a = new Polygon();
        Polygon b = new Polygon();

        a.addPoint(x2, y2);
        a.addPoint(x5, y5);
        a.addPoint(bx, by);
        b.addPoint(x1, y1);
        b.addPoint(bx, by);
        b.addPoint(x3, y3);
        b.addPoint(x4, y4);

        g2d.fillPolygon(a);
        g2d.fillPolygon(b);


        /***************draw string head**************/

        int fontSize = 18;
        if (head.length() <= 10) {
            fontSize = 26;
        } else if (head.length() <= 15) {
            fontSize = 22;
        } else if (head.length() <= 19) {
            fontSize = 18;
        } else if (head.length() <= 20) {
            fontSize = 18;
        } else if (head.length() <= 23) {
            fontSize = 16;
        } else if (head.length() <= 25) {
            fontSize = 15;
        } else if (head.length() <= 27) {
            fontSize = 14;
        } else {
            fontSize = 13;
        }


//       Font f = new Font("楷体", Font.PLAIN, fontSize);
        InputStream resourceAsStream = GraphicsUtil.class.getClassLoader().getResourceAsStream("com/mytff/fzlt.ttf");
        Font f = FontUtil.getFont(fontSize, resourceAsStream, Font.PLAIN);
        FontRenderContext context = g2d.getFontRenderContext();
        Rectangle2D bounds = f.getStringBounds(head, context);
        context = g2d.getFontRenderContext();

        double msgWidth = bounds.getWidth();
        int countOfMsg = head.length();
        double interval = msgWidth / (countOfMsg - 1);//计算间距


        double newRadius = circleRadius + bounds.getY() - 9;//bounds.getY()是负数，这样可以将弧形文字固定在圆内了。-5目的是离圆环稍远一点
        double radianPerInterval = 2.3 * Math.asin(interval / (2.5 * newRadius));//每个间距对应的角度

        //第一个元素的角度
        double firstAngle;
        if (countOfMsg % 2 == 1) {//奇数
            firstAngle = (countOfMsg - 1) * radianPerInterval / 2.0 + Math.PI / 2 + 0.08;
        } else {//偶数
            firstAngle = (countOfMsg / 2.0 - 1) * radianPerInterval + radianPerInterval / 2.0 + Math.PI / 2 + 0.08;
        }

        for (int i = 0; i < countOfMsg; i++) {
            double aa = firstAngle - i * radianPerInterval;
            double ax = newRadius * Math.sin(Math.PI / 2 - aa);//小小的trick，将【0，pi】区间变换到[pi/2,-pi/2]区间
            double ay = newRadius * Math.cos(aa - Math.PI / 2);//同上类似，这样处理就不必再考虑正负的问题了
            AffineTransform transform = AffineTransform.getRotateInstance(Math.PI / 2 - aa);// ,x0 + ax, y0 + ay);
            Font f2 = f.deriveFont(transform);
            g2d.setFont(f2);
            g2d.drawString(head.substring(i, i + 1), (float) (circleRadius + ax), (float) (circleRadius - ay));
        }

        g2d.dispose();//销毁资源
        return bi;
    }
}