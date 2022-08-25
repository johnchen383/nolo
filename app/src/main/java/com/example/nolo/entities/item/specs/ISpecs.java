package com.example.nolo.entities.item.specs;

import com.example.nolo.entities.item.specs.specsoption.SpecsOption;

import java.util.List;
import java.util.Map;

public interface ISpecs {
    Map<String, String> getFixedSpecs();
    Map<String, List<SpecsOption>> getCustomisableSpecs();
}
