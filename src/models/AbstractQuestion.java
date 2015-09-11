/*
 * File Overview: TODO
 */
package models;

import core.FatalError;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public abstract class AbstractQuestion implements Serializable {
  // Permanent fields central to class.
  protected final Type type;
  protected Integer id = null;  // Non-nullable
  protected ArrayList<Subject> subjects = new ArrayList<>();
  protected ArrayList<Tag> tags = new ArrayList<>();
  protected ArrayList<String> notes = new ArrayList<>();
  protected ArrayList<Response> responses = new ArrayList<>();

  // Transient fields which do not get serialized.
  protected Boolean persistent = false;
  private Boolean hasFinishedLoading = false;
  
  /**
   * Constructor used when reading a question from database
   */
  AbstractQuestion(int id) {
    this.persistent = true;
    this.id = id;
    this.type = this.getType();
  }
  
  /**
   * Constructor used when creating a new question.
   */
  protected AbstractQuestion() {
    this.persistent = false;
    this.type = this.getType();
  }
  
  public abstract Type getType();
  
  public abstract boolean isValid();
  
  @Override
  public String toString() {
    return String.format("%3s #%d", this.type.toString(), this.id);
  }
  
  /**
   * Performs necessary post-loading/creating processing work.
   * This includes:
   *  - Sorting responses by date
   */
  public void finishLoading() {
    if (hasFinishedLoading) {
      throw new FatalError("FinishLoading called when loading was already finished");
    } else if (!this.persistent) {
      throw new FatalError("FinishLoading called on non-persistent instance");
    } else {
      this.hasFinishedLoading = true;
      Collections.sort(this.responses);
      this.validate();
    }
  }

  public int getId() {
    return this.id;
  }
  
  public String getNote() {
    return this.notes.isEmpty() ? "" : this.notes.get(0);
  }
  
  public ArrayList<Response> getResponses() {
    return this.responses;
  }
  
  /**
   * Gets most recent Response.
   * @return Response with most recent Date or null if no responses.
   */
  public Response getLastResponse() {
    Collections.sort(this.responses);  // should not be necessary.
    if (this.responses.isEmpty()) {
      return null;
    } else {
      return this.responses.get(0);
    }
  }

  /**
   * Gets most recent Date that this question was answered.
   * @return Date of last response or null if no responses.
   */
  public Date getLastResponseTime() {
    Response r = this.getLastResponse();
    if (r == null) return null;
    else return r.date;
  }
  
  public ArrayList<Subject> getSubjects() {
    return this.subjects;
  }

  public ArrayList<Tag> getTags() {
    return this.tags;
  }
  
  public boolean hasSubject(Subject subject) {
    return this.subjects.contains(subject);
  }
  
  public boolean hasTag(Tag tag) {
    return this.tags.contains(tag);
  }
  
  public void setNote(String note) {
    if (this.notes.isEmpty())
      this.notes.add(note);
    else
      this.notes.set(0, note);
  }
  
  public void addResponse(Response r) {
    this.responses.add(r);
    // Keep responses sorted by last answered date.
    Collections.sort(this.responses);
  }
  
  public void setQuestionSubjects(ArrayList<Subject> subjects) {
    this.subjects = subjects;
  }
  
  public void setSubject(Subject subject, boolean value) {
    if (this.subjects.contains(subject)) {
      if (!value) {
        this.subjects.remove(subject);
      }
    } else if (value) {
      this.subjects.add(subject);
    }
  }
  
  public void setQuestionTags(ArrayList<Tag> tags) {
    this.tags = tags;
  }
  
  public void setTag(Tag tag, boolean value) {
    if (this.tags.contains(tag)) {
      if (!value) {
        this.tags.remove(tag);
      }
    } else if (value) {
      this.tags.add(tag);
    }
  }
  
  public boolean isPersistent() {
    return this.persistent;
  }

  public void validate() {
    if (!this.isValid()) {
      throw new FatalError("Question failed validation: " + this);
    }
  }
  
  @Override
  public abstract boolean equals(Object obj);
  
  @Override
  public abstract int hashCode();
  
}
