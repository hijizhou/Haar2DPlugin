package plugins.hijizhou.utilities.test;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import plugins.hijizhou.utilities.Haar2D;

public class testHaar2D {

	public static void main(String[] args) throws IOException {

		int width = 256;
		int height = 256;
		File sourceimage = new File(
				"/Users/Jizhou/Documents/Documents/Research/A Projects/A2 PURE deconv/01 Codes/NaturalImages/Cameraman256.png");
		BufferedImage img = ImageIO.read(sourceimage);

		WritableRaster raster0 = img.getRaster();
		// convert it to 2d array
		double[][] arrImg = new double[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				arrImg[i][j] = raster0.getSampleDouble(i, j, 0);
			}
		}
		
		long startTime = System.currentTimeMillis();
		Haar2D wavelet = new Haar2D(arrImg,1);
		wavelet.decomposition();

//		 double[][] LLimg = wavelet.getLowHigh(1);
//		double[][] LLimg = wavelet.getHighHigh(1);
//		 double[][] LLimg = wavelet.reconstructionByBand("HL",4);
		double[][] LLimg = wavelet.reconstruction();
//		 System.out.println(Arrays.deepToString(LLimg));

		double endTime   = System.currentTimeMillis();
		double totalTime = (endTime - startTime)/1000;
		System.out.println(totalTime);
		// convert it to BufferedImage
		BufferedImage LLimgBuffered = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = LLimgBuffered.getRaster();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				raster.setSample(x, y, 0, LLimg[x][y]);
			}
		}

		ImageIcon icon = new ImageIcon(LLimgBuffered);
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(200, 300);
		JLabel lbl = new JLabel();
		lbl.setIcon(icon);
		frame.add(lbl);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
