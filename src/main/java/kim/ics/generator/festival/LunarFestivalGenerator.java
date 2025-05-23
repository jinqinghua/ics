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

import static kim.ics.calenar.Consts.GENERATED_HOME;

public class LunarFestivalGenerator {
    public static final Path ICS_PATH_WRITE_TO = Paths.get(GENERATED_HOME, "festival-lunar.ics");

    public static void main(String[] args) {
        generate();
    }

    @SneakyThrows
    public static void generate() {
        var vEvents = buildVEVents();
        MyCalendar myCalendar = new MyCalendar("节日（农历）", "#007AFF", vEvents);
        myCalendar.add(new Transp(Transp.VALUE_TRANSPARENT));
        myCalendar.add(new XProperty("X-APPLE-SPECIAL-DAY", "TRUE"));

        Files.writeString(ICS_PATH_WRITE_TO, myCalendar.toString());
    }

    private static VEvent buildVEvent(LunarFestival lunarFestival) {
        LocalDate startDate = Tyme4jUtils.toLocalDate(lunarFestival.getDay().getSolarDay());
        VEvent vEvent = new VEvent(startDate, startDate.plusDays(1), lunarFestival.getName());
        vEvent.add(new Md5UidGenerator(startDate).generateUid());
        return vEvent;
    }

    private static List<VEvent> buildVEVents() {
        List<VEvent> vEvents = new ArrayList<>();
        Tyme4jUtils.listLunarFestival(SolarDay.fromYmd(2025, 1, 1), 1000)
                .forEach(lunarFestival -> vEvents.add(buildVEvent(lunarFestival)));
        return vEvents;
    }

}
