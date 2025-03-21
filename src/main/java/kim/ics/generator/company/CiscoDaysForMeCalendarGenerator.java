package kim.ics.generator.company;

import kim.ics.calenar.MyCalendar;
import kim.ics.util.CsvUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Transp;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.util.RandomUidGenerator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static kim.ics.calenar.Consts.PROJECT_HOME;

@Slf4j
public class CiscoDaysForMeCalendarGenerator {

    public static final String CVS_FILENAME_IN_RESOURCE_FOLDER = "cisco-days-for-me.csv";
    public static final Path ICS_PATH_WRITE_TO = Paths.get(PROJECT_HOME, "cisco-days-for-me.ics");

    @SneakyThrows
    public static void main(String[] args) {
        var vEvents = buildVEVents();
        MyCalendar myCalendar = new MyCalendar("Cisco Days for me", "#0085CA", vEvents);
        myCalendar.add(new Transp(Transp.VALUE_OPAQUE)); // OPAQUE（事件不透明，影响忙碌时间）
        myCalendar.add(new XProperty("X-APPLE-SPECIAL-DAY", "TRUE"));

        Files.writeString(ICS_PATH_WRITE_TO, myCalendar.toString());
    }

    private static VEvent buildVEvent(CiscoDay ciscoDay) {
        VEvent vEvent = new VEvent(ciscoDay.getDay1(), ciscoDay.getDay1().plusDays(1), ciscoDay.getComment());
        vEvent.add(new RandomUidGenerator().generateUid());
        return vEvent;
    }

    private static List<VEvent> buildVEVents() {
        List<VEvent> vEvents = new ArrayList<>();
        CsvUtils.listCsvRecord(CVS_FILENAME_IN_RESOURCE_FOLDER, CiscoDay.class)
                .forEach(ciscoDay -> vEvents.add(buildVEvent(ciscoDay)));
        return vEvents;
    }
}
