package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tv.radiotherapy.tools.dicom.xml.model.registry.TemplateUid;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.TableHelper;
import tv.radiotherapy.tools.dicom.xml.parser.TableParser;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class TemplateUidTableParser implements TableParser<List<TemplateUid>, TemplateUidTableRowParser> {
    @Override
    public List<TemplateUid> parse(@NotNull Element element, @NotNull TemplateUidTableRowParser rowParser) throws ParserException, XPathExpressionException {
        var registry = new ArrayList<TemplateUid>();
        build(element, registry, rowParser, "table_A-4");
        return registry;
    }

    private void build(@NotNull Element root, List<TemplateUid> registry, @NotNull TemplateUidTableRowParser rowParser, @NotNull String tableId) throws XPathExpressionException, IllegalArgumentException, NullPointerException, ParserException {
        // Find table by id.
        final Node table = TableHelper.findById(root, tableId);
        // Extract table rows and iterate the rows.
        final NodeList rows = TableHelper.getRows(table);
        final var nrows = rows.getLength();
        for (int r = 0; r < nrows; r++) {
            var opt = rowParser.parseRow(rows.item(r));
            opt.ifPresent(registry::add);
        }
    }
}
