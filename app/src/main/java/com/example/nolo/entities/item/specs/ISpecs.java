package com.example.nolo.entities.item.specs;

import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.enums.SpecsOptionType;
import com.example.nolo.enums.SpecsType;

import java.util.List;
import java.util.Map;

public interface ISpecs {
    Map<SpecsType, String> getFixedSpecs();
    Map<SpecsOptionType, List<SpecsOption>> getCustomisableSpecs();
}
