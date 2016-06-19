package plugins.hijizhou.utilities;

public class Array {
	public static double[] Two2One(double[][] tI) {
		int width = tI.length;
		int height = tI[0].length;
		double[] rI = new double[width * height];
		int s = 0;
		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				rI[s++] = tI[wi][hi];
			}
		}
		return rI;
	}
	public static double[][] One2Two(double[] tI, int width, int height) {
		double[][] rI = new double[width][height];
		for (int hi = 0; hi < height; hi++) {
			for (int wi = 0; wi < width; wi++) {
				rI[wi][hi] = tI[(wi * height) + hi];
			}
		}
		return rI;
	}
	public static double[] array1Inverse(double[] tI) {
		double[] rI = new double[tI.length];
		for (int si = 0; si < tI.length; si++) {
			rI[si] = tI[tI.length - si - 1];
		}
		return rI;
	}

	public static double[] array1Add(double[] A, double[] B) {
		int length = A.length;
		double[] ApB = new double[length];
		for (int i = 1; i < length; i++) {
			ApB[i] = A[i] + B[i];
		}
		return ApB;
	}

	public static double[] array1Minus(double[] A, double[] B) {
		int length = A.length;
		double[] ApB = new double[length];
		for (int i = 1; i < length; i++) {
			ApB[i] = A[i] - B[i];
		}
		return ApB;
	}

	public static double[] array1Times(double[] A, double[] B) {
		int length = A.length;
		double[] ApB = new double[length];
		for (int i = 1; i < length; i++) {
			ApB[i] = A[i] * B[i];
		}
		return ApB;
	}

	public static double[] array1Div(double[] A, double[] B) {
		int length = A.length;
		double[] ApB = new double[length];
		for (int i = 1; i < length; i++) {
			ApB[i] = A[i] / B[i];
		}
		return ApB;
	}

	public static double[][] array2RowEx(double[][] tI, int scale) {
		int width = tI.length;
		int height = tI[0].length;
		double[][] rI = new double[width][height];
		int swi = 0;
		int shi = 0;
		for (int wi = 0; wi < width; wi++) {
			shi = 0;
			try {
				for (int hi = height - scale; hi < height; hi++) {
					rI[swi][shi++] = tI[wi][hi];
				}
			} catch (Exception e) {
				e.getMessage();
			}
			for (int hi = 0; hi < height - scale; hi++) {
				rI[swi][shi++] = tI[wi][hi];
			}
			swi = swi + 1;
		}
		return rI;
	}

	public static double[][] array2ColEx(double[][] tI, int scale) {
		int width = tI.length;
		int height = tI[0].length;
		double[][] rI = new double[width][height];
		int swi = 0;
		int shi = 0;
		for (int hi = 0; hi < height; hi++) {
			swi = 0;
			for (int wi = width - scale; wi < width; wi++) {
				rI[swi++][shi] = tI[wi][hi];
			}
			for (int wi = 0; wi < width - scale; wi++) {
				rI[swi++][shi] = tI[wi][hi];
			}
			shi = shi + 1;
		}
		return rI;
	}

	public static double[] array2Mean1(double[][] tI) {
		int width = tI.length;
		int height = tI[0].length;
		double[] s = new double[width];
		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				s[wi] = s[wi] + tI[wi][hi] / height;
			}
		}
		return s;
	}

	public static double[] array2Mean2(double[][] tI) {
		int width = tI.length;
		int height = tI[0].length;
		double[] s = new double[height];
		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				s[hi] = s[hi] + tI[wi][hi] / width;
			}
		}
		return s;
	}

	public static double[][] array2Add(double[][] A, double[][] B) {
		int width = A.length;
		int height = A[0].length;
		double[][] rI = new double[width][height];
		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				rI[wi][hi] = A[wi][hi] + B[wi][hi];
			}
		}
		return rI;
	}

	public static double[][] array2Minus(double[][] A, double[][] B) {
		int width = A.length;
		int height = A[0].length;
		double[][] rI = new double[width][height];
		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				rI[wi][hi] = A[wi][hi] - B[wi][hi];
			}
		}
		return rI;
	}

	public static double[][] array2Times(double[][] A, double[][] B) {
		int width = A.length;
		int height = A[0].length;
		double[][] rI = new double[width][height];
		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				rI[wi][hi] = A[wi][hi] * B[wi][hi];
			}
		}
		return rI;
	}

	public static double[][] array2Div(double[][] A, double[][] B) {
		int width = A.length;
		int height = A[0].length;
		double[][] rI = new double[width][height];
		for (int wi = 0; wi < width; wi++) {
			for (int hi = 0; hi < height; hi++) {
				rI[wi][hi] = A[wi][hi] / B[wi][hi];
			}
		}
		return rI;
	}

	public static double[][] array2CumSum1(double[][] tI) {
		int width = tI.length;
		int height = tI[0].length;
		double[][] cumSum = new double[width][height];
		for (int wi = 0; wi < width; wi++) {
			cumSum[wi][0] = tI[wi][0];
			for (int hi = 1; hi < height; hi++) {
				cumSum[wi][hi] = cumSum[wi][hi - 1] + tI[wi][hi];
			}
		}
		return cumSum;
	}

	public static double[][] array2CumSum2(double[][] tI) {
		int width = tI.length;
		int height = tI[0].length;
		double[][] cumSum = new double[width][height];
		for (int hi = 0; hi < height; hi++) {
			cumSum[0][hi] = tI[0][hi];
			for (int wi = 1; wi < width; wi++) {
				cumSum[wi][hi] = cumSum[wi - 1][hi] + tI[wi][hi];
			}
		}
		return cumSum;
	}
}
