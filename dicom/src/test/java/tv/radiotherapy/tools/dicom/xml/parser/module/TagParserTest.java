package tv.radiotherapy.tools.dicom.xml.parser.module;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;
import tv.radiotherapy.tools.dicom.xml.model.module.Tag;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagParserTest {

    @Test
    void parse() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, ParserException {
        @Language("XML") var xml = """
                <?xml version="1.0" encoding="utf-8" standalone="no"?>
                <book>
                    <para>(1234,5678)</para>
                </book>
                """;
        var doc = DocumentReader.readXmlString(xml);
        assertNotNull(doc);
        var xpath = XPathFactory.newInstance().newXPath();
        var elements = (NodeList) xpath.compile("/book/para").evaluate(doc.getDocumentElement(), XPathConstants.NODESET);
        var tags = new ArrayList<Tag>();
        var parser = new TagParser();
        for (int i = 0; i < elements.getLength(); i++) {
            tags.add(parser.parse((Element) elements.item(i)));
        }

        var expected = List.of(
                new Tag(0x1234,0x5678)
        );
        assertEquals(expected.size(), tags.size());
        for (int i = 0; i < elements.getLength(); i++) {
            assertEquals(expected.get(i), tags.get(i));
        }
    }
    @Test
    void parseException() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        @Language("XML") var xml = """
                <?xml version="1.0" encoding="utf-8" standalone="no"?>
                <book>
                    <para>(12345678)</para>
                    <para>(123,5678)</para>
                    <para>(1234,678)</para>
                    <para>1234,5678)</para>
                    <para>(1234,5678</para>
                </book>
                """;
        var doc = DocumentReader.readXmlString(xml);
        assertNotNull(doc);
        var xpath = XPathFactory.newInstance().newXPath();
        var elements = (NodeList) xpath.compile("/book/para").evaluate(doc.getDocumentElement(), XPathConstants.NODESET);
        var parser = new TagParser();
        for (int i = 0; i < elements.getLength(); i++) {
            final var elem = (Element) elements.item(i);
            assertThrows(IllegalArgumentException.class,
                    () -> parser.parse(elem)
            );
        }
    }
}