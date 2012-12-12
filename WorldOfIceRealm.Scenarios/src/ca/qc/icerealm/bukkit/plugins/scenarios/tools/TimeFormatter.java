package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

public class TimeFormatter {

	private static int HOUR = 3600000;
	private static int MINUTES = 60000;
	private static int SECOND = 1000;
	
	public static String readableTime(long millisecond) {
		StringBuffer buf = new StringBuffer();
		
		int hour = (int)millisecond / HOUR;
		if (hour > 0) {
			buf.append(hour + "h ");
			millisecond = millisecond - (hour * HOUR);
		}
		
		int minute = (int)millisecond / MINUTES;
		if (minute > 0) {
			buf.append(minute + "m ");
		}
		else if (minute == 0 && hour > 0) {
			buf.append("00m ");
		}
		else if (minute > 0 && minute < 10) {
			buf.append("0" + minute + "m");
		}
		millisecond = millisecond - (minute * MINUTES);
		
		int second = (int)millisecond / SECOND;
		if (second > 0) {
			buf.append(second + "s");
		}
		
		return buf.toString();

	}
	
}
