package tv.radiotherapy.tools.dicom.xml.parser;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataElementRegistryParserTest {

    @Test
    void build() throws IOException, ParserConfigurationException, SAXException, ParserException, XPathExpressionException {
        var classLoader = getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream("dicom_standard/part06/part06.xml");
        assertNotNull(inputStream);
        var doc = DocumentReader.read(inputStream);
        var root = doc.getDocumentElement();
        var registry = new DataElementRegistryParser().parse(root);
        assertNotNull(registry);
    }
}