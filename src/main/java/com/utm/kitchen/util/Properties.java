package com.utm.kitchen.util;

import lombok.SneakyThrows;

import java.io.InputStream;

public class Properties {
  public static int TIME_UNIT;
  public static int OVENS;
  public static int STOVES;

  @SneakyThrows
  public static void readProperties() {
    InputStream s = Properties.class.getResourceAsStream("/application.properties");

    java.util.Properties props = new java.util.Properties();
    props.load(s);

    TIME_UNIT = Integer.parseInt(props.getProperty("time_unit"));
    OVENS = Integer.parseInt(props.getProperty("ovens"));
    STOVES = Integer.parseInt(props.getProperty("stoves"));
  }
}
