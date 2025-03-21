package kim.ics.generator.example;

import kim.ics.calenar.MyCalendar;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Categories;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Transp;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.util.RandomUidGenerator;

import java.time.LocalDate;
import java.util.Collections;

@Slf4j
public class ExampleGenerator {

    public static void main(String[] args) {
        MyCalendar cal = new MyCalendar("Family Birthdays", "#FF0000");
        cal.add(new Transp(Transp.VALUE_TRANSPARENT)); // TRANSP:TRANSPARENT（事件透明度，不影响忙碌时间）
        cal.add(new Method(Method.VALUE_PUBLISH));
        cal.add(new Categories("生日"));
        cal.add(new XProperty("X-APPLE-SPECIAL-DAY", "TRUE"));

        VEvent vEvent = new VEvent(LocalDate.now(), "Christmas Day");
        vEvent.add(new RandomUidGenerator().generateUid());

        cal.add(vEvent);
        cal.add(Collections.singletonList(vEvent));

        log.info(cal.toString());
    }
}
