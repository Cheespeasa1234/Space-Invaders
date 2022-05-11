/*from  w w w  .j a va 2  s .c  o m
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class Blur {
    /**
     * Applies a gaussian blur of the given radius to the given {@link BufferedImage} using a kernel
     * convolution.
     *
     * @param source The source image.
     * @param radius The blur radius, in pixels.
     * @return A new, blurred image, or the source image if no blur is performed.
     */
    public static BufferedImage blurredImage(BufferedImage source,
            double radius) {
        if (radius == 0) {
            return source;
        }

        final int r = (int) Math.ceil(radius);
        final int rows = r * 2 + 1;
        final float[] kernelData = new float[rows * rows];

        final double sigma = radius / 3;
        final double sigma22 = 2 * sigma * sigma;
        final double sqrtPiSigma22 = Math.sqrt(Math.PI * sigma22);
        final double radius2 = radius * radius;

        double total = 0;
        int index = 0;
        double distance2;

        int x, y;
        for (y = -r; y <= r; y++) {
            for (x = -r; x <= r; x++) {
                distance2 = 1.0 * x * x + 1.0 * y * y;
                if (distance2 > radius2) {
                    kernelData[index] = 0;
                } else {
                    kernelData[index] = (float) (Math.exp(-distance2
                            / sigma22) / sqrtPiSigma22);
                }
                total += kernelData[index];
                ++index;
            }
        }

        for (index = 0; index < kernelData.length; index++) {
            kernelData[index] /= total;
        }

        // We first pad the image so the kernel can operate at the edges.
        BufferedImage paddedSource = paddedImage(source, r);
        BufferedImage blurredPaddedImage = operatedImage(paddedSource,
                new ConvolveOp(new Kernel(rows, rows, kernelData),
                        ConvolveOp.EDGE_ZERO_FILL, null));
        return blurredPaddedImage.getSubimage(r, r, source.getWidth(),
                source.getHeight());
    }

    /**
     * Pads the given {@link BufferedImage} on all sides by the given padding amount.
     *
     * @param source  The source image.
     * @param padding The amount to pad on all sides, in pixels.
     * @return A new, padded image, or the source image if no padding is performed.
     */
    public static BufferedImage paddedImage(BufferedImage source,
            int padding) {
        if (padding == 0) {
            return source;
        }

        BufferedImage newImage = newArgbBufferedImage(source.getWidth()
                + padding * 2, source.getHeight() + padding * 2);
        Graphics2D g = (Graphics2D) newImage.getGraphics();
        g.drawImage(source, padding, padding, null);
        return newImage;
    }

    /**
     * Applies a {@link BufferedImageOp} on the given {@link BufferedImage}.
     *
     * @param source The source image.
     * @param op     The operation to perform.
     * @return A new image with the operation performed.
     */
    public static BufferedImage operatedImage(BufferedImage source,
            BufferedImageOp op) {
        BufferedImage newImage = newArgbBufferedImage(source.getWidth(),
                source.getHeight());
        Graphics2D g = (Graphics2D) newImage.getGraphics();
        g.drawImage(source, op, 0, 0);
        return newImage;
    }

    /**
     * Creates a new ARGB {@link BufferedImage} of the given width and height.
     *
     * @param width  The width of the new image.
     * @param height The height of the new image.
     * @return The newly created image.
     */
    public static BufferedImage newArgbBufferedImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
}
