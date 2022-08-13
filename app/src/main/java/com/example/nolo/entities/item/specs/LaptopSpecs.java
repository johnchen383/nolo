package com.example.nolo.entities.item.specs;

import java.util.List;

public class LaptopSpecs extends Specs {
    private String operatingSystem, display, cpu, gpu, camera, keyboard,
            communication, audio, touchscreen, fingerprintReader, opticalDrive, ports, battery,
            acAdaptor, dimensions;
    private List<ISpecsOption> ram, storage;

    public LaptopSpecs() {}

    public LaptopSpecs(String summary, String weight, String operatingSystem,
                       String display, String cpu, String gpu, List<ISpecsOption> ram,
                       List<ISpecsOption> storage, String camera, String keyboard,
                       String communication, String audio, String touchscreen,
                       String fingerprintReader, String opticalDrive, String ports, String battery,
                       String acAdaptor, String dimensions) {
        super(summary, weight);
        this.operatingSystem = operatingSystem;
        this.display = display;
        this.cpu = cpu;
        this.gpu = gpu;
        this.ram = ram;
        this.storage = storage;
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
    public List<ISpecsOption> getRam() {
        return ram;
    }

    @Override
    public List<ISpecsOption> getStorage() {
        return storage;
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
}
