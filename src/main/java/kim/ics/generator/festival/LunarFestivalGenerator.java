package kim.ics.generator.festival;

import com.tyme.festival.LunarFestival;
import com.tyme.solar.SolarDay;
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

public class LunarFestivalGenerator {
    public static final String CALENDAR_NAME = "Festival Lunar";
    public static final String FILE_NAME = CALENDAR_NAME.toLowerCase(Locale.ROOT).replace(' ', '-');
    public static final Path ICS_PATH_WRITE_TO = Paths.get(GENERATED_HOME, "%s.ics".formatted(FILE_NAME));

    private LunarFestivalGenerator() {
    }

    @SneakyThrows
    public static void generate(SolarDay solarDayFrom, int offsetDays) {
        var vEvents = buildVEVents(solarDayFrom, offsetDays);
        MyCalendar myCalendar = new MyCalendar("节日（农历）", "#007AFF", vEvents);
        myCalendar.add(new Transp(Transp.VALUE_TRANSPARENT));
        myCalendar.add(new XProperty("X-APPLE-SPECIAL-DAY", "TRUE"));

        Files.writeString(ICS_PATH_WRITE_TO, myCalendar.toString());
    }

    private static VEvent buildVEvent(LunarFestival lunarFestival) {
        LocalDate startDate = Tyme4jUtils.toLocalDate(lunarFestival.getDay().getSolarDay());
        VEvent vEvent = new VEvent(startDate, startDate.plusDays(1), lunarFestival.getName());
        vEvent.add(new Md5UidGenerator(CALENDAR_NAME, startDate).generateUid());
        return vEvent;
    }

    private static List<VEvent> buildVEVents(SolarDay solarDayFrom, int offsetDays) {
        List<VEvent> vEvents = new ArrayList<>();
        Tyme4jUtils.listLunarFestival(solarDayFrom, offsetDays)
                .forEach(lunarFestival -> vEvents.add(buildVEvent(lunarFestival)));
        return vEvents;
    }

}
