package tv.radiotherapy.tools.dicom.xml.parser;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;
import tv.radiotherapy.tools.dicom.xml.model.OLink;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class OLinkParserTest {

    @Test
    void parse() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, ParserException {
        @Language("XML") var xml = """
                <?xml version="1.0" encoding="utf-8" standalone="no"?>
                <book>
                    <olink targetdoc="document" targetptr="ptr" xrefstyle="style"/>
                </book>""";
        var doc = DocumentReader.readXmlString(xml);
        var xpath = XPathFactory.newInstance().newXPath();
        var element = (Element) xpath.compile("/book/olink").evaluate(doc.getDocumentElement(), XPathConstants.NODE);
        OLink olink = new OLinkParser().parse(element);
        assertEquals("document", olink.document());
        assertEquals("ptr", olink.ptr());
        assertEquals("style", olink.style());
    }


    @Test
    void parseInvalidElement() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        @Language("XML") var xml = """
                <?xml version="1.0" encoding="utf-8" standalone="no"?>
                <book>
                    <other targetdoc="document" targetptr="ptr" xrefstyle="style"/>
                </book>""";
        var doc = DocumentReader.readXmlString(xml);
        var xpath = XPathFactory.newInstance().newXPath();
        var element = (Element) xpath.compile("/book/other").evaluate(doc.getDocumentElement(), XPathConstants.NODE);
        var exception = assertThrows(ParserException.class, () -> new OLinkParser().parse(element));
        assertTrue(exception.getMessage().contains("found an element"));
    }

    @Test
    void parseNoTargetDoc() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        @Language("XML") var xml = """
                <?xml version="1.0" encoding="utf-8" standalone="no"?>
                <book>
                    <olink targetdoc="" targetptr="ptr" xrefstyle="style"/>
                </book>""";
        var doc = DocumentReader.readXmlString(xml);
        var xpath = XPathFactory.newInstance().newXPath();
        var element = (Element) xpath.compile("/book/olink").evaluate(doc.getDocumentElement(), XPathConstants.NODE);
        var exception = assertThrows(ParserException.class, () -> new OLinkParser().parse(element));
        assertTrue(exception.getMessage().contains("targetdoc"));
    }

    @Test
    void parseNoTargetPtr() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        @Language("XML") var xml = """
                <?xml version="1.0" encoding="utf-8" standalone="no"?>
                <book>
                    <olink targetdoc="document" targetptr="" xrefstyle="style"/>
                </book>""";
        var doc = DocumentReader.readXmlString(xml);
        var xpath = XPathFactory.newInstance().newXPath();
        var element = (Element) xpath.compile("/book/olink").evaluate(doc.getDocumentElement(), XPathConstants.NODE);
        var exception = assertThrows(ParserException.class, () -> new OLinkParser().parse(element));
        assertTrue(exception.getMessage().contains("targetptr"));
    }

    @Test
    void parseNoXRefStyle() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        @Language("XML") var xml = """
                <?xml version="1.0" encoding="utf-8" standalone="no"?>
                <book>
                    <olink targetdoc="document" targetptr="ptr" xrefstyle=""/>
                </book>""";
        var doc = DocumentReader.readXmlString(xml);
        var xpath = XPathFactory.newInstance().newXPath();
        var element = (Element) xpath.compile("/book/olink").evaluate(doc.getDocumentElement(), XPathConstants.NODE);
        var exception = assertThrows(ParserException.class, () -> new OLinkParser().parse(element));
        assertTrue(exception.getMessage().contains("xrefstyle"));
    }

}