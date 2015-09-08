/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 *
 * @author sambryant
 */
public class IO {

  /**
   *
   * @param name
   * @return
   */
  public static boolean fileExists(String name) {
    File f = new File(name);
    return f.exists();
  }

  /**
   *
   * @param dirname
   */
  public static void createDirOrDie(String dirname) {
    File dir = new File(dirname);
    if (dir.exists() && dir.isDirectory()) {
      return;
    } else if (dir.exists()) {
      throw new FatalError("createDirOrDie failed for file: " + dirname
            + ". (Tried to create dir with same name as non-dir)");
    }
    dir.mkdir();
    existsOrDie(dir);
  }

  /**
   *
   * @param filename
   */
  public static void createOrDie(String filename) {
    File file = new File(filename);
    if (file.exists()) {
      return;
    }
    try{
      file.createNewFile();
      existsOrDie(file);
    } catch(IOException ex) {
      throw new FatalError("createOrDie failed for file: " + filename, ex);
    }
  }

  /**
   *
   * @param filename
   */
  public static void existsOrDie(String filename) {
    existsOrDie(new File(filename));
  }

  /**
   *
   * @param file
   */
  public static void existsOrDie(File file) {
    if (!file.exists()) {
      throw new FatalError("existsOrDie failed for file: " + file);
    }
  }

  /**
   *
   * @param filename
   */
  public static void notExistsOrDie(String filename) {
      notExistsOrDie(new File(filename));
  }

  /**
   *
   * @param file
   */
  public static void notExistsOrDie(File file) {
    if (file.exists()) {
      throw new FatalError("notExistsOrDie failed for file: " + file);
    }
  }

  /**
   *
   * @param filename
   */
  public static void deleteOrDie(String filename) {
    File file = new File(filename);
    if (file.exists()) {
      file.delete();
    }
    notExistsOrDie(file);
  }

  /**
   *
   * @param srcFilename
   * @param dstFilename
   */
  public static void copyOrDie(String srcFilename, String dstFilename) {
    File src = new File(srcFilename);
    File dst = new File(dstFilename);
    if (!src.exists()) {
      return;
    }

    // Ensure target file is non-existent.
    deleteOrDie(dstFilename);

    // Copy file.
    try {
      Files.copy(src.toPath(), dst.toPath());
    } catch (IOException ex) {
      throw new FatalError(
          "copyOrDie failed for '" + srcFilename + "' -> '" + dstFilename + "'", ex);
    }

    // Check if target exists.
    existsOrDie(dst);
  }

  /**
   *
   * @param filename
   */
  public static void backupAndRecreateOrDie(String filename) {
    File file = new File(filename);
    if (!file.exists()) {
      return;
    }

    String backupFilename = filename + ".backup";

    // Delete backup file if it exists.
    deleteOrDie(backupFilename);

    // Copy current file to backup file.
    copyOrDie(filename, backupFilename);

    // Delete target file.
    deleteOrDie(filename);

    // Recreate file.
    createOrDie(filename);
  }

  /**
   *
   * @param c
   */
  public static void closeOrLive(Closeable c) {
    try {
      c.close();
    } catch(IOException ex) {
      System.out.printf("LOG: Could not close closeable %o", c);
    }
  }

  /**
   *
   * @param filename
   * @return
   */
  public static BufferedReader getBufferedReaderOrLive(String filename) {
    try {
      return new BufferedReader(new FileReader(filename));
    } catch(IOException ex) {
      return null;
    }
  }

  /**
   *
   * @param filename
   * @return
   */
  public static ObjectInputStream getObjectInputStreamOrLive(String filename) {
    try {
      return new ObjectInputStream(new FileInputStream(filename));
    } catch(IOException ex) {
      return null;
    }
  }

  /**
   *
   * @param filename
   * @return
   */
  public static PrintWriter getPrintWriterOrDie(String filename) {
    try {
      createOrDie(filename);

      PrintWriter pw = new PrintWriter(filename);
      if (pw == null) throw new IOException();
      return pw;
    } catch(IOException ex) {
      throw new FatalError("getPrintWriterOrDie failed for: " + filename, ex);
    }
  }

  /**
   *
   * @param filename
   * @return
   */
  public static ObjectOutputStream getObjectOutputStreamOrDie(String filename) {
    try {
      createOrDie(filename);

      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
      if (oos == null) throw new IOException();
      return oos;
    } catch(IOException ex) {
      throw new FatalError("getObjectOutputStreamOrDie failed for: " + filename, ex);
    }
  }

  /**
   *
   * @param filename
   * @return
   */
  public static BufferedImage loadImageOrDie(String filename) {
    try {
      BufferedImage img = ImageIO.read(new File(filename));
      if (img == null) throw new IOException();
      return img;
    } catch(IOException ex) {
      throw new FatalError("loadImageOrDie failed for: " + filename, ex);
    }
  }

  /**
   *
   * @param filename
   * @return
   */
  public static Scanner getScannerOrDie(String filename) {
    try {
      return new Scanner(new File(filename));
    } catch(IOException ex) {
      throw new FatalError("getScannerOrDie failed for " + filename, ex);
    }
  }

  /**
   *
   * @param filename
   * @return
   */
  public static String readEntireFileOrDie(String filename) {
    Scanner scan = getScannerOrDie(filename);
    scan.useDelimiter("\\Z");  
    String contents = scan.next();
    IO.closeOrLive(scan);
    return contents;
  }
}
