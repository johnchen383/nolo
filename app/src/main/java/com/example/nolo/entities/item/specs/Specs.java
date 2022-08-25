package com.example.nolo.entities.item.specs;

import com.example.nolo.entities.item.specs.specsoption.SpecsOption;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Specs implements ISpecs {
    private Map<String, String> fixedSpecs = new HashMap<>();
    private Map<String, List<SpecsOption>> customisableSpecs = new HashMap<>();

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Specs() {}

    public Specs(Map<String, String> fixedSpecs) {
        this.fixedSpecs = fixedSpecs;
    }

    public Specs(Map<String, String> fixedSpecs, Map<String, List<SpecsOption>> customisableSpecs) {
        this.fixedSpecs = fixedSpecs;
        this.customisableSpecs = customisableSpecs;
    }

    @Override
    public Map<String, String> getFixedSpecs() {
        return fixedSpecs;
    }

    @Override
    public Map<String, List<SpecsOption>> getCustomisableSpecs() {
        return customisableSpecs;
    }
}
