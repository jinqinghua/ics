package kim.ics.calenar;

import lombok.Getter;
import lombok.NonNull;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.model.property.immutable.ImmutableCalScale;
import net.fortuna.ical4j.model.property.immutable.ImmutableVersion;

import java.util.Collection;
import java.util.Collections;

@Getter
public class MyCalendar extends Calendar {

    private final String name;

    public MyCalendar(@NonNull String name, String xAppleCalendarColor) {
        this(name, xAppleCalendarColor, Collections.emptyList());
    }

    public MyCalendar(@NonNull String name, String xAppleCalendarColor, @NonNull Collection<VEvent> vEvents) {
        this.name = name;

        this.add(ImmutableVersion.VERSION_2_0);
        this.add(ImmutableCalScale.GREGORIAN);
        this.add(new ProdId("-//Kim//%s//EN".formatted(this.name)));
        this.add(new Method(Method.VALUE_PUBLISH));

        if (null != xAppleCalendarColor) {
            this.add(new XProperty("X-APPLE-CALENDAR-COLOR", xAppleCalendarColor));
        }
        this.add(new XProperty("X-WR-CALNAME", this.name));

        this.add(vEvents);
    }

    public void add(@NonNull Collection<VEvent> vEvents) {
        vEvents.forEach(this::add);
    }

}
