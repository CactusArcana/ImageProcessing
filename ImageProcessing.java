import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;

public class ImageProcessing {
	public static void main(String[] args) {

        // This code allows you to convery an image into a 2D Array of RGBA values,
        // edit those values in a number of ways, and convert them back to a new image
        // You can paste in the URL for an image of your choosing to test out the edits
		
        // call the imgToTwoD method to store your image data in the imageData 2D Array
        int[][] imageData = imgToTwoD("./apple.jpg");
        
        // call the twoDToImage method passing the image into the negativeColor method and signifying the location of the new image
        twoDToImage(negativeColor(imageData), "./negative_apple.jpg");

        // call the twoDToImage method passing the image into the trimBorders method and signifying the location of the new image
		int[][] trimmed = trimBorders(imageData, 60);
		twoDToImage(trimmed, "./trimmed_apple.jpg");

        // call the twoDToImage method passing the image into the stretchHorizontally method and signifying the location of the new image
        int[][] stretched = stretchHorizontally(imageData);
        twoDToImage(stretched, "./stretched_apple.jpg");

        // call the twoDToImage method passing the image into the shrinkVertically method and signifying the location of the new image
        int[][] shrank = shrinkVertically(imageData);
        twoDToImage(shrank, "./shrank_apple.jpg");

        // call the twoDToImage method passing the image into the invertImage method and signifying the location of the new image
        int[][] inverted = invertImage(imageData);
        twoDToImage(inverted, "./inverted_apple.jpg");

        // call the twoDToImage method passing the image into the colorFilter method and signifying the location of the new image
        int[][] filter = colorFilter(imageData, -200, 100, -67);
        twoDToImage(filter, "./filtered_apple.jpg");

        // create a painting of rectangles using the paintRandomImage method
        int[][] painting = new int[500][500];
        int[][] brush = paintRandomImage(painting);
        twoDToImage(painting, "./painting_apple.jpg");

        // call the paintRectangle method to paint a rectangle on top of your image
        int[] colorRec = {230, 77, 168, 255};
        int[][] rectangle = paintRectangle(imageData, 87, 65, 40, 40, getColorIntValFromRGBA(colorRec));
        twoDToImage(rectangle, "./paintRec_apple.jpg");
   
        // call generateRectangles method to generate randome rectangles in your image
        int[][] randRectangles = generateRectangles(imageData, 7);
        twoDToImage(randRectangles, "./randRectangles_apple.jpg");

	}
	// Image Processing Methods

    // method to trim borders of an image
	public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {
		if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
			int[][] trimmedImg = new int[imageTwoD.length - pixelCount * 2][imageTwoD[0].length - pixelCount * 2];
			for (int i = 0; i < trimmedImg.length; i++) {
				for (int j = 0; j < trimmedImg[i].length; j++) {
					trimmedImg[i][j] = imageTwoD[i + pixelCount][j + pixelCount];
				}
			}
			return trimmedImg;
		} else {
			System.out.println("Cannot trim that many pixels from the given image.");
			return imageTwoD;
		}
	}

    // method to 
	public static int[][] negativeColor(int[][] imageTwoD) {
        int[][] negative = new int[imageTwoD.length][imageTwoD[0].length];

        for (int i = 0; i < negative.length; i++) {
            for (int j = 0; j < negative[0].length; j++) {
            //get pixel data from pixel and store in an array
            int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);

            //set the data in the array to the negative version
            rgba[0] = 255 - rgba[0];
            rgba[1] = 255 - rgba[1];
            rgba[2] = 255 - rgba[2];

            //get int hexadecimal pixel data from the array and store in the 2D array negative
            negative[i][j] = getColorIntValFromRGBA(rgba);
      }
    }
		return negative;
	}

    // method to stretch image horizontally by duplicating pixels
	public static int[][] stretchHorizontally(int[][] imageTwoD) {
	
        //create stretched object that is twice the width of the given image
        int[][] stretched = new int[imageTwoD.length][2 * imageTwoD[0].length];

        // interate through pixels 
        int position = 0;
        for (int rowIndex = 0; rowIndex < imageTwoD.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < imageTwoD[0].length; columnIndex++) {
            position = columnIndex * 2;

            // duplicate and copy the pixels to the stretched 2DArray
            stretched[rowIndex][position] = imageTwoD[rowIndex][columnIndex];
            stretched[rowIndex][position + 1] = imageTwoD[rowIndex][columnIndex];
            }
        }
		return stretched;
	}

    // method to shrink the image vertically
	public static int[][] shrinkVertically(int[][] imageTwoD) {

        //create new 2D Array
		int[][] shrank = new int[imageTwoD.length / 2][imageTwoD[0].length];

        // for loops to itterate through the given 2D Array
        for (int i = 0; i < imageTwoD[0].length; i++) {
            for (int j = 0; j < imageTwoD.length - 1; j+=2) {
        
            // copy only half of the vertical pixels to the new 2D Array
            shrank[j / 2][i] = imageTwoD[j][i];
            }
        }
		return shrank;
	}

    // method to invert the image color
	public static int[][] invertImage(int[][] imageTwoD) {
		int[][] inverted = new int[imageTwoD.length][imageTwoD[0].length];

        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[0].length; j++) {

            inverted[i][j] = imageTwoD[(imageTwoD.length - 1) - i][(imageTwoD[i].length - 1) - j];
            }
        }
		return inverted;
	}

    // method to apply color filter with the given parameters
	public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue, int blueChangeValue) {
		
        int[][] filter = new int[imageTwoD.length][imageTwoD[0].length];

        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[0].length; j++) {
            int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);

            // copy edited pixel values
            int newRed = rgba[0] + redChangeValue;
            int newGreen = rgba[1] + greenChangeValue;
            int newBlue = rgba[2] + blueChangeValue;

            // set red values to max/min if they go over max/min values
            if (newRed > 255) {
                newRed = 255;
            } 
            else if (newRed < 0) {
                newRed = 0;
            }

            // set green values to max/min if they go over max/min values
            if (newGreen > 255) {
                newGreen = 255;
            } 
            else if (newGreen < 0) {
                newGreen = 0;
            }

            // set blue values to max/min if they go over max/min values
            if (newBlue > 255) {
                newBlue = 255;
            } 
            else if (newBlue < 0) {
            newBlue = 0;
            }

            // set the filtered values if any changed after adjustment
            rgba[0] = newRed;
            rgba[1] = newGreen;
            rgba[2] = newBlue;

            // set the new rgba value into the filtered 2D Array
            filter[i][j] = getColorIntValFromRGBA(rgba);

        }
    }
		return filter;
	}

	// Painting Methods
	
    // method to paint a random image
    public static int[][] paintRandomImage(int[][] canvas) {
	    Random rand = new Random();

        // for loops to create each random rectangle
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[0].length; j++) {
            int randRed = rand.nextInt(256);
            int randGreen = rand.nextInt(256);
            int randBlue = rand.nextInt(256);

            // create an array using the random values to select the rectangle color
            int[] rgba ={randRed, randGreen, randBlue, 255};

            // place value into 2D Array
            canvas[i][j] = getColorIntValFromRGBA(rgba);
            }
        }  
  	    return canvas;
	}

    // method to paint a rectangle using the given parameters
	public static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition, int color) {
		
        // iterate through the image data and paint the rectangle using the passed parameters
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[0].length; j++) {
                if (i >= rowPosition && i <= rowPosition + width) {
                    if (j >= colPosition && j <= colPosition + height) {
                    canvas[i][j] = color;
                    }
                }
            }
        }
		return canvas;
	}

    // method to generate random rectangles in your image
	public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
		Random rand = new Random();

        // for loop for each rectangle
        for (int i = 0; i < numRectangles; i++) {

            // create variables and random data for the rectangle's height and width
            int randWidth = rand.nextInt(canvas[0].length);
            int randHeight = rand.nextInt(canvas.length);

            // create variables and random data for the rectangle's position in the image
            int randRow = rand.nextInt(canvas[0].length - randHeight);
            int randCol = rand.nextInt(canvas.length - randWidth);

            // create variables and random data for the rectangle's color
            int red = rand.nextInt(256);
            int green = rand.nextInt(256);
            int blue = rand.nextInt(256);

            // create variables and store color data
            int[] rgba = {red, green, blue, 255};
            int randColor = getColorIntValFromRGBA(rgba);

            // store the generated rectangle in the canvas 2D Array
            canvas = paintRectangle(canvas, randWidth, randHeight, randRow, randCol, randColor);

        }
		return canvas;
	}

	// Utility Methods
	
    // converts image data and stores it in a 2D Array
    public static int[][] imgToTwoD(String inputFileOrLink) {
		try {
			BufferedImage image = null;
			if (inputFileOrLink.substring(0, 4).toLowerCase().equals("http")) {
				URL imageUrl = new URL(inputFileOrLink);
				image = ImageIO.read(imageUrl);
				if (image == null) {
					System.out.println("Failed to get image from provided URL.");
				}
			} else {
				image = ImageIO.read(new File(inputFileOrLink));
			}
			int imgRows = image.getHeight();
			int imgCols = image.getWidth();
			int[][] pixelData = new int[imgRows][imgCols];
			for (int i = 0; i < imgRows; i++) {
				for (int j = 0; j < imgCols; j++) {
					pixelData[i][j] = image.getRGB(j, i);
				}
			}
			return pixelData;
		} catch (Exception e) {
			System.out.println("Failed to load image: " + e.getLocalizedMessage());
			return null;
		}
	}

    // converts 2D Array into an image
	public static void twoDToImage(int[][] imgData, String fileName) {
		try {
			int imgRows = imgData.length;
			int imgCols = imgData[0].length;
			BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < imgRows; i++) {
				for (int j = 0; j < imgCols; j++) {
					result.setRGB(j, i, imgData[i][j]);
				}
			}
			File output = new File(fileName);
			ImageIO.write(result, "jpg", output);
		} catch (Exception e) {
			System.out.println("Failed to save image: " + e.getLocalizedMessage());
		}
	}

    // grabs the color values from pixel
	public static int[] getRGBAFromPixel(int pixelColorValue) {
		Color pixelColor = new Color(pixelColorValue);
		return new int[] { pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha() };
	}

    // gets the color value from RGBA as an int
	public static int getColorIntValFromRGBA(int[] colorData) {
		if (colorData.length == 4) {
			Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
			return color.getRGB();
		} else {
			System.out.println("Incorrect number of elements in RGBA array.");
			return -1;
		}
	}

    /*
	public static void viewImageData(int[][] imageTwoD) {
		if (imageTwoD.length > 3 && imageTwoD[0].length > 3) {
			int[][] rawPixels = new int[3][3];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					rawPixels[i][j] = imageTwoD[i][j];
				}
			}
			System.out.println("Raw pixel data from the top left corner.");
			System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");
			int[][][] rgbPixels = new int[3][3][4];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
				}
			}
			System.out.println();
			System.out.println("Extracted RGBA pixel data from top the left corner.");
			for (int[][] row : rgbPixels) {
				System.out.print(Arrays.deepToString(row) + System.lineSeparator());
			}
		} else {
			System.out.println("The image is not large enough to extract 9 pixels from the top left corner");
		}
	}*/
}