package com.example.nolo.entities.item.specs;

import com.example.nolo.enums.SpecsOptionType;
import com.example.nolo.enums.SpecsType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LaptopSpecs {
    public static final List<SpecsType> FIXED_SPECS = new ArrayList<>(Arrays.asList(
            SpecsType.operatingSystem,
            SpecsType.display,
            SpecsType.cpu,
            SpecsType.gpu,
            SpecsType.camera,
            SpecsType.keyboard,
            SpecsType.communication,
            SpecsType.audio,
            SpecsType.touchscreen,
            SpecsType.fingerprintReader,
            SpecsType.opticalDrive,
            SpecsType.ports,
            SpecsType.battery,
            SpecsType.acAdaptor,
            SpecsType.dimensions,
            SpecsType.weight
    ));

    public static final List<SpecsOptionType> CUSTOMISABLE_SPECS = new ArrayList<>(Arrays.asList(
            SpecsOptionType.ram,
            SpecsOptionType.storage
    ));
}
