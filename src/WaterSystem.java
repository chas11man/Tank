import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

public class WaterSystem
{
	Set<Tank> system;
	
	/**
	 * Sole constructor
	 * 
	 * @param init_system Set of type Tank containing the tanks that are in the water system
	 */
	public WaterSystem(Set<Tank> init_system)
	{
		system = init_system;
	}
	
	/**
	 * Returns a NavigableMap that associates to each break point in the system, the set of
	 * tanks whose bottom is exactly at the break point. It looks through each tank in the
	 * system and adds them to the NavigableMap. If the bottom height of the tank is already
	 * in the NavigableMap, the Set of Tanks for that key is pulled out, added to the set,
	 * and put back in the NavigableMap. If the break point is new, a new Set is created.
	 * 
	 * @return the map of all tanks and their bottom heights 
	 */
	public NavigableMap<Double, Set<Tank>> tanksByBottom()
	{
		NavigableMap<Double, Set<Tank>> byBottom = new TreeMap<Double, Set<Tank>>();
		for(Tank t : system)
		{
			Set<Tank> temp = new HashSet<Tank>();
			if(byBottom.containsKey(t.getBottom()))
			{
				temp = byBottom.get(t.getBottom());
				temp.add(t);
			}
			else
			{
				temp.add(t);
			}
			byBottom.put(t.getBottom(), temp);
		}
		
		return byBottom;
	}
	
	/**
	 * Returns a NavigableMap that associates to each break point in the system, the set of
	 * tanks whose top is exactly at the break point. It looks through each tank in the
	 * system and adds them to the NavigableMap. If the top height of the tank is already
	 * in the NavigableMap, the Set of Tanks for that key is pulled out, added to the set,
	 * and put back in the NavigableMap. If the break point is new, a new Set is created.
	 * 
	 * @return the map of all tanks and their top heights
	 */
	public NavigableMap<Double, Set<Tank>> tanksByTop()
	{
		NavigableMap<Double, Set<Tank>> byTop = new TreeMap<Double, Set<Tank>>();
		for(Tank t : system)
		{
			Set<Tank> temp = new HashSet<Tank>();
			if(byTop.containsKey(t.getTop()))
			{
				temp = byTop.get(t.getTop());
				temp.add(t);
			}
			else
			{
				temp.add(t);
			}
			byTop.put(t.getTop(), temp);
		}
		return byTop;
	}
	
	/**
	 * At each break point in the system, check each tank in the system.  If the bottom of the
	 * tank is less than or equal to the break point and if the top is greater than it, the
	 * tank is added to a set that is then associated to the corresponding break point.
	 * 
	 * @return a map that associates each break point to its active tanks
	 */
	public NavigableMap<Double, Set<Tank>> activeTanks()
	{
		NavigableMap<Double, Set<Tank>> actives = new TreeMap<Double, Set<Tank>>();
		Set<Double> breakPoints = getBreakPoints();
		
		for(Double bP : breakPoints)
		{
			Set<Tank> activeAtBP = new HashSet<Tank>();
			for(Tank t : system)
			{
				if(t.getBottom() <= bP && t.getTop() > bP)
				{
					activeAtBP.add(t);
				}
			}
			actives.put(bP, activeAtBP);
		}
				
		return actives;
	}
	
	/**
	 * Checks every break point in the system. If a tank is active at that point,
	 * it's base area is added to a temporary count.  When the break point
	 * is done being checked, the association is added to the map.
	 * 
	 * @return a NavigableMap associating the break points to the active base area when the water is at that break point level.
	 */
	public NavigableMap<Double, Double> activeBaseArea()
	{
		NavigableMap<Double, Set<Tank>> actives = new TreeMap<Double, Set<Tank>>();
		actives = activeTanks();
		NavigableMap<Double, Double> activeBase = new TreeMap<Double, Double>();
		double baseArea;
		for(Double breakPoint : actives.keySet())
		{
			baseArea = 0;
			for(Tank active : actives.get(breakPoint))
			{
				baseArea += active.baseArea();
			}
			activeBase.put(breakPoint, baseArea);
		}
		return activeBase;
	}
	
	/**
	 * Returns a map that associates each tank with the level, as measured from the tank base,
	 * to which the tank is filled, assuming the given water height in the system.
	 * It checks every tank that has water in it based on the method filledTanksAtHeight.
	 * 
	 * @param waterHeight The level of the water in the system
	 * @return A map that associates each tank with the level to which the tank is filled
	 */
	public Map<Tank, Double> heightToTankLevel(Double waterHeight)
	{
		Map<Tank, Double> heightToTankLevel = new HashMap<Tank, Double>();
		for(Tank t : system)
		{
			if(t.getTop()<waterHeight)
			{
				heightToTankLevel.put(t, t.getHeight());
			}
			else if(t.getBottom()<waterHeight)
			{
				heightToTankLevel.put(t, Math.abs(waterHeight - t.getBottom()));
			}
			else
			{
				heightToTankLevel.put(t, 0.0);
			}
		}
		return heightToTankLevel;
	}
	
	/**
	 * Finds the total volume of water in the system at a given water level
	 * 
	 * @param waterHeight The level of the water in the system
	 * @return Volume of water in the system at a given water level
	 * @throws IllegalStateException if the volume is less than 0 or greater than the max possible volume 
	 */
	public Double heightToVolume(Double waterHeight) throws IllegalArgumentException
	{
		if(waterHeight <= tanksByTop().lastKey() && waterHeight >= tanksByBottom().firstKey())
		{
			Map<Tank, Double> heightToTankLevel = heightToTankLevel(waterHeight);
			double systemVolume = 0;
			for(Tank t : heightToTankLevel.keySet())
			{
				systemVolume += (heightToTankLevel.get(t) * t.baseArea());
			}
			return systemVolume;
		}
		else
		{
			throw new IllegalArgumentException("Water height is outside the range of the water system.");
		}
	}
	
	/**
	 * Given a volume, finds the level to which each tank is individually filled
	 * 
	 * @param waterVolume Volume of water in the system
	 * @return Map associating all tanks to the level that they are filled
	 * @throws IllegalArgumentException If the given volume is less than zero or greater than the maximum possible volume
	 */
	public Map<Tank, Double> volumeToTankLevel(Double waterVolume) throws IllegalArgumentException
	{
		if(waterVolume > 0 && waterVolume < getMaxVol())
		{
			Double waterLeft = waterVolume;
			Set<Tank> partFullTanks = partFullTanks(waterVolume);
			double fullTanksVol = volFullTanks(waterVolume);
			
			double[] partFullTanksNumbers = partFullTanksHelperNumbers(partFullTanks);
			double highestBottom = partFullTanksNumbers[0];
			double totalBaseArea = partFullTanksNumbers[1];
			
			//The volume of water between the bottom of the highest bottom and the actual water level of partially filled tanks
			waterLeft = waterVolume - fullTanksVol - heightToVolumeFromSet(highestBottom, partFullTanks);
			//The height of the volume of water in waterLeft
			double heightLeft = waterLeft / totalBaseArea;
			//The water level of the system
			double heightOfWater = highestBottom + heightLeft;
			
			//With the height of the water in the system now known, heightToTankLevel can now be used
			return heightToTankLevel(heightOfWater);
		}
		else
		{
			throw new IllegalArgumentException("Given water volume is negative or more than the maximum capacity of the system");
		}
	}
	
	//***********************Private Methods******************
	
	/**
	 * Finds the maximum volume of all the tanks in the system combined. It checks
	 * the volume of each tank and adds it to a total.
	 * 
	 * @return Maximum volume of the system
	 */
	private Double getMaxVol()
	{
		double volume = 0.0;
		for(Tank t : system)
		{
			volume += (t.getVolume());
		}
		return volume;
	}
	

	/**
	 * Finds the total volume of water in the set at a given water level
	 * 
	 * @param waterHeight Height of water in the system
	 * @param tanks Set of tanks to be considered
	 * @return volume of water in the set of tanks
	 */
	private Double heightToVolumeFromSet(Double waterHeight, Set<Tank> tanks)
	{
		Map<Tank, Double> heightToTankLevel = heightToTankLevel(waterHeight);
		double systemVolume = 0;
		for(Tank t : tanks)
		{
			systemVolume += (heightToTankLevel.get(t) * t.baseArea());
		}
		return systemVolume;
	}
	

	/**
	 * Finds all break points in the system
	 * 
	 * @return set of the break points
	 */
	private Set<Double> getBreakPoints()
	{
		Set<Double> breakPoints = new HashSet<Double>();
		for(Tank t : system)
		{
			breakPoints.add(t.getBottom());
			breakPoints.add(t.getTop());
		}
		return breakPoints;
	}
	
	
	/**
	 * For each tank in the system, as long as the volume of water in the system is less than the volume of
	 * the water level at the top of the tank and greater than if it was at the bottom of the tank,
	 * it can be inferred that the tank has water in it, but is not full
	 * 
	 * @param waterVolume volume of water in the system
	 * @return set of tanks that are partially full
	 */
	private Set<Tank> partFullTanks(Double waterVolume)
	{
		Set<Tank> partFullTanks = new HashSet<Tank>();
		for(Tank t : system)
		{
			if(heightToVolume(t.getTop()) > waterVolume && heightToVolume(t.getBottom()) < waterVolume)
			{
				partFullTanks.add(t);
			}
		}
		return partFullTanks;
	}
	
	
	/**
	 * If the volume of water in the system is greater than the volume of water in the system if the height
	 * of the water was at the top of a tank, we can infer that that tank is full of water. By adding all of
	 * their volumes, we have the combined volume of all full tanks in the system
	 * 
	 * @param waterVolume volume of water in the system
	 * @return volume of water only in completely full tanks
	 */
	private Double volFullTanks(Double waterVolume)
	{
		double fullTanksVol = 0.0;
		for(Tank t : system)
		{
			if(heightToVolume(t.getTop()) < waterVolume)
			{
				fullTanksVol += t.getVolume();
			}
		}
		return fullTanksVol;
	}
	

	/**
	 * Finds the partially filled tank with a bottom higher than any other partially filled tank
	 * and finds the total base area of all the partially filled tanks
	 * 
	 * @param partFullTanks set of tanks that are partially filled
	 * @return array containing the highest bottom of a tank and the total base area of all partially filled tanks
	 */
	private double[] partFullTanksHelperNumbers(Set<Tank> partFullTanks)
	{
		double highestBottom = 0.0;
		double totalBaseArea = 0.0;
		for(Tank t : partFullTanks)
		{
			if(t.getBottom() > highestBottom)
			{
				highestBottom = t.getBottom();
			}
			totalBaseArea += t.baseArea();
		}
		double[] retArray = new double[2];
		retArray[0] = highestBottom;
		retArray[1] = totalBaseArea;
		return retArray;
	}
}