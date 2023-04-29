package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Node;
import tv.radiotherapy.tools.dicom.xml.model.registry.FrameOfReference;
import tv.radiotherapy.tools.dicom.xml.parser.InnerText;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.TableHelper;
import tv.radiotherapy.tools.dicom.xml.parser.TableRowParser;

import javax.xml.xpath.XPathExpressionException;
import java.util.Optional;

public class FrameOfReferenceTableRowParser implements TableRowParser<FrameOfReference> {
    @Override
    public Optional<FrameOfReference> parseRow(@NotNull Node row) throws XPathExpressionException, IllegalArgumentException, ParserException {
        if (row.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException("XML node must be of an XML element");
        }
        var tds = TableHelper.getColumns(row);
        if (tds.getLength() != 4) {
            throw new ParserException(String.format("Expected a table row with 4 columns but the actual column count is %d", tds.getLength()));
        }

        var uid = InnerText.get(tds.item(0)).replace(" ", "");
        var name = InnerText.get(tds.item(1));
        var keyword = InnerText.get(tds.item(2)).replace(" ", "");
        return Optional.of(new FrameOfReference(uid, name, keyword));
    }
}
