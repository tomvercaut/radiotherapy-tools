package tv.radiotherapy.tools.dicom.xml.parser;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;
import tv.radiotherapy.tools.dicom.xml.model.Usage;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UsageParserTest {

    @Test
    void parse() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, ParserException {
        @Language("XML") var xml = """
                <?xml version="1.0" encoding="utf-8" standalone="no"?>
                <book>
                    <para>M</para>
                    <para>U</para>
                    <para>C</para>
                    <para>M - other</para>
                    <para>U - other</para>
                    <para>C - other</para>
                </book>""";
        var doc = DocumentReader.readXmlString(xml);
        assertNotNull(doc);
        var xpath = XPathFactory.newInstance().newXPath();
        var elements = (NodeList) xpath.compile("/book/para").evaluate(doc.getDocumentElement(), XPathConstants.NODESET);
        var usages = new ArrayList<Usage>();
        var parser = new UsageParser();
        for (int i = 0; i < elements.getLength(); i++) {
            usages.add(parser.parse((Element) elements.item(i)));
        }

        var expected = List.of(
                Usage.M,
                Usage.U,
                Usage.C,
                Usage.M,
                Usage.U,
                Usage.C
        );
        assertEquals(expected.size(), usages.size());
        for (int i = 0; i < elements.getLength(); i++) {
            assertEquals(expected.get(i), usages.get(i));
        }
    }
}