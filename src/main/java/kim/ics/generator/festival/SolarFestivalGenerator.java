package kim.ics.generator.festival;

import com.tyme.festival.SolarFestival;
import com.tyme.solar.SolarDay;
import kim.ics.calenar.MyCalendar;
import kim.ics.util.Tyme4jUtils;
import lombok.SneakyThrows;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Transp;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.util.RandomUidGenerator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static kim.ics.calenar.Consts.GENERATED_HOME;

public class SolarFestivalGenerator {
    public static final Path ICS_PATH_WRITE_TO = Paths.get(GENERATED_HOME, "festival-solar.ics");

    public static void main(String[] args) {
        generate();
    }

    @SneakyThrows
    public static void generate() {
        var vEvents = buildVEVents();
        MyCalendar myCalendar = new MyCalendar("节日（阳历）", "#FF9500", vEvents);
        myCalendar.add(new Transp(Transp.VALUE_TRANSPARENT));
        myCalendar.add(new XProperty("X-APPLE-SPECIAL-DAY", "TRUE"));

        Files.writeString(ICS_PATH_WRITE_TO, myCalendar.toString());
    }

    private static VEvent buildVEvent(SolarFestival solarFestival) {
        LocalDate startDate = Tyme4jUtils.toLocalDate(solarFestival.getDay());
        VEvent vEvent = new VEvent(startDate, startDate.plusDays(1), solarFestival.getName());
        vEvent.add(new RandomUidGenerator().generateUid());
        return vEvent;
    }

    private static List<VEvent> buildVEVents() {
        List<VEvent> vEvents = new ArrayList<>();
        Tyme4jUtils.listSolarFestival(SolarDay.fromYmd(2025, 1, 1), 1000)
                .forEach(solarFestival -> vEvents.add(buildVEvent(solarFestival)));
        return vEvents;
    }

}
