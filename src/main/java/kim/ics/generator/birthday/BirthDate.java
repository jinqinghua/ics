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

    /**
     * 生肖
     */
    public String getChineseZodiac() {
        return this.getLunarBirthDate().getSixtyCycleDay().getYear().getEarthBranch().getZodiac().getName();
    }

}
