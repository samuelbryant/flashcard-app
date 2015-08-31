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
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author author
 */
public class Utilities {
  
  public static <T> String getArrayString(List<T> list) {
    String str = "";
    for (int i = 0; i < list.size(); i++) {
      if (i < list.size() - 1) {
        str += list.get(i).toString() + ",";
      } else {
        str += list.get(i);
      }
    }
    return str;
  }
  
  public static <T> String getCollectionString(Collection<T> list) {
    String str = "";
    Iterator<T> iter = list.iterator();
    while (iter.hasNext()) {
      Object o = iter.next();
      if (iter.hasNext()) {
        str += o.toString() + ",";
      } else {
        str += o.toString();
      }
    }
    return str;
  }
  
  public static Object getObjectFieldValue(Object obj, String fieldName) throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
    Field f = obj.getClass().getField(fieldName);
    return f.get(obj);
  }
  
  public static void setObjectFieldValue(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    Field f = obj.getClass().getField(fieldName);
    f.set(obj, value);
  }
  
  public static String encodeObjectStringField(Object obj, String fieldName) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    return String.format("%s:%s\n", fieldName, getObjectFieldValue(obj, fieldName).toString());
  }
  
  
  
  public static String encodeObjectListField(Object obj, String fieldName) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    List<Object> list = (List<Object>) getObjectFieldValue(obj, fieldName);
    String str = "";
    for (int i = 0; i < list.size(); i++) {
      if (i < list.size() - 1) {
        str += list.get(i).toString() + ",";
      } else {
        str += list.get(i);
      }
    }
    return str + "\n";
  }
  
  public static void decodeObjectStringField(Object obj, String line) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    String parts[] = line.split(":");
    String fieldName = parts[0];
    setObjectFieldValue(obj, parts[0], parts[1]);
  }
  
//  public static void decodeObjectListField(Object obj, String line) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
//    String parts[] = line.split(":");
//    String fieldName = parts[0];
//    String[] list = 
//    
//    setObjectFieldValue(obj, parts[0], parts[1]);
//  }
//  
//  private String _encodeList(String fieldName) {
//    List<Object> list = (List<Object>) this._getValue(fieldName);
//    String str = "";
//    
//    return String.format("%s:%s\n", fieldName, this._getValue(fieldName));
//  }
//  
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
