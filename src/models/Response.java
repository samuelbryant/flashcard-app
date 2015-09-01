/**
 * TODO:
 * 1) Add time+date parameter so we can get time analysis.
 * 2) Build "Quiz" entity and link it to responses for automatic quiz response
 * grouping
 */
package models;

public class Response {

  final Answer response;
  final Integer responseTime;

  public Response(Answer response, Integer responseTime) {
    this.response = response;
    this.responseTime = responseTime;
  }
}
