package kim.ics.birthday;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.Year;
import java.util.*;

@Slf4j
public class BirthdayCalendarGenerator {

    @SneakyThrows
    public static void main(String[] args) {
        var params = Map.of(
                "PRODID", "-//Kim ICS//Birthday Calendar//EN",
                "X-APPLE-CALENDAR-COLOR", "#FFB6C1",
                "X-WR-CALNAME", "Birthday Calendar"
        );

        String calendar = buildCalendar(Year.of(2025), Year.of(2030), params);
        // System.out.printf(calendar)
        Files.writeString(Paths.get("/Users/qinjin/git/kim-github/ics/family-birthday.ics"), calendar);
    }

    private static String buildCalendar(Year yearFrom, Year yearTo, Map<String, String> params) {
        List<String> lines = Arrays.asList(
                "BEGIN:VCALENDAR\n",
                "VERSION:2.0",
                "PRODID:" + params.get("PRODID"),
                "CALSCALE:GREGORIAN",
                "X-APPLE-CALENDAR-COLOR:" + params.get("X-APPLE-CALENDAR-COLOR"),
                "X-WR-CALNAME:" + params.get("X-WR-CALNAME"),
                "METHOD:PUBLISH",
                buildEvents(yearFrom, yearTo),
                "END:VCALENDAR"
        );

        return String.join("\n", lines) + "\n";
    }

    private static String buildEvents(Year yearFrom, Year yearTo) {
        StringBuilder eventsBuilder = new StringBuilder();

        List<BirthDate> birthDateList = getBirthdayDataList();
        for (int year = yearFrom.getValue(); year <= yearTo.getValue(); year++) {
            for (BirthDate birthDate : birthDateList) {
                eventsBuilder.append(buildEvent(birthDate, Year.of(year), year == yearFrom.getValue()));
            }
        }

        return eventsBuilder.toString();
    }

    private static String buildEvent(BirthDate birthDate, Year year, boolean isYearFrom) {
        // 阳历生日，只在当年生成
        if (birthDate.isSolar() && !isYearFrom) {
            return "";
        }

        List<String> eventItems = new ArrayList<>();
        eventItems.add("\nBEGIN:VEVENT");
        eventItems.add("UID:" + UUID.randomUUID());
        eventItems.add("DTSTAMP:" + Instant.now().toString());
        eventItems.add("DTSTART;VALUE=DATE:" + birthDate.getBirthday(year));
        String summary = "SUMMARY:%s\uD83C\uDF82%s";
        eventItems.add(summary.formatted(birthDate.getName(), birthDate.isSolar() ? "" : "(" + birthDate.getBirthDateType().getName() + ")"));


        if (birthDate.isSolar()) {
            eventItems.add("RRULE:FREQ=YEARLY");
        }
        eventItems.add("END:VEVENT\n");

        return String.join("\n", eventItems);
    }

    @SneakyThrows
    private static List<BirthDate> getBirthdayDataList() {
        InputStream inputStream = getInputStreamFromCSV();

        List<BirthDate> birthDateList = new ArrayList<>();
        @Cleanup CSVParser parser = CSVParser.parse(inputStream, StandardCharsets.UTF_8, CSVFormat.Builder.create().setSkipHeaderRecord(true).get());
        for (CSVRecord csvRecord : parser) {
            log.info("{}, {}, {}", csvRecord.get(0), csvRecord.get(1), csvRecord.get(2));
            BirthDate birthDate = new BirthDate(csvRecord.get(0), BirthDateType.from(csvRecord.get(1)), csvRecord.get(2));
            birthDateList.add(birthDate);
            log.info("{}", birthDate);
        }

        return birthDateList;
    }

    public static InputStream getInputStreamFromCSV() {
        return BirthdayCalendarGenerator.class.getClassLoader().getResourceAsStream("birthday.csv");
    }

}
