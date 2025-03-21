package kim.ics.generator;

import kim.ics.generator.birthday.BirthdayCalendarGenerator;
import kim.ics.generator.company.CiscoDaysForMeCalendarGenerator;

public class AllCalendarGenerator {

    public static void main(String[] args) {
        CiscoDaysForMeCalendarGenerator.generate();
        BirthdayCalendarGenerator.generate();
    }
}
