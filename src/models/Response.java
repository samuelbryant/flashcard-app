/**
 * TODO:
 * 1) Add time+date parameter so we can get time analysis.
 * 2) Build "Quiz" entity and link it to responses for automatic quiz response
 * grouping
 */
package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Response {
  
  public static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy h:mm a");

  final Answer response;
  final Long responseTime;
  final String dateString;

  public Response(Answer response, Long responseTime, Date date) {
    this.response = response;
    this.responseTime = responseTime;
    this.dateString = DATE_FORMAT.format(date);
  }
  
  /**
   * Constructor for parsing Response from file.
   * @param response
   * @param responseTime
   * @param date 
   */
  Response(Answer response, Long responseTime, String dateString) {
    this.response = response;
    this.responseTime = responseTime;
    this.dateString = dateString;
  }
}
