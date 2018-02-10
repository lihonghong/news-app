package com.lee.model;

import java.sql.Date;

/**
 * Created by honghong on 16/9/26.
 */
public class HotQuery {

  private String name;
  private String source;
  private String type;
  private String category;
  private int position;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return "HotQuery{" +
        "name='" + name + '\'' +
        ", source='" + source + '\'' +
        ", type='" + type + '\'' +
        ", category='" + category + '\'' +
        ", position=" + position +
        '}';
  }
}
