package plugins.hijizhou.Haar2D;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import icy.image.IcyBufferedImage;
import icy.sequence.Sequence;
import plugins.adufour.ezplug.EzPlug;
import plugins.adufour.ezplug.EzStoppable;
import plugins.adufour.ezplug.EzVarDouble;
import plugins.adufour.ezplug.EzVarSequence;
import plugins.hijizhou.utilities.Haar2D;

/**
 * 
 * @author Jizhou Li
 * 
 *         Icy Plugin to generate the decomposition and reconstruction of 2D
 *         Undecimated Haar Wavelet Transform
 */
public class Haar2DPlugin extends EzPlug implements EzStoppable {
	private EzVarSequence _seq;
	private EzVarDouble _level;

	@Override
	protected void initialize() {
		// Main components
		_seq = new EzVarSequence("Input");
		_level = new EzVarDouble("Level");
		_level.setValue(4D);
		addEzComponent(_seq);
		addEzComponent(_level);

	}

	@Override
	protected void execute() {
		// Inputs
		Sequence in = _seq.getValue(true);

		IcyBufferedImage img = in.getImage(0, 0);
		
//		Sequence testout = new Sequence();
//		testout.setImage(0, 0, img);
//
//		addSequence(testout);
		
		int width = img.getWidth();
		int height = img.getHeight();
		
		double[][] inputImg = new double[width][height];
		WritableRaster rasterIn = img.getRaster();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				inputImg[x][y] = rasterIn.getSample(x, y, 0);
			}
		}
		
		Haar2D wavelet = new Haar2D(inputImg, _level.getValue().intValue());
		wavelet.decomposition();

		// double[][] LLimg = wavelet.getLowHigh(1);
		// double[][] LLimg = wavelet.getHighHigh(1);
		// double[][] LLimg = wavelet.reconstructionByBand("HL",4);
		double[][] outimg = wavelet.reconstruction();
		double[][] outLL = wavelet.reconstructionByBand("LL",0);
		double[][] outHH = wavelet.reconstructionByBand("HH",1);
		
		addSequence(convertArray2ToSeq(outimg, "Reconstructed Image"));
		addSequence(convertArray2ToSeq(outLL, "Reconstructed Low-Low Subband"));
		addSequence(convertArray2ToSeq(outHH, "Reconstructed High-High Subband [level:1]"));

	}
	
	public Sequence convertArray2ToSeq(double[][] outimg, String name){
		Sequence out = new Sequence();
		int width = outimg.length;
		int height = outimg[0].length;
		BufferedImage outimgBuffered = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster rasterOut = outimgBuffered.getRaster();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				rasterOut.setSample(x, y, 0, outimg[x][y]);
			}
		}
		out.setImage(0, 0, outimgBuffered);
		out.setName(name);
		return out;
	}

	@Override
	public void clean() {

	}
}
