package kim.ics.birthday;

import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;

/**
 * birthDate 出生日期，只有一个
 * birthday 生日，每年都有
 */
@Data
@AllArgsConstructor
public class BirthDate {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private String name;
    private BirthDateType birthDateType;
    private String birthDateString;

    public boolean isSolar() {
        return BirthDateType.SOLAR.equals(birthDateType);
    }

    public boolean isLunar() {
        return BirthDateType.LUNAR.equals(birthDateType);
    }

    private LocalDate getBirthDate() {
        return LocalDate.parse(birthDateString, DATE_FORMATTER);
    }

    public Solar getSolarBirthDate() {
        return switch (birthDateType) {
            case SOLAR -> {
                LocalDate birthDate = getBirthDate();
                yield new Solar(birthDate.getYear(), birthDate.getMonthValue(), birthDate.getDayOfMonth());
            }
            case LUNAR -> getLunarBirthDate().getSolar();
        };
    }

    public Lunar getLunarBirthDate() {
        return switch (birthDateType) {
            case SOLAR -> getSolarBirthDate().getLunar();
            case LUNAR -> {
                LocalDate birthDate = getBirthDate();
                yield new Lunar(birthDate.getYear(), birthDate.getMonthValue(), birthDate.getDayOfMonth());
            }
        };
    }

    public String getBirthday(Year year) {
        return switch (birthDateType) {
            case SOLAR -> {
                var solarBirthDate = getSolarBirthDate();
                var solarBirthday = new Solar(year.getValue(), solarBirthDate.getMonth(), solarBirthDate.getDay());
                yield DATE_FORMATTER.format(LocalDate.of(solarBirthday.getYear(), solarBirthDate.getMonth(), solarBirthDate.getDay()));

            }
            case LUNAR -> {
                var lunarBirthDate = getLunarBirthDate();
                var lunarBirthday = new Lunar(year.getValue(), lunarBirthDate.getMonth(), lunarBirthDate.getDay());
                var solarBirthday = lunarBirthday.getSolar();
                yield DATE_FORMATTER.format(LocalDate.of(solarBirthday.getYear(), solarBirthday.getMonth(), solarBirthday.getDay()));
            }
        };
    }

}
