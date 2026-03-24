package FWReports.com.rp.reports.enums;

public enum ReportLabels {
    HEADER_TEXT("ATU Graphical Reports for Selenium - TESTNG"),
    PASS("Passed"),
    FAIL("Failed"),
    SKIP("Skipped"),
    X_AXIS("Run Number"),
    Y_AXIS("TC Number"),
    LINE_CHART_TOOLTIP("Test Cases"),
    ATU_LOGO("atu_logo.png"),
    ATU_CAPTION("<i style=\"float:left;padding-left:20px;font-size:12px\"></i>"),
    PROJ_LOGO(""),
    PROJ_CAPTION(""),
    PIE_CHART_LABEL("Test Case Percent Distribution"),
    TC_INFO_LABEL("Requirement Coverage/Build Info/Cycle - Description");

    private String label;

    private ReportLabels(String paramString) {
        this.setLabel(paramString);
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String paramString) {
        this.label = paramString;
    }
}
