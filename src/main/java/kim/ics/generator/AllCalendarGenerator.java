package kim.ics.generator;

import com.tyme.solar.SolarDay;
import kim.ics.generator.birthday.BirthdayCalendarGenerator;
import kim.ics.generator.company.CiscoDaysForMeCalendarGenerator;
import kim.ics.generator.festival.LegalHolidayGenerator;
import kim.ics.generator.festival.LunarFestivalGenerator;
import kim.ics.generator.festival.SolarFestivalGenerator;
import kim.ics.generator.festival.SolarTermDayGenerator;
import kim.ics.util.LocalDateUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AllCalendarGenerator {

    private AllCalendarGenerator() {
    }

    static void main() {
        SolarDay fromSolarDay = SolarDay.fromYmd(2025, 1, 1);
        LocalDate fromLocalDate = LocalDateUtils.fromSolarDay(fromSolarDay);
        LocalDate toLocalDate = fromLocalDate.plusYears(5);

        int offsetDays = (int) ChronoUnit.DAYS.between(fromLocalDate, toLocalDate);

        CiscoDaysForMeCalendarGenerator.generate();
        BirthdayCalendarGenerator.generate(fromLocalDate.getYear(), toLocalDate.getYear());

        LegalHolidayGenerator.generate(fromSolarDay, offsetDays);
        LunarFestivalGenerator.generate(fromSolarDay, offsetDays);
        SolarFestivalGenerator.generate(fromSolarDay, offsetDays);
        SolarTermDayGenerator.generate(fromSolarDay, offsetDays);
    }
}
