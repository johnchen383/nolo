package com.example.nolo.entities.item.specs;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public abstract class Specs implements ISpecs {
    private String summary;

    public Specs() {}

    public Specs(String summary) {
        this.summary = summary;
    }

    /**
     * All items
     */
    @Override
    public String getSummary() {
        return summary;
    }

    /**
     * Laptops & Phones
     */
    @Exclude
    public String getOperatingSystem() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getDisplay() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getCpu() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public List<ISpecsOption> getStorage() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getCamera() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getCommunication() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getAudio() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getTouchscreen() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getBattery() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getDimensions() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Override
    public String getWeight() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    /**
     * Laptops
     */
    @Exclude
    public String getGpu() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public List<ISpecsOption> getRam() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getKeyboard() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getFingerprintReader() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getOpticalDrive() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getPorts() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getAcAdaptor() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    /**
     * Phones
     */
    @Exclude
    public String getProtectionResistance() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getSimCard() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getSensors() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }
}
