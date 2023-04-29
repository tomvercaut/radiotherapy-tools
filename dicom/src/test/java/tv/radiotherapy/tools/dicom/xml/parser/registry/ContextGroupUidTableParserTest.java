package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import tv.radiotherapy.tools.dicom.xml.SharedResources;
import tv.radiotherapy.tools.dicom.xml.model.OLink;
import tv.radiotherapy.tools.dicom.xml.model.registry.ContextGroupUid;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContextGroupUidTableParserTest {

    @Test
    void parse() throws ParserConfigurationException, IOException, SAXException, ParserException, XPathExpressionException {
        var doc = SharedResources.getInstance().getPart06();
        assertNotNull(doc);
        var root = doc.getDocumentElement();
        var registry = new ContextGroupUidTableParser().parse(root, new ContextGroupUidTableRowParser());
        assertNotNull(registry);

        var expected = List.of(
                new ContextGroupUid("1.2.840.10008.6.1.1", new OLink("PS3.16", "sect_CID_2", "select: labelnumber"), new OLink("PS3.16", "sect_CID_2", "select: title"), ""),
                new ContextGroupUid("1.2.840.10008.6.1.2", new OLink("PS3.16", "sect_CID_4", "select: labelnumber"), new OLink("PS3.16", "sect_CID_4", "select: title"), ""),
                new ContextGroupUid("1.2.840.10008.6.1.945", new OLink("", "", ""), new OLink("", "", ""), "Retired"),
                new ContextGroupUid("1.2.840.10008.6.1.946", new OLink("", "", ""), new OLink("", "", ""), "Retired")
        );
        for (var item : expected) {
            assertTrue(registry.contains(item));
        }
    }
}