package kim.ics.generator.example;

import com.tyme.festival.LunarFestival;
import com.tyme.festival.SolarFestival;
import com.tyme.holiday.LegalHoliday;
import com.tyme.lunar.LunarDay;
import com.tyme.solar.SolarDay;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TymeShowCase {

    public static void main(String[] args) {
        SolarDay firstSolarDay = SolarDay.fromYmd(2025, 1, 1);
        LunarDay firstLunarDay = LunarDay.fromYmd(2025, 1, 1);
        int offset = 365;

        printLegalHoliday(firstSolarDay, offset);
        printSolarTerm(firstSolarDay, offset);
        printSolarFestival(firstSolarDay, offset);
        printLunarFestival(firstLunarDay, offset);
    }

    /**
     * 法定节假日
     */
    private static void printLegalHoliday(SolarDay firstSolarDay, int offset) {
        for (int i = 0; i < offset; i++) {
            SolarDay nextSolarDay = firstSolarDay.next(i);
            LegalHoliday legalHoliday = nextSolarDay.getLegalHoliday();
            if (null != legalHoliday) {
                log.info("{}, {}, {}, {}", legalHoliday.getDay(), legalHoliday.getName(), legalHoliday.isWork(), legalHoliday.getTarget());
            }
        }
        System.out.println();
    }

    /**
     * 二十四节气
     */
    private static void printSolarTerm(SolarDay firstSolarDay, int offset) {
        for (int i = 0; i < offset; i++) {
            SolarDay nextSolarDay = firstSolarDay.next(i);
            if (0 == nextSolarDay.getTermDay().getDayIndex()) {
                log.info("{}, {}, {}", nextSolarDay, nextSolarDay.getTerm(), nextSolarDay.getTermDay());
            }
        }
        System.out.println();
    }

    /**
     * 阳历节日
     */
    private static void printSolarFestival(SolarDay firstSolarDay, int offset) {
        for (int i = 0; i < offset; i++) {
            SolarDay nextSolarDay = firstSolarDay.next(i);
            SolarFestival solarFestival = nextSolarDay.getFestival();
            if (null != solarFestival) {
                log.info("{}", solarFestival);
            }
        }
        System.out.println();
    }

    /**
     * 农历节日
     */
    private static void printLunarFestival(LunarDay firstLunarDay, int offset) {
        for (int i = 0; i < offset; i++) {
            LunarDay nextLunarDay = firstLunarDay.next(i);
            LunarFestival lunarFestival = nextLunarDay.getFestival();
            if (null != lunarFestival) {
                log.info("{}", lunarFestival);
            }
        }
        System.out.println();
    }

}
