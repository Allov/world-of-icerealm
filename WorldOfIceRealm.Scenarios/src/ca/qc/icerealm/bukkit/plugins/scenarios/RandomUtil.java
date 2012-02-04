package ca.qc.icerealm.bukkit.plugins.scenarios;

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
		
		return Double.parseDouble(random.toString());
	}
	
}
