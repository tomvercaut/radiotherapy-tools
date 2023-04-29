package tv.radiotherapy.tools.dicom.xml.parser.registry.impl;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tv.radiotherapy.tools.dicom.xml.model.registry.DataElementItem;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.TableHelper;
import tv.radiotherapy.tools.dicom.xml.parser.TableParser;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class DataElementTableParser implements TableParser<List<DataElementItem>, DataElementTableRowParser> {

    @Override
    public List<DataElementItem> parse(@NotNull Element element, @NotNull DataElementTableRowParser rowParser) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        var registry = new ArrayList<DataElementItem>();
        build(element, registry, rowParser, "table_6-1");
        build(element, registry, rowParser, "table_7-1");
        build(element, registry, rowParser, "table_8-1");
        build(element, registry, rowParser, "table_9-1");
        return registry;
    }

    private void build(@NotNull Element root, @NotNull List<DataElementItem> registry, @NotNull DataElementTableRowParser rowParser, @NotNull String tableId) throws XPathExpressionException, IllegalArgumentException, ParserException {
        // Find table by id.
        final Node table = TableHelper.findById(root, tableId);
        // Extract table rows and iterate the rows.
        final NodeList rows = TableHelper.getRows(table);
        final var nrows = rows.getLength();
        for (int r = 0; r < nrows; r++) {
            var opt = rowParser.parseRow(rows.item(r));
            if (opt.isPresent()) {
                var item = opt.get();
                registry.add(item);
            }
        }
    }
}
