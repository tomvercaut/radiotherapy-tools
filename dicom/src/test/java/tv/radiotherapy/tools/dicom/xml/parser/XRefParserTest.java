package tv.radiotherapy.tools.dicom.xml.parser;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XRefParserTest {

    @Test
    void parse() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, ParserException {
        @Language("XML") var xml = """
                <?xml version="1.0" encoding="utf-8" standalone="no"?>
                <book>
                    <xref linkend="link" xrefstyle="style"/>
                </book>""";
        var doc = DocumentReader.readXmlString(xml);
        var xpath = XPathFactory.newInstance().newXPath();
        var element = (Element) xpath.compile("/book/xref").evaluate(doc.getDocumentElement(), XPathConstants.NODE);
        var xref = new XRefParser().parse(element);
        assertEquals("link", xref.link());
        assertEquals("style", xref.style());
    }

    @Test
    void parseInvalidLinkend() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        @Language("XML") var xml = """
                <?xml version="1.0" encoding="utf-8" standalone="no"?>
                <book>
                    <xref linkend="" xrefstyle="style"/>
                </book>""";
        var doc = DocumentReader.readXmlString(xml);
        var xpath = XPathFactory.newInstance().newXPath();
        var element = (Element) xpath.compile("/book/xref").evaluate(doc.getDocumentElement(), XPathConstants.NODE);
        assertThrows(ParserException.class, () -> new XRefParser().parse(element));
    }
    @Test
    void parseInvalidXrefstyle() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        @Language("XML") var xml = """
                <?xml version="1.0" encoding="utf-8" standalone="no"?>
                <book>
                    <xref linkend="link" xrefstyle=""/>
                </book>""";
        var doc = DocumentReader.readXmlString(xml);
        var xpath = XPathFactory.newInstance().newXPath();
        var element = (Element) xpath.compile("/book/xref").evaluate(doc.getDocumentElement(), XPathConstants.NODE);
        assertThrows(ParserException.class, () -> new XRefParser().parse(element));
    }
}