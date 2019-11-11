
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.log;
import static java.lang.Math.sin;
import java.lang.Math.*;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.tanh;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import sun.jvm.hotspot.oops.java_lang_Class;

//...

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author icema
 */
public class MyFrame extends javax.swing.JFrame {

    /**
     * Creates new form MyFrame
     */
    public MyFrame() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        Image img = null;
    }

    /**
     * Produces an histogram of the given image. In order that this method works
     * properly the image must be in grayscale
     *
     * @param i recives an image
     * @return an image that it is the histogram of the given image
     */
    private BufferedImage myHistogram(BufferedImage i) {
        int x = i.getWidth();
        int y = i.getHeight();
        int maxY = 0;
        int[] valores = new int[256];
        Color c;
        for (int j = 0; j < x; j++) {
            for (int k = 0; k < y; k++) {
                c = new Color(i.getRGB(j, k));
                valores[c.getBlue()] = valores[c.getBlue()] + 1;
                maxY = (valores[c.getBlue()] > maxY) ? valores[c.getBlue()] : maxY;
            }
        }
        BufferedImage res = new BufferedImage(1024, maxY + 1, i.getType());
        for (int j = 0; j < maxY; j++) {
            for (int k = 0; k < 1024; k++) {
                res.setRGB(k, j, new Color(255, 255, 255).getRGB());
            }
        }
        for (int j = 0; j < 256; j++) {
            res.setRGB((j * 4), valores[j], new Color(0, 0, 0).getRGB());
            res.setRGB((j * 4) + 1, valores[j], new Color(0, 0, 0).getRGB());
            res.setRGB((j * 4) + 2, valores[j], new Color(0, 0, 0).getRGB());
            res.setRGB((j * 4) + 3, valores[j], new Color(0, 0, 0).getRGB());
        }
//        File outp = new File("Histogram.jpg");
//        try {
//            ImageIO.write(res, "jpg", outp);
//        } catch (IOException iOException) {
//            System.out.println(iOException.toString());
//        }
        return res;
    }

    /**
     * Produces a normalized histogram of the given image. In order that this
     * method works properly the image must be in grayscale
     *
     * @param i a buffered image
     * @return a buffered image that is the histogram of the given image
     */
    private BufferedImage NHistogram_GS(BufferedImage i) {
        int x = i.getWidth();
        int y = i.getHeight();
        int rImgH = 200;
        double maxY = 0;
        double[] values = new double[256];
        double[] probability = new double[256];
        Color c;
        for (int j = 0; j < x; j++) {
            for (int k = 0; k < y; k++) {
                c = new Color(i.getRGB(j, k));
                values[c.getBlue()] = values[c.getBlue()] + 1;
            }
        }
        for (int j = 0; j < 256; j++) {
            probability[j] = (values[j] / (x * y));
            maxY = (probability[j] > maxY) ? probability[j] : maxY;
        }
        double sf = (rImgH / maxY);
        BufferedImage res = new BufferedImage(1024, rImgH + 1, i.getType());
        for (int j = 0; j < rImgH; j++) {
            for (int k = 0; k < 1024; k++) {
                res.setRGB(k, j, new Color(255, 255, 255).getRGB());
            }
        }
        for (int j = 0; j < 256; j++) {
            res.setRGB((j * 4), (int) (probability[j] * sf), new Color(0, 0, 0).getRGB());
            res.setRGB((j * 4) + 1, (int) (probability[j] * sf), new Color(0, 0, 0).getRGB());
            res.setRGB((j * 4) + 2, (int) (probability[j] * sf), new Color(0, 0, 0).getRGB());
            res.setRGB((j * 4) + 3, (int) (probability[j] * sf), new Color(0, 0, 0).getRGB());
        }
        File outp = new File("NrmlizdHistogram.jpg");
        try {
            ImageIO.write(res, "jpg", outp);
        } catch (IOException iOException) {
            System.out.println(iOException.toString());
        }
        return res;
    }
    /**
     * This is a spacial filter to apply to an image, the mask or kernel of this ffilter
     * will calculate the average of the pixels in the mask and 
     * @param maskSize
     * @return 
     */
    private BufferedImage AverageSpatialFilter(int maskSize){
        int x = img.getWidth();
        int y = img.getHeight();
        Color color;
        int mValue=1;
        double[][] mask=new double[maskSize][maskSize];
        for (int i = 0; i < mask.length; i++) {
            for (int j = 0; j < mask.length; j++) {
                mask[i][j]=mValue;
            }
        }
        BufferedImage resImg=new BufferedImage((x-(mask.length-2))+1,(y-(mask.length-2))+1,img.getType());
        for (int i = 1; i < (x-(mask.length-2)); i++) {
            for (int j = 1; j < (y-(mask.length-2)); j++) {
                double zR=0;
                double zG=0;
                double zB=0;
                //mask 3*3
                for (int k = -1; k < mask.length-1; k++) {
                    for (int l = -1; l < mask.length-1; l++) {
                        color=new Color(img.getRGB(i+k, j+l));
                        zR=zR+(color.getRed()*mask[k+1][l+1]);
                        zG=zG+(color.getGreen()*mask[k+1][l+1]);
                        zB=zB+(color.getBlue()*mask[k+1][l+1]);
                    }
                }
                double avgR = zR/(mask.length*mask.length);
                double avgG = zG/(mask.length*mask.length);
                double avgB = zB/(mask.length*mask.length);
                resImg.setRGB(i, j, new Color((int)avgR,(int)avgG,(int)avgB).getRGB());
            }
        }
        img=resImg;
        return img;
    }
    private BufferedImage BorderDetectionFilter(double[][] mask){
        Color color;
        int x = img.getWidth();
        int y = img.getHeight();
        BufferedImage resImg = new BufferedImage(x, y, img.getType());
        for (int i = 1; i < x - 1; i++) {
            for (int j = 1; j < y - 1; j++) {
                double zR = 0;
                double zG = 0;
                double zB = 0;
                //mask 3*3
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        color = new Color(img.getRGB(i + k, j + l));
                        zR =zR + (color.getRed() * mask[k + 1][l + 1]);
                        zG =zG + (color.getGreen() * mask[k + 1][l + 1]);
                        zB =zB + (color.getBlue() * mask[k + 1][l + 1]);
                    }
                }
                zR=(zR<0)?0:zR;
                zG=(zG<0)?0:zG;
                zB=(zB<0)?0:zB;
                zR=(zR<256)?zR:255;
                zG=(zG<256)?zG:255;
                zB=(zB<256)?zB:255;
                resImg.setRGB(i, j, new Color((int) zR, (int) zG, (int) zB).getRGB());
            }
        }
        return resImg;
    }
    /**
     * creates the histogram of the image that is recived in the @parameters of
     * the method
     *
     * @param image
     * @return An image that is the histogram of the given image
     */
    private BufferedImage NHistogram(BufferedImage image) {
        int x = image.getWidth();
        int y = image.getHeight();
        int rImgH = 200;
        double mxY = 0;
        double mxYRed = 0;
        double mxYGreen = 0;
        double mxYBlue = 0;
        double[] valR = new double[256];
        double[] valG = new double[256];
        double[] valB = new double[256];
        double[] pbRed = new double[256];
        double[] pbGreen = new double[256];
        double[] pbBlue = new double[256];
        Color c;
        //se cuenta las repeticiones de intensidad de cada color
        for (int j = 0; j < x; j++) {
            for (int k = 0; k < y; k++) {
                c = new Color(image.getRGB(j, k));
                valR[c.getRed()] = valR[c.getRed()] + 1;
                valG[c.getGreen()] = valG[c.getGreen()] + 1;
                valB[c.getBlue()] = valB[c.getBlue()] + 1;
            }
        }
        //se calcula la probabilidad de cada intensidad
        for (int j = 0; j < 256; j++) {
            pbRed[j] = (valR[j] / (x * y));
            pbGreen[j] = (valG[j] / (x * y));
            pbBlue[j] = (valB[j] / (x * y));
            //se obtiene el valor maximo de cada probabilidad
            mxYRed = (pbRed[j] > mxYRed) ? pbRed[j] : mxYRed;
            mxYGreen = (pbGreen[j] > mxYGreen) ? pbGreen[j] : mxYGreen;
            mxYBlue = (pbBlue[j] > mxYBlue) ? pbBlue[j] : mxYBlue;
        }
        //valor maximmo de probabilidad en general
        mxY = (mxYRed > mxYGreen && mxYRed > mxYBlue) ? mxYRed : (mxYGreen > mxYBlue) ? mxYGreen : mxYBlue;
        //factor standard en general
        
        double sf = (rImgH / mxY);
        //se crea una imagen en blanco
        BufferedImage res = new BufferedImage(1024, rImgH + 1, image.getType());
        for (int k = 0; k < 1024; k++) {
            for (int j = 0; j < rImgH; j++) {
                res.setRGB(k,j, new Color(0, 0, 0).getRGB());
            }
        }

        for (int j = 255; j >= 0; j--) {
            if (pbRed[j] > pbGreen[j] && pbRed[j] > pbBlue[j]) {
                //for the red color
                FillWRed(pbRed, sf, j, res);
                if (pbGreen[j] > pbBlue[j]) {
                    FillWGreen(pbGreen, sf, j, res);
                    FillWBlue(pbBlue, sf, j, res);
                } else {
                    FillWBlue(pbBlue, sf, j, res);
                    FillWGreen(pbGreen, sf, j, res);
                }
            } else if (pbGreen[j] > pbRed[j] && pbGreen[j] > pbBlue[j]) {
                FillWGreen(pbGreen, sf, j, res);
                if (pbRed[j] > pbBlue[j]) {
                    FillWRed(pbRed, sf, j, res);
                    FillWBlue(pbBlue, sf, j, res);
                } else {
                    FillWBlue(pbBlue, sf, j, res);
                    FillWRed(pbRed, sf, j, res);
                }
            } else {
                FillWBlue(pbBlue, sf, j, res);
                if (pbRed[j] > pbGreen[j]) {
                    FillWRed(pbRed, sf, j, res);
                    FillWGreen(pbGreen, sf, j, res);
                } else {
                    FillWGreen(pbGreen, sf, j, res);
                    FillWRed(pbRed, sf, j, res);
                }
            }
        }
        File outp = new File("NrmlizdHistogram.jpg");
        try {
            ImageIO.write(res, "jpg", outp);
        } catch (IOException iOException) {
            System.out.println(iOException.toString());
        }
        return res;
    }

    private void FillWRed(double[] pbRed, double sf, int j, BufferedImage res) {
        for (int k = 0; k <= (int) (pbRed[j] * sf); k++) {
            res.setRGB((j * 4), (res.getHeight()-1)-k, new Color(255, 0, 0).getRGB());
            res.setRGB((j * 4) + 1, (res.getHeight()-1)-k, new Color(255, 0, 0).getRGB());
            res.setRGB((j * 4) + 2, (res.getHeight()-1)-k, new Color(255, 0, 0).getRGB());
            res.setRGB((j * 4) + 3, (res.getHeight()-1)-k, new Color(255, 0, 0).getRGB());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSP_Image = new javax.swing.JScrollPane();
        jLblImage = new javax.swing.JLabel();
        jSP_Histogram = new javax.swing.JScrollPane();
        jLblHistogram = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu_File = new javax.swing.JMenu();
        jMenuOpen = new javax.swing.JMenuItem();
        jMIOpenTIFF = new javax.swing.JMenuItem();
        jMenu_Transformations = new javax.swing.JMenu();
        jMenuIR_X = new javax.swing.JMenuItem();
        jMIR_Y = new javax.swing.JMenuItem();
        jMI_Negative = new javax.swing.JMenuItem();
        jSM_Grayscale = new javax.swing.JMenu();
        jMI_GrayScale = new javax.swing.JMenuItem();
        jMIGrayscale_B = new javax.swing.JMenuItem();
        jMIGS_LightnessMethod = new javax.swing.JMenuItem();
        jMIGS_LuminosityMethod = new javax.swing.JMenuItem();
        jSM_Zoom = new javax.swing.JMenu();
        jMI_ZEasy = new javax.swing.JMenuItem();
        jMI_ZoutEasy = new javax.swing.JMenuItem();
        jSM_Rotate = new javax.swing.JMenu();
        jMI_90Cw = new javax.swing.JMenuItem();
        jMI_90Ccw = new javax.swing.JMenuItem();
        jMenu_SpacialFilters = new javax.swing.JMenu();
        jSM_PointFilters = new javax.swing.JMenu();
        jMILogaritmicFilter = new javax.swing.JMenuItem();
        jMIGammaFilter = new javax.swing.JMenuItem();
        jMISinFilter = new javax.swing.JMenuItem();
        jMICosFilter = new javax.swing.JMenuItem();
        jMITanhFilter = new javax.swing.JMenuItem();
        jMI_HistogramEqualization = new javax.swing.JMenuItem();
        jSM_SpaceFilters = new javax.swing.JMenu();
        jSM_AvgSF = new javax.swing.JMenu();
        jSMI_AFAverage3x3 = new javax.swing.JMenuItem();
        jSMI_AFAverage15x15 = new javax.swing.JMenuItem();
        jSMI_AFAverage32x32 = new javax.swing.JMenuItem();
        jSMI_MediumSF = new javax.swing.JMenuItem();
        jSM_BorderDetectionFilters = new javax.swing.JMenu();
        jMI_BDetectionX = new javax.swing.JMenuItem();
        jMI_BDetectionY = new javax.swing.JMenuItem();
        jMI_BDetectionXY = new javax.swing.JMenuItem();
        jMI_BDetectionAll = new javax.swing.JMenuItem();
        jSM_SobelOps = new javax.swing.JMenu();
        jSMISO_Sum = new javax.swing.JMenuItem();
        jSMISO_Substract = new javax.swing.JMenuItem();
        jSMISO_Mult = new javax.swing.JMenuItem();
        jSMISO_Division = new javax.swing.JMenuItem();
        jMI_SumImg = new javax.swing.JMenu();
        jMI_AddImg = new javax.swing.JMenuItem();
        jMI_SubstractImg = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLblImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jSP_Image.setViewportView(jLblImage);

        getContentPane().add(jSP_Image, java.awt.BorderLayout.CENTER);

        jSP_Histogram.setPreferredSize(new java.awt.Dimension(1024, 200));

        jLblHistogram.setToolTipText("");
        jLblHistogram.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLblHistogram.setPreferredSize(new java.awt.Dimension(0, 200));
        jSP_Histogram.setViewportView(jLblHistogram);

        getContentPane().add(jSP_Histogram, java.awt.BorderLayout.PAGE_END);

        jMenu_File.setMnemonic('f');
        jMenu_File.setText("File");

        jMenuOpen.setMnemonic('o');
        jMenuOpen.setText("Open");
        jMenuOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuOpenActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuOpen);

        jMIOpenTIFF.setText("Open TIFF");
        jMIOpenTIFF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIOpenTIFFActionPerformed(evt);
            }
        });
        jMenu_File.add(jMIOpenTIFF);

        jMenuBar1.add(jMenu_File);

        jMenu_Transformations.setMnemonic('t');
        jMenu_Transformations.setText("Transformations");

        jMenuIR_X.setMnemonic('x');
        jMenuIR_X.setText("Reflex X");
        jMenuIR_X.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuIR_XActionPerformed(evt);
            }
        });
        jMenu_Transformations.add(jMenuIR_X);

        jMIR_Y.setText("Reflex Y");
        jMenu_Transformations.add(jMIR_Y);

        jMI_Negative.setText("Negative");
        jMI_Negative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_NegativeActionPerformed(evt);
            }
        });
        jMenu_Transformations.add(jMI_Negative);

        jSM_Grayscale.setText("Gray Scale");

        jMI_GrayScale.setText("Average");
        jMI_GrayScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_GrayScaleActionPerformed(evt);
            }
        });
        jSM_Grayscale.add(jMI_GrayScale);

        jMIGrayscale_B.setText("Grayscale B");
        jMIGrayscale_B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIGrayscale_BActionPerformed(evt);
            }
        });
        jSM_Grayscale.add(jMIGrayscale_B);

        jMIGS_LightnessMethod.setText("Lightness");
        jMIGS_LightnessMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIGS_LightnessMethodActionPerformed(evt);
            }
        });
        jSM_Grayscale.add(jMIGS_LightnessMethod);

        jMIGS_LuminosityMethod.setText("Luminosity");
        jMIGS_LuminosityMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIGS_LuminosityMethodActionPerformed(evt);
            }
        });
        jSM_Grayscale.add(jMIGS_LuminosityMethod);

        jMenu_Transformations.add(jSM_Grayscale);

        jSM_Zoom.setText("Zoom");

        jMI_ZEasy.setText("Easy +");
        jMI_ZEasy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_ZEasyActionPerformed(evt);
            }
        });
        jSM_Zoom.add(jMI_ZEasy);

        jMI_ZoutEasy.setText("Easy -");
        jMI_ZoutEasy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_ZoutEasyActionPerformed(evt);
            }
        });
        jSM_Zoom.add(jMI_ZoutEasy);

        jMenu_Transformations.add(jSM_Zoom);

        jSM_Rotate.setText("Rotate");

        jMI_90Cw.setText("90 Clockwise");
        jMI_90Cw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_90CwActionPerformed(evt);
            }
        });
        jSM_Rotate.add(jMI_90Cw);

        jMI_90Ccw.setText("90 Counter Clockwise");
        jSM_Rotate.add(jMI_90Ccw);

        jMenu_Transformations.add(jSM_Rotate);

        jMenuBar1.add(jMenu_Transformations);

        jMenu_SpacialFilters.setText("Filters");

        jSM_PointFilters.setText("Point Filters");

        jMILogaritmicFilter.setText("Logaritmic Filter");
        jMILogaritmicFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMILogaritmicFilterActionPerformed(evt);
            }
        });
        jSM_PointFilters.add(jMILogaritmicFilter);

        jMIGammaFilter.setText("Gamman Filter");
        jMIGammaFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIGammaFilterActionPerformed(evt);
            }
        });
        jSM_PointFilters.add(jMIGammaFilter);

        jMISinFilter.setText("Sin Filter");
        jMISinFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMISinFilterActionPerformed(evt);
            }
        });
        jSM_PointFilters.add(jMISinFilter);

        jMICosFilter.setText("Cos  Filter");
        jMICosFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMICosFilterActionPerformed(evt);
            }
        });
        jSM_PointFilters.add(jMICosFilter);

        jMITanhFilter.setText("Tanh Filter");
        jMITanhFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMITanhFilterActionPerformed(evt);
            }
        });
        jSM_PointFilters.add(jMITanhFilter);

        jMenu_SpacialFilters.add(jSM_PointFilters);

        jMI_HistogramEqualization.setText("Histogram Equalization");
        jMI_HistogramEqualization.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_HistogramEqualizationActionPerformed(evt);
            }
        });
        jMenu_SpacialFilters.add(jMI_HistogramEqualization);

        jSM_SpaceFilters.setText("Space Filters");

        jSM_AvgSF.setText("Average");

        jSMI_AFAverage3x3.setText("3 x 3");
        jSMI_AFAverage3x3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSMI_AFAverage3x3ActionPerformed(evt);
            }
        });
        jSM_AvgSF.add(jSMI_AFAverage3x3);

        jSMI_AFAverage15x15.setText("15 x 15");
        jSMI_AFAverage15x15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSMI_AFAverage15x15ActionPerformed(evt);
            }
        });
        jSM_AvgSF.add(jSMI_AFAverage15x15);

        jSMI_AFAverage32x32.setText("32 x 32");
        jSMI_AFAverage32x32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSMI_AFAverage32x32ActionPerformed(evt);
            }
        });
        jSM_AvgSF.add(jSMI_AFAverage32x32);

        jSM_SpaceFilters.add(jSM_AvgSF);

        jSMI_MediumSF.setText("Medium");
        jSMI_MediumSF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSMI_MediumSFActionPerformed(evt);
            }
        });
        jSM_SpaceFilters.add(jSMI_MediumSF);

        jSM_BorderDetectionFilters.setText("Border detection");

        jMI_BDetectionX.setText("X");
        jMI_BDetectionX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_BDetectionXActionPerformed(evt);
            }
        });
        jSM_BorderDetectionFilters.add(jMI_BDetectionX);

        jMI_BDetectionY.setText("Y");
        jMI_BDetectionY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_BDetectionYActionPerformed(evt);
            }
        });
        jSM_BorderDetectionFilters.add(jMI_BDetectionY);

        jMI_BDetectionXY.setText("X,Y");
        jMI_BDetectionXY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_BDetectionXYActionPerformed(evt);
            }
        });
        jSM_BorderDetectionFilters.add(jMI_BDetectionXY);

        jMI_BDetectionAll.setText("All directions");
        jMI_BDetectionAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_BDetectionAllActionPerformed(evt);
            }
        });
        jSM_BorderDetectionFilters.add(jMI_BDetectionAll);

        jSM_SpaceFilters.add(jSM_BorderDetectionFilters);

        jSM_SobelOps.setText("Sobel Ops");

        jSMISO_Sum.setText("Sum");
        jSMISO_Sum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSMISO_SumActionPerformed(evt);
            }
        });
        jSM_SobelOps.add(jSMISO_Sum);

        jSMISO_Substract.setText("Substract");
        jSMISO_Substract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSMISO_SubstractActionPerformed(evt);
            }
        });
        jSM_SobelOps.add(jSMISO_Substract);

        jSMISO_Mult.setText("Multiplication");
        jSMISO_Mult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSMISO_MultActionPerformed(evt);
            }
        });
        jSM_SobelOps.add(jSMISO_Mult);

        jSMISO_Division.setText("Division");
        jSMISO_Division.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSMISO_DivisionActionPerformed(evt);
            }
        });
        jSM_SobelOps.add(jSMISO_Division);

        jSM_SpaceFilters.add(jSM_SobelOps);

        jMenu_SpacialFilters.add(jSM_SpaceFilters);

        jMI_SumImg.setText("Elementary Ops");

        jMI_AddImg.setText("+ Image");
        jMI_AddImg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_AddImgActionPerformed(evt);
            }
        });
        jMI_SumImg.add(jMI_AddImg);

        jMI_SubstractImg.setText("- Image");
        jMI_SubstractImg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_SubstractImgActionPerformed(evt);
            }
        });
        jMI_SumImg.add(jMI_SubstractImg);

        jMenu_SpacialFilters.add(jMI_SumImg);

        jMenuBar1.add(jMenu_SpacialFilters);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuOpenActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser("C:/Users/icema/Documents/NetBeansProjects/PDI/src"/*"C:/Users/icema/Documents/MEGA/wpp/"*/);
        int returnVal = fc.showOpenDialog(fc);
        String filepath = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filepath = fc.getSelectedFile().getAbsolutePath();
        }
        try {
            img = ImageIO.read(new File(filepath));

        } catch (IOException e) {
            System.out.println(e);
        }
        jLblImage.setIcon(new ImageIcon(img));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(img)));

        //reading tiff
//        try {
//            BufferedImage bfimg = ImageIO.read(new File(filepath));
//        } catch (IOException e) {
//            System.out.println("Something went wrong!");
//        }
//        jLblImage.setIcon(new ImageIcon(bfimg));
        //filtro negativo
        //System.out.println("imagen de:" + img.getWidth() + " x " + img.getHeight());
        /*for (int h = 0; h < img.getHeight(); h++) {
            for (int w = 0; w < img.getWidth(); w++) {
                Color color = new Color(img.getRGB(h, w));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                System.out.println(h + "h " + w + "w: (" + r + ", " + g + ", " + b + ")");
            }
    }//GEN-LAST:event_jMenuOpenActionPerformed
        */
    }

    /**
     * refleja la imagen en el eje de x
     *
     * @param evt
     */
    private void jMenuIR_XActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuIR_XActionPerformed
        // TODO add your handling code here:
        BufferedImage img2;
        img2 = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        int x = img.getWidth();//columnas
        int y = img.getHeight();//renglones
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                img2.setRGB(i, (y - 1) - j, img.getRGB(i, j));
            }
        }
        img = img2;
        jLblImage.setIcon(new ImageIcon(img));
    }//GEN-LAST:event_jMenuIR_XActionPerformed

    private void jMIOpenTIFFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIOpenTIFFActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser("C:/Users/icema/Documents/MEGA/wpp/");
        int returnVal = fc.showOpenDialog(fc);
        String filepath = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filepath = fc.getSelectedFile().getAbsolutePath();
        }
        try {
            img = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            System.out.println("Something went wrong while reading this image!");
        }
        if (rootPaneCheckingEnabled) {
            jLblImage.setIcon(new ImageIcon(img));
        } else {
            System.out.println("tiff image not read");
        }
    }//GEN-LAST:event_jMIOpenTIFFActionPerformed
    /**
     * filtro negativo
     *
     * @param evt
     */
    private void jMI_NegativeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_NegativeActionPerformed
        // TODO add your handling code here:
        BufferedImage img2;
        Color c;
        img2 = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        int x = img.getWidth();//columnas
        int y = img.getHeight();//renglones
        int r, g, b;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                c = new Color(img.getRGB(i, j));
                r = 255 - c.getRed();
                g = 255 - c.getGreen();
                b = 255 - c.getBlue();
                img2.setRGB(i, j, new Color(r, g, b).getRGB());
            }
        }
        img = img2;
        jLblImage.setIcon(new ImageIcon(img));
    }//GEN-LAST:event_jMI_NegativeActionPerformed

    private void jMI_GrayScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_GrayScaleActionPerformed
        // TODO add your handling code here:
        int x = img.getWidth();
        int y = img.getHeight();
        BufferedImage img2 = new BufferedImage(x, y, img.getType());
        Color c1;
        int gray;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                c1 = new Color(img.getRGB(i, j));
                gray = (c1.getRed() + c1.getGreen() + c1.getBlue()) / 3;
                img2.setRGB(i, j, new Color(gray, gray, gray).getRGB());
            }

        }
        img = img2;
        jLblImage.setIcon(new ImageIcon(img));
    }//GEN-LAST:event_jMI_GrayScaleActionPerformed

    private void jMIGrayscale_BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIGrayscale_BActionPerformed
        // TODO add your handling code here:
        int x = img.getWidth();
        int y = img.getHeight();
        BufferedImage img2 = new BufferedImage(x, y, img.getType());
        Color c1;
        int gray;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                c1 = new Color(img.getRGB(i, j));
                gray = (int) ((c1.getRed() * 0.3) + (c1.getGreen() * .59) + (c1.getBlue() * 0.11));
                img2.setRGB(i, j, new Color(gray, gray, gray).getRGB());
            }

        }
        img = img2;
        jLblImage.setIcon(new ImageIcon(img));
    }//GEN-LAST:event_jMIGrayscale_BActionPerformed

    private void jMI_ZEasyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_ZEasyActionPerformed
        // TODO add your handling code here:
        int x = img.getWidth();
        int y = img.getHeight();
        BufferedImage img2 = new BufferedImage(
                2 * x, 2 * y, img.getType());
        Color color;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                img2.setRGB(2 * i, 2 * j, img.getRGB(i, j));
                img2.setRGB(2 * i + 1, 2 * j, img.getRGB(i, j));

                img2.setRGB(2 * i, 2 * j + 1, img.getRGB(i, j));
                img2.setRGB(2 * i + 1, 2 * j + 1, img.getRGB(i, j));
            }
        }
        img = img2;
        jLblImage.setIcon(new ImageIcon(img2));
    }//GEN-LAST:event_jMI_ZEasyActionPerformed
    /**
     * Lightness method formula (max(R, G, B) + min(R, G, B)) / 2.
     *
     * @param evt
     */
    private void jMIGS_LightnessMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIGS_LightnessMethodActionPerformed
        // TODO add your handling code here:
        int x = img.getWidth();
        int y = img.getHeight();
        BufferedImage img2 = new BufferedImage(x, y, img.getType());
        Color clr;
        int gray, mxRGB, minRGB;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                clr = new Color(img.getRGB(i, j));
                minRGB = (clr.getRed() < clr.getGreen() && clr.getRed() < clr.getBlue()) ? clr.getRed() : ((clr.getGreen() < clr.getBlue()) ? clr.getGreen() : clr.getBlue());
                mxRGB = (clr.getRed() > clr.getGreen() && clr.getRed() > clr.getBlue()) ? clr.getRed() : ((clr.getGreen() > clr.getBlue()) ? clr.getGreen() : clr.getBlue());
                gray = (int) ((mxRGB + minRGB) / 2);
                System.out.println(clr.getRed() + ", " + clr.getGreen() + ", " + clr.getBlue() + " mx=" + mxRGB + ", min=" + minRGB + ", gray=" + gray);
                img2.setRGB(i, j, new Color(gray, gray, gray).getRGB());
            }
        }
        img = img2;
        jLblImage.setIcon(new ImageIcon(img));
    }//GEN-LAST:event_jMIGS_LightnessMethodActionPerformed
    /**
     * Luminosity method formula: 0.21 R + 0.72 G + 0.07 B
     *
     * @param evt
     */
    private void jMIGS_LuminosityMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIGS_LuminosityMethodActionPerformed
        // TODO add your handling code here:
        int x = img.getWidth();
        int y = img.getHeight();
        BufferedImage img2 = new BufferedImage(x, y, img.getType());
        Color clr;
        int gray;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                clr = new Color(img.getRGB(i, j));
                gray = (int) ((clr.getRed() * 0.21) + (clr.getGreen() * 0.72) + (clr.getBlue() * 0.07));
                img2.setRGB(i, j, new Color(gray, gray, gray).getRGB());
            }
        }
        img = img2;
        jLblImage.setIcon(new ImageIcon(img));
    }//GEN-LAST:event_jMIGS_LuminosityMethodActionPerformed

    private void jMI_ZoutEasyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_ZoutEasyActionPerformed
        // TODO add your handling code here:
        int x = img.getWidth();
        int y = img.getHeight();
        BufferedImage BfImg = new BufferedImage(
                x / 2, y / 2, img.getType());
        Color color;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if ((i % 2 == 0) && (j % 2 == 0)) {
                    BfImg.setRGB(i / 2, j / 2, img.getRGB(i, j));
                }
            }
        }
        img = BfImg;
        jLblImage.setIcon(new ImageIcon(BfImg));
    }//GEN-LAST:event_jMI_ZoutEasyActionPerformed
    /**
     * rotates the image 90 degrees clockwise
     *
     * @param evt
     */
    private void jMI_90CwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_90CwActionPerformed
        // TODO add your handling code here:
        int x = img.getWidth();
        int y = img.getHeight();
        BufferedImage BfImg = new BufferedImage(x, y, img.getType());
        Color color;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                BfImg.setRGB(x, (y - 1) - i, img.getRGB(i, j));
            }
        }
        img = BfImg;
        jLblImage.setIcon(new ImageIcon(BfImg));
    }//GEN-LAST:event_jMI_90CwActionPerformed

    private void jMILogaritmicFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMILogaritmicFilterActionPerformed
        // TODO add your handling code here:
        int x = img.getWidth();
        int y = img.getHeight();
        //BufferedImage BfImg = new BufferedImage(x, y, img.getType());
        Color color;
        int rR, rG, rB;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                color = new Color(img.getRGB(i, j));
                rR = (int) ((log(color.getRed() + 1) / log(256)) * 255);
                rG = (int) ((log(color.getGreen() + 1) / log(256)) * 255);
                rB = (int) ((log(color.getBlue() + 1) / log(256)) * 255);
                img.setRGB(i, j, new Color(rR, rG, rB).getRGB());
            }
        }
        //img = BfImg;
        jLblImage.setIcon(new ImageIcon(img));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(img)));
    }//GEN-LAST:event_jMILogaritmicFilterActionPerformed

    private void jMICosFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMICosFilterActionPerformed
        // TODO add your handling code here:
        int x = img.getWidth();
        int y = img.getHeight();
        //BufferedImage BfImg = new BufferedImage(x, y, img.getType());
        Color color;
        int rR, rG, rB;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                color = new Color(img.getRGB(i, j));
                rR = (int) (255.0 * (1.0 - cos((Math.PI / 2.0) * (color.getRed() / 255.0))));
                rG = (int) (255.0 * (1.0 - cos((Math.PI / 2.0) * (color.getGreen() / 255.0))));
                rB = (int) (255.0 * (1.0 - cos((Math.PI / 2.0) * (color.getBlue() / 255.0))));
                img.setRGB(i, j, new Color(rR, rG, rB).getRGB());
            }
        }
        //img = BfImg;
        jLblImage.setIcon(new ImageIcon(img));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(img)));
    }//GEN-LAST:event_jMICosFilterActionPerformed

    private void jMITanhFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMITanhFilterActionPerformed
        // TODO add your handling code here:
        int x = img.getWidth();
        int y = img.getHeight();
        //BufferedImage BfImg = new BufferedImage(x, y, img.getType());
        Color color;
        int rR, rG, rB;
        double q = 255.0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                color = new Color(img.getRGB(i, j));
                rR = (int) ((q / 2.0) * (1.0 + tanh(q * (color.getRed() - (q / 2.0)))));
                rB = (int) ((q / 2.0) * (1.0 + tanh(q * (color.getGreen() - (q / 2.0)))));
                rG = (int) ((q / 2.0) * (1.0 + tanh(q * (color.getBlue() - (q / 2.0)))));
                img.setRGB(i, j, new Color(rR, rG, rB).getRGB());
            }
        }
        //img = BfImg;
        jLblImage.setIcon(new ImageIcon(img));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(img)));
    }//GEN-LAST:event_jMITanhFilterActionPerformed

    private void jMIGammaFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIGammaFilterActionPerformed
        // TODO add your handling code here:
        int x = img.getWidth();
        int y = img.getHeight();
        double gamma = (4.0);
        //BufferedImage BfImg = new BufferedImage(x, y, img.getType());
        Color color;
        int rR, rG, rB;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                color = new Color(img.getRGB(i, j));
                //encoded = ((original / 255) ^ (1 / gamma)) * 255;
                rR = (int) (pow((color.getRed() / 255.0), (1.0 / gamma)));
                rG = (int) (pow((color.getGreen()/ 255.0), (1.0 / gamma)));
                rB = (int) (pow((color.getBlue()/ 255.0), (1.0 / gamma)));
                img.setRGB(i, j, new Color(rR, rG, rB).getRGB());
            }
        }
        //img = BfImg;
        jLblImage.setIcon(new ImageIcon(img));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(img)));
    }//GEN-LAST:event_jMIGammaFilterActionPerformed

    private void jMISinFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMISinFilterActionPerformed
        // TODO add your handling code here:
        int x = img.getWidth();
        int y = img.getHeight();
        //BufferedImage BfImg = new BufferedImage(x, y, img.getType());
        Color color;
        int rR, rG, rB;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                color = new Color(img.getRGB(i, j));
                rR = (int) (255.0 * (sin((Math.PI / 2.0) * (color.getRed() / 255.0))));
                rG = (int) (255.0 * (sin((Math.PI / 2.0) * (color.getGreen() / 255.0))));
                rB = (int) (255.0 * (sin((Math.PI / 2.0) * (color.getBlue() / 255.0))));
                img.setRGB(i, j, new Color(rR, rG, rB).getRGB());
            }
        }
        //img = BfImg;
        jLblImage.setIcon(new ImageIcon(img));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(img)));
    }//GEN-LAST:event_jMISinFilterActionPerformed

    private void jMI_HistogramEqualizationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_HistogramEqualizationActionPerformed
        // TODO add your handling code here:
        int x = img.getWidth();
        int y = img.getHeight();
        double[] skR=new double[256];
        double[] skG=new double[256];
        double[] skB=new double[256];
        double[] valR = new double[256];
        double[] valG = new double[256];
        double[] valB = new double[256];
        double[] pbRed = new double[256];
        double[] pbGreen = new double[256];
        double[] pbBlue = new double[256];
        double[] zPbR=new double[256];
        double[] zPbG=new double[256];
        double[] zPbB=new double[256];
        Color c;
        //se cuenta las repeticiones de intensidad de cada color
        for (int j = 0; j < x; j++) {
            for (int k = 0; k < y; k++) {
                c = new Color(img.getRGB(j, k));
                valR[c.getRed()] = valR[c.getRed()] + 1;
                valG[c.getGreen()] = valG[c.getGreen()] + 1;
                valB[c.getBlue()] = valB[c.getBlue()] + 1;
            }
        }
        //se calcula la probabilidad de cada intensidad
        for (int j = 0; j < 256; j++) {
            pbRed[j] = (valR[j] / (x * y));
            pbGreen[j] = (valG[j] / (x * y));
            pbBlue[j] = (valB[j] / (x * y));
        }
        //se hace la sumatoria de las probabilidades de cada color para cada nivel de intensidad
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < i+1; j++) {
                zPbR[i]=zPbR[i]+pbRed[j];
                zPbG[i]=zPbG[i]+pbGreen[j];
                zPbB[i]=zPbB[i]+pbBlue[j];
            }
            skR[i]=255*zPbR[i];
            skG[i]=255*zPbG[i];
            skB[i]=255*zPbB[i];
        }
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                c = new Color(img.getRGB(i, j));
                img.setRGB(i, j, new Color((int)skR[c.getRed()], (int)skG[c.getGreen()], (int)skB[c.getBlue()]).getRGB());
            }
        }
        //img = BfImg;
        jLblImage.setIcon(new ImageIcon(img));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(img)));
    }//GEN-LAST:event_jMI_HistogramEqualizationActionPerformed

    private void jMI_BDetectionXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_BDetectionXActionPerformed
        // TODO add your handling code here:
        double[][] mask = {
            {0, 0, 0},
            {-1, 2, -1},
            {0, 0, 0}
        };
        jLblImage.setIcon(new ImageIcon(BorderDetectionFilter(mask)));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(BorderDetectionFilter(mask))));
    }//GEN-LAST:event_jMI_BDetectionXActionPerformed

    private void jSMI_AFAverage3x3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSMI_AFAverage3x3ActionPerformed
        // TODO add your handling code here:
        jLblImage.setIcon(new ImageIcon(AverageSpatialFilter(3)));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(img)));
    }//GEN-LAST:event_jSMI_AFAverage3x3ActionPerformed

    private void jSMI_MediumSFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSMI_MediumSFActionPerformed
        // TODO add your handling code here:
        int x = img.getWidth();
        int y = img.getHeight();
        Color color;
        double[][] mask={{1,1,1},{1,1,1},{1,1,1}};
        BufferedImage resImg=new BufferedImage(x,y,img.getType());
        for (int i = 1; i < x-1; i++) {
            for (int j = 1; j < y-1; j++) {
                double vR[]=new double[9];
                double vG[]=new double[9];
                double vB[]=new double[9];
                //mask 3*3
                int m=0;
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        color=new Color(img.getRGB(i+k, j+l));
                        vR[m]=color.getRed();
                        vG[m]=color.getGreen();
                        vB[m]=color.getBlue();
                        m++;
                    }
                }
                Arrays.sort(vR);
                Arrays.sort(vG);
                Arrays.sort(vB);
                resImg.setRGB(i, j, new Color((int)vR[4],(int)vG[4],(int)vB[4]).getRGB());
            }
        }
        img=resImg;
        jLblImage.setIcon(new ImageIcon(img));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(img)));
    }//GEN-LAST:event_jSMI_MediumSFActionPerformed

    private void jSMI_AFAverage15x15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSMI_AFAverage15x15ActionPerformed
        // TODO add your handling code here:
        jLblImage.setIcon(new ImageIcon(AverageSpatialFilter(15)));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(AverageSpatialFilter(15))));
    }//GEN-LAST:event_jSMI_AFAverage15x15ActionPerformed

    private void jSMI_AFAverage32x32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSMI_AFAverage32x32ActionPerformed
        // TODO add your handling code here:
        jLblImage.setIcon(new ImageIcon(AverageSpatialFilter(32)));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(AverageSpatialFilter(32))));
    }//GEN-LAST:event_jSMI_AFAverage32x32ActionPerformed

    private void jMI_BDetectionYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_BDetectionYActionPerformed
        // TODO add your handling code here:
        double[][] mask = {
            {0, 1, 0},
            {0, -2, 0},
            {0, 1, 0}
        };
        jLblImage.setIcon(new ImageIcon(BorderDetectionFilter(mask)));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(BorderDetectionFilter(mask))));
    }//GEN-LAST:event_jMI_BDetectionYActionPerformed

    private void jMI_BDetectionXYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_BDetectionXYActionPerformed
        // TODO add your handling code here:
        double[][] mask = {
            {0, -1, 0},
            {-1, 4, -1},
            {0, -1, 0}
        };
        jLblImage.setIcon(new ImageIcon(BorderDetectionFilter(mask)));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(BorderDetectionFilter(mask))));
    }//GEN-LAST:event_jMI_BDetectionXYActionPerformed

    private void jMI_BDetectionAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_BDetectionAllActionPerformed
        // TODO add your handling code here:
        double[][] mask = {
            {-1,-1,-1},
            {-1,8,-1},
            {-1,-1,-1},
        };
        jLblImage.setIcon(new ImageIcon(BorderDetectionFilter(mask)));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(BorderDetectionFilter(mask))));
    }//GEN-LAST:event_jMI_BDetectionAllActionPerformed

    private void jSMISO_SumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSMISO_SumActionPerformed
        // TODO add your handling code here:
        double[][] m1={
            {-1,-2,-1},
            {0,0,0},
            {1,2,1},
        };
        double m2[][]={
            {-1,0,1},
            {-2,0,2},
            {-1,0,1},
        };
        Color color;
        int x = img.getWidth();
        int y = img.getHeight();
        BufferedImage resImg = new BufferedImage(x, y, img.getType());
        for (int i = 1; i < x - 1; i++) {
            for (int j = 1; j < y - 1; j++) {
                double zR = 0;
                double zG = 0;
                double zB = 0;
                //mask 3*3
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        color = new Color(img.getRGB(i + k, j + l));
                        zR =zR + ((color.getRed() * m1[k + 1][l + 1])+(color.getRed() * m2[k + 1][l + 1]));
                        zG =zG + ((color.getGreen() * m1[k + 1][l + 1])+(color.getGreen() * m2[k + 1][l + 1]));
                        zB =zB + ((color.getBlue() * m1[k + 1][l + 1])+(color.getBlue() * m2[k + 1][l + 1]));
                    }
                }
                zR=(zR<0)?0:zR;
                zG=(zG<0)?0:zG;
                zB=(zB<0)?0:zB;
                zR=(zR<256)?zR:255;
                zG=(zG<256)?zG:255;
                zB=(zB<256)?zB:255;
                resImg.setRGB(i, j, new Color((int) zR, (int) zG, (int) zB).getRGB());
            }
        }
        jLblImage.setIcon(new ImageIcon(BorderDetectionFilter(m1)));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(BorderDetectionFilter(m1))));
    }//GEN-LAST:event_jSMISO_SumActionPerformed

    private void jSMISO_SubstractActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSMISO_SubstractActionPerformed
        // TODO add your handling code here:
        double[][] m1={
            {-1,-2,-1},
            {0,0,0},
            {1,2,1},
        };
        double m2[][]={
            {-1,0,1},
            {-2,0,2},
            {-1,0,1},
        };
        Color color;
        int x = img.getWidth();
        int y = img.getHeight();
        BufferedImage resImg = new BufferedImage(x, y, img.getType());
        for (int i = 1; i < x - 1; i++) {
            for (int j = 1; j < y - 1; j++) {
                double zR = 0;
                double zG = 0;
                double zB = 0;
                //mask 3*3
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        color = new Color(img.getRGB(i + k, j + l));
                        zR+= ((color.getRed() * m1[k + 1][l + 1])-(color.getRed() * m2[k + 1][l + 1]));
                        zG+= ((color.getGreen() * m1[k + 1][l + 1])-(color.getGreen() * m2[k + 1][l + 1]));
                        zB+= ((color.getBlue() * m1[k + 1][l + 1])-(color.getBlue() * m2[k + 1][l + 1]));
                    }
                }
                zR=(zR<0)?0:zR;
                zG=(zG<0)?0:zG;
                zB=(zB<0)?0:zB;
                zR=(zR<256)?zR:255;
                zG=(zG<256)?zG:255;
                zB=(zB<256)?zB:255;
                resImg.setRGB(i, j, new Color((int) zR, (int) zG, (int) zB).getRGB());
            }
        }
        jLblImage.setIcon(new ImageIcon(BorderDetectionFilter(m1)));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(BorderDetectionFilter(m1))));
    }//GEN-LAST:event_jSMISO_SubstractActionPerformed

    private void jSMISO_MultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSMISO_MultActionPerformed
        // TODO add your handling code here:
        double[][] m1={
            {-1,-2,-1},
            {0,0,0},
            {1,2,1},
        };
        double m2[][]={
            {-1,0,1},
            {-2,0,2},
            {-1,0,1},
        };
        Color color;
        int x = img.getWidth();
        int y = img.getHeight();
        BufferedImage resImg = new BufferedImage(x, y, img.getType());
        for (int i = 1; i < x - 1; i++) {
            for (int j = 1; j < y - 1; j++) {
                double zR = 0;
                double zG = 0;
                double zB = 0;
                //mask 3*3
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        color = new Color(img.getRGB(i + k, j + l));
                        zR =zR + ((color.getRed() * m1[k + 1][l + 1])*(color.getRed() * m2[k + 1][l + 1]));
                        zG =zG + ((color.getGreen() * m1[k + 1][l + 1])*(color.getGreen() * m2[k + 1][l + 1]));
                        zB =zB + ((color.getBlue() * m1[k + 1][l + 1])*(color.getBlue() * m2[k + 1][l + 1]));
                    }
                }
                zR=(zR<0)?0:zR;
                zG=(zG<0)?0:zG;
                zB=(zB<0)?0:zB;
                zR=(zR<256)?zR:255;
                zG=(zG<256)?zG:255;
                zB=(zB<256)?zB:255;
                resImg.setRGB(i, j, new Color((int) zR, (int) zG, (int) zB).getRGB());
            }
        }
        jLblImage.setIcon(new ImageIcon(BorderDetectionFilter(m1)));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(BorderDetectionFilter(m1))));
    }//GEN-LAST:event_jSMISO_MultActionPerformed

    private void jSMISO_DivisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSMISO_DivisionActionPerformed
        // TODO add your handling code here:
        double[][] m1={
            {-1,-2,-1},
            {0,0,0},
            {1,2,1},
        };
        double m2[][]={
            {-1,0,1},
            {-2,0,2},
            {-1,0,1},
        };
        Color color;
        int x = img.getWidth();
        int y = img.getHeight();
        BufferedImage resImg = new BufferedImage(x, y, img.getType());
        for (int i = 1; i < x - 1; i++) {
            for (int j = 1; j < y - 1; j++) {
                double zR = 0;
                double zG = 0;
                double zB = 0;
                //mask 3*3
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        color = new Color(img.getRGB(i + k, j + l));
                        zR =zR + ((color.getRed() * m1[k + 1][l + 1])/(color.getRed() * m2[k + 1][l + 1]));
                        zG =zG + ((color.getGreen() * m1[k + 1][l + 1])/(color.getGreen() * m2[k + 1][l + 1]));
                        zB =zB + ((color.getBlue() * m1[k + 1][l + 1])/(color.getBlue() * m2[k + 1][l + 1]));
                    }
                }
                zR=(zR<0)?0:zR;
                zG=(zG<0)?0:zG;
                zB=(zB<0)?0:zB;
                zR=(zR<256)?zR:255;
                zG=(zG<256)?zG:255;
                zB=(zB<256)?zB:255;
                resImg.setRGB(i, j, new Color((int) zR, (int) zG, (int) zB).getRGB());
            }
        }
        jLblImage.setIcon(new ImageIcon(BorderDetectionFilter(m1)));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(BorderDetectionFilter(m1))));
    }//GEN-LAST:event_jSMISO_DivisionActionPerformed

    private void jMI_SubstractImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_SubstractImgActionPerformed
        // TODO add your handling code here:
        Open2Images();
        int x=img.getWidth();
        int y=img.getHeight();
        int x2=SecondImg.getWidth();
        int y2=SecondImg.getHeight();
        if ((x==x2)&&(y==y2)) {
            int r;
            int g;
            int b;
            Color c;
            Color c2;
            BufferedImage resImg = new BufferedImage(x, y, img.getType());
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    c = new Color(img.getRGB(i, j));
                    c2 = new Color(SecondImg.getRGB(i, j));
                    r = (c.getRed() < c2.getRed()) ? c2.getRed() : c.getRed();
                    g = (c.getGreen() < c2.getGreen()) ? c2.getGreen() : c.getGreen();
                    b = (c.getBlue() < c2.getBlue()) ? c2.getBlue() : c.getBlue();
                    resImg.setRGB(i, j, new Color(r, g, b).getRGB());
                   
                }
            }
            jLblImage.setIcon(new ImageIcon(resImg));
            jLblHistogram.setIcon(new ImageIcon(NHistogram(resImg)));
            JOptionPane.showMessageDialog(this, "Done a-b");
        }else{
        JOptionPane.showMessageDialog(this, "both images must be from the same size");
        }
    }//GEN-LAST:event_jMI_SubstractImgActionPerformed

    private void jMI_AddImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_AddImgActionPerformed
        // TODO add your handling code here:
        Open2Images();
        int x=img.getWidth();
        int y=img.getHeight();
        int x2=SecondImg.getWidth();
        int y2=SecondImg.getHeight();
        if ((x>=x2)&&(y>=y2)) {
            int r;
            int g;
            int b;
            Color c;
            Color c2;
            BufferedImage resImg = new BufferedImage(x, y, img.getType());
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    c = new Color(img.getRGB(i, j));
                    c2 = new Color(SecondImg.getRGB(i, j));
                    r = (c.getRed() < c2.getRed()) ? c.getRed() : c2.getRed();
                    g = (c.getGreen() < c2.getGreen()) ? c.getGreen() : c2.getGreen();
                    b = (c.getBlue() < c2.getBlue()) ? c.getBlue() : c2.getBlue();
                    resImg.setRGB(i, j, new Color(r, g, b).getRGB());
                }
            }
            jLblImage.setIcon(new ImageIcon(resImg));
            jLblHistogram.setIcon(new ImageIcon(NHistogram(resImg)));
            JOptionPane.showMessageDialog(this, "Done a+b");
        }else{
        JOptionPane.showMessageDialog(this, "both images must be from the same size");
        }
    }//GEN-LAST:event_jMI_AddImgActionPerformed
    public void Open2Images(){
        JFileChooser fc;
        JOptionPane.showMessageDialog(this, "Select the first image");
        fc = new JFileChooser("C:/Users/icema/Documents/NetBeansProjects/PDI/src"/*"C:/Users/icema/Documents/MEGA/wpp/"*/);
        int returnVal = fc.showOpenDialog(fc);
        String filepath = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filepath = fc.getSelectedFile().getAbsolutePath();
        }
        try {
            img = ImageIO.read(new File(filepath));

        } catch (IOException e) {
            System.out.println(e);
        }
        jLblImage.setIcon(new ImageIcon(img));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(img)));
        // second image
        JOptionPane.showMessageDialog(this, "Select the second image");
        fc = new JFileChooser("C:/Users/icema/Documents/NetBeansProjects/PDI/src"/*"C:/Users/icema/Documents/MEGA/wpp/"*/);
        int returnValb = fc.showOpenDialog(fc);
        String filepathb = null;
        if (returnValb == JFileChooser.APPROVE_OPTION) {
            filepathb = fc.getSelectedFile().getAbsolutePath();
        }
        try {
            SecondImg = ImageIO.read(new File(filepathb));

        } catch (IOException e) {
            System.out.println(e);
        }
        //img=SecondImg;
        jLblImage.setIcon(new ImageIcon(SecondImg));
        jLblHistogram.setIcon(new ImageIcon(NHistogram(SecondImg)));
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MyFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MyFrame().setVisible(true);
            }
        });
    }
    private BufferedImage img;
    private BufferedImage SecondImg;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLblHistogram;
    private javax.swing.JLabel jLblImage;
    private javax.swing.JMenuItem jMICosFilter;
    private javax.swing.JMenuItem jMIGS_LightnessMethod;
    private javax.swing.JMenuItem jMIGS_LuminosityMethod;
    private javax.swing.JMenuItem jMIGammaFilter;
    private javax.swing.JMenuItem jMIGrayscale_B;
    private javax.swing.JMenuItem jMILogaritmicFilter;
    private javax.swing.JMenuItem jMIOpenTIFF;
    private javax.swing.JMenuItem jMIR_Y;
    private javax.swing.JMenuItem jMISinFilter;
    private javax.swing.JMenuItem jMITanhFilter;
    private javax.swing.JMenuItem jMI_90Ccw;
    private javax.swing.JMenuItem jMI_90Cw;
    private javax.swing.JMenuItem jMI_AddImg;
    private javax.swing.JMenuItem jMI_BDetectionAll;
    private javax.swing.JMenuItem jMI_BDetectionX;
    private javax.swing.JMenuItem jMI_BDetectionXY;
    private javax.swing.JMenuItem jMI_BDetectionY;
    private javax.swing.JMenuItem jMI_GrayScale;
    private javax.swing.JMenuItem jMI_HistogramEqualization;
    private javax.swing.JMenuItem jMI_Negative;
    private javax.swing.JMenuItem jMI_SubstractImg;
    private javax.swing.JMenu jMI_SumImg;
    private javax.swing.JMenuItem jMI_ZEasy;
    private javax.swing.JMenuItem jMI_ZoutEasy;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuIR_X;
    private javax.swing.JMenuItem jMenuOpen;
    private javax.swing.JMenu jMenu_File;
    private javax.swing.JMenu jMenu_SpacialFilters;
    private javax.swing.JMenu jMenu_Transformations;
    private javax.swing.JMenuItem jSMISO_Division;
    private javax.swing.JMenuItem jSMISO_Mult;
    private javax.swing.JMenuItem jSMISO_Substract;
    private javax.swing.JMenuItem jSMISO_Sum;
    private javax.swing.JMenuItem jSMI_AFAverage15x15;
    private javax.swing.JMenuItem jSMI_AFAverage32x32;
    private javax.swing.JMenuItem jSMI_AFAverage3x3;
    private javax.swing.JMenuItem jSMI_MediumSF;
    private javax.swing.JMenu jSM_AvgSF;
    private javax.swing.JMenu jSM_BorderDetectionFilters;
    private javax.swing.JMenu jSM_Grayscale;
    private javax.swing.JMenu jSM_PointFilters;
    private javax.swing.JMenu jSM_Rotate;
    private javax.swing.JMenu jSM_SobelOps;
    private javax.swing.JMenu jSM_SpaceFilters;
    private javax.swing.JMenu jSM_Zoom;
    private javax.swing.JScrollPane jSP_Histogram;
    private javax.swing.JScrollPane jSP_Image;
    // End of variables declaration//GEN-END:variables

    private void FillWGreen(double[] pbGreen, double sf, int j, BufferedImage res) {
        for (int k = 0; k < (int) (pbGreen[j] * sf); k++) {
            res.setRGB((j * 4), (res.getHeight()-1)-k, new Color(0, 255, 0).getRGB());
            res.setRGB((j * 4) + 1, (res.getHeight()-1)-k, new Color(0, 255, 0).getRGB());
            res.setRGB((j * 4) + 2, (res.getHeight()-1)-k, new Color(0, 255, 0).getRGB());
            res.setRGB((j * 4) + 3, (res.getHeight()-1)-k, new Color(0, 255, 0).getRGB());
        }
    }

    private void FillWBlue(double[] pbBlue, double StandardFactor, int posX, BufferedImage res) {
        for (int k = 0; k < (int) (pbBlue[posX] * StandardFactor); k++) {
            res.setRGB((posX * 4), (res.getHeight()-1)-k, new Color(0, 0, 255).getRGB());
            res.setRGB((posX * 4) + 1, (res.getHeight()-1)-k, new Color(0, 0, 255).getRGB());
            res.setRGB((posX * 4) + 2, (res.getHeight()-1)-k, new Color(0, 0, 255).getRGB());
            res.setRGB((posX * 4) + 3, (res.getHeight()-1)-k, new Color(0, 0, 255).getRGB());
        }
    }
}
