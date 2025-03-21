package kim.ics.generator.birthday;

import com.tyme.lunar.LunarDay;
import com.tyme.solar.SolarDay;
import kim.ics.calenar.Consts;
import kim.ics.util.CsvHeader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Year;

/**
 * birthDate 出生日期，只有一个
 * birthday 生日，每年都有
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BirthDate {

    @CsvHeader(name = "姓名")
    private String name;

    @CsvHeader(name = "历法")
    private String birthDateTypeString;

    @CsvHeader(name = "出生日期")
    private String birthDateString;

    public BirthDateType getBirthDateType() {
        return BirthDateType.from(birthDateTypeString);
    }

    public boolean isSolar() {
        return BirthDateType.SOLAR.equals(this.getBirthDateType());
    }

    public boolean isLunar() {
        return BirthDateType.LUNAR.equals(this.getBirthDateType());
    }

    private LocalDate getBirthDate() {
        return LocalDate.parse(birthDateString, Consts.DATE_FORMATTER);
    }

    public SolarDay getSolarBirthDate() {
        return switch (this.getBirthDateType()) {
            case SOLAR -> {
                LocalDate birthDate = getBirthDate();
                yield new SolarDay(birthDate.getYear(), birthDate.getMonthValue(), birthDate.getDayOfMonth());
            }
            case LUNAR -> getLunarBirthDate().getSolarDay();
        };
    }

    public LunarDay getLunarBirthDate() {
        return switch (this.getBirthDateType()) {
            case SOLAR -> getSolarBirthDate().getLunarDay();
            case LUNAR -> {
                LocalDate birthDate = getBirthDate();
                yield new LunarDay(birthDate.getYear(), birthDate.getMonthValue(), birthDate.getDayOfMonth());
            }
        };
    }

    public int getAge(Year year) {
        return switch (this.getBirthDateType()) {
            case SOLAR -> {
                var solarBirthDate = getSolarBirthDate();
                yield year.getValue() - solarBirthDate.getYear();
            }
            case LUNAR -> {
                var lunarBirthDate = getLunarBirthDate();
                var solarBirthday = lunarBirthDate.getSolarDay();
                yield year.getValue() - solarBirthday.getYear();
            }
        };
    }

    public LocalDate getBirthday(Year year) {
        return switch (this.getBirthDateType()) {
            case SOLAR -> {
                var solarBirthDate = getSolarBirthDate();
                var solarBirthday = new SolarDay(year.getValue(), solarBirthDate.getMonth(), solarBirthDate.getDay());
                yield LocalDate.of(solarBirthday.getYear(), solarBirthDate.getMonth(), solarBirthDate.getDay());

            }
            case LUNAR -> {
                var lunarBirthDate = getLunarBirthDate();
                var lunarBirthday = new LunarDay(year.getValue(), lunarBirthDate.getMonth(), lunarBirthDate.getDay());
                var solarBirthday = lunarBirthday.getSolarDay();
                yield LocalDate.of(solarBirthday.getYear(), solarBirthday.getMonth(), solarBirthday.getDay());
            }
        };
    }

    public String getChineseZodiac() {
        return getLunarBirthDate().getYearSixtyCycle().getEarthBranch().getZodiac().getName();
    }
}
