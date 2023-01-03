package ru.job4j.todo.util;

import lombok.experimental.UtilityClass;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@UtilityClass
public final class TaskTimeZone {

    public static Task setTimeWithTimeZone(Task task) {
        TimeZone zoneId = task.getUser().getUserZone();

        if (zoneId == null) {
            zoneId = TimeZone.getDefault();
        }

        task.setCreated(task.getCreated()
                .atZone(TimeZone.getDefault().toZoneId())
                .withZoneSameInstant(zoneId.toZoneId())
                .toLocalDateTime());
        return task;
    }

   public static List<TimeZone> getAllZone() {
       var zones = new ArrayList<TimeZone>();
       for (String timeId : TimeZone.getAvailableIDs()) {
           zones.add(TimeZone.getTimeZone(timeId));
       }
       return zones;
   }
}
