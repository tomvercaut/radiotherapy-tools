package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.SharedResources;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataElementTableParserTest {

    @Test
    void build() throws IOException, ParserConfigurationException, SAXException, ParserException, XPathExpressionException {
        var doc = SharedResources.getInstance().getPart06();
        assertNotNull(doc);
        var root = doc.getDocumentElement();
        var registry = new DataElementTableParser().parse(root, new DataElementTableRowParser());
        assertNotNull(registry);
    }
}