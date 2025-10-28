package kim.ics.generator.birthday;

import kim.ics.calenar.MyCalendar;
import kim.ics.util.CsvUtils;
import kim.ics.util.Md5UidGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.RRule;
import net.fortuna.ical4j.model.property.Transp;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.transform.recurrence.Frequency;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static kim.ics.calenar.Consts.GENERATED_HOME;

@Slf4j
public class BirthdayCalendarGenerator {
    public static final String CALENDAR_NAME = "Family Birthday";
    public static final String FILE_NAME = CALENDAR_NAME.toLowerCase(Locale.ROOT).replace(' ', '-'); // family-birthday
    public static final String CVS_FILENAME_IN_RESOURCE_FOLDER = "%s.csv".formatted(FILE_NAME);
    public static final Path ICS_PATH_WRITE_TO = Paths.get(GENERATED_HOME, "%s.ics".formatted(FILE_NAME));

    static void main(String[] args) {
        generate();
    }

    @SneakyThrows
    public static void generate() {
        var vEvents = buildVEVents(Year.of(2025), Year.of(2030));
        MyCalendar myCalendar = new MyCalendar("生日", "#AF52DE", vEvents);
        myCalendar.add(new Transp(Transp.VALUE_TRANSPARENT));
        myCalendar.add(new XProperty("X-APPLE-SPECIAL-DAY", "TRUE"));

        Files.writeString(ICS_PATH_WRITE_TO, myCalendar.toString());
    }

    private static List<VEvent> buildVEVents(Year yearFrom, Year yearTo) {
        List<VEvent> vEvents = new ArrayList<>();
        List<BirthDate> birthDateList = CsvUtils.listCsvRecord(CVS_FILENAME_IN_RESOURCE_FOLDER, BirthDate.class);
        for (int year = yearFrom.getValue(); year <= yearTo.getValue(); year++) {
            for (BirthDate birthDate : birthDateList) {
                VEvent vEvent = buildVEvent(birthDate, Year.of(year), year == yearFrom.getValue());
                if (null != vEvent) {
                    vEvents.add(vEvent);
                }
            }
        }
        return vEvents;
    }

    private static VEvent buildVEvent(BirthDate birthDate, Year year, boolean isYearFrom) {
        // 阳历生日，只在当年生成
        if (birthDate.isSolar() && !isYearFrom) {
            return null;
        }
        LocalDate birthday = birthDate.getBirthday(year);
        String summary = "%s%d岁%s生日(%s)\uD83C\uDF82".formatted(birthDate.getName(), birthDate.getAge(year), birthDate.isSolar() ? "" : birthDate.getBirthDateType().getName(), birthDate.getChineseZodiac());
        VEvent vEvent = new VEvent(birthday, birthday.plusDays(1), summary);
        vEvent.add(new Md5UidGenerator(CALENDAR_NAME, birthday).generateUid());
        if (birthDate.isSolar()) {
            vEvent.add(new RRule<>(Frequency.YEARLY));
        }
        return vEvent;
    }

}
