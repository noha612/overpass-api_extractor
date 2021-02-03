package sample.util;

public class CommonUtil {

  public static double haversineFormula(
      double fromLat, double fromLng,
      double toLat, double toLng
  ) {

    double dLat = Math.toRadians(toLat - fromLat);
    double dLon = Math.toRadians(toLng - fromLng);
    double lat1 = Math.toRadians(fromLat);
    double lat2 = Math.toRadians(toLat);

    double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1)
        * Math.cos(lat2);
    double c = 2 * Math.asin(Math.sqrt(a));
    return 6400 * c;
  }

}
