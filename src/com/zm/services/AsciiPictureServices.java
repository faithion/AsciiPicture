package com.zm.services;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AsciiPictureServices {
	private BufferedImage img;
	private BufferedImage img2;
	private StringBuilder asciiPictureTxt;
	private int maxGray;
	private int minGray;

	public AsciiPictureServices(String oldImgPath) throws Exception {
		asciiPictureTxt = new StringBuilder();
		BufferedImage img = ImageIO.read(new File(oldImgPath));
		img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		if (img.getWidth() >= 220) {
			double scale = img.getWidth() / 220;
			img2 = zoomBySize((int) (img.getWidth() / scale), (int) (img.getHeight() / (2*scale)), img);
		} else {
			img2 = zoomBySize(img.getWidth(), img.getHeight() / 2, img);
		}
		// ת�ڰ���Ƭ
		img2 = transformGray(img2);
		int[] minMax = maxMinGray(img);
		minGray = minMax[0];
		maxGray = minMax[1];
	}

	public String processing() throws Exception {
		for (int y = 0; y < img2.getHeight(); y++) {
			for (int x = 0; x < img2.getWidth(); x++) {
				asciiPictureTxt.append(getChar(Color.getColor("", img2.getRGB(x, y)), img));
			}
			asciiPictureTxt.append("\n");
		}
		return asciiPictureTxt.toString();
	}

	/**
	 * 
	 * @param color
	 * @return ����ѧ�Ҷ�ֵ
	 */
	public static int gray(Color color) {
		return (int) (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
	}

	/**
	 * ��С�Ҷ������Ҷ�
	 * 
	 * @param img
	 * @return ����0��min 1��max
	 */
	public static int[] maxMinGray(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();
		int max = 0;
		int min = 256;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (gray(Color.getColor("", img.getRGB(x, y))) > max) {
					max = gray(Color.getColor("", img.getRGB(x, y)));
				}
				if (gray(Color.getColor("", img.getRGB(x, y))) < min) {
					min = gray(Color.getColor("", img.getRGB(x, y)));
				}
			}
		}
		int[] minMax = { min, max };
		return minMax;
	}

	/**
	 * 
	 * @param width
	 * @param height
	 * @param ԴͼƬ
	 * @return Ŀ��ͼƬ
	 * @throws IOException
	 */
	public static BufferedImage zoomBySize(int width, int height, Image img) throws IOException {
		// �밴�������ŵĲ�ֻͬ����,����Ҫ��ȡ�µĳ��Ϳ�,������ͬ.
		Image _img = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		// ׼������ͼƬ
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.drawImage(_img, 0, 0, null);
		graphics.dispose();
		return image;
	}

	public static BufferedImage transformGray(BufferedImage img) {
		// ת�ڰ�ͼƬ
		int height = img.getHeight();
		int width = img.getWidth();
		BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				img2.setRGB(x, y, img.getRGB(x, y));
			}
		}
		return img2;
	}

	/**
	 * ��һ����ɫת���ɻҶ��ַ�
	 * 
	 * @param color
	 * @return
	 */
	public char getChar(Color color, BufferedImage img) {
		String xulieString = "@[]Oo=\\/*^`,. ";
		char[] xulieArray = xulieString.toCharArray();
		int length = xulieArray.length;
		int gray = gray(color);
		int unitcount = (int) ((maxGray - minGray) / length);
		if ((int) ((gray - minGray) / unitcount) == length) {
			return ' ';
		}
		return xulieArray[(int) ((gray - minGray) / unitcount)];
	}

	/**
	 * ����׼���õ���Ƭ
	 * 
	 * @param �ļ�·��(��������)
	 * @param �����ͼƬ
	 * @param ��Ƭ����
	 * @throws Exception
	 */
	public static void savePicture(String newFilePath, BufferedImage img, String type) throws Exception {
		File out = new File(newFilePath);
		if (!out.exists()) {
			out.createNewFile();
		}
		FileOutputStream output = new FileOutputStream(out);
		ImageIO.write(img, type, output);
	}

	public static void main(String[] args) {
		String oldImgPath = "src/girl.png";
		String txtPath = "src/girl.txt";
		try {
			AsciiPictureServices picture = new AsciiPictureServices(oldImgPath);
			picture.processing();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("ִ�н���");
		}

	}
}