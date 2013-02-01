import java.util.Arrays;

public class Tank
{
	private double[] bottomLeft;
	private double[] topRight;
	boolean coordsSet;
	
	/**
	 * Sole constructor that creates a tank defined by two arrays that act as coordinates
	 * for two corners of the tank in a 3D space. The arrays are ordered as such:
	 */
	public Tank()
	{
		bottomLeft = new double[] {0.0,0.0,0.0};
		topRight = new double[] {0.0,0.0,0.0};
		coordsSet = false;
	}
	
	/**
	 * Defines the locations of the bottom left and top right corners of the tank to give
	 * location and dimensions to the space. The coordinates must be only 3 points each.
	 * 
	 * @param bL The bottom left corner's set of coordinates
	 * @param tR The top right corner's set of coordinates
	 * @throws IllegalArgumentException if the coordinates are not three points
	 */
	public void setCoordinates(double[] bL, double[] tR) throws IllegalArgumentException
	{
		boolean validCoords = false;
		try
		{
			validCoords = checkValidCoords(bL, tR);
		}
		catch(IllegalArgumentException ex)
		{
			throw ex;
		}
		if(validCoords)
		{
			bottomLeft = bL;
			topRight = tR;
			coordsSet = true;
		}
	}
	
	/**
	 * @return the double value of the height of the bottom of the tank from the ground
	 */
	public double getBottom()
	{
		return bottomLeft[2];
	}
	
	/**
	 * @return the double value of the height of the top of the tank from the ground
	 */
	public double getTop()
	{
		return topRight[2];
	}
	
	/**
	 * Calculates the area of the base of a tank by subtracting two pairs of points and
	 * multiplying them together
	 * 
	 * @return the area of the base
	 */
	public double baseArea()
	{
		return (Math.abs((topRight[1]-bottomLeft[1])*(topRight[0]-bottomLeft[0])));
	}
	
	/**
	 * Finds the height of a tank by taking the difference between the top and bottom
	 * 
	 * @return Height of a tank
	 */
	public double getHeight()
	{
		return (Math.abs(topRight[2]-bottomLeft[2]));
	}
	
	/**
	 * Finds the volume of a tank by taking the height of the tank multiplied by the base area
	 * 
	 * @return Volume of a tank
	 */
	public double getVolume()
	{
		return (Math.abs(baseArea() * getHeight()));
	}

	/**
	 * Checks if two tanks are equal
	 * 
	 * @param tank Tank to be compared to
	 * @return True if the tanks are equal; else, false
	 */
	@Override
	public boolean equals(Object tank)
	{
		Tank t = (Tank)tank;
		boolean retVal = false;
		if(Arrays.equals(this.bottomLeft, t.bottomLeft) && Arrays.equals(this.topRight, t.topRight) && (this.coordsSet == t.coordsSet))
		{
			retVal = true;
		}
		return retVal;
	}
	
	/**
	 * Finds the hash code of a tank
	 * 
	 * @return hash code of tank
	 */
	@Override
	public int hashCode()
	{
		double[] concatArray = new double[6];
		System.arraycopy(bottomLeft, 0, concatArray, 0, bottomLeft.length);
		System.arraycopy(topRight, 0, concatArray, bottomLeft.length, topRight.length);
		return Arrays.hashCode(concatArray);
	}
	
	/**
	 * Checks to see if a pair of 3d coordinates have positive length
	 * 
	 * @param bL Bottom left coordinates
	 * @param tR Top right coordinates
	 * @return True if positive, false if negative
	 */
	private boolean coordHasPosLength(double[] bL, double[] tR)
	{
		return (tR[0]-bL[0]>0 && tR[1]-bL[1]>0 && tR[2]-bL[2]>0);
	}
	
	/**
	 * Checks to see if the given coordinates are valid
	 * 
	 * @param bL Bottom left coordinate
	 * @param tR Top right coordinate
	 * @return True if valid coordinates, false if not
	 * @throws IllegalArgumentException If the coordinates are not valid
	 */
	private boolean checkValidCoords(double[] bL, double[] tR) throws IllegalArgumentException
	{
		boolean retVal = false;
		if(coordsSet == false)
		{
			retVal = true;
		}
		else
		{
			throw new IllegalArgumentException("Coordinates may only be set once.");
		}
		if(coordHasPosLength(bL, tR))
		{
			retVal = true;
		}
		else
		{
			throw new IllegalArgumentException("Coordinates must have positive length.");
		}
		if(bL.length == 3 && tR.length == 3)
		{
			retVal = true;
		}
		else
		{
			throw new IllegalArgumentException("Coordinate arrays must have length 3.");
		}
		return retVal;
	}
}