package kim.ics.util;

import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.util.UidGenerator;

import java.nio.charset.StandardCharsets;
import java.time.temporal.Temporal;
import java.util.UUID;

public class Md5UidGenerator implements UidGenerator {

    private final String calenderName;
    private final Temporal temporal;

    public Md5UidGenerator(final String calenderName, final Temporal temporal) {
        this.calenderName = calenderName;
        this.temporal = temporal;
    }

    @Override
    public Uid generateUid() {
        byte[] inputBytes = (calenderName + temporal.toString()).getBytes(StandardCharsets.UTF_8);
        UUID uuid = UUID.nameUUIDFromBytes(inputBytes);
        return new Uid(uuid.toString());
    }

}
