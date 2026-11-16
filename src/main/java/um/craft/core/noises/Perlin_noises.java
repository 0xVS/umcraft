package um.craft.core.noises;

import org.joml.Vector2d;


import java.awt.*;
import java.awt.image.BufferedImage;

public class Perlin_noises {

    public static void main(String[] args) {
        Perlin_noises pn = new Perlin_noises();

        Color[][] pixels = new Color[500][500];

        for (int y = 0; y < 500; y++) {
            for (int x = 0; x < 500; x++) {
                // Noise2D generally returns a value approximately in the range [-1.0, 1.0]
                float n = (float) pn.noise2D(x, y, 3);

                // Transform the range to [0.0, 1.0], supposing that the range of Noise2D is [-1.0, 1.0]
                n += 1.0;
                n /= 2.0;

                int rgb = Math.round(255*n);
//                pixels[y][x] = new Color(rgb, rgb, rgb);

                if(n < 0.5){
                    pixels[y][x] = new Color(0,0,(rgb * 2));
                } else if (n < 0.9) {
                    pixels[y][x] = new Color(0, rgb, (int) Math.round(rgb * 0.5));
                }else {
                    pixels[y][x] = new Color(rgb, rgb, rgb);
                }

            }
        }
        int height = pixels.length;
        int width = pixels[0].length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, pixels[y][x].getRGB());
            }
        }

        System.out.println("fertig");
    }

    public int[] shuffle(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            int index = (int) Math.round(Math.random() * i);
            int temp = arr[i];

            arr[i] = arr[index];
            arr[index] = temp;
        }
        return arr;
    }

    public int[] makePermutation(){
        int[] permutationFirst = new int[256];
        for (int i = 0; i < permutationFirst.length; i++) {
            permutationFirst[i] = i;
        }
        permutationFirst = shuffle(permutationFirst);
        int[] permutation = new int[256 *2];

        for (int i = 0; i < permutationFirst.length; i++) {
            permutation[i] = permutationFirst[i];
        }

        for (int i = permutationFirst.length; i < permutation.length; i++) {
            permutation[i] = permutationFirst[i - permutationFirst.length];
        }

        return permutation;
    }

    int[] Permutation = makePermutation();

    public Vector2d getConstantVector(int v){
        int temp = v & 3;
        if ( temp == 0){
            return new Vector2d(1.0, 1.0);
        }else if(temp == 1){
            return new Vector2d(-1.0, 1.0);
        }else if (temp == 2){
            return new Vector2d(-1.0, -1.0);
        }else
            return new Vector2d(1.0, -1.0);
    }

    public double fade(double t){
        return ((6*t - 15)*t + 10)*t*t*t;
    }

    public double lerp(double t, double a1, double a2){
        return a1 + t*(a2-a1);
    }

    public double noise2D(double x, double y){
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;

        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);

        //warum sind die Vektoren gespiegelt?
        Vector2d topRight = new Vector2d(xf - 1.0, yf - 1.0);
        Vector2d topLeft = new Vector2d(xf, yf - 1.0);
        Vector2d bottomRight = new Vector2d(xf - 1.0, yf);
        Vector2d bottomLeft = new Vector2d(xf, yf);

        int valueTopRight = Permutation[Permutation[X + 1] + Y + 1];
        int valueTopLeft = Permutation[Permutation[X] + Y + 1];
        int valueBottomRight = Permutation[Permutation[X + 1] + Y];
        int valueBottomLeft = Permutation[Permutation[X] + Y];

        double dotTopRight = topRight.dot(getConstantVector(valueTopRight));
        double dotTopLeft = topLeft.dot(getConstantVector(valueTopLeft));
        double dotBottomRight = bottomRight.dot(getConstantVector(valueBottomRight));
        double dotBottomLeft = bottomLeft.dot(getConstantVector(valueBottomLeft));

        double u = fade(xf);
        double v = fade(yf);

        return lerp(u,
                lerp(v, dotBottomLeft, dotTopLeft),
                lerp(v, dotBottomRight, dotTopRight));
    }

    public double noise2D(double x, double y, int numOctaves){
//        double result = 0.0;
//        double amplitude = 1.0;
//        double frequency = 0.005;
//
//        for (int octave = 0; octave < numOctaves; octave++) {
//		    double n = amplitude * noise2D(x * frequency, y * frequency);
//            result += n;
//
//            amplitude *= 0.5;
//            frequency *= 2.0;
//        }
//
//        return result;

        double result = 0.0;
        double amplitude = 1.0;
        double frequency = 0.005;

        for (int octave = 0; octave < numOctaves; octave++) {
            double n = amplitude * noise2D(x * frequency, y * frequency);
            result += n;

            amplitude *= 0.5;
            frequency *= 2.0;
        }

        return result;


//        double result = 0.0;
//        double amplitude = 1.0;
//        double frequency = 0.005;
//        double maxValue = 0.0; // ← neu: maximaler möglicher Wert
//
//        for (int octave = 0; octave < numOctaves; octave++) {
//            double n = amplitude * noise2D(x * frequency, y * frequency);
//            result += n;
//
//            maxValue += amplitude; // ← summiere maximale Amplitude auf
//            amplitude *= 0.5;
//            frequency *= 2.0;
//        }
//
//        return result / maxValue; // ← normalisiere auf [-1, 1]
    }
}
