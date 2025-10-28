package kim.ics.generator.birthday;

import com.tyme.lunar.LunarDay;
import com.tyme.solar.SolarDay;
import kim.ics.calenar.Consts;
import kim.ics.util.CsvHeader;
import kim.ics.util.LocalDateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.Year;

/**
 * birthDate 出生日期，只有一个
 * birthday 生日，每年都有
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BirthDate {

    @CsvHeader(name = "姓名")
    private String name;

    @CsvHeader(name = "阳历出生日期")
    private String inputSolarBirthDate;

    @CsvHeader(name = "农历出生日期")
    private String inputLunarBirthDate;

    private SolarDay solarBirthDate;

    private LunarDay lunarBirthDate;

    private String chineseZodiac;

    public void verifyBirthDate() {
        if (!StringUtils.hasText(inputSolarBirthDate) && !StringUtils.hasText(inputLunarBirthDate)) {
            throw new UnsupportedOperationException(this.getName() + " 至少要有一个阳历或农历出生日期！");
        } else if (StringUtils.hasText(inputSolarBirthDate) && StringUtils.hasText(inputLunarBirthDate)) { // 阳历，农历都有，则判断一下是不是同一天
            LocalDate solarLocalDate = LocalDate.parse(inputSolarBirthDate, Consts.DATE_FORMATTER);
            LocalDate lunarLocalDate = LocalDate.parse(inputLunarBirthDate, Consts.DATE_FORMATTER);
            SolarDay solarDay = LocalDateUtils.toSolarDay(solarLocalDate);
            LunarDay lunarDay = LocalDateUtils.toLunarDay(lunarLocalDate);
            Assert.isTrue(solarDay.equals(lunarDay.getSolarDay()), "阳历(%s)，农历(%s)不是同一天！".formatted(inputSolarBirthDate, inputLunarBirthDate));
            this.solarBirthDate = solarDay;
            this.lunarBirthDate = lunarDay;
            this.chineseZodiac = getChineseZodiac();
        } else if (StringUtils.hasText(inputSolarBirthDate)) { // 阳历有，农历没有，用阳历日期得到农历日期
            LocalDate solarLocalDate = LocalDate.parse(inputSolarBirthDate, Consts.DATE_FORMATTER);
            this.solarBirthDate = LocalDateUtils.toSolarDay(solarLocalDate);
            this.lunarBirthDate = solarBirthDate.getLunarDay();
            this.chineseZodiac = getChineseZodiac();
        } else if (StringUtils.hasText(inputLunarBirthDate)) { // 农历有，没有阳历，用农历日期得到阳历日期
            LocalDate lunarLocalDate = LocalDate.parse(inputLunarBirthDate, Consts.DATE_FORMATTER);
            this.lunarBirthDate = LocalDateUtils.toLunarDay(lunarLocalDate);
            this.chineseZodiac = getChineseZodiac();
            this.solarBirthDate = lunarBirthDate.getSolarDay();
        }
    }

    public SolarDay getSolarBirthdayByYear(Year year) {
        return SolarDay.fromYmd(year.getValue(), this.solarBirthDate.getMonth(), this.solarBirthDate.getDay());
    }

    public LunarDay getLunarBirthdayByYear(Year year) {
        return LunarDay.fromYmd(year.getValue(), this.lunarBirthDate.getMonth(), this.lunarBirthDate.getDay());
    }

    public int getSolarAgeByYear(Year year) {
        return Math.abs(year.getValue() - getSolarBirthDate().getYear());
    }

    public int getLunarAgeByYear(Year year) {
        return Math.abs(getLunarBirthdayByYear(year).getYear() - getLunarBirthDate().getYear());
    }

//    public BirthDate getBirthday(Year year) {
//        SolarDay solarBirthday = SolarDay.fromYmd(year.getValue(), this.solarBirthDate.getMonth(), this.solarBirthDate.getDay());
//        LunarDay lunarBirthday = LunarDay.fromYmd(year.getValue(), this.lunarBirthDate.getMonth(), this.lunarBirthDate.getDay());
//
//        BirthDate birthDate = new BirthDate();
//        birthDate.setName(this.name);
//        birthDate.setInputSolarBirthDate(solarBirthday.toString());
//        birthDate.setLunarBirthDate(lunarBirthday);
//        birthDate.setInputLunarBirthDate(lunarBirthday.toString());
//        return birthDate;
//    }


//    public BirthDateType getBirthDateType() {
//        return BirthDateType.from(birthDateTypeString);
//    }
//
//    public boolean isSolar() {
//        return BirthDateType.SOLAR.equals(this.getBirthDateType());
//    }
//
//    /**
//     * 将日期(阳历 | 农历)转成 LocalDate, 方便取 Year, Month, Day
//     */
//    private LocalDate getBirthDate() {
//        return LocalDate.parse(birthDateString, Consts.DATE_FORMATTER);
//    }
//
//    public SolarDay getSolarBirthDay() {
//        return switch (this.getBirthDateType()) {
//            case SOLAR -> {
//                LocalDate birthDate = getBirthDate();
//                yield new SolarDay(birthDate.getYear(), birthDate.getMonthValue(), birthDate.getDayOfMonth());
//            }
//            case LUNAR -> throw new UnsupportedOperationException("Only has SOLAR birth date!");
//        };
//    }
//
//    public LunarDay getLunarBirthDate() {
//        return switch (this.getBirthDateType()) {
//            case SOLAR -> throw new UnsupportedOperationException("Only has Lunar birth date!");
//            case LUNAR -> {
//                LocalDate birthDate = getBirthDate();
//                yield new LunarDay(birthDate.getYear(), birthDate.getMonthValue(), birthDate.getDayOfMonth());
//            }
//        };
//    }
//
//    public int getAge(Year year) {
//        return switch (this.getBirthDateType()) {
//            case SOLAR -> {
//                var solarBirthDate = getSolarBirthDay();
//                yield year.getValue() - solarBirthDate.getYear();
//            }
//            case LUNAR -> {
//                var lunarBirthDay = getBirthday(year);
//                yield year.getValue() - lunarBirthDay.getYear();
//            }
//        };
//    }
//
//    public LocalDate getBirthday(Year year) {
//        return switch (this.getBirthDateType()) {
//            case SOLAR -> {
//                var solarBirthDate = getSolarBirthDay();
//                var solarBirthday = new SolarDay(year.getValue(), solarBirthDate.getMonth(), solarBirthDate.getDay());
//                yield LocalDate.of(solarBirthday.getYear(), solarBirthDate.getMonth(), solarBirthDate.getDay());
//
//            }
//            case LUNAR -> {
//                var lunarBirthDate = getLunarBirthDate();
//                var lunarBirthday = new LunarDay(year.getValue(), lunarBirthDate.getMonth(), lunarBirthDate.getDay());
//                var solarBirthday = lunarBirthday.getSolarDay();
//                yield LocalDate.of(solarBirthday.getYear(), solarBirthday.getMonth(), solarBirthday.getDay());
//            }
//        };
//    }
//
    /**
     * 生肖
     */
    public String getChineseZodiac() {
        return this.getLunarBirthDate().getSixtyCycleDay().getYear().getEarthBranch().getZodiac().getName();
    }

}
