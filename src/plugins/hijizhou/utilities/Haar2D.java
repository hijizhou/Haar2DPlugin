package plugins.hijizhou.utilities;

/**
 * Haar2D <code>Haar2D</code> Multi-level 2D Haar wavelet decomposition and
 * reconstruction
 * 
 * @author Jizhou Li <hijizhou@gmail.com>
 * @thanks Thierry Blu for the initial Matlab version
 * @version 1.0
 * @create date 19 June, 2016
 */

public class Haar2D {
	private int depth = 4;
	private int width;
	private int height;
	private double[] Ioriginal;
	private double[] LL;
	private double[][] LH = new double[depth][];
	private double[][] HL = new double[depth][];
	private double[][] HH = new double[depth][];

	/**
	 * Constructing function img - the original image setdepth - the
	 * decomposition depth
	 **/
	public Haar2D(double[][] img, int setdepth) {
		width = img.length;
		height = img[0].length;
		depth = setdepth;

		Ioriginal = Array.Two2One(img);

		for (int di = 0; di < depth; di++) {
			LH[di] = new double[width * height];
			HL[di] = new double[width * height];
			HH[di] = new double[width * height];
		}
	}

	/**
	 * Get the Low-Low subband
	 * 
	 * @return
	 */
	public double[][] getLowLow() {
		double[][] rI = new double[width][height];
		rI = Array.One2Two(LL, width, height);
		return rI;
	}

	/**
	 * Get the High-Low subband
	 * 
	 * @return
	 */
	public double[][] getHighLow(int depth) {
		double[][] rI = new double[width][height];
		rI = Array.One2Two(HL[depth - 1], width, height);
		return rI;
	}

	/**
	 * Get the Low-High subband
	 * 
	 * @return
	 */
	public double[][] getLowHigh(int depth) {
		double[][] rI = new double[width][height];
		rI = Array.One2Two(LH[depth - 1], width, height);
		return rI;
	}

	/**
	 * Get the High-High subband
	 * 
	 * @return
	 */
	public double[][] getHighHigh(int depth) {
		double[][] rI = new double[width][height];
		rI = Array.One2Two(HH[depth - 1], width, height);
		return rI;
	}

	/**
	 * Wavelet decompostion
	 * 
	 * @return
	 */
	public void decomposition() {
		LL = LowLow(Ioriginal, "dec");
		for (int di = 0; di < depth; di++) {
			LH[di] = LowHigh(Ioriginal, "dec", di + 1);
			HL[di] = HighLow(Ioriginal, "dec", di + 1);
			HH[di] = HighHigh(Ioriginal, "dec", di + 1);
		}
	}

	/**
	 * Decompostion by subband
	 * 
	 * @return
	 */
	public double[][] decompositionByBand(String band, int idepth) {
		double[][] decI = new double[width][height];
		double[] decIarray = new double[width * height];
		switch (band) {
		case "LL":
			decIarray = LowLow(LL, "dec");
			break;
		case "HL":
			decIarray = HighLow(HL[idepth - 1], "dec", idepth);
			break;
		case "LH":
			decIarray = LowHigh(LH[idepth - 1], "dec", idepth);
			break;
		case "HH":
			decIarray = HighHigh(HH[idepth - 1], "dec", idepth);
			break;

		}
		decI = Array.One2Two(decIarray, width, height);
		return decI;
	}

	/**
	 * Reconstruction by subband
	 * 
	 * @return
	 */
	public double[][] reconstructionByBand(String band, int idepth) {
		double[][] recI = new double[width][height];
		double[] recIarray = new double[width * height];
		switch (band) {
		case "LL":
			recIarray = LowLow(LL, "rec");
			break;
		case "HL":
			recIarray = HighLow(HL[idepth - 1], "rec", idepth);
			break;
		case "LH":
			recIarray = LowHigh(LH[idepth - 1], "rec", idepth);
			break;
		case "HH":
			recIarray = HighHigh(HH[idepth - 1], "rec", idepth);
			break;

		}
		recI = Array.One2Two(recIarray, width, height);
		return recI;
	}

	/**
	 * Reconstruction image
	 * 
	 * @return
	 */
	public double[][] reconstruction() {
		double[][] recI = new double[width][height];
		double[] recIarray = new double[width * height];
		double[] tmpRec = new double[width * height];
		recIarray = LowLow(LL, "rec");
		for (int di = 0; di < depth; di++) {
			tmpRec = LowHigh(LH[di], "rec", di + 1);
			recIarray = Array.array1Add(recIarray, tmpRec);
			tmpRec = HighLow(HL[di], "rec", di + 1);
			recIarray = Array.array1Add(recIarray, tmpRec);
			tmpRec = HighHigh(HH[di], "rec", di + 1);
			recIarray = Array.array1Add(recIarray, tmpRec);

		}
		recI = Array.One2Two(recIarray, width, height);
		return recI;
	}

	/**
	 * LowLow subband
	 * 
	 * @return
	 */
	public double[] LowLow(double[] I, String type) {
		int scale = (int) Math.pow(2, depth);
		double[] inverI;
		if (type == "dec") {
			inverI = Array.array1Inverse(I);
		} else {
			inverI = I;
		}
		double[][] inverI2 = Array.One2Two(inverI, width, height);

		// Part 1
		double[] s = Array.array2Mean1(inverI2);
		double[][] rowI = Array.array2RowEx(inverI2, scale);

		inverI2 = Array.array2Minus(inverI2, rowI);
		inverI2 = Array.array2CumSum1(inverI2);

		double[] meanI1 = Array.array2Mean1(inverI2);

		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				inverI2[wi][hi] = inverI2[wi][hi] + scale * s[wi] - meanI1[wi];
			}
		}

		// Part 2
		double[] meanI2 = Array.array2Mean2(inverI2);
		double[][] colI = Array.array2ColEx(inverI2, scale);
		inverI2 = Array.array2Minus(inverI2, colI);

		inverI2 = Array.array2CumSum2(inverI2);

		double[] meanI22 = Array.array2Mean2(inverI2);

		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				inverI2[wi][hi] = inverI2[wi][hi] + scale * meanI2[hi] - meanI22[hi];
				inverI2[wi][hi] = inverI2[wi][hi] / (scale);
			}
		}

		inverI = Array.Two2One(inverI2);

		if (type == "dec") {
			inverI = Array.array1Inverse(inverI);
		} else {
			for (int si = 0; si < inverI.length; si++) {
				inverI[si] = inverI[si] / (Math.pow(scale, 2));
			}
		}
		// System.out.println(Arrays.deepToString(inverI2));
		// return I;
		// /*

		return inverI;

	}

	/**
	 * LowHigh subband
	 * 
	 * @return
	 */
	public double[] LowHigh(double[] I, String type, int ndepth) {
		int scale = (int) Math.pow(2, ndepth);
		double[] inverI;
		if (type == "dec") {
			inverI = Array.array1Inverse(I);
		} else {
			inverI = I;
		}
		double[][] inverI2 = Array.One2Two(inverI, width, height);

		// System.out.println(Arrays.deepToString(inverI2));
		// Part 1
		double[] s = Array.array2Mean1(inverI2);
		double[][] rowI = Array.array2RowEx(inverI2, scale);

		inverI2 = Array.array2Minus(inverI2, rowI);

		inverI2 = Array.array2CumSum1(inverI2);

		double[] meanI1 = Array.array2Mean1(inverI2);

		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				inverI2[wi][hi] = inverI2[wi][hi] + scale * s[wi] - meanI1[wi];
			}
		}

		// Part 2
		double[][] colI = Array.array2ColEx(inverI2, scale);
		double[][] colI2 = Array.array2ColEx(inverI2, scale / 2);

		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				inverI2[wi][hi] = inverI2[wi][hi] - 2 * colI2[wi][hi] + colI[wi][hi];
			}
		}

		inverI2 = Array.array2CumSum2(inverI2);

		double[] meanI22 = Array.array2Mean2(inverI2);

		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				inverI2[wi][hi] = inverI2[wi][hi] - meanI22[hi];
				inverI2[wi][hi] = inverI2[wi][hi] / (scale);
			}
		}

		inverI = Array.Two2One(inverI2);

		if (type == "dec") {
			inverI = Array.array1Inverse(inverI);
		} else {
			for (int si = 0; si < inverI.length; si++) {
				inverI[si] = inverI[si] / (Math.pow(scale, 2));
			}
		}

		return inverI;

	}

	/**
	 * HighLow subband
	 * 
	 * @return
	 */
	public double[] HighLow(double[] I, String type, int ndepth) {
		int scale = (int) Math.pow(2, ndepth);
		double[] inverI;
		if (type == "dec") {
			inverI = Array.array1Inverse(I);
		} else {
			inverI = I;
		}
		double[][] inverI2 = Array.One2Two(inverI, width, height);

		// Part 1
		double[][] rowI = Array.array2RowEx(inverI2, scale);
		double[][] rowI2 = Array.array2RowEx(inverI2, scale / 2);

		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				inverI2[wi][hi] = inverI2[wi][hi] - 2 * rowI2[wi][hi] + rowI[wi][hi];
			}
		}

		inverI2 = Array.array2CumSum1(inverI2);

		double[] meanI1 = Array.array2Mean1(inverI2);

		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				inverI2[wi][hi] = inverI2[wi][hi] - meanI1[wi];
			}
		}

		// Part 2
		double[] meanI2 = Array.array2Mean2(inverI2);
		double[][] colI = Array.array2ColEx(inverI2, scale);

		inverI2 = Array.array2Minus(inverI2, colI);

		inverI2 = Array.array2CumSum2(inverI2);

		double[] meanI22 = Array.array2Mean2(inverI2);

		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				inverI2[wi][hi] = inverI2[wi][hi] + scale * meanI2[hi] - meanI22[hi];
				inverI2[wi][hi] = inverI2[wi][hi] / (scale);
			}
		}

		inverI = Array.Two2One(inverI2);

		if (type == "dec") {
			inverI = Array.array1Inverse(inverI);
		} else {
			for (int si = 0; si < inverI.length; si++) {
				inverI[si] = inverI[si] / (Math.pow(scale, 2));
			}
		}

		return inverI;

	}

	/**
	 * HighHigh subband
	 * 
	 * @return
	 */
	public double[] HighHigh(double[] I, String type, int ndepth) {
		int scale = (int) Math.pow(2, ndepth);
		double[] inverI;
		if (type == "dec") {
			inverI = Array.array1Inverse(I);
		} else {
			inverI = I;
		}
		double[][] inverI2 = Array.One2Two(inverI, width, height);

		// Part 1
		double[][] rowI = Array.array2RowEx(inverI2, scale);
		double[][] rowI2 = Array.array2RowEx(inverI2, scale / 2);

		// System.out.println(Arrays.deepToString(rowI));
		// return Two2One(inverI2);
		//
		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				inverI2[wi][hi] = inverI2[wi][hi] - 2 * rowI2[wi][hi] + rowI[wi][hi];
			}
		}

		inverI2 = Array.array2CumSum1(inverI2);

		double[] meanI1 = Array.array2Mean1(inverI2);

		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				inverI2[wi][hi] = inverI2[wi][hi] - meanI1[wi];
			}
		}

		// Part 2
		double[][] colI = Array.array2ColEx(inverI2, scale);
		double[][] colI2 = Array.array2ColEx(inverI2, scale / 2);

		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				inverI2[wi][hi] = inverI2[wi][hi] - 2 * colI2[wi][hi] + colI[wi][hi];
			}
		}

		inverI2 = Array.array2CumSum2(inverI2);

		double[] meanI22 = Array.array2Mean2(inverI2);

		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				inverI2[wi][hi] = inverI2[wi][hi] - meanI22[hi];
				inverI2[wi][hi] = inverI2[wi][hi] / (scale);
			}
		}

		inverI = Array.Two2One(inverI2);

		if (type == "dec") {
			inverI = Array.array1Inverse(inverI);
		} else {
			for (int si = 0; si < inverI.length; si++) {
				inverI[si] = inverI[si] / (Math.pow(scale, 2));
			}
		}

		return inverI;

	}

}
