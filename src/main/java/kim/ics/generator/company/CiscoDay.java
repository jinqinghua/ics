package kim.ics.generator.company;

import com.opencsv.bean.CsvBindByName;
import kim.ics.calenar.Consts;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CiscoDay {

    @CsvBindByName(column = "day")
    private String day;

    @CsvBindByName(column = "comment")
    private String comment;

    public LocalDate getLocalDate() {
        return Consts.DATE_FORMATTER.parse(day, LocalDate::from);
    }

}
