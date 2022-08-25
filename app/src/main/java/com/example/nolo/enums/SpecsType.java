package com.example.nolo.enums;

public enum SpecsType {
    /**
     * All items
     */
    summary("Summary"),

    /**
     * Laptops & Phones
     */
    operatingSystem("Sperating System"),
    display("Display"),
    cpu("CPU"),
    camera("Camera"),
    communication("Communication"),
    audio("Audio"),
    touchscreen("Touchscreen"),
    battery("Battery"),
    dimensions("Dimensions"),
    weight("Weight"),

    /**
     * Laptops
     */
    gpu("GPU"),
    keyboard("Keyboard"),
    fingerprintReader("Fingerprint Reader"),
    opticalDrive("Optical Drive"),
    ports("Ports"),
    acAdaptor("AC Adaptor"),

    /**
     * Phones
     */
    protectionResistance("Protection Resistance"),
    simCard("Sim Card"),
    sensors("Sensors");

    private final String fullname;

    SpecsType(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return this.fullname;
    };
}
