package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.Formattable;
import java.util.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class LoggerFormater extends SimpleFormatter {

	@Override
	public String format(LogRecord rec) {
		Date timestamp = new Date(rec.getMillis());
		StringBuffer buf = new StringBuffer();
		if (timestamp.getHours() < 10) { buf.append("0"); }
		buf.append(timestamp.getHours() + ":");
		if (timestamp.getMinutes() < 10) { buf.append("0"); }
		buf.append(timestamp.getMinutes() + ":");
		if (timestamp.getSeconds() < 10) { buf.append("0"); }
		buf.append(timestamp.getSeconds() + " ");
		buf.append(rec.getLevel().toString() + " ");
		buf.append(rec.getMessage() + "\n");
		return buf.toString();
	}

}
