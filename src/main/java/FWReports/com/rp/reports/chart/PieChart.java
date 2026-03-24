package FWReports.com.rp.reports.chart;

import FWReports.com.rp.reports.enums.ReportLabels;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.Color;
import java.text.DecimalFormat;

public class PieChart {

    public PieChart() {
    }

    public static JFreeChart generate2DPieChart(int paramInt1, int paramInt2, int paramInt3) {
        DefaultPieDataset localDefaultPieDataset = new DefaultPieDataset();
        localDefaultPieDataset.setValue(ReportLabels.PASS.getLabel(), (double) paramInt1);
        localDefaultPieDataset.setValue(ReportLabels.FAIL.getLabel(), (double) paramInt2);
        localDefaultPieDataset.setValue(ReportLabels.SKIP.getLabel(), (double) paramInt3);
        JFreeChart localJFreeChart = ChartFactory.createPieChart(ReportLabels.PIE_CHART_LABEL.getLabel(), localDefaultPieDataset, true, true, true);
        PiePlot localPiePlot = (PiePlot) localJFreeChart.getPlot();
        localPiePlot.setCircular(true);
        localPiePlot.setForegroundAlpha(0.9F);
        localPiePlot.setBackgroundAlpha(0.9F);
        localPiePlot.setSectionPaint(ReportLabels.PASS.getLabel(), ChartColor.DARK_GREEN);
        localPiePlot.setSectionPaint(ReportLabels.FAIL.getLabel(), ChartColor.RED);
        localPiePlot.setSectionPaint(ReportLabels.SKIP.getLabel(), ChartColor.BLUE);
        localPiePlot.setExplodePercent(ReportLabels.PASS.getLabel(), 0.05);
        localPiePlot.setExplodePercent(ReportLabels.FAIL.getLabel(), 0.05);
        localPiePlot.setExplodePercent(ReportLabels.SKIP.getLabel(), 0.05);
        localPiePlot.setOutlinePaint(Color.BLACK);
        localPiePlot.setOutlineVisible(true);
        Color localColor = new Color(255, 255, 255, 0);
        localJFreeChart.setBackgroundPaint(localColor);
        localPiePlot.setBackgroundPaint(localColor);
        StandardPieSectionLabelGenerator localStandardPieSectionLabelGenerator = new StandardPieSectionLabelGenerator("{2}", new DecimalFormat("0"), new DecimalFormat("0%"));
        localPiePlot.setLabelGenerator(localStandardPieSectionLabelGenerator);
        return localJFreeChart;
    }
}