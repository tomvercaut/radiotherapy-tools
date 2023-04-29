package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.DocumentReader;
import tv.radiotherapy.tools.dicom.xml.model.registry.FrameOfReferenceItem;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FrameOfReferenceTableParserTest {

    @Test
    void parse() throws ParserConfigurationException, IOException, SAXException, ParserException, XPathExpressionException {
        var classLoader = getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream("dicom_standard/part06/part06.xml");
        assertNotNull(inputStream);
        var doc = DocumentReader.read(inputStream);
        var root = doc.getDocumentElement();
        var registry = new FrameOfReferenceTableParser().parse(root, new FrameOfReferenceTableRowParser());
        assertNotNull(registry);

        var expected = List.of(
                new FrameOfReferenceItem("1.2.840.10008.1.4.1.1", "Talairach Brain Atlas Frame of Reference", "TalairachBrainAtlas"),
                new FrameOfReferenceItem("1.2.840.10008.1.4.1.2", "SPM2 T1 Frame of Reference", "SPM2T1")
        );
        for (FrameOfReferenceItem item : expected) {
            assertTrue(registry.contains(item));
        }
    }
}