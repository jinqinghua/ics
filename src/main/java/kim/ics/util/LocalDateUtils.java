package kim.ics.util;

import com.tyme.lunar.LunarDay;
import com.tyme.solar.SolarDay;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class LocalDateUtils {

    private LocalDateUtils() {
        throw new UnsupportedOperationException();
    }

    public static LocalDate fromSolarDay(SolarDay solarDay) {
        return LocalDate.of(solarDay.getYear(), solarDay.getMonth(), solarDay.getDay());
    }

    public static LocalDate fromLunarDay(LunarDay lunarDay) {
        return fromSolarDay(lunarDay.getSolarDay());
    }

    public static SolarDay toSolarDay(LocalDate localDate) {
        return SolarDay.fromYmd(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
    }

    public static LunarDay toLunarDay(LocalDate localDate) {
        return LunarDay.fromYmd(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
    }

}
