package com.example.nolo.entities.item.specs;

import com.example.nolo.entities.item.specs.specsoption.SpecsOption;

import java.util.List;

public class LaptopSpecs extends Specs {
    private String operatingSystem, display, cpu, gpu, camera, keyboard,
            communication, audio, touchscreen, fingerprintReader, opticalDrive, ports, battery,
            acAdaptor, dimensions, weight;
    private List<SpecsOption> ramOptions, storageOptions;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public LaptopSpecs() {}

    public LaptopSpecs(String summary, String operatingSystem, String display, String cpu,
                       String gpu, List<SpecsOption> ramOptions, List<SpecsOption> storageOptions, String camera,
                       String keyboard, String communication, String audio, String touchscreen,
                       String fingerprintReader, String opticalDrive, String ports, String battery,
                       String acAdaptor, String dimensions, String weight) {
        super(summary);
        this.operatingSystem = operatingSystem;
        this.display = display;
        this.cpu = cpu;
        this.gpu = gpu;
        this.ramOptions = ramOptions;
        this.storageOptions = storageOptions;
        this.camera = camera;
        this.keyboard = keyboard;
        this.communication = communication;
        this.audio = audio;
        this.touchscreen = touchscreen;
        this.fingerprintReader = fingerprintReader;
        this.opticalDrive = opticalDrive;
        this.ports = ports;
        this.battery = battery;
        this.acAdaptor = acAdaptor;
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
    public String getGpu() {
        return gpu;
    }

    @Override
    public List<SpecsOption> getRamOptions() {
        return ramOptions;
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
    public String getKeyboard() {
        return keyboard;
    }

    @Override
    public String getCommunication() {
        return communication;
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
    public String getFingerprintReader() {
        return fingerprintReader;
    }

    @Override
    public String getOpticalDrive() {
        return opticalDrive;
    }

    @Override
    public String getPorts() {
        return ports;
    }

    @Override
    public String getBattery() {
        return battery;
    }

    @Override
    public String getAcAdaptor() {
        return acAdaptor;
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
