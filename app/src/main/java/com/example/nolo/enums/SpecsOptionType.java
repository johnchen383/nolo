package com.example.nolo.enums;

public enum SpecsOptionType {
    storage("SSD"),
    ram("RAM");

    private final String units;

    SpecsOptionType(String units) {
        this.units = units;
    }

    public String getUnits() {
        return this.units;
    }
}
