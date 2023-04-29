package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.registry.impl.DataElementTableRowParser;
import tv.radiotherapy.tools.dicom.xml.parser.registry.impl.DataElementTableParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataElementTableParserTest {

    @Test
    void build() throws IOException, ParserConfigurationException, SAXException, ParserException, XPathExpressionException {
        var classLoader = getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream("dicom_standard/part06/part06.xml");
        assertNotNull(inputStream);
        var doc = DocumentReader.read(inputStream);
        var root = doc.getDocumentElement();
        var registry = new DataElementTableParser().parse(root, new DataElementTableRowParser());
        assertNotNull(registry);
    }
}