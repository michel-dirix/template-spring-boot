package fr.midix.template.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.util.TextUtils;

public class Utils {

  private Utils() {

  }

  public static Date getUTCDateNow() {
    return new Date();
  }

  public static String calendarToString(Date calendar) {
    if (calendar == null) {
      return null;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    return sdf.format(calendar.getTime());
  }

  public static String calendarToFrenchDayString(Date calendar) {
    if (calendar == null) {
      return null;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    return sdf.format(calendar.getTime());
  }

  public static Date stringToCalendar(String local, String timezone) {
    try {
      if (TextUtils.isBlank(local) || "null".equals(local)) {
        return null;
      }

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

      if (local.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]+")) {
        local = local.substring(0, 19);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      } else if (local.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z")) {
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      } else if (local
          .matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{4,}Z?")) {
        local = local.substring(0, 23) + "Z";
      } else if (local.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{2,}")) {
        local = local.substring(0, 23) + "Z";
      } else if (local
          .matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}[+-][0-9]{2}:[0-9]{2}")) {
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        timezone = null;
      }

      if (timezone != null) {
        sdf.setTimeZone(TimeZone.getTimeZone(timezone));
      }

      return sdf.parse(local);
    } catch (Exception e) {
      return null;
    }
  }

  public static Date dayToCalendar(String local) {
    try {
      if (TextUtils.isBlank(local) || "null".equals(local)) {
        return null;
      }

      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

      return sdf.parse(local);
    } catch (Exception e) {
      return null;
    }
  }

}
