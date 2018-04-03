package httpcores;

import static java.util.Calendar.getInstance;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;


/**
 * To get the date according to the timezone.
 * @author DMOG
 */
public final class DateUtils {

    private static final String DEF_DATE_TIME_FMT               = "yyyyMMddHHmmss";
    
    private static final String SHORT_DATE_TIME_FMT = "yyyy-MM-dd";
    
    private static Logger log = Logger.getLogger(DateUtils.class);
    
    private static int DATE_FORMAT_ERR = 1;
    
    private static String DATE_FORMAT_ERR_MESS = "Date format error."; 
    
    public static Date getDate(String timezone) {
        Calendar calendar = getCalendar(timezone);
        
        return calendar.getTime();
    }
    
    public static Date now() {
        return getInstance().getTime();
    }
    
    /**
     * @param timeZone
     * @return the Date
     */
    public static Date now(String timeZone) {
        if(isNotBlank(timeZone)) {
            return DateUtils.getDate(timeZone);
        } else {
            return getInstance().getTime();
        }
    }

    private static Calendar getCalendar(String timezone) {
        TimeZone timeZone = TimeZone.getTimeZone(timezone);
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTimeInMillis(timeInMillis + timeZone.getOffset(timeInMillis));
        return calendar;
    }
    
    public static java.sql.Date getSqlDate(String timezone) {
        Calendar calendar = getCalendar(timezone);
        return new java.sql.Date(calendar.getTimeInMillis());
    }
    
    public static XMLGregorianCalendar getXMLGregorianCalendarFromDate(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            if (date != null) {
                XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(df.format(date));
                return xmlCalendar;
            }
        } catch (DatatypeConfigurationException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static XMLGregorianCalendar getformatedYYYYMMDDXMLGregorianCalendarFromDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (date != null) {
                XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(df.format(date));
                return xmlCalendar;
            }
        } catch (DatatypeConfigurationException e) {
            log.error(e.getMessage());
        }
        return null;
    }
    
    private DateUtils() {
        
    }
    
    // -------------------------------------------------------------------------
    
    public static String getFormatedDate(Date date, String format) {
        
        SimpleDateFormat xmlFmter = new SimpleDateFormat(format);

        String sRet = null;

        if (date != null) {
            sRet = xmlFmter.format(date);
        }

        return sRet;
    }
    
    public static Date convertFormattedDateTime (String date) throws ParseException {
        
        DateFormat format = new SimpleDateFormat(DEF_DATE_TIME_FMT);
        
        return date == null ? null : format.parse(date);
    }
    
    public static Long getHourAndMinute(Date date) {
    	Calendar currentCalendar = Calendar.getInstance();
    	currentCalendar.setTime(date);
    	int hours = currentCalendar.get(Calendar.HOUR_OF_DAY);
		int minutes = currentCalendar.get(Calendar.MINUTE);
		return Long.valueOf(hours * 100 + minutes);
    }
    
}
