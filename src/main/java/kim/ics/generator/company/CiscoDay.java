package kim.ics.generator.company;

import kim.ics.calenar.Consts;
import kim.ics.util.CsvHeader;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CiscoDay {

    @CsvHeader(name = "day", pattern = "yyyyMMdd")
    private String day;

    @CsvHeader(name = "comment")
    private String comment;

    public LocalDate getDay1() {
        return Consts.DATE_FORMATTER.parse(day, LocalDate::from);
    }

}
