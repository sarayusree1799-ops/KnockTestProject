package FWReports.com.rp.reports.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class TestNGXMLReader {
    public TestNGXMLReader() {
    }

    public static void read(String paramString) {
        DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
            Document localDocument = localDocumentBuilder.parse(paramString);
            localDocument.getDocumentElement().normalize();
            NodeList localNodeList1 = localDocument.getElementsByTagName("atu");
            if (localNodeList1.getLength() > 0) {
                Node localNode = localNodeList1.item(0);
                if (localNode.getNodeType() == 1) {
                    Element localElement = (Element) localNode;
                    NodeList localNodeList2 = localElement.getElementsByTagName("dir");
                    Object localObject = null;
                    if (localNodeList2.getLength() > 0) {
                        localObject = ((Element) localNodeList2.item(0)).getAttribute("value");
                    }
                    Object va15 = localElement.getElementsByTagName("header");
                    if (((NodeList) va15).getLength() > 0) {
                        String str1 = ((Element) ((NodeList) localObject).item(0)).getAttribute("text");
                        String var10 = ((Element) ((NodeList) localObject).item(0)).getAttribute("logo");
                    }
                }
            }
        }catch (ParserConfigurationException var11){
        } catch (IOException var12) {
        } catch (SAXException var13) {
        }
    }
}