package kim.ics.generator.festival;

import com.tyme.holiday.LegalHoliday;
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

public class LegalHolidayGenerator {
    public static final Path ICS_PATH_WRITE_TO = Paths.get(GENERATED_HOME, "legal-holiday.ics");

    public static void main(String[] args) {
        generate();
    }

    @SneakyThrows
    public static void generate() {
        var vEvents = buildVEVents();
        MyCalendar myCalendar = new MyCalendar("法定节假", "#34C759", vEvents);
        myCalendar.add(new Transp(Transp.VALUE_TRANSPARENT));
        myCalendar.add(new XProperty("X-APPLE-SPECIAL-DAY", "TRUE"));

        Files.writeString(ICS_PATH_WRITE_TO, myCalendar.toString());
    }

    private static VEvent buildVEvent(LegalHoliday legalHoliday) {
        LocalDate startDate = Tyme4jUtils.toLocalDate(legalHoliday.getDay());
        VEvent vEvent = new VEvent(startDate, startDate.plusDays(1), legalHoliday.getName());
        vEvent.add(new RandomUidGenerator().generateUid());
        return vEvent;
    }

    private static List<VEvent> buildVEVents() {
        List<VEvent> vEvents = new ArrayList<>();
        Tyme4jUtils.listLegalHoliday(SolarDay.fromYmd(2025, 1, 1), 365)
                .forEach(legalHoliday -> vEvents.add(buildVEvent(legalHoliday)));
        return vEvents;
    }

}
