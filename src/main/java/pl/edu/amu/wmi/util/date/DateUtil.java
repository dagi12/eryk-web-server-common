package pl.edu.amu.wmi.util.date;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    private DateUtil() {
    }

    public static int currentIntTimestamp() {
        return (int) (new Date().getTime() / 1000);
    }

    public static Date getYearAgo() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -1);
        return calendar.getTime();
    }

    public static Date getMonthAgo() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    public static Date getMinuteAgo(Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -1);
        return calendar.getTime();
    }

    public static Timestamp getEndOfTheDay(Date inputDate) {
        Date date = addDays(inputDate, 1);
        Date truncate = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        return new Timestamp(truncate.getTime());
    }

    public static int dayDifference(Timestamp dateFrom, Timestamp dateTo) {
        long diff = dateTo.getTime() - dateFrom.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static int dayDifference(Date dateFrom, Date dateTo) {
        long diff = dateTo.getTime() - dateFrom.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static int hourDifference(Timestamp dateFrom, Timestamp dateTo) {
        long diff = dateTo.getTime() - dateFrom.getTime();
        return (int) TimeUnit.MILLISECONDS.toHours(diff);
    }

    public static int minuteDifference(Timestamp dateFrom, Timestamp dateTo) {
        long diff = dateTo.getTime() - dateFrom.getTime();
        return (int) TimeUnit.MILLISECONDS.toMinutes(diff);
    }

    private static void clearDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void clearWeek(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        clearDay(calendar);
    }

    public static Date addDays(int days) {
        return addDays(new Date(), days);
    }

    public static Date addDays(Date date, int days) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    private static void clearMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 0);
    }

    public static DatePair getDateRange(DateRange dateRange) {
        final Calendar calendar = Calendar.getInstance();
        Date fromDate = new Date();
        Date toDate = null;
        calendar.setTime(fromDate);
        switch (dateRange) {
            case CURRENT_DAY:
                clearDay(calendar);
                fromDate = calendar.getTime();
                calendar.add(Calendar.DATE, 1);
                toDate = calendar.getTime();
                break;
            case CURRENT_WEEK:
                clearWeek(calendar);
                fromDate = calendar.getTime();
                calendar.add(Calendar.DAY_OF_YEAR, 7);
                toDate = calendar.getTime();
                break;
            case CURRENT_MONTH:
                clearMonth(calendar);
                fromDate = calendar.getTime();
                calendar.add(Calendar.MONTH, 1);
                toDate = calendar.getTime();
                break;
            case NEXT_DAY:
                clearDay(calendar);
                calendar.add(Calendar.DATE, 1);
                fromDate = calendar.getTime();
                calendar.add(Calendar.DATE, 1);
                toDate = calendar.getTime();
                break;
            case NEXT_WEEK:
                clearWeek(calendar);
                calendar.add(Calendar.WEEK_OF_MONTH, 1);
                fromDate = calendar.getTime();
                calendar.add(Calendar.WEEK_OF_MONTH, 1);
                toDate = calendar.getTime();
                break;
            case NEXT_MONTH:
                clearMonth(calendar);
                clearDay(calendar);
                calendar.add(Calendar.MONTH, 1);
                fromDate = calendar.getTime();
                calendar.add(Calendar.MONTH, 1);
                toDate = calendar.getTime();
                break;
            default:
                break;
        }
        return new DatePair(fromDate, toDate);
    }

    public static Timestamp currentTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    public static boolean differentDay(Date date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date);
        cal2.setTime(new Date());
        return !(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static Timestamp toTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    public static Timestamp currentTimestampWithoutHours() {
        return new Timestamp(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH).getTime());
    }

    public static Timestamp truncateSeconds(Timestamp timestamp) {
        return new Timestamp(DateUtils.truncate(timestamp.getTime(), Calendar.MINUTE).getTime());
    }

    public static Timestamp timestampFromString(String source) {
        try {
            return new Timestamp(DateFormat.getDateInstance().parse(source).getTime());
        } catch (ParseException e) {
            LOGGER.error("Cannot parse", e);
        }
        return null;
    }

    public static String stringFromDate(Date date) {
        return new SimpleDateFormat("yyyy-mm-dd HH:MM:SS").format(date);
    }

    public static long benchmark(String context, long timestamp) {
        long newTimestamp = System.currentTimeMillis();
        LOGGER.info("{}, time: {}", context, newTimestamp - timestamp);
        return newTimestamp;
    }
}
