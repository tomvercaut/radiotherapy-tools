package tv.radiotherapy.tools.dicom.xml.parser.register;

import org.junit.jupiter.api.Test;
import tv.radiotherapy.tools.dicom.xml.model.OLink;
import tv.radiotherapy.tools.dicom.xml.model.UidType;
import tv.radiotherapy.tools.dicom.xml.model.registry.TemplateUid;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.SharedResources;
import tv.radiotherapy.tools.dicom.xml.parser.registry.TemplateUidTableParser;
import tv.radiotherapy.tools.dicom.xml.parser.registry.TemplateUidTableRowParser;

import javax.xml.xpath.XPathExpressionException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemplateUidTableParserTest {

    @Test
    void parse() throws ParserException, XPathExpressionException, ExecutionException, InterruptedException {
        var doc = SharedResources.getInstance().getPart06();
        assertNotNull(doc);
        var root = doc.getDocumentElement();
        var registry = new TemplateUidTableParser().parse(root, new TemplateUidTableRowParser());
        assertNotNull(registry);

        var expected = List.of(
                new TemplateUid("1.2.840.10008.9.1", new OLink("PS3.20", "sect_7.1", "select: title"), UidType.DocumentTemplateId, new OLink("PS3.20", "PS3.20", "select: labelnumber")),
                new TemplateUid("1.2.840.10008.9.2", new OLink("PS3.20", "sect_9.2", "select: title"), UidType.SectionTemplateId, new OLink("PS3.20", "PS3.20", "select: labelnumber"))
        );
        for (var item : expected) {
            assertTrue(registry.contains(item));
        }
    }
}