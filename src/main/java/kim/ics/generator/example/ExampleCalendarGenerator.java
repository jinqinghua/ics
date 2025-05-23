package kim.ics.generator.example;

import kim.ics.calenar.MyCalendar;
import kim.ics.util.Md5UidGenerator;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Categories;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Transp;
import net.fortuna.ical4j.model.property.XProperty;

import java.time.LocalDate;
import java.util.Collections;

@Slf4j
public class ExampleCalendarGenerator {

    public static void main(String[] args) {
        MyCalendar cal = new MyCalendar("Family Birthdays", "#FF0000");
        cal.add(new Transp(Transp.VALUE_TRANSPARENT)); // TRANSP:TRANSPARENT（事件透明度，不影响忙碌时间）
        cal.add(new Method(Method.VALUE_PUBLISH));
        cal.add(new Categories("生日"));
        cal.add(new XProperty("X-APPLE-SPECIAL-DAY", "TRUE"));

        LocalDate today = LocalDate.now();
        VEvent vEvent = new VEvent(today, "Christmas Day");
        vEvent.add(new Md5UidGenerator(today).generateUid());

        cal.add(vEvent);
        cal.add(Collections.singletonList(vEvent));

        log.info(cal.toString());
    }
}
