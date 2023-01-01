package ru.job4j.todo.util;

import lombok.experimental.UtilityClass;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@UtilityClass
public final class TaskZone {

    public static Task setZone(Task task) {
        TimeZone zoneId = task.getUser().getUserZone();
        if (zoneId == null) {
            zoneId = TimeZone.getDefault();
        }
        task.setCreated(LocalDateTime.from(task.getCreated()
                .atZone(zoneId.toZoneId())));
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
