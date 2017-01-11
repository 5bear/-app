package com.springapp.classes;



import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;



/**
 * Created by 11369 on 2016/12/28.
 */
public class LabelUtil {


    /**要生成的二维码的长宽*/
    private static Integer width = 144;

    private static Integer height = 144;

    // 水印透明度
    private static float alpha = 0.5f;

    //水印图片位置
    private static int pos_X = 14;

    private static int pos_Y = 617;

    /** 水印图片旋转角度 */
    private static double degree = -90f;


    private static int interval = 0;

    private static int size = 70;//windows  70
    // 水印文字字体
    private static Font font = new Font("微软雅黑", Font.BOLD, size);
    // 水印文字颜色
    private static Color color = new Color(190,190,190);


    public static void setColor(Integer rgb){
        color = new Color(rgb, rgb, rgb);
    }



    /**
     *
     * @param alpha
     *            水印透明度
     * @param degree
     *            翻转度数
     * @param interval
     *            间隔
     * @param font
     *            水印文字字体
     * @param color
     *            水印文字颜色
     */
    public static void setImageMarkOptions(float alpha, int interval,
                                           int degree, Font font, Color color) {
        if (alpha != 0.0f)
            LabelUtil.alpha = alpha;
        if (interval != 0)
            LabelUtil.interval = interval;
        if (degree != 0)
            LabelUtil.degree = degree;
        if (font != null)
            LabelUtil.font = font;
        if (color != null)
            LabelUtil.color = color;
    }

    /**
     * 给图片添加水印图片
     *
     * @param iconPath
     *            水印图片路径
     * @param srcImgPath
     *            源图片路径
     * @param targetPath
     *            目标图片路径
     */
    public static void markImageByIcon(String iconPath, String srcImgPath,
                                       String targetPath,Integer pos_X, Integer pos_Y) {
        markImageByIcon(iconPath, srcImgPath, targetPath, pos_X, pos_Y, null);
    }

    /**
     * 给图片添加水印图片、可设置水印图片旋转角度
     *
     * @param iconPath
     *            水印图片路径
     * @param srcImgPath
     *            源图片路径
     * @param targetPath
     *            目标图片路径
     * @param degree
     *            水印图片旋转角度
     */
    public static void markImageByIcon(String iconPath, String srcImgPath,
                                       String targetPath,Integer pos_X, Integer pos_Y, Integer degree) {
        OutputStream os = null;
        try {

            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 1、得到画笔对象
            Graphics2D g = buffImg.createGraphics();

            // 2、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g.drawImage(
                    srcImg.getScaledInstance(srcImg.getWidth(null),
                            srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0,
                    null);
            // 3、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2,
                        (double) buffImg.getHeight() / 2);
            }

            // 4、水印图片的路径 水印图片一般为gif或者png的，这样可设置透明度
            ImageIcon imgIcon = new ImageIcon(iconPath);

            // 5、得到Image对象。
            Image img = imgIcon.getImage();

            /*g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    1.0f));*/

            // 6、水印图片的位置
            g.drawImage(img, pos_X , pos_Y ,null);

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            // 7、释放资源
            g.dispose();

            // 8、生成图片
            os = new FileOutputStream(targetPath);
            ImageIO.write(buffImg, "PNG", os);

            System.out.println("图片完成添加水印图片");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给图片添加水印文字
     *
     * @param logoText
     *            水印文字
     * @param srcImgPath
     *            源图片路径
     * @param targerPath
     *            目标图片路径
     */
    public static void markImageByText(String logoText, String srcImgPath,
                                       String targerPath,/*String textParentPath, */Integer count) {
        markImageByText(logoText, srcImgPath, targerPath, /*textParentPath, */count,  0, 0);
    }

    /**
     * 给图片添加水印文字、可设置水印文字的旋转角度
     *
     * @param logoText
     * @param srcImgPath
     * @param targetParentPath 生成文件所在文件夹
     * @param count 生成文件数量
     * @param degree
     * @param interval 间隔
     */
    public static Stack<String> markImageByText(String logoText, String srcImgPath,
                                       String targetParentPath, /*String textParentPath, */Integer count, Integer degree ,Integer interval) {
        if (degree != 0f) {
            LabelUtil.degree = degree;
        }
        if (interval != 0f) {
            LabelUtil.interval = interval;
        }
        Stack<String> stack = new Stack<String>();
        InputStream is = null;
        OutputStream os = null;
        try {
            // 1、源图片
            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(
                    srcImg.getScaledInstance(srcImg.getWidth(null),
                            srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0,
                    null);
/*
            g.translate(-buffImg.getWidth()/2,-buffImg.getHeight()/2);
*/
            // 4、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2,
                        (double) buffImg.getHeight() / 2);
            }
            // 5、设置水印文字颜色
            g.setColor(color);
            // 6、设置水印文字Font
            g.setFont(font);
            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(font);
            Integer logoWidth = fm.stringWidth(logoText);
            Integer logoHeight = fm.getHeight();
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
            for (int height = interval; height < buffImg
                    .getHeight()*2 + logoHeight; height = height +interval+ logoHeight) {
                for (int weight = interval; weight < buffImg
                        .getWidth()*2 + logoWidth; weight = weight +interval+ logoWidth) {
                    g.drawString(logoText, weight - logoWidth, height - logoHeight);
                }
            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            /*for (int height = interval + size; height < buffImg
                    .getHeight(); height = height +interval+ size) {
                for (int weight = interval + size * logoText.length(); weight < buffImg
                        .getWidth(); weight = weight +interval+ size * logoText.length()) {
                    g.drawString(logoText, weight - size * logoText.length(), height
                            - size);
                }
            }*/
            // 9、释放资源
            g.dispose();
            // 10、生成图片 存储文件位置到文本文件
            /*String targetTextPath = textParentPath + "/" + System.currentTimeMillis()/1000 + ".txt";
            FileWriter fileWritter = new FileWriter(targetTextPath,false);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);*/
            while(count>0){
                String fileName = logoText + count + ".png";
                String targetPath = targetParentPath  + "/" + fileName;
                os = new FileOutputStream(targetPath);
                ImageIO.write(buffImg, "PNG", os);
              /*  bufferWritter.write(targetPath);
                bufferWritter.newLine();*/
                stack.push(fileName);
                count--;
            }
            /*bufferWritter.close();*/
            System.out.println("图片完成添加水印文字");
            /*return targetTextPath;*/
            return stack;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /*public static Stack<String> createWaterPicture(String gCode, String targetParentPath, Integer count){}*/

    /**
     * 创建空白文件
     * @param targetParentPath
     * @param width
     * @param height
     * @param count
     * @return
     * @throws IOException
     */
    public static Stack<String> createEmptyPicture(String gCode, String targetParentPath,Integer width, Integer height,Integer from, Integer count) throws IOException {
        Stack<String> stack = new Stack<String>();
        OutputStream os = null;
        Integer imgWidth = width>height?width:height;
        Integer imgHeight = width>height?height:width;
        BufferedImage bufferedImage = new BufferedImage( imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        try {
            while (from <= count) {
                Graphics2D g2 = bufferedImage.createGraphics();
                g2.setBackground(Color.WHITE);
                g2.clearRect(0, 0, imgWidth, imgHeight);
                /*// 4、设置水印旋转
                g2.rotate(Math.toRadians(degree),
                        (double) width / 2,
                        (double) height / 2);*/
                String fileName = from + ".png";
                if(gCode!=null){
                    fileName = from + ".png";
                    // 5、设置水印文字颜色
                    g2.setColor(color);
                    // 6、设置水印文字Font
                    g2.setFont(font);
                    // 7、设置水印文字透明度
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                            1.0f));
                    FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(font);
                    Integer logoWidth = fm.stringWidth(gCode);
                    Integer logoHeight = fm.getHeight();
                    // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
                    /*
                    linux系统
                    for (int y = 10; y < bufferedImage
                            .getHeight()*2 + logoHeight; y = y +interval+ logoHeight) {
                        g2.drawString(gCode, -10, y - logoHeight);//水印位置
                     */
                     /*
                    windows
                     for (int y = 10; y < bufferedImage
                            .getHeight()*2 + logoHeight; y = y +interval+ logoHeight - 12) {
                        g2.drawString(gCode, -10, y - logoHeight);//水印位置
                     */
                    for (int y = 0; y < bufferedImage
                            .getHeight()*2 + logoHeight; y = y + interval+ logoHeight - 12) {
                        g2.drawString(gCode, (imgWidth-logoWidth)/3*2, y + logoHeight/2 + 8);//水印位置
                        /*for (int x = interval; x < bufferedImage
                                .getWidth()*2 + logoWidth; x = x +interval+ logoWidth) {
                            g2.drawString(gCode, x - logoWidth, y - logoHeight);
                        }*/
                    }
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    /*
                     BufferedImage bufferedImage = new BufferedImage(width , height, BufferedImage.TYPE_INT_RGB);
                    FontFamily fontFamily = new FontFamily("宋体");
GraphicsPath path=new GraphicsPath();
//向区域中追加文本，字体大小为60
path.AddString("文字特效",fontFamily,(int)FontStyle.Regular,
60, new Point(0, 0),new StringFormat());

//获取路径的点信息
PointF[] dataPoints = path.PathPoints;
//获取路径的点类型信息
byte [] pTypes = path.PathTypes;

//复制路径
GraphicsPath path2=(GraphicsPath)path.Clone();

//将文本在水平方向上缩小，在垂直方向上不变
Matrix matrix = new Matrix(0.50f, 0.0f, 0.0f, 1f, 0.0f,0.0f);
//对points数据中的每一个成员进行矩形运算
matrix.TransformPoints(dataPoints);
//根据计算后的点重新构造路径
GraphicsPath newpath=new GraphicsPath(dataPoints,pTypes);
//设置文本输出质量
Graphics g = bufferedImage.createGraphics();
g.TextRenderingHint=TextRenderingHint.ClearTypeGridFit;
g.SmoothingMode=SmoothingMode.AntiAlias;
SolidBrush redBrush = new SolidBrush(Color.Red);
//填充路径
g.FillPath(redBrush,newpath);
g.Dispose();
redBrush.Dispose();
OutputStream os = new FileOutputStream("d://test);
                ImageIO.write(bufferedImage, "png", os);
                     */
                }
                String targetPath = targetParentPath + "/" + fileName;
                os = new FileOutputStream(targetPath);
                ImageIO.write(bufferedImage, "png", os);
                stack.push(fileName);
                from++;
            }
            return stack;
        }catch (Exception e){
            System.out.print(e.getMessage());
            return null;
        }
    }


    /**
     * 1.10日修改 创建水印图片
     * @param gCode
     * @param targetParentPath
     * @param width
     * @param height
     * @param from
     * @param count
     * @return
     * @throws IOException
     */
    public static Stack<String> createEmptyPicture1(ArrayList<String> gCode, String targetParentPath, Integer width, Integer height, Integer from, Integer count) throws IOException {
        Stack<String> stack = new Stack<String>();
        OutputStream os = null;
        Integer imgWidth = width>height?width:height; //图片宽度
        Integer imgHeight = width>height?height:width; //图片高度
        BufferedImage bufferedImage = new BufferedImage( imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        try {
            while (from <= count) {
/*
                font = new Font("微软雅黑", Font.BOLD, from*5);
*/
                Graphics2D g2 = bufferedImage.createGraphics();
                g2.setBackground(Color.WHITE);
                g2.clearRect(0, 0, imgWidth, imgHeight);
                String fileName = from + ".png";
                if(gCode!=null){
                    fileName = from + ".png";
                    // 设置水印文字颜色
                    g2.setColor(color);
                    // 设置水印文字Font
                    g2.setFont(font);
                    // 设置水印文字透明度
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                            1.0f));
                    FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(font);
                    Integer logoWidth = fm.stringWidth(gCode.get(from-1));
                    Integer logoHeight = fm.getHeight();
                    // 设置水印位置
                    /*
                    linux系统
                    for (int y = 10; y < bufferedImage
                            .getHeight()*2 + logoHeight; y = y +interval+ logoHeight) {
                        g2.drawString(gCode, -10, y - logoHeight);//水印位置
                     */
                     /*
                    windows
                    for (int y = logoHeight/4*3; y < bufferedImage
                            .getHeight()*2 + logoHeight; y = y + (height-5*logoHeight)/4 + 3) {
                        g2.drawString(gCode, (imgWidth-logoWidth)/3*2, y );//水印位置
                     */
                     /*
                     linux 1.10 version
                      */
                    for (int y = logoHeight/4*3; y < bufferedImage
                            .getHeight()*2 + logoHeight; y = y + (height-5*logoHeight)/5*4) {
                        g2.drawString(gCode.get(from-1), (imgWidth-logoWidth)/3*2, y );//水印位置
                        /*for (int x = interval; x < bufferedImage
                                .getWidth()*2 + logoWidth; x = x +interval+ logoWidth) {
                            g2.drawString(gCode, x - logoWidth, y - logoHeight);
                        }*/
                    }
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                }
                String targetPath = targetParentPath + "/" + fileName;
                os = new FileOutputStream(targetPath);
                ImageIO.write(bufferedImage, "png", os);
                stack.push(fileName);
                from++;
            }
            return stack;
        }catch (Exception e){
            System.out.print(e.getMessage());
            return null;
        }
    }
    public static Stack<String> createEmptyPicture3(String gCode, String targetParentPath,Integer width, Integer height,Integer from, Integer count) throws IOException {
        Stack<String> stack = new Stack<String>();
        OutputStream os = null;
        Integer imgWidth = width>height?width:height;
        Integer imgHeight = width>height?height:width;
        BufferedImage bufferedImage = new BufferedImage( imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        try {
            while (from <= count) {
                Graphics2D g2 = bufferedImage.createGraphics();
                g2.setBackground(Color.WHITE);
                g2.clearRect(0, 0, imgWidth, imgHeight);
                /*// 4、设置水印旋转
                g2.rotate(Math.toRadians(degree),
                        (double) width / 2,
                        (double) height / 2);*/
                String fileName = from + ".png";
                if(gCode!=null){
                    fileName = from + ".png";
                    // 5、设置水印文字颜色
                    g2.setColor(color);
                    // 6、设置水印文字Font
                    g2.setFont(font);
                    // 7、设置水印文字透明度
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                            1.0f));
                    FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(font);
                    Integer logoWidth = fm.stringWidth(gCode);
                    Integer logoHeight = fm.getHeight();
                    // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
                    /*
                    linux系统
                    for (int y = 10; y < bufferedImage
                            .getHeight()*2 + logoHeight; y = y +interval+ logoHeight) {
                        g2.drawString(gCode, -10, y - logoHeight);//水印位置
                     */
                     /*
                    windows
                     for (int y = 10; y < bufferedImage
                            .getHeight()*2 + logoHeight; y = y +interval+ logoHeight - 12) {
                        g2.drawString(gCode, -10, y - logoHeight);//水印位置
                     */
                    for (int y = logoHeight/4*3; y < bufferedImage
                            .getHeight()*2 + logoHeight; y = y + (height-5*logoHeight)/4 + 3) {
                        g2.drawString(gCode, (imgWidth-logoWidth)/3*2, y );//水印位置
                        /*for (int x = interval; x < bufferedImage
                                .getWidth()*2 + logoWidth; x = x +interval+ logoWidth) {
                            g2.drawString(gCode, x - logoWidth, y - logoHeight);
                        }*/
                    }
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    /*
                     BufferedImage bufferedImage = new BufferedImage(width , height, BufferedImage.TYPE_INT_RGB);
                    FontFamily fontFamily = new FontFamily("宋体");
GraphicsPath path=new GraphicsPath();
//向区域中追加文本，字体大小为60
path.AddString("文字特效",fontFamily,(int)FontStyle.Regular,
60, new Point(0, 0),new StringFormat());

//获取路径的点信息
PointF[] dataPoints = path.PathPoints;
//获取路径的点类型信息
byte [] pTypes = path.PathTypes;

//复制路径
GraphicsPath path2=(GraphicsPath)path.Clone();

//将文本在水平方向上缩小，在垂直方向上不变
Matrix matrix = new Matrix(0.50f, 0.0f, 0.0f, 1f, 0.0f,0.0f);
//对points数据中的每一个成员进行矩形运算
matrix.TransformPoints(dataPoints);
//根据计算后的点重新构造路径
GraphicsPath newpath=new GraphicsPath(dataPoints,pTypes);
//设置文本输出质量
Graphics g = bufferedImage.createGraphics();
g.TextRenderingHint=TextRenderingHint.ClearTypeGridFit;
g.SmoothingMode=SmoothingMode.AntiAlias;
SolidBrush redBrush = new SolidBrush(Color.Red);
//填充路径
g.FillPath(redBrush,newpath);
g.Dispose();
redBrush.Dispose();
OutputStream os = new FileOutputStream("d://test);
                ImageIO.write(bufferedImage, "png", os);
                     */
                }
                String targetPath = targetParentPath + "/" + fileName;
                os = new FileOutputStream(targetPath);
                ImageIO.write(bufferedImage, "png", os);
                stack.push(fileName);
                from++;
            }
            return stack;
        }catch (Exception e){
            System.out.print(e.getMessage());
            return null;
        }
    }



    /**
     * 生成指定数量的标签和空白
     * @param gCode 串码
     * @param lCode 箱码
     * @param srcImgPath 模板文件地址
     * @param qrImgParentPath 生成的二维码存储文件夹
     * @param targetParentPath 生成标签存储文件夹
     * @param realCount
     * @param emptyCount
     * @throws IOException
     */
    public static Map<String, Stack<String>> generateLabel(String gCode, String lCode, String srcImgPath, String qrImgParentPath, String targetParentPath,/*String textParentPath, */Integer realCount, Integer emptyCount) throws IOException {
        //生成对应的二维码
        System.out.println("开始生成二维码...");
        BufferedImage buffImg = GenerateQRCode.getQRBufferedImage(lCode ,width, height);
        OutputStream os = null;
        String qrTarget = qrImgParentPath + "/" + System.currentTimeMillis()/1000 + ".png";
        Map<String, Stack<String>> map = new HashMap<String, Stack<String>>();
        try {
            os = new FileOutputStream(qrTarget);
            ImageIO.write(buffImg, "PNG", os);
/*
        String srcImgPath = "d:/template.png";
*/

            Image srcImg = ImageIO.read(new File(srcImgPath));
            //给模板加上文字
            // 给图片添加水印文字,水印文字旋转-45
            System.out.println("开始给图片添加水印文字...");
            Stack<String> labelStack = markImageByText(gCode, srcImgPath, targetParentPath, /*textParentPath, */realCount, 90, 0);
            System.out.println("给图片添加水印文字结束...");

            System.out.println("开始给图片添加二维码...");
            //给模板加上二维码
            while (!labelStack.isEmpty()){
                String path = targetParentPath + "/" + labelStack.pop();
                markImageByIcon(qrTarget, path, path, pos_X, pos_Y);
            }
            System.out.println("给图片添加二维码结束...");

            map.put("label", labelStack);
            System.out.println("生成空白页...");
            Stack<String> emptyStack = createEmptyPicture(null, targetParentPath,srcImg.getWidth(null),srcImg.getHeight(null), realCount, emptyCount);
            map.put("empty", emptyStack);
            return map;
        }catch (Exception e){
            System.out.println(e.getMessage() + "出错了...");
            return null;
        }
    }

    /**
     * 生成指定数量的标签和空白:先创建带水印的空白图片，然后给空白图片加上模板文字，最后再贴上二维码
     * @param gCode 串码
     * @param lCode 箱码
     * @param srcImgPath 模板文件地址
     * @param qrImgParentPath 生成的二维码存储文件夹
     * @param targetParentPath 生成标签存储文件夹
     * @param realCount
     * @param emptyCount
     * @throws IOException
     */
    public static Map<String, Stack<String>> generateLabel2(String gCode, String lCode, String srcImgPath, String qrImgParentPath, String targetParentPath,/*String textParentPath, */Integer realCount, Integer emptyCount) throws IOException {
        //生成对应的二维码
        System.out.println("开始生成二维码...");
        BufferedImage buffImg = GenerateQRCode.getQRBufferedImage(lCode ,width, height);
        OutputStream os = null;
        String qrTarget = qrImgParentPath + "/" + System.currentTimeMillis()/1000 + ".png";
        Map<String, Stack<String>> map = new HashMap<String, Stack<String>>();
        try {
            os = new FileOutputStream(qrTarget);
            ImageIO.write(buffImg, "PNG", os);
/*
        String srcImgPath = "d:/template.png";
*/
            //导入模板文件
            Image srcImg = ImageIO.read(new File(srcImgPath));
            //给模板加上文字
            // 给图片添加水印文字,水印文字旋转-45
            System.out.println("开始创建带水印的图片...");
            Stack<String> labelStack = createEmptyPicture(gCode,  targetParentPath,srcImg.getWidth(null),srcImg.getHeight(null), 1, realCount);
            System.out.println("创建完成...");

            System.out.println("开始给图片添加模板文字...");
            //给模板加上模板
            for (String fileName:labelStack){
                String path = targetParentPath + "/" + fileName;
                markImageByIcon(srcImgPath, path, path, 0, 0);
                markImageByIcon(qrTarget, path, path, pos_X, pos_Y);
            }
            System.out.println("添加模板文字结束...");

           /* System.out.println("开始给图片添加二维码...");
            //给模板加上二维码
            while (!labelStack.isEmpty()){
                String path = targetParentPath + "/" + labelStack.pop();
                markImageByIcon(qrTarget, path, path, pos_X, pos_Y);
            }
            System.out.println("给图片添加二维码结束...");*/

            map.put("label", labelStack);
            System.out.println("生成空白页...");
            Stack<String> emptyStack = createEmptyPicture(null, targetParentPath,srcImg.getWidth(null),srcImg.getHeight(null), realCount + 1, emptyCount + realCount);
            map.put("empty", emptyStack);
            return map;
        }catch (Exception e){
            System.out.println(e.getMessage() + "出错了...");
            return null;
        }
    }

    /**
     * 1.9日修改
     *生成指定数量的标签和空白:先创建带水印的空白图片，然后给空白图片加上模板文字，最后再贴上二维码
     * @param gCode 串码
     * @param srcImgPath 模板文件地址
     * @param targetParentPath 生成标签存储文件夹
     * @param realCount
     * @return
     * @throws IOException
     */
    public static Stack<String> generateLabel3(ArrayList<String> gCode, String srcImgPath, String targetParentPath,/*String textParentPath, */Integer realCount) throws IOException {
        try {
            /*
        String srcImgPath = "d:/template.png";
*/
            //导入模板文件
            Image srcImg = ImageIO.read(new File(srcImgPath));
            //给模板加上文字
            // 给图片添加水印文字,水印文字旋转-45
            System.out.println("开始创建带水印的图片...");
            Stack<String> labelStack = createEmptyPicture1(gCode,  targetParentPath,srcImg.getWidth(null),srcImg.getHeight(null), 1, realCount);
            System.out.println("创建完成...");
            return labelStack;
        }catch (Exception e){
            System.out.println(e.getMessage() + "出错了...");
            return null;
        }
    }


    public static void main(String[] args) throws IOException {
        String file2 = "d://template5.PNG";
        Image srcImg = ImageIO.read(new File(file2));
/*
        markImageByIcon(file2, file1,file1);
*/
        String targetTextPath2 = "d:/label";
/*
        markImageByText("CH12121", file1, targetTextPath2, 1);
*/
        String path = "d:/label/CH1212114831424361.png";
        createEmptyPicture3("CHM56222262", targetTextPath2, srcImg.getWidth(null), srcImg.getHeight(null), 1, 1);
        /*Stack<String> stack = createEmptyPicture("CH12121", targetTextPath2,srcImg.getWidth(null),srcImg.getHeight(null),1);
        while (!stack.empty()){
            String path = stack.pop();
            markImageByIcon(file2, path, file1, pos_X, pos_Y);
        }*/
        //生成对应的二维码
      /*  System.out.println("开始生成二维码...");
        String qrTarget = "d:/qrCode.png";
        BufferedImage buffImg = GenerateQRCode.getQRBufferedImage("12121" ,width, height);
        OutputStream os = null;
        os = new FileOutputStream(qrTarget);
        ImageIO.write(buffImg, "PNG", os);
        String srcImgPath = "d:/template.png";*/
        //给模板加上二维码
       /* System.out.println("开始给图片添加二维码...");
        markImageByIcon(qrTarget, srcImgPath, srcImgPath);*/
        //给模板加上文字
        /*String logoText = "CHM52501178";
        String targetTextPath2 = "d:/label";*/
        // 给图片添加水印文字,水印文字旋转-45
        /*System.out.println("开始给图片添加水印文字...");
        markImageByText(logoText, srcImgPath, targetTextPath2,*//*targetTextPath3,  *//*10, -45, 0);
        System.out.println("给图片添加水印文字结束...");*/
        /*generateLabel(logoText, "50ea9dbb4fcb046848ce3659e6aaed9f", "d:/template.png", targetTextPath2,targetTextPath2,10,10);*/
    }

}
