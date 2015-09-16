/**
 * TODO:
 * 1) Add time+date parameter so we can get time analysis.
 * 2) Build "Quiz" entity and link it to responses for automatic quiz response
 * grouping
 */
package models;

import core.FatalError;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author sambryant
 */
public class Response implements Comparable<Response> {

  /**
   *
   */
  public static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy h:mm a");

  Answer response;
  final Long responseTime;
  final String dateString;
  final Date date;  // Transient.

  /**
   *
   * @param response
   * @param responseTime
   * @param date
   */
  public Response(Answer response, Long responseTime, Date date) {
    this.response = response;
    this.responseTime = responseTime;
    this.dateString = DATE_FORMAT.format(date);
    this.date = (Date) date.clone();
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
    try {
      this.date = DATE_FORMAT.parse(dateString);
    } catch (ParseException ex) {
      throw new FatalError("Could not parse date string: " + dateString);
    }
  }

  /**
   *
   * @param answer
   */
  public void setAnswer(Answer answer) {
    this.response = answer;
  }

  /**
   * Compares two responses by comparing the date they were answered.
   * @param o
   * @return 
   */
  @Override
  public int compareTo(Response o) {
    return -1 * this.date.compareTo(o.date);
  }

  /**
   *
   * @return
   */
  public Answer getSelectedAnswer() {
    return this.response;
  }

  /**
   *
   * @return
   */
  public Double getTimeInSeconds() {
    if (this.responseTime == null) {
      return null;
    } else {
      return (((double) this.responseTime) / 1000000000);
    }
  }
  
  public Long getResponseTime() {
    if (this.responseTime == null) {
      return null;
    } else {
      return this.responseTime;
    }
  }
  
  public Date getDate() {
    return this.date;
  }

}
