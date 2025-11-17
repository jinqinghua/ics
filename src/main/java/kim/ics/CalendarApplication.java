package kim.ics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CalendarApplication {

    private CalendarApplication() {
    }

    static void main() {
        SpringApplication.run(CalendarApplication.class);
        log.info("{} started.", CalendarApplication.class.getSimpleName());
    }

}
