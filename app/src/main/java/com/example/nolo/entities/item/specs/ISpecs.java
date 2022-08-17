package com.example.nolo.entities.item.specs;

import com.example.nolo.entities.item.specs.specsoption.ISpecsOption;

import java.util.List;

public interface ISpecs {
    /**
     * All items
     */
    String getSummary();

    /**
     * Laptops & Phones
     */
    String getOperatingSystem();
    String getDisplay();
    String getCpu();
    List<ISpecsOption> getStorageOptions();
    String getCamera();
    String getCommunication();
    String getAudio();
    String getTouchscreen();
    String getBattery();
    String getDimensions();
    String getWeight();

    /**
     * Laptops
     */
    String getGpu();
    List<ISpecsOption> getRamOptions();
    String getKeyboard();
    String getFingerprintReader();
    String getOpticalDrive();
    String getPorts();
    String getAcAdaptor();

    /**
     * Phones
     */
    String getProtectionResistance();
    String getSimCard();
    String getSensors();
}
