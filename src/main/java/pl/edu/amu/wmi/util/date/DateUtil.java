package pl.edu.amu.wmi.util.date;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    private DateUtil() {
    }

    private static final int SEMI_MONTH = 1001;

    private static final int[][] fields = {
            {Calendar.MILLISECOND},
            {Calendar.SECOND},
            {Calendar.MINUTE},
            {Calendar.HOUR_OF_DAY, Calendar.HOUR},
            {Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM
                    /* Calendar.DAY_OF_YEAR, Calendar.DAY_OF_WEEK, Calendar.DAY_OF_WEEK_IN_MONTH */
            },
            {Calendar.MONTH, SEMI_MONTH},
            {Calendar.YEAR},
            {Calendar.ERA}};

    public static Date truncate(final Date date, final int field) {
        final Calendar gval = Calendar.getInstance();
        gval.setTime(date);
        modify(gval, field);
        return gval.getTime();
    }

    // copied from apache commons
    @SuppressWarnings("squid:S3776")
    private static void modify(final Calendar val, final int field) {
        final Date date = val.getTime();
        long time = date.getTime();
        boolean done = false;
        final int millisecs = val.get(Calendar.MILLISECOND);
        time = time - millisecs;
        if (field == Calendar.SECOND) {
            done = true;
        }
        final int seconds = val.get(Calendar.SECOND);
        time = time - (seconds * 1000L);
        if (field == Calendar.MINUTE) {
            done = true;
        }
        final int minutes = val.get(Calendar.MINUTE);
        if (!done && (minutes < 30)) {
            time = time - (minutes * 60000L);
        }
        if (date.getTime() != time) {
            date.setTime(time);
            val.setTime(date);
        }
        for (final int[] aField : fields) {
            int offset = 0;
            boolean offsetSet = false;
            switch (field) {
                case SEMI_MONTH:
                    if (aField[0] == Calendar.DATE) {
                        offset = val.get(Calendar.DATE) - 1;
                        if (offset >= 15) {
                            offset -= 15;
                        }
                        offsetSet = true;
                    }
                    break;
                case Calendar.AM_PM:
                    if (aField[0] == Calendar.HOUR_OF_DAY) {
                        offset = val.get(Calendar.HOUR_OF_DAY);
                        if (offset >= 12) {
                            offset -= 12;
                        }
                        offsetSet = true;
                    }
                    break;
                default:
                    break;
            }
            if (!offsetSet) {
                final int min = val.getActualMinimum(aField[0]);
                offset = val.get(aField[0]) - min;
            }
            if (offset != 0) {
                val.set(aField[0], val.get(aField[0]) - offset);
            }
        }
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

    public static Date getMinuteAgo() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, -1);
        return calendar.getTime();
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

}
