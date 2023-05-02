package tv.radiotherapy.tools.dicom.xml.parser.module;

import org.junit.jupiter.api.Test;
import tv.radiotherapy.tools.dicom.xml.SharedResources;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

import javax.xml.xpath.XPathExpressionException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class CiodTablesParserTest {

    @Test
    void parse() throws ExecutionException, InterruptedException, ParserException, XPathExpressionException {
        var doc = SharedResources.getInstance().getPart03();
        assertNotNull(doc);
        var root = doc.getDocumentElement();
        final var ciods = new CiodTablesParser().parse(root);
        assertFalse(ciods.isEmpty());
    }
}