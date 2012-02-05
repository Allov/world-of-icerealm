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
	
	/*
	 * Renvoie un nombre au hasard entre 0 et max
	 */
	public static int getRandomInt(int max) {
		Random r = new Random();
		return r.nextInt(max);
	}
	
	/*
	 * Permet d'obtenir le resultat d'un tirage. Un premier nombre est généré
	 * au hasard (entre 0 et number). Ensuite, un deuxieme nombre est généré et
	 * s'ils sont égaux, renvoie true, sinon false.
	 * Utile pour calculer des probabilités basé sur 1 chance sur N
	 */
	public static boolean getDrawResult(int number) {
		// on genere un nombre au hasard de 0 a N
		int magicNumber = RandomUtil.getRandomInt(number);
		
		// on refait un random
		int pickedNumber = RandomUtil.getRandomInt(number);
		
		// si les deux sont pareils, on a gagné!
		return magicNumber == pickedNumber;
	}
}
