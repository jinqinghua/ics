package kim.ics.generator.birthday;

import kim.ics.calenar.MyCalendar;
import kim.ics.util.CsvUtils;
import kim.ics.util.LocalDateUtils;
import kim.ics.util.Md5UidGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Categories;
import net.fortuna.ical4j.model.property.Transp;
import net.fortuna.ical4j.model.property.XProperty;
import org.springframework.util.StringUtils;

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
    public static final String SUMMERY = "%s%d岁%s生日(%s)\uD83C\uDF82";

    private BirthdayCalendarGenerator() {
    }

    @SneakyThrows
    public static void generate(int fromYear, int toYear) {
        var vEvents = buildVEvents(fromYear, toYear);
        MyCalendar myCalendar = new MyCalendar("生日", "#AF52DE", vEvents);
        myCalendar.add(new Transp(Transp.VALUE_TRANSPARENT));
        myCalendar.add(new XProperty("X-APPLE-SPECIAL-DAY", "TRUE"));

        Files.writeString(ICS_PATH_WRITE_TO, myCalendar.toString());
    }

    private static List<VEvent> buildVEvents(int fromYear, int toYear) {
        List<VEvent> vEvents = new ArrayList<>();
        List<BirthDate> birthDateList = CsvUtils.listCsvRecord(CVS_FILENAME_IN_RESOURCE_FOLDER, BirthDate.class);

        birthDateList.forEach(birthDate -> {
            birthDate.verifyBirthDate();
            log.info("birthDate: {}", birthDate);

            for (int year = fromYear; year <= toYear; year++) {
                vEvents.addAll(buildVEvents(birthDate, Year.of(year)));
            }
        });
        return vEvents;
    }

    private static List<VEvent> buildVEvents(BirthDate birthDate, Year year) {
        List<VEvent> events = new ArrayList<>();

        if (StringUtils.hasText(birthDate.getInputSolarBirthDate())) { // 阳历生日
            LocalDate birthday = LocalDateUtils.fromSolarDay(birthDate.getSolarBirthdayByYear(year));
            String summary = SUMMERY.formatted(birthDate.getName(), birthDate.getSolarAgeByYear(year), "阳历", birthDate.getChineseZodiac());
            VEvent vevent = new VEvent(birthday, birthday.plusDays(1), summary);
            vevent.add(new Md5UidGenerator(CALENDAR_NAME + "阳历", birthday).generateUid());
            vevent.add(new Categories("BIRTHDAY"));
            // vevent.add(new RRule<>(Frequency.YEARLY))
            events.add(vevent);
        }

        if (StringUtils.hasText(birthDate.getInputLunarBirthDate())) { // 阴历生日
            LocalDate birthday = LocalDateUtils.fromLunarDay(birthDate.getLunarBirthdayByYear(year));
            String summary = SUMMERY.formatted(birthDate.getName(), birthDate.getLunarAgeByYear(year), "阴历", birthDate.getChineseZodiac());
            VEvent vevent = new VEvent(birthday, birthday.plusDays(1), summary);
            vevent.add(new Md5UidGenerator(CALENDAR_NAME + "阴历", birthday).generateUid());
            vevent.add(new Categories("BIRTHDAY"));
            events.add(vevent);
        }

        return events;
    }

}
