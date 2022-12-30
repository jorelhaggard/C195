package Jorbo.Util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Chrono {

    public static java.sql.Timestamp getTimestamp() {
        ZoneId zoneid = ZoneId.of("UTC");
        LocalDateTime localDateTime = LocalDateTime.now(zoneid);
        java.sql.Timestamp TS = Timestamp.valueOf(localDateTime);
        return TS;
    }

}
