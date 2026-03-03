package FrameWorkPackage.com.rp.automation.framework.util;

public class Constants {
    public static enum PROPERTYPICKER_VIEW {
        SEARCH_TAB {
            public String toString() {
                return "Search";
            }
        },
        GROUPS_TAB {
            public String toString() {
                return "Groups";
            }
        },
        SELECTED_TAB {
            public String toString() {
                return "Selected";
            }
        };

        private PROPERTYPICKER_VIEW() {

        }
    }
}
