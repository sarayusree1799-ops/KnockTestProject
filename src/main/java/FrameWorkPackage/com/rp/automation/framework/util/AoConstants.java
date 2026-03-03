package FrameWorkPackage.com.rp.automation.framework.util;

public class AoConstants {
    public static enum PROPERTYPICKER_VIEW {
        SEARCH_TAB {
            @Override
            public String toString() {
                return "Search";
            }
        },
        GROUPS_TAB {
            @Override
            public String toString() {
                return "Groups";
            }
        },
        SELECTED_TAB {
            @Override
            public String toString() {
                return "Selected";
            }
        }
    }
}
