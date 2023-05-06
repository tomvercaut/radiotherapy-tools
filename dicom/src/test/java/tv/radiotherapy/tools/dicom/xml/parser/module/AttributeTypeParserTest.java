package tv.radiotherapy.tools.dicom.xml.parser.module;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;
import tv.radiotherapy.tools.dicom.xml.model.module.AttributeType;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AttributeTypeParserTest {

    @Test
    void parse() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, ParserException {
        @Language("XML") var xml = """
                <?xml version="1.0" encoding="utf-8" standalone="no"?>
                <book>
                    <para>1</para>
                    <para>1C</para>
                    <para>2</para>
                    <para>2 </para>
                    <para>3
                    </para>
                </book>
                """;
        var doc = DocumentReader.readXmlString(xml);
        assertNotNull(doc);
        var xpath = XPathFactory.newInstance().newXPath();
        var elements = (NodeList) xpath.compile("/book/para").evaluate(doc.getDocumentElement(), XPathConstants.NODESET);
        var types = new ArrayList<AttributeType>();
        var parser = new AttributeTypeParser();
        for (int i = 0; i < elements.getLength(); i++) {
            types.add(parser.parse((Element) elements.item(i)));
        }

        var expected = List.of(
                AttributeType.ONE,
                AttributeType.ONE_C,
                AttributeType.TWO,
                AttributeType.TWO,
                AttributeType.THREE
        );
        assertEquals(expected.size(), types.size());
        for (int i = 0; i < elements.getLength(); i++) {
            assertEquals(expected.get(i), types.get(i));
        }
    }
}