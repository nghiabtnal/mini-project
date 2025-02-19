package org.example.miniproject.libs.utils;

import org.example.miniproject.libs.exceptions.SystemException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    /**
     * non-public constructor.
     */
    private DateTimeUtils() {
    }


    /**
     * the default time zone
     */
    public static final ZoneId DEFAULT_TIME_ZONE = ZoneOffset.UTC;

    /**
     * the default date format:yyyy-MM-dd
     */
    public static final DateTimeFormatter DEFAULT_DATE_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd").parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .toFormatter();

    /**
     * the default time format:yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMAT = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Adds or subtracts the specified amount of time to the given calendar field, based on the
     * calendar's rules.
     *
     * @param date   the date, not null
     * @param field  the calendar field.
     * @param amount the amount of date or time to be added to the field.
     * @return the new date object with the days added
     */
    public static Date add(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * Adds a number of months to a date returning a new object.<br>
     * The original date object is unchanged.
     *
     * @param target the date, not null
     * @param amount the amount of month to add, may be negative
     * @return the new date object with the days added
     */
    public static Date addMonths(Date target, int amount) {
        return add(target, Calendar.MONTH, amount);
    }

    /**
     * Adds a number of months to a date returning a new object.<br>
     * The original date object is unchanged.
     *
     * @param target the date, not null
     * @param amount the amount of month to add, may be negative
     * @return the new date object with the days added
     */
    public static String addMonths(String target, int amount) {
        return format(addMonths(parse(target), amount));
    }

    /**
     * Adds a number of days to a date returning a new object.<br>
     * The original date object is unchanged.
     *
     * @param target the date, not null
     * @param amount the amount of day to add, may be negative
     * @return the new date object with the days added
     */
    public static Date addDays(Date target, int amount) {
        return add(target, Calendar.DATE, amount);
    }

    /**
     * Adds a number of days to a String date(format:yyyy-MM-dd) returning a new object.
     *
     * @param target the date String, not null
     * @param amount the amount of day to add, may be negative
     * @return the new date object with the days added
     */
    public static String addDays(String target, int amount) {

        return format(addDays(parse(target), amount));
    }

    /**
     * get the day between two String dates(format:yyyy-MM-dd) by send - start.
     *
     * @return the days between two dates
     * @throws SystemException cause by the startDate or endDate is not date String
     *                         format:yyyy-MM-dd.
     */
    public static long intervalDays(String start, String end) {
        return intervalDays(parse(start), parse(end));
    }

    /**
     * get the days between two dates
     *
     * @param start start date
     * @param end   end date
     * @return the days between two dates
     */
    public static long intervalDays(Date start, Date end) {
        return (end.getTime() - start.getTime()) / (1000 * 3600 * 24);
    }

    /**
     * parse a {@linkplain String} to {@linkplain Date} by the specified format and time zone.
     *
     * @param target The target string
     * @param zoneId The {@linkplain ZoneId}
     * @Return the result date
     */
    public static Date parse(String target, DateTimeFormatter formatter, ZoneId zoneId) {

        if (target == null || target.isEmpty()) {
            throw new SystemException("the input string is empty.");
        }

        return new Date(LocalDateTime.parse(target, formatter).atZone(zoneId).toInstant().toEpochMilli());
    }

    /**
     * parse a {@linkplain String} to {@linkplain Date} by the specified format.<br>
     * the time zone is fixed use UTC.
     *
     * @param target The target string
     * @param format The specified format
     * @Return the result date
     */
    public static Date parse(String target, String format) {
        DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder().appendPattern(format)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0).toFormatter();
        return parse(target, dateFormatter, DEFAULT_TIME_ZONE);
    }

    /**
     * parse a {@linkplain String} to {@linkplain Date} by the default date format(yyyy-MM-dd).<br>
     * the time zone is fixed use UTC.
     *
     * @param target The target string
     * @Return the result date
     */
    public static Date parse(String target) {
        return parse(target, DEFAULT_DATE_FORMAT, DEFAULT_TIME_ZONE);
    }

    /**
     * parse a {@linkplain String} to {@linkplain Date} by the default date time
     * format(yyyy-MM-dd).<br>
     * the time zone is fixed use UTC.
     *
     * @param target The target string
     * @Return the result date
     */
    public static Date parseTime(String target) {
        return parse(target, DEFAULT_DATE_TIME_FORMAT, DEFAULT_TIME_ZONE);
    }

    /**
     * format a {@linkplain Date} to {@linkplain String} by the specified format and time zone.
     *
     * @param target    The target date
     * @param formatter The specified {@linkplain DateTimeFormatter}
     * @Return the result string
     */
    public static String format(Date target, DateTimeFormatter formatter, ZoneId zoneId) {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(target.getTime()), zoneId).format(formatter);
    }

    /**
     * format a {@linkplain Date} to {@linkplain String} by the specified time zone with the default
     * format (yyyy-MM-dd).
     *
     * @param target The target date
     * @param zoneId The specified {@linkplain ZoneId}
     * @Return the result string
     */
    public static String format(Date target, ZoneId zoneId) {
        return format(target, DEFAULT_DATE_FORMAT, zoneId);
    }

    /**
     * format a {@linkplain Date} to {@linkplain String} by the specified format.<br>
     * the time zone is fixed use UTC.
     *
     * @param target The target date
     * @param format The specified format
     * @Return the result string
     */
    public static String format(Date target, String format) {
        return format(target, DateTimeFormatter.ofPattern(format), DEFAULT_TIME_ZONE);
    }

    /**
     * format a {@linkplain Date} to {@linkplain String} by the default date format(yyyy-MM-dd).<br>
     * the time zone is fixed use UTC.
     *
     * @param target The target date
     * @Return the result string
     */
    public static String format(Date target) {
        return format(target, DEFAULT_DATE_FORMAT, DEFAULT_TIME_ZONE);
    }
}
