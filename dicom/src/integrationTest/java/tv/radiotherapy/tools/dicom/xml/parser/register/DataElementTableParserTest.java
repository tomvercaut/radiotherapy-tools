package tv.radiotherapy.tools.dicom.xml.parser.register;

import org.junit.jupiter.api.Test;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.SharedResources;
import tv.radiotherapy.tools.dicom.xml.parser.registry.DataElementTableParser;
import tv.radiotherapy.tools.dicom.xml.parser.registry.DataElementTableRowParser;

import javax.xml.xpath.XPathExpressionException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataElementTableParserTest {

    @Test
    void build() throws ParserException, XPathExpressionException, ExecutionException, InterruptedException {
        var doc = SharedResources.getInstance().getPart06();
        assertNotNull(doc);
        var root = doc.getDocumentElement();
        var registry = new DataElementTableParser().parse(root, new DataElementTableRowParser());
        assertNotNull(registry);
    }
}