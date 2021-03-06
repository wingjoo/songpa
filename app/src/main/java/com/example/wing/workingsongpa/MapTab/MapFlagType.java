package com.example.wing.workingsongpa.MapTab;

/**
 * Created by wing on 2016-12-23.
 */

public class MapFlagType {

    public static final int UNKNOWN = 0x0000;

    // Single POI icons
    private static final int SINGLE_POI_BASE = 0x0100;

    // Pin icons
    public static final int SPOT = SINGLE_POI_BASE + 1;
    public static final int START = SPOT + 1;
    public static final int COURSE = START + 1;
    public static final int NORMAL = COURSE + 1;
    public static final int IN = NORMAL + 1;
    public static final int OUT = IN + 1;
    //거점 이름핀 이미지
    public static final int TITLE1 = OUT + 1;
    public static final int TITLE2 = TITLE1 + 1;
    public static final int TITLE3 = TITLE2 + 1;
    public static final int TITLE4 = TITLE3 + 1;
    public static final int TITLE5 = TITLE4 + 1;
    //
    public static final int SELECTNUM1 = TITLE5 + 1;
    public static final int SELECTNUM2 = SELECTNUM1 + 1;
    public static final int SELECTNUM3 = SELECTNUM2 + 1;
    public static final int SELECTNUM4 = SELECTNUM3 + 1;
    public static final int SELECTNUM5 = SELECTNUM4 + 1;
    public static final int SELECTNUM6 = SELECTNUM5 + 1;
    public static final int SELECTNUM7 = SELECTNUM6 + 1;
    public static final int SELECTNUM8 = SELECTNUM7 + 1;
    public static final int SELECTNUM9 = SELECTNUM8 + 1;

    // public static final int SELECT = NORMAL + 1;

    // Direction POI icons: From, To
    private static final int DIRECTION_POI_BASE = 0x0200;
    public static final int FROM = DIRECTION_POI_BASE + 1;
    public static final int TO = FROM + 1;

    // end of single marker icon
    public static final int SINGLE_MARKER_END = 0x04FF;

    // Direction Number icons
    private static final int MAX_NUMBER_COUNT = 1000;
    public static final int NUMBER_BASE = 0x1000; // set NUMBER_BASE + 1 for '1' number
    public static final int NUMBER_END = NUMBER_BASE + MAX_NUMBER_COUNT;

    // Custom POI icons
    private static final int MAX_CUSTOM_COUNT = 1000;
    public static final int CUSTOM_BASE = NUMBER_END;
    public static final int CUSTOM_END = CUSTOM_BASE + MAX_CUSTOM_COUNT;

    // Clickable callout에 보여지는 화살표
//    public static final int CLICKABLE_ARROW = CUSTOM_END + 1;

    public static boolean isBoundsCentered(int markerId) {
        boolean boundsCentered = true;

//        switch (markerId) {
//            default:
//                if (markerId >= MapFlagType.NUMBER_BASE && markerId < MapFlagType.NUMBER_END) {
//                    boundsCentered = true;
//                }
//                break;
//        }

        return boundsCentered;
    }

    public static int getMarkerId(int poiFlagType, int iconIndex) {
        int markerId = poiFlagType + iconIndex;

        return markerId;
    }

    public static int getPOIflagType(int markerId) {
        int poiFlagType = UNKNOWN;

        // Alphabet POI icons
        if (markerId >= NUMBER_BASE && markerId < NUMBER_END) { // Direction Number icons
            poiFlagType = NUMBER_BASE;
        } else if (markerId >= CUSTOM_BASE && markerId < CUSTOM_END) { // Custom POI icons
            poiFlagType = CUSTOM_BASE;
        } else if (markerId > SINGLE_POI_BASE) {
            poiFlagType = markerId;
        }

        return poiFlagType;
    }

    public static int getPOIflagIconIndex(int markerId) {
        int iconIndex = 0;

        if (markerId >= NUMBER_BASE && markerId < NUMBER_END) { // Direction Number icons
            iconIndex = markerId - (NUMBER_BASE + 1);
        } else if (markerId >= CUSTOM_BASE && markerId < CUSTOM_END) { // Custom POI icons
            iconIndex = markerId - (CUSTOM_BASE + 1);
        } else if (markerId > SINGLE_POI_BASE) {
            iconIndex = 0;
        }

        return iconIndex;
    }

}
