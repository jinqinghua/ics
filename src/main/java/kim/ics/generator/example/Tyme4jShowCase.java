package kim.ics.generator.example;

import com.tyme.solar.SolarDay;
import kim.ics.util.Tyme4jUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Tyme4jShowCase {

    static void main(String[] args) {
        SolarDay firstSolarDay = SolarDay.fromYmd(2025, 1, 1);
        int offset = 365;

        printLegalHoliday(firstSolarDay, offset);
        printSolarTerm(firstSolarDay, offset);
        printSolarFestival(firstSolarDay, offset);
        printLunarFestival(firstSolarDay, offset);
    }

    /**
     * 法定节假日
     */
    private static void printLegalHoliday(SolarDay firstSolarDay, int offset) {
        Tyme4jUtils.listLegalHoliday(firstSolarDay, offset).forEach(legalHoliday -> log.info("{}", legalHoliday));
    }

    /**
     * 二十四节气
     */
    private static void printSolarTerm(SolarDay firstSolarDay, int offset) {
        Tyme4jUtils.listSolarTermDay(firstSolarDay, offset).forEach(solarTermDay -> log.info("{}", solarTermDay));
    }

    /**
     * 阳历节日
     */
    private static void printSolarFestival(SolarDay firstSolarDay, int offset) {
        Tyme4jUtils.listSolarFestival(firstSolarDay, offset).forEach(solarFestival -> log.info("{}", solarFestival));
    }

    /**
     * 农历节日
     */
    private static void printLunarFestival(SolarDay firstSolarDay, int offset) {
        Tyme4jUtils.listLunarFestival(firstSolarDay, offset).forEach(lunarFestival -> log.info("{}", lunarFestival));
    }

}
