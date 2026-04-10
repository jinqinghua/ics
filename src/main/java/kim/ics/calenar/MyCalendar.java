package kim.ics.calenar;

import lombok.Getter;
import lombok.NonNull;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.model.property.immutable.ImmutableCalScale;
import net.fortuna.ical4j.model.property.immutable.ImmutableVersion;

import java.time.*;
import java.time.temporal.Temporal;
import java.util.*;

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
        mergeConsecutiveEvents(vEvents).forEach(vEvent -> {
            applyStableDtStamp(vEvent);
            this.add(vEvent);
        });
    }

    private static List<VEvent> mergeConsecutiveEvents(Collection<VEvent> vEvents) {
        Map<String, List<VEvent>> eventsBySummary = new HashMap<>();
        List<VEvent> passthroughEvents = new ArrayList<>();

        vEvents.forEach(vEvent -> {
            if (toLocalDate(vEvent, Property.DTSTART).isPresent() && toLocalDate(vEvent, Property.DTEND).isPresent()) {
                String summary = vEvent.getProperty(Property.SUMMARY).map(Property::getValue).orElse("");
                eventsBySummary.computeIfAbsent(summary, ignored -> new ArrayList<>()).add(vEvent);
            } else {
                passthroughEvents.add(vEvent);
            }
        });

        List<VEvent> mergedEvents = new ArrayList<>();
        eventsBySummary.values().forEach(summaryEvents -> {
            summaryEvents.sort(Comparator.comparing(event -> toLocalDate(event, Property.DTSTART).orElseThrow()));

            VEvent current = summaryEvents.getFirst();
            for (int i = 1; i < summaryEvents.size(); i++) {
                VEvent next = summaryEvents.get(i);
                LocalDate currentEnd = toLocalDate(current, Property.DTEND).orElseThrow();
                LocalDate nextStart = toLocalDate(next, Property.DTSTART).orElseThrow();

                if (currentEnd.equals(nextStart)) {
                    LocalDate nextEnd = toLocalDate(next, Property.DTEND).orElseThrow();
                    current.replace(new DtEnd<>(nextEnd));
                } else {
                    mergedEvents.add(current);
                    current = next;
                }
            }
            mergedEvents.add(current);
        });

        mergedEvents.addAll(passthroughEvents);
        mergedEvents.sort(Comparator
                .comparing((VEvent event) -> toLocalDate(event, Property.DTSTART).orElse(LocalDate.MAX))
                .thenComparing(event -> event.getProperty(Property.SUMMARY).map(Property::getValue).orElse("")));
        return mergedEvents;
    }

    private static void applyStableDtStamp(VEvent vEvent) {
        vEvent.<DateProperty<? extends Temporal>>getProperty(Property.DTSTART)
                .map(DateProperty::getDate)
                .map(MyCalendar::toInstant)
                .ifPresent(instant -> vEvent.replace(new DtStamp(instant)));
    }

    private static Instant toInstant(Temporal startDate) {
        if (startDate instanceof Instant instant) {
            return instant;
        }
        if (startDate instanceof LocalDate localDate) {
            return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        }
        if (startDate instanceof LocalDateTime localDateTime) {
            return localDateTime.toInstant(ZoneOffset.UTC);
        }
        if (startDate instanceof OffsetDateTime offsetDateTime) {
            return offsetDateTime.toInstant();
        }
        if (startDate instanceof ZonedDateTime zonedDateTime) {
            return zonedDateTime.toInstant();
        }
        return Instant.from(startDate);
    }

    private static Optional<LocalDate> toLocalDate(VEvent vEvent, String propertyName) {
        return vEvent.<DateProperty<? extends Temporal>>getProperty(propertyName)
                .map(DateProperty::getDate)
                .filter(LocalDate.class::isInstance)
                .map(LocalDate.class::cast);
    }

}
