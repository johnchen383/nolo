package com.example.nolo.entities.item.specs;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public abstract class Specs implements ISpecs {
    private String summary, weight;
    private List<IColour> colours;

    public Specs() {}

    public Specs(String summary, String weight, List<IColour> colours) {
        this.summary = summary;
        this.weight = weight;
        this.colours = colours;
    }

    /**
     * All items
     */
    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public String getWeight() {
        return weight;
    }

    @Override
    public List<IColour> getColours() {
        return colours;
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
