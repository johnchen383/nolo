package com.example.nolo.entities.item.specs;

import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.enums.SpecsOptionType;
import com.example.nolo.enums.SpecsType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Specs implements ISpecs {
    private Map<SpecsType, String> fixedSpecs = new HashMap<>();
    private Map<SpecsOptionType, List<SpecsOption>> customisableSpecs = new HashMap<>();

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Specs() {}

    public Specs(Map<SpecsType, String> fixedSpecs) {
        this.fixedSpecs = fixedSpecs;
    }

    public Specs(Map<SpecsType, String> fixedSpecs, Map<SpecsOptionType, List<SpecsOption>> customisableSpecs) {
        this.fixedSpecs = fixedSpecs;
        this.customisableSpecs = customisableSpecs;
    }

    @Override
    public Map<SpecsType, String> getFixedSpecs() {
        return fixedSpecs;
    }

    @Override
    public Map<SpecsOptionType, List<SpecsOption>> getCustomisableSpecs() {
        return customisableSpecs;
    }
}
