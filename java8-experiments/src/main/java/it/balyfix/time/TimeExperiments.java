package it.balyfix.time;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;


public class TimeExperiments
{

    public static void main(String[] args)
    {
        examples();

    }

    private static void examples()
    {

        LocalDateTime now = LocalDateTime.now();
        LocalDate mybirthDay = LocalDate.of(1977, Month.FEBRUARY, 10);

        LocalTime parse = LocalTime.parse("00:00:00");

        LocalDate theDate = now.toLocalDate();
        Month month = now.getMonth();
        int day = now.getDayOfMonth();
        int second = now.getSecond();

        LocalDateTime withHour = now.withHour(0);
        LocalDateTime withSecond = withHour.withSecond(0);
        LocalDateTime withMinute = withSecond.withMinute(0);

        LocalDateTime thePast = now.withDayOfMonth(10).withYear(2010);

        LocalDateTime yetAnother = thePast.plusWeeks(3).plus(3, ChronoUnit.WEEKS);

        LocalDateTime foo = now.with(lastDayOfMonth());
        LocalDateTime bar = now.with(previousOrSame(DayOfWeek.WEDNESDAY));

        now.with(LocalTime.now());

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        LocalTime truncatedTime = time.truncatedTo(ChronoUnit.SECONDS);

        ZoneId id = ZoneId.of("Europe/Rome");
        ZonedDateTime zoned = ZonedDateTime.of(now, id);
        ZoneId value = ZoneId.from(zoned);
    }

}