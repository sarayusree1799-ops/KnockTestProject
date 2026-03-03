package com.realpage.com.Reports.com.rp.reports.writers;


import java.io.PrintWriter;

public class ReportsPage {
    public ReportsPage() {
    }

    public static void footer(PrintWriter paramPrintWriter) {
        paramPrintWriter.println("            <tr id=\"footer\">\n            <td colspan=\"2\">\n                 Best Viewed in &nbsp;\n <a href=\"http://www.mozilla.org/en-US/firefox/new/\">Firefox</a> &nbsp;\n   <a href=\"http://www.apple.com/in/safari/\">Safari</a>&nbsp;\n  <a href=\"http://www.google.com/chrome/\">Chrome</a>&nbsp;\n     <a href=\"http://windows.microsoft.com/en-IN/internet-explorer/download-ie\">IE 9 & Above</a>&nbsp;\n     Realpage ATU Reporter Version: 2023.06-RELEASE &nbsp;\n   Reports by: Automation Team\n    </td>\n  V</tr>\n    </table>\n   </body>\n</html>");
    }
}