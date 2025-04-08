package kim.ics.generator;

import kim.ics.generator.birthday.BirthdayCalendarGenerator;
import kim.ics.generator.company.CiscoDaysForMeCalendarGenerator;
import kim.ics.generator.festival.LegalHolidayGenerator;
import kim.ics.generator.festival.LunarFestivalGenerator;
import kim.ics.generator.festival.SolarFestivalGenerator;
import kim.ics.generator.festival.SolarTermDayGenerator;

public class AllCalendarGenerator {

    public static void main(String[] args) {
        CiscoDaysForMeCalendarGenerator.generate();
        BirthdayCalendarGenerator.generate();

        LegalHolidayGenerator.generate();
        LunarFestivalGenerator.generate();
        SolarFestivalGenerator.generate();
        SolarTermDayGenerator.generate();
    }
}
