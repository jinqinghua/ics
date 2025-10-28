package kim.ics.util;

import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.util.UidGenerator;

import java.nio.charset.StandardCharsets;
import java.time.temporal.Temporal;
import java.util.UUID;

public record Md5UidGenerator(String calenderName, Temporal temporal) implements UidGenerator {

    @Override
    public Uid generateUid() {
        byte[] inputBytes = (calenderName + temporal.toString()).getBytes(StandardCharsets.UTF_8);
        UUID uuid = UUID.nameUUIDFromBytes(inputBytes);
        return new Uid(uuid.toString());
    }

}
