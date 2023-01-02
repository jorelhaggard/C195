package Jorbo.Util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * This class provides a useful method for the application
 */
public class Chrono {

    /**
     * This method returns an sql timestamp signifying now.
     * @return timestamp
     */
    public static Timestamp getTimestamp() {
        ZoneId zoneid = ZoneId.of("UTC");
        LocalDateTime localDateTime = LocalDateTime.now(zoneid);
        Timestamp TS = Timestamp.valueOf(localDateTime);
        return TS;
    }

}
