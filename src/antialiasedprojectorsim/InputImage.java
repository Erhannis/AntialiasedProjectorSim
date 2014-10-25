/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package antialiasedprojectorsim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import mathnstuff.MeMath;
import mathnstuff.MeUtils;

/**
 * Note that I've tacitly made this n-dimensional.  Most people will probably
 * only use 2 dims, though.
 * Top-left is origin.
 * @author MEwer
 */
public abstract class InputImage {
    /** 
     * These are like, "the image is basically in this area."
     */
    public abstract double[] roughOffsets();
    public abstract double[] roughSize();
    
    /**
     * Returns the color of a region (probably the average).
     * All ranges should be valid.
     * @param offset
     * @param size
     * @return 
     */
    public abstract Color getRegionColor(double[] offset, double[] size);
    
    public static class RasterInputImage extends InputImage {
        private BufferedImage image;
        private double[] offsets;
        private double[] size;
        
        public RasterInputImage(BufferedImage image) {
            this.image = image;
            this.offsets = new double[] {0, 0};
            this.size = new double[] {image.getWidth(), image.getHeight()};
        }
        
        @Override
        public double[] roughOffsets() {
            return offsets;
        }

        @Override
        public double[] roughSize() {
            return size;
        }

        // Hmm.  I *think* this assumes that the region is at least one pixel big?
        @Override
        public Color getRegionColor(double[] offset, double[] size) {
            double[] sums = new double[4];
            double area = 0;

            int leftIntInside = (int)offset[0];
            if (leftIntInside < offset[0]) leftIntInside++;
            int topIntInside = (int)offset[1];
            if (topIntInside < offset[1]) topIntInside++;
            int rightIntInside = (int)(size[0] + offset[0]);
            int botIntInside = (int)(size[1] + offset[1]);
         
            // Whole internal pixels
            for (int y = topIntInside; y < botIntInside; y++) {
                for (int x = leftIntInside; x < rightIntInside; x++) {
                    int[] pix = MeUtils.intToARGB(getPixel(x, y));
                    for (int i = 0; i < 4; i++) {
                        sums[i] += pix[i];
                    }
                }
            }
            // Assuming non-negative area....
            area += (rightIntInside - leftIntInside) * (botIntInside - topIntInside);
            
            // Left edge
            for (int y = topIntInside; y < botIntInside; y++) {
                double x = offset[0];
                double width = leftIntInside - x;
                int[] pix = MeUtils.intToARGB(getPixel((int)x, y));
                for (int i = 0; i < 4; i++) {
                    sums[i] += (pix[i] * width);
                }
            }            
            // Assuming non-negative area....
            area += (leftIntInside - offset[0]) * (botIntInside - topIntInside);

            // Right edge
            for (int y = topIntInside; y < botIntInside; y++) {
                double x = rightIntInside;
                double width = (offset[0] + size[0]) - x;
                int[] pix = MeUtils.intToARGB(getPixel((int)x, y));
                for (int i = 0; i < 4; i++) {
                    sums[i] += (pix[i] * width);
                }
            }            
            // Assuming non-negative area....
            area += (offset[0] + size[0] - rightIntInside) * (botIntInside - topIntInside);

            // Top edge
            for (int x = leftIntInside; x < rightIntInside; x++) {
                double y = offset[1];
                double height = topIntInside - y;
                int[] pix = MeUtils.intToARGB(getPixel(x, (int)y));
                for (int i = 0; i < 4; i++) {
                    sums[i] += (pix[i] * height);
                }
            }            
            // Assuming non-negative area....
            area += (rightIntInside - leftIntInside) * (topIntInside - offset[1]);

            // Bottom edge
            for (int x = leftIntInside; x < rightIntInside; x++) {
                double y = botIntInside;
                double height = (offset[1] + size[1]) - y;
                int[] pix = MeUtils.intToARGB(getPixel(x, (int)y));
                for (int i = 0; i < 4; i++) {
                    sums[i] += (pix[i] * height);
                }
            }            
            // Assuming non-negative area....
            area += (rightIntInside - leftIntInside) * (offset[1] + size[1] - botIntInside);
            
            // Top left corner
            {
                double x = offset[0];
                double y = offset[1];
                double width = leftIntInside - x;
                double height = topIntInside - y;
                int[] pix = MeUtils.intToARGB(getPixel((int)x, (int)y));
                for (int i = 0; i < 4; i++) {
                    sums[i] += (pix[i] * width * height);
                }
            }
            // Assuming non-negative area....
            area += (leftIntInside - offset[0]) * (topIntInside - offset[1]);

            // Top right corner
            {
                double x = rightIntInside;
                double y = offset[1];
                double width = (offset[0] + size[0]) - x;
                double height = topIntInside - y;
                int[] pix = MeUtils.intToARGB(getPixel((int)x, (int)y));
                for (int i = 0; i < 4; i++) {
                    sums[i] += (pix[i] * width * height);
                }
            }
            // Assuming non-negative area....
            area += (offset[0] + size[0] - rightIntInside) * (topIntInside - offset[1]);
            
            // Bottom left corner
            {
                double x = offset[0];
                double y = botIntInside;
                double width = leftIntInside - x;
                double height = (offset[1] + size[1]) - y;
                int[] pix = MeUtils.intToARGB(getPixel((int)x, (int)y));
                for (int i = 0; i < 4; i++) {
                    sums[i] += (pix[i] * width * height);
                }
            }
            // Assuming non-negative area....
            area += (leftIntInside - offset[0]) * (offset[1] + size[1] - botIntInside);

            // Bottom right corner
            {
                double x = rightIntInside;
                double y = botIntInside;
                double width = (offset[0] + size[0]) - x;
                double height = (offset[1] + size[1]) - y;
                int[] pix = MeUtils.intToARGB(getPixel((int)x, (int)y));
                for (int i = 0; i < 4; i++) {
                    sums[i] += (pix[i] * width * height);
                }
            }
            // Assuming non-negative area....
            area += (offset[0] + size[0] - rightIntInside) * (offset[1] + size[1] - botIntInside);
            
            float[] avg = new float[sums.length];
            for (int i = 0; i < 4; i++) {
                // Also scaling to [0,1]
                avg[i] = (float)((sums[i] / area) / 0xFF);
            }
            
            return new Color(MeMath.bound(avg[1], 0, 1), MeMath.bound(avg[2], 0, 1), MeMath.bound(avg[3], 0, 1), MeMath.bound(avg[0], 0, 1));
        }
        
        public int getPixel(int x, int y) {
            if (x >= 0 && x < size[0] && y >= 0 && y < size[1]) {
                return image.getRGB(x, y);
            } else {
                return 0xFF000000;
            }
        }
    }
}
