package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tv.radiotherapy.tools.dicom.xml.model.registry.FrameOfReferenceItem;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.TableHelper;
import tv.radiotherapy.tools.dicom.xml.parser.TableParser;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class FrameOfReferenceTableParser implements TableParser<List<FrameOfReferenceItem>, FrameOfReferenceTableRowParser> {
    @Override
    public List<FrameOfReferenceItem> parse(@NotNull Element element, @NotNull FrameOfReferenceTableRowParser rowParser) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        var registry = new ArrayList<FrameOfReferenceItem>();
        build(element, registry, rowParser, "table_A-2");
        return registry;
    }

    private void build(@NotNull Element element, @NotNull List<FrameOfReferenceItem> registry, @NotNull FrameOfReferenceTableRowParser rowParser, String tableId) throws XPathExpressionException, ParserException {
        // Find table by id.
        final Node table = TableHelper.findById(element, tableId);
        // Extract table rows and iterate the rows.
        final NodeList rows = TableHelper.getRows(table);
        final var nrows = rows.getLength();
        for (int r = 0; r < nrows; r++) {
            var opt = rowParser.parseRow(rows.item(r));
            opt.ifPresent(registry::add);
        }
    }
}
