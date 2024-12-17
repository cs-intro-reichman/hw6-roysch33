import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		/// 
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] image;

		// Tests the horizontal flipping of an image:
		image = flippedHorizontally(tinypic);
		System.out.println();
		print(image);

		// Tests the vertical flipping of an image:
		image = flippedVertically(tinypic);
		System.out.println();
		print(image);
		
		image = grayScaled(tinypic);
		System.out.println();
		print(image);

		image = scaled(tinypic, 3, 5);
		System.out.println();
		print(image);

		Color[][] ironman = read("ironman.ppm");
		Color[][] thor = read("thor.ppm");

		Color color1 = new Color(100, 40, 100);
		Color color2 = new Color (200, 20, 40);
		System.out.println(blend(color1,color2,0.25));

		//setCanvas(ironman);
		//display(ironman);
		//display(blend(ironman,grayScaled(ironman),1));
		morph(thor,ironman,3);

		
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		// Reads the RGB values from the file into the image array. 
		// For each pixel (i,j), reads 3 values from the file,
		// creates from the 3 colors a new Color object, and 
		// makes pixel (i,j) refer to that object.
		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				// Read the next three integers for RGB values
				int r = in.readInt();
				int g = in.readInt();
				int b = in.readInt();
				// Create a new Color object with the RGB values
				image[i][j] = new Color(r, g, b);
			}
		}
		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		// checking if the image is null:
		if (image == null) {
			System.out.println("Error: The image array is null.");
			return;
		}
		
		for (int i = 0; i < image.length; i++){
			// Loop through each column of the current row
			for (int j = 0; j < image[i].length; j++) {
				// Get the Color object at the current position
				Color pixel = image[i][j];
				// Print the RGB values of the pixel in the format (r,g,b)
				print(pixel);
			}
			// Move to the next line after printing all columns of the current row
			System.out.println();
		}
		//// Replace this comment with your code
		//// Notice that all you have to so is print every element (i,j) of the array using the print(Color) function.
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		Color[][] newImage = new Color[image.length][image[0].length];
		for (int i = 0; i < image.length; i++ ){
			for (int j = 0; j < image[i].length; j++){				
				newImage[i][image[i].length-j-1] = image[i][j];
			}
		}
		return newImage;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image){
		Color[][] newImage = new Color[image.length][image[0].length];
		for (int i = 0; i < image.length; i++ ){
			for (int j = 0; j < image[i].length; j++){				
				newImage[image.length-i-1][j] = image[i][j];
			}
		}
		return newImage;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	private static Color luminance(Color pixel) {
		int r = pixel.getRed();   // get the red component
		int g = pixel.getGreen(); // get the green component
        int b = pixel.getBlue();  // get the the blue component
		// Compute the luminance value as a weighted sum:
		int lum = (int) (0.299 * r + 0.587 * g + 0.114 * b);
		// Create and return a grayscale color with r = g = b = lum:
		return new Color(lum, lum, lum);
	}
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
		Color[][] grayImage = new Color[image.length][image[0].length];
		for (int i = 0; i < image.length; i++ ){
			for (int j = 0; j < image[i].length; j++){				
				grayImage[i][j] = luminance(image[i][j]);
			}
		}
		return grayImage;
	}	
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) {
		int originalWidth = image[0].length;  // Number of columns in the original image
		int originalHeight = image.length;   // Number of rows in the original image
	
		// Create a new image with the specified dimensions
		Color[][] scaledImage = new Color[height][width]; // Height = rows, Width = columns
	
		// Compute scaling factors for width and height
		double widthScale = (double) originalWidth / width;   // Horizontal scaling factor
		double heightScale = (double) originalHeight / height; // Vertical scaling factor
	
		// Map each pixel in the new image to the corresponding pixel in the original image
		for (int i = 0; i < height; i++) {  // Rows correspond to the height
			for (int j = 0; j < width; j++) { // Columns correspond to the width
				// Find the corresponding pixel in the original image
				int originalX = (int) (j * widthScale); // Column index
				int originalY = (int) (i * heightScale); // Row index
	
				// Assign the color from the original image to the scaled image
				scaledImage[i][j] = image[originalY][originalX];
			}
		}	
		
		return scaledImage;
	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		int r1 = c1.getRed();   // get the red component
		int g1 = c1.getGreen(); // get the green component
        int b1 = c1.getBlue();  // get the the blue component
		int r2 = c2.getRed();   // get the red component
		int g2 = c2.getGreen(); // get the green component
        int b2 = c2.getBlue();  // get the the blue component
		Color newColor = new Color((int)((alpha*r1) + ((1-alpha) * r2)) ,(int)((alpha*g1) + ((1-alpha) * g2)),(int)((alpha*b1) + ((1-alpha) * b2)));
		return newColor;
	}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		Color[][] blendedImage = new Color[image1.length][image1[0].length];
		for (int i = 0; i < image1.length; i++ ){
			for (int j = 0; j < image1[i].length; j++){				
				blendedImage[i][j] = blend(image1[i][j], image2[i][j], alpha);
			}
		}
		return blendedImage;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		//// Replace this comment with your code
		Color[][] newTarget = scaled(target,source[0].length,source.length);
		for (int i = 0; i < n+1; i++){
			double alpha = (double)(n-i)/n;
			Color[][] newBlend = blend(source,newTarget,alpha);
			setCanvas(newBlend);
			display(newBlend);
			StdDraw.pause(500); 
		}
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(width, height);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

