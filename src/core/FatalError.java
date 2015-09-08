/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author author
 */
public class FatalError extends RuntimeException {

  /**
   *
   * @param msg
   */
  public FatalError(String msg) {
    super("FATAL: " + msg);
  }

  /**
   *
   * @param msg
   * @param ex
   */
  public FatalError(String msg, Exception ex) {
    super("FATAL: " + msg, ex);
  }

}
