package ca.qc.icerealm.bukkit.plugins.time;

/** 
 *	Interface that enable interaction with a TimeSubject. Use it to receive a call in a 
 *  predefined time span in the future. Use it with a TimeSubject implementation.
 */
public interface TimeObserver {
	
	/**
	 * This method is called when the alarm value is greater that System.getCurrentMillis();
	 * @param time
	 */
	void timeHasCome(long time);
	
	/**
	 * This is used by the time subject to set the alarm for this observer
	 * @param time
	 */
	void setAlaram(long time);
	
	/**
	 * This is used by the time subject to get the alarm for this observer
	 * @return
	 */
	long getAlarm();
	
}
