package kim.ics.util;

import com.tyme.festival.LunarFestival;
import com.tyme.festival.SolarFestival;
import com.tyme.holiday.LegalHoliday;
import com.tyme.lunar.LunarDay;
import com.tyme.solar.SolarDay;
import com.tyme.solar.SolarTermDay;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class Tyme4jUtils {

    private Tyme4jUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * LocalDate → SolarDay
     */
    public static SolarDay toSolarDay(LocalDate localDate) {
        return SolarDay.fromYmd(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
    }

    /**
     * SolarDay → LocalDate
     */
    public static LocalDate toLocalDate(SolarDay solarDay) {
        return LocalDate.of(solarDay.getYear(), solarDay.getMonth(), solarDay.getDay());
    }

    /**
     * 法定假日
     */
    public static List<LegalHoliday> listLegalHoliday(SolarDay firstSolarDay, int offset) {
        return IntStream.range(0, offset)
                .mapToObj(i -> firstSolarDay.next(i).getLegalHoliday())
                .filter(Objects::nonNull)
                .toList();
    }


    /**
     * 二十四节气
     */
    public static List<SolarTermDay> listSolarTermDay(SolarDay firstSolarDay, int offset) {
        return IntStream.range(0, offset)
                .mapToObj(i -> firstSolarDay.next(i).getTermDay())
                .filter(solarTermDay -> solarTermDay.getDayIndex() == 0)
                .toList();
    }

    /**
     * 阳历节日
     */
    public static List<SolarFestival> listSolarFestival(SolarDay firstSolarDay, int offset) {
        return IntStream.range(0, offset)
                .mapToObj(i -> firstSolarDay.next(i).getFestival())
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 阴历节日
     */
    public static List<LunarFestival> listLunarFestival(SolarDay firstSolarDay, int offset) {
        return listLunarFestival(firstSolarDay.getLunarDay(), offset);
    }

    /**
     * 阴历节日
     */
    public static List<LunarFestival> listLunarFestival(LunarDay firstLunarDay, int offset) {
        return IntStream.range(0, offset)
                .mapToObj(i -> firstLunarDay.next(i).getFestival())
                .filter(Objects::nonNull)
                .toList();
    }

}
