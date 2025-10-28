package kim.ics.generator.festival;

import com.tyme.solar.SolarDay;
import com.tyme.solar.SolarTermDay;
import kim.ics.calenar.MyCalendar;
import kim.ics.util.Md5UidGenerator;
import kim.ics.util.Tyme4jUtils;
import lombok.SneakyThrows;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Transp;
import net.fortuna.ical4j.model.property.XProperty;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static kim.ics.calenar.Consts.GENERATED_HOME;

public class SolarTermDayGenerator {
    public static final String CALENDAR_NAME = "Solar Term Day";
    public static final String FILE_NAME = CALENDAR_NAME.toLowerCase(Locale.ROOT).replace(' ', '-');
    public static final Path ICS_PATH_WRITE_TO = Paths.get(GENERATED_HOME, "%s.ics".formatted(FILE_NAME));

    static void main(String[] args) {
        generate();
    }

    @SneakyThrows
    public static void generate() {
        var vEvents = buildVEVents();
        MyCalendar myCalendar = new MyCalendar("二十四节气", "#0085CA", vEvents);
        myCalendar.add(new Transp(Transp.VALUE_TRANSPARENT));
        myCalendar.add(new XProperty("X-APPLE-SPECIAL-DAY", "TRUE"));

        Files.writeString(ICS_PATH_WRITE_TO, myCalendar.toString());
    }

    private static VEvent buildVEvent(SolarTermDay solarTermDay) {
        LocalDate startDate = Tyme4jUtils.toLocalDate(solarTermDay.getSolarTerm().getJulianDay().getSolarDay());
        VEvent vEvent = new VEvent(startDate, startDate.plusDays(1), solarTermDay.getName());
        vEvent.add(new Md5UidGenerator(CALENDAR_NAME, startDate).generateUid());
        return vEvent;
    }

    private static List<VEvent> buildVEVents() {
        List<VEvent> vEvents = new ArrayList<>();
        Tyme4jUtils.listSolarTermDay(SolarDay.fromYmd(2025, 1, 1), 1000)
                .forEach(solarTermDay -> vEvents.add(buildVEvent(solarTermDay)));
        return vEvents;
    }

}
