package com.example.nolo.entities.item.specs;

import com.example.nolo.entities.item.specs.specsoption.SpecsOption;

import java.util.List;

public class PhoneSpecs extends Specs {
    private String operatingSystem, display, cpu, camera, connectivity, audio, touchscreen,
            protectionResistance, simCard, sensors, battery, dimensions, weight;
    private List<SpecsOption> storageOptions;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public PhoneSpecs() {}

    public PhoneSpecs(String summary, String operatingSystem, String display, String cpu,
                      List<SpecsOption> storageOptions, String camera, String connectivity, String audio,
                      String touchscreen, String protectionResistance, String simCard,
                      String sensors, String battery, String dimensions, String weight) {
        super(summary);
        this.operatingSystem = operatingSystem;
        this.display = display;
        this.cpu = cpu;
        this.storageOptions = storageOptions;
        this.camera = camera;
        this.connectivity = connectivity;
        this.audio = audio;
        this.touchscreen = touchscreen;
        this.protectionResistance = protectionResistance;
        this.simCard = simCard;
        this.sensors = sensors;
        this.battery = battery;
        this.dimensions = dimensions;
        this.weight = weight;
    }

    @Override
    public String getOperatingSystem() {
        return operatingSystem;
    }

    @Override
    public String getDisplay() {
        return display;
    }

    @Override
    public String getCpu() {
        return cpu;
    }

    @Override
    public List<SpecsOption> getStorageOptions() {
        return storageOptions;
    }

    @Override
    public String getCamera() {
        return camera;
    }

    @Override
    public String getCommunication() {
        return connectivity;
    }

    @Override
    public String getAudio() {
        return audio;
    }

    @Override
    public String getTouchscreen() {
        return touchscreen;
    }

    @Override
    public String getProtectionResistance() {
        return protectionResistance;
    }

    @Override
    public String getSimCard() {
        return simCard;
    }

    @Override
    public String getSensors() {
        return sensors;
    }

    @Override
    public String getBattery() {
        return battery;
    }

    @Override
    public String getDimensions() {
        return dimensions;
    }

    @Override
    public String getWeight() {
        return weight;
    }
}
