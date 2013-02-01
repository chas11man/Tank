import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class WaterSystemTest {
	private Tank tankE = new Tank();
	private Tank tankF = new Tank();
	private Tank tankG = new Tank();
	private Tank tankH = new Tank();
	private Tank tankI = new Tank();
	
	private Tank tankNE = new Tank();
	private Tank tankNF = new Tank();
	private Tank tankNG = new Tank();
	private Tank tankNH = new Tank();
	
	Set<Tank> tanks = new HashSet<Tank>();
	Set<Tank> tanksN = new HashSet<Tank>();
	
	WaterSystem wS;
	WaterSystem wSN;
	
	@Before
	public void setupWaterSystem() {
		double[] e1 = {3,2,4};
		double[] e2 = {6,4,12};
		tankE.setCoordinates(e1, e2);
		double[] f1 = {5,3,6};
		double[] f2 = {10,6,17};
		tankF.setCoordinates(f1, f2);
		double[] g1 = {2,6,7};
		double[] g2 = {5,8,10};
		tankG.setCoordinates(g1, g2);
		double[] h1 = {1,10,7};
		double[] h2 = {2,12,12};
		tankH.setCoordinates(h1, h2);
		double[] i1 = {0,0,9};
		double[] i2 = {1,1,13};
		tankI.setCoordinates(i1, i2);
		
		tanks.add(tankE);
		tanks.add(tankF);
		tanks.add(tankG);
		tanks.add(tankH);
		tanks.add(tankI);
		
		wS = new WaterSystem(tanks);
	}
	
	@Before
	public void setupWaterSystemN() {
		double[] ne1 = {7,2,-18};
		double[] ne2 = {9,12,-7};
		tankNE.setCoordinates(ne1, ne2);
		double[] nf1 = {-15,-13,2};
		double[] nf2 = {4,6,20};
		tankNF.setCoordinates(nf1, nf2);
		double[] ng1 = {10,-8,-5};
		double[] ng2 = {18,1,-1};
		tankNG.setCoordinates(ng1, ng2);
		double[] nh1 = {13,14,-23};
		double[] nh2 = {21,30,3};
		tankNH.setCoordinates(nh1, nh2);
		
		tanksN.add(tankNE);
		tanksN.add(tankNF);
		tanksN.add(tankNG);
		tanksN.add(tankNH);
		
		wSN = new WaterSystem(tanksN);
	}
	
	@Test
	public void testCoordinates() {
		assertEquals(4, tankE.getBottom(), 0);
		assertEquals(12, tankH.getTop(), 0);
		assertEquals(15, tankF.baseArea(), 0);
		assertEquals(11, tankF.getHeight(), 0);
		assertEquals(18, tankG.getVolume(), 0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBadCoordinates() {
		Tank bad1 = new Tank();
		Tank bad2 = new Tank();
		
		double[] a1 = {0,0,0};
		double[] a2 = {-1,0,0};
		bad1.setCoordinates(a1, a2);
		double[] b1 = {2,3,4};
		double[] b2 = {5,1,4};
		bad1.setCoordinates(b1, b2);
		double[] c1 = {1,2,3,4};
		double[] c2 = {5,6,7,8};
		bad2.setCoordinates(c1, c2);
	}
	
	@Test
	public void testEquals() {
		Tank tankE1 = new Tank();
		double[] e11 = {3,2,4};
		double[] e12 = {6,4,12};
		tankE1.setCoordinates(e11, e12);
		assertTrue(tankE.equals(tankE1));
		
		Tank tankE2 = new Tank();
		double[] e21 = {1,1,1};
		double[] e22 = {6,4,12};
		tankE2.setCoordinates(e21, e22);
		assertFalse(tankE.equals(tankE2));
	}
	
	@Test
	public void testCoordinatesN() {
		assertEquals(-18, tankNE.getBottom(), 0);
		assertEquals(3, tankNH.getTop(), 0);
		assertEquals(361, tankNF.baseArea(), 0);
		assertEquals(18, tankNF.getHeight(), 0);
		assertEquals(288, tankNG.getVolume(), 0);
	}
	
	@Test
	public void testTanksByBottom() {
		NavigableMap<Double, Set<Tank>> bottom = new TreeMap<Double, Set<Tank>>();
		Set<Tank> tanksSet1 = new HashSet<Tank>();
		Set<Tank> tanksSet2 = new HashSet<Tank>();
		Set<Tank> tanksSet3 = new HashSet<Tank>();
		Set<Tank> tanksSet4 = new HashSet<Tank>();
		
		tanksSet1.add(tankE);
		bottom.put(4.0, tanksSet1);
		
		tanksSet2.add(tankF);
		bottom.put(6.0, tanksSet2);
		
		tanksSet3.add(tankG);
		tanksSet3.add(tankH);
		bottom.put(7.0, tanksSet3);
		
		tanksSet4.add(tankI);
		bottom.put(9.0, tanksSet4);
		
		assertEquals(bottom, wS.tanksByBottom());
	}
	
	@Test
	public void testTanksByBottomN() {
		NavigableMap<Double, Set<Tank>> bottom = new TreeMap<Double, Set<Tank>>();
		Set<Tank> tanksSet1 = new HashSet<Tank>();
		Set<Tank> tanksSet2 = new HashSet<Tank>();
		Set<Tank> tanksSet3 = new HashSet<Tank>();
		Set<Tank> tanksSet4 = new HashSet<Tank>();
		
		tanksSet1.add(tankNH);
		bottom.put(-23.0, tanksSet1);
		
		tanksSet2.add(tankNE);
		bottom.put(-18.0, tanksSet2);
		
		tanksSet3.add(tankNG);
		bottom.put(-5.0, tanksSet3);
		
		tanksSet4.add(tankNF);
		bottom.put(2.0, tanksSet4);
		
		assertEquals(bottom, wSN.tanksByBottom());
	}
	
	@Test
	public void testTanksByTop() {
		NavigableMap<Double, Set<Tank>> top = new TreeMap<Double, Set<Tank>>();
		Set<Tank> tanksSet1 = new HashSet<Tank>();
		Set<Tank> tanksSet2 = new HashSet<Tank>();
		Set<Tank> tanksSet3 = new HashSet<Tank>();
		Set<Tank> tanksSet4 = new HashSet<Tank>();
		
		tanksSet1.add(tankG);
		top.put(10.0, tanksSet1);
		
		tanksSet2.add(tankE);
		tanksSet2.add(tankH);
		top.put(12.0, tanksSet2);
		
		tanksSet4.add(tankI);
		top.put(13.0, tanksSet4);
		
		tanksSet3.add(tankF);
		top.put(17.0, tanksSet3);
		
		assertEquals(top, wS.tanksByTop());
	}
	
	@Test
	public void testTanksByTopN() {
		NavigableMap<Double, Set<Tank>> top = new TreeMap<Double, Set<Tank>>();
		Set<Tank> tanksSet1 = new HashSet<Tank>();
		Set<Tank> tanksSet2 = new HashSet<Tank>();
		Set<Tank> tanksSet3 = new HashSet<Tank>();
		Set<Tank> tanksSet4 = new HashSet<Tank>();
		
		tanksSet1.add(tankNE);
		top.put(-7.0, tanksSet1);
		
		tanksSet2.add(tankNG);
		top.put(-1.0, tanksSet2);
		
		tanksSet3.add(tankNH);
		top.put(3.0, tanksSet3);
		
		tanksSet4.add(tankNF);
		top.put(20.0, tanksSet4);
		
		assertEquals(top, wSN.tanksByTop());
	}
	
	@Test
	public void testActiveTanks() {
		NavigableMap<Double, Set<Tank>> aTanks = new TreeMap<Double, Set<Tank>>();
		Set<Tank> tanksSet4 = new HashSet<Tank>();
		Set<Tank> tanksSet6 = new HashSet<Tank>();
		Set<Tank> tanksSet7 = new HashSet<Tank>();
		Set<Tank> tanksSet9 = new HashSet<Tank>();
		Set<Tank> tanksSet10 = new HashSet<Tank>();
		Set<Tank> tanksSet12 = new HashSet<Tank>();
		Set<Tank> tanksSet13 = new HashSet<Tank>();
		Set<Tank> tanksSet17 = new HashSet<Tank>();
		
		tanksSet4.add(tankE);
		
		tanksSet6.add(tankE);
		tanksSet6.add(tankF);
		
		tanksSet7.add(tankE);
		tanksSet7.add(tankF);
		tanksSet7.add(tankG);
		tanksSet7.add(tankH);
		
		tanksSet9.add(tankE);
		tanksSet9.add(tankF);
		tanksSet9.add(tankG);
		tanksSet9.add(tankH);
		tanksSet9.add(tankI);
		
		tanksSet10.add(tankE);
		tanksSet10.add(tankF);
		tanksSet10.add(tankH);
		tanksSet10.add(tankI);
		
		tanksSet12.add(tankF);
		tanksSet12.add(tankI);
		
		tanksSet13.add(tankF);
		
		aTanks.put(4.0, tanksSet4);
		aTanks.put(6.0, tanksSet6);
		aTanks.put(7.0, tanksSet7);
		aTanks.put(9.0, tanksSet9);
		aTanks.put(10.0, tanksSet10);
		aTanks.put(12.0, tanksSet12);
		aTanks.put(13.0, tanksSet13);
		aTanks.put(17.0, tanksSet17);
		
		assertEquals(aTanks, wS.activeTanks());
	}
	
	@Test
	public void testActiveTanksN() {
		NavigableMap<Double, Set<Tank>> aTanks = new TreeMap<Double, Set<Tank>>();
		Set<Tank> tanksSet1 = new HashSet<Tank>();
		Set<Tank> tanksSet2 = new HashSet<Tank>();
		Set<Tank> tanksSet3 = new HashSet<Tank>();
		Set<Tank> tanksSet4 = new HashSet<Tank>();
		Set<Tank> tanksSet5 = new HashSet<Tank>();
		Set<Tank> tanksSet6 = new HashSet<Tank>();
		Set<Tank> tanksSet7 = new HashSet<Tank>();
		Set<Tank> tanksSet8 = new HashSet<Tank>();
		
		tanksSet1.add(tankNH);
		
		tanksSet2.add(tankNE);
		tanksSet2.add(tankNH);
		
		tanksSet3.add(tankNH);
		
		tanksSet4.add(tankNH);
		tanksSet4.add(tankNG);
		
		tanksSet5.add(tankNH);
		
		tanksSet6.add(tankNF);
		tanksSet6.add(tankNH);
		
		tanksSet7.add(tankNF);
		
		aTanks.put(-23.0, tanksSet1);
		aTanks.put(-18.0, tanksSet2);
		aTanks.put(-7.0, tanksSet3);
		aTanks.put(-5.0, tanksSet4);
		aTanks.put(-1.0, tanksSet5);
		aTanks.put(2.0, tanksSet6);
		aTanks.put(3.0, tanksSet7);
		aTanks.put(20.0, tanksSet8);
		
		assertEquals(aTanks, wSN.activeTanks());
	}
	
	@Test
	public void testActiveBaseArea() {
		NavigableMap<Double, Double> aBA = new TreeMap<Double, Double>();
		
		double e, f, g, h, i;
		
		e = tankE.baseArea();
		f = tankF.baseArea();
		g = tankG.baseArea();
		h = tankH.baseArea();
		i = tankI.baseArea();
		
		aBA.put(4.0, e);
		aBA.put(6.0, e+f);
		aBA.put(7.0, e+f+g+h);
		aBA.put(9.0, e+f+g+h+i);
		aBA.put(10.0, e+f+h+i);
		aBA.put(12.0, f+i);
		aBA.put(13.0, f);
		aBA.put(17.0, 0.0);
		
		assertEquals(aBA, wS.activeBaseArea());
	}
	
	@Test
	public void testHeightToTankLevel() {
		Map<Tank, Double> testMap = new HashMap<Tank, Double>();
		testMap.put(tankE, 7.0);
		testMap.put(tankF, 5.0);
		testMap.put(tankG, 3.0);
		testMap.put(tankH, 4.0);
		testMap.put(tankI, 2.0);
		
		assertEquals(testMap, wS.heightToTankLevel(11.0));
	}
	
	@Test
	public void testHeightToVolume() {
		assertEquals(145, wS.heightToVolume(11.0), 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testHeightToVolumeBad() {
		wS.heightToVolume(20.0);
	}

	@Test
	public void testVolumeToTankLevel() {
		Map<Tank, Double> testMap = new HashMap<Tank, Double>();
		testMap.put(tankE, 6.5);
		testMap.put(tankF, 4.5);
		testMap.put(tankG, 3.0);
		testMap.put(tankH, 3.5);
		testMap.put(tankI, 1.5);
		assertEquals(testMap, wS.volumeToTankLevel(133.0));
	}
}