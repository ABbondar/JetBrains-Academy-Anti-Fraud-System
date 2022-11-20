package antifraud.model;

import java.util.List;

public class Region {
    private static final List<String> regions =
            List.of("EAP", "ECA", "HIC", "LAC", "MENA", "SA", "SSA");

    public static List<String> getRegions() {
        return regions;
    }
}