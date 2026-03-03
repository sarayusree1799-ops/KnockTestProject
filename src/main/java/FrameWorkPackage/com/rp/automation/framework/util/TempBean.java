package FrameWorkPackage.com.rp.automation.framework.util;

public class TempBean {
    private String propertyName;
    private String beginDateRange;

    public String getPropertyName() {
        return this.propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getBeginDateRange() {
        return this.beginDateRange;
    }

    public void setBeginDateRange(String beginDateRange) {
        if (beginDateRange != null && beginDateRange.contains("-")) {
            beginDateRange = beginDateRange.replace("-", "").substring(0, 6);
        }

        this.beginDateRange = beginDateRange;
    }

    public String toString() {
        return "TempBean [propertyName=" + this.propertyName + ", beginDateRange=" + this.beginDateRange + "]";
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.beginDateRange == null ? 0 : this.beginDateRange.hashCode());
        result = 31 * result + (this.propertyName == null ? 0 : this.propertyName.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            TempBean other = (TempBean) obj;
            if (this.beginDateRange == null) {
                if (other.beginDateRange != null) {
                    return false;
                }
            } else if (!this.beginDateRange.equals(other.beginDateRange)) {
                return false;
            }

            if (this.propertyName == null) {
                if (other.propertyName != null) {
                    return false;
                }
            } else if (!this.propertyName.equals(other.propertyName)) {
                return false;
            }
            return true;
        }
    }
}