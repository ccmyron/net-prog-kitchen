package com.utm.kitchen.apparatus;

public class Oven implements Apparatus {
  private boolean available;

  public Oven() {
    this.available = true;
  }

  @Override
  public void use() {
    available = false;
  }

  @Override
  public boolean isAvailable() {
    return available;
  }

  @Override
  public void free() {
    available = true;
  }
}
