package kim.ics.calenar;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Consts {

    public static final String USER_HOME = Objects.requireNonNull(System.getProperty("user.home"));
    public static final String PROJECT_HOME = USER_HOME + "/git/kim-github/ics";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private Consts() {
        throw new UnsupportedOperationException();
    }
}
