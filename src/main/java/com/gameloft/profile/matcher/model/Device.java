package com.gameloft.profile.matcher.model;

import lombok.Data;

@Data
public class Device {
    private int id;
    private String model;
    private String carrier;
    private String firmware;
}
