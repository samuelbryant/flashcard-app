/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author author
 */
public class Utilities {
  
  public static <T extends Serializable> T makeDeepCopy(T object) {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(object);
      oos.flush();
      oos.close();
      bos.close();
      byte[] byteData = bos.toByteArray();
      ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
      Object obj2 = (Object) new ObjectInputStream(bais).readObject();
      return (T) obj2;
    } catch (IOException ex) {
      throw new RuntimeException("FATAL EXCEPTION IN OBJECT DEEP COPY", ex);
    } catch (ClassNotFoundException ex) {
      throw new RuntimeException("FATAL EXCEPTION IN OBJECT DEEP COPY", ex);
    }
  }
  
}
