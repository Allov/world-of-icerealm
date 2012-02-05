package ca.qc.icerealm.bukkit.plugins.common;

import java.util.Random;

public class RandomUtil {

	public static double getRandomDouble(Double min, Double max) {
		int iMax = max.intValue();
		int iMin = min.intValue();
		Random r = new Random();
		
		Integer random = r.nextInt(iMax);
		
		while (random < iMin) {
			random = r.nextInt();
		}

		return random.doubleValue();
	}
	
	public static int getRandomInt(int max) {
		Random r = new Random();
		return r.nextInt(max);
	}
	
}
