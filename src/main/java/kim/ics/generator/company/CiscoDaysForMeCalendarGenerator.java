package kim.ics.generator.company;

import kim.ics.calenar.MyCalendar;
import kim.ics.util.CsvUtils;
import kim.ics.util.Md5UidGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Transp;
import net.fortuna.ical4j.model.property.XProperty;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static kim.ics.calenar.Consts.GENERATED_HOME;

@Slf4j
public class CiscoDaysForMeCalendarGenerator {
    public static final String CALENDAR_NAME = "Cisco Day for me";
    public static final String FILE_NAME = CALENDAR_NAME.toLowerCase(Locale.ROOT).replace(' ', '-'); // cisco-days-for-me
    public static final String CVS_FILENAME_IN_RESOURCE_FOLDER = "%s.csv".formatted(FILE_NAME);
    public static final Path ICS_PATH_WRITE_TO = Paths.get(GENERATED_HOME, "%s.ics".formatted(FILE_NAME));

    private CiscoDaysForMeCalendarGenerator() {
    }

    static void main() {
        generate();
    }

    @SneakyThrows
    public static void generate() {
        var vEvents = buildVEVents();
        MyCalendar myCalendar = new MyCalendar(CALENDAR_NAME, "#049FD9", vEvents);
        myCalendar.add(new Transp(Transp.VALUE_OPAQUE)); // OPAQUE（事件不透明，影响忙碌时间）
        myCalendar.add(new XProperty("X-APPLE-SPECIAL-DAY", "TRUE"));

        Files.writeString(ICS_PATH_WRITE_TO, myCalendar.toString());
    }

    private static VEvent buildVEvent(CiscoDay ciscoDay) {
        LocalDate ciscoLocalDate = ciscoDay.getLocalDate();
        VEvent vEvent = new VEvent(ciscoLocalDate, ciscoLocalDate.plusDays(1), CALENDAR_NAME + ciscoDay.getComment());
        vEvent.add(new Md5UidGenerator(CALENDAR_NAME, ciscoLocalDate).generateUid());
        return vEvent;
    }

    private static List<VEvent> buildVEVents() {
        return CsvUtils.listCsvRecord(CVS_FILENAME_IN_RESOURCE_FOLDER, CiscoDay.class).stream()
                .map(CiscoDaysForMeCalendarGenerator::buildVEvent)
                .toList();
    }

}
