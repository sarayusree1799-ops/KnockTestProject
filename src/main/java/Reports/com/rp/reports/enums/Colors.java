package Reports.com.rp.reports.enums;

public enum Colors {
    PASS("#7BB661"),
    FAIL("#E03C31"),
    SKIP("#21ABCD");

    private String color;

    private Colors(String paramString) {
        this.setColor(paramString);
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String paramString) {
        this.color = paramString;
    }
}
