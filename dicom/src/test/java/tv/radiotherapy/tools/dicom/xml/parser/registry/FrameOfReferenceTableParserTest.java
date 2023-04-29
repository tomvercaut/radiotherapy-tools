package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.SharedResources;
import tv.radiotherapy.tools.dicom.xml.model.registry.FrameOfReference;
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
        var doc = SharedResources.getInstance().getPart06();
        assertNotNull(doc);
        var root = doc.getDocumentElement();
        var registry = new FrameOfReferenceTableParser().parse(root, new FrameOfReferenceTableRowParser());
        assertNotNull(registry);

        var expected = List.of(
                new FrameOfReference("1.2.840.10008.1.4.1.1", "Talairach Brain Atlas Frame of Reference", "TalairachBrainAtlas"),
                new FrameOfReference("1.2.840.10008.1.4.1.2", "SPM2 T1 Frame of Reference", "SPM2T1")
        );
        for (var item : expected) {
            assertTrue(registry.contains(item));
        }
    }
}