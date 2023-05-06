package tv.radiotherapy.tools.dicom.xml.parser.module;

import org.junit.jupiter.api.Test;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.SharedResources;

import javax.xml.xpath.XPathExpressionException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CiodTablesParserTest {

    @Test
    void parse() throws ExecutionException, InterruptedException, ParserException, XPathExpressionException {
        var doc = SharedResources.getInstance().getPart03();
        assertNotNull(doc);
        var root = doc.getDocumentElement();
        var tableParser = new CiodTablesParser();
        tableParser.enableParallel(true);
        final var ciods = tableParser.parse(root);
        assertFalse(ciods.isEmpty());
    }
}