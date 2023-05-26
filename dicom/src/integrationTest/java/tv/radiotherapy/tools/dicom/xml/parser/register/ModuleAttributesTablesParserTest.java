package tv.radiotherapy.tools.dicom.xml.parser.register;

import org.junit.jupiter.api.Test;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.SharedResources;
import tv.radiotherapy.tools.dicom.xml.parser.module.ModuleAttributesTablesParser;

import javax.xml.xpath.XPathExpressionException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ModuleAttributesTablesParserTest {

    @Test
    void parse() throws ParserException, XPathExpressionException, ExecutionException, InterruptedException {
        var doc = SharedResources.getInstance().getPart03();
        assertNotNull(doc);
        var root = doc.getDocumentElement();
        var list = new ModuleAttributesTablesParser().parse(root);
        assertNotNull(list);
    }
}