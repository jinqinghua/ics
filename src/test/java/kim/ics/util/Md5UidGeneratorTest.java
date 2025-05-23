package kim.ics.util;

import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.model.property.Uid;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@Slf4j
class Md5UidGeneratorTest {

    @Test
    void generateUid() {
        LocalDate now = LocalDate.now();

        Uid uid1 = new Md5UidGenerator(now).generateUid();
        Uid uid2 = new Md5UidGenerator(now).generateUid();

        log.info("uid1: {}", uid1);
        log.info("uid2: {}", uid2);

        Assertions.assertThat(uid1).isEqualTo(uid2);
    }
}