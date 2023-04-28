package tv.radiotherapy.tools.dicom.xml.parser;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tv.radiotherapy.tools.dicom.xml.model.DataElementRegistry;
import tv.radiotherapy.tools.dicom.xml.model.RangedTag;
import tv.radiotherapy.tools.dicom.xml.model.VR;

import javax.xml.xpath.XPathExpressionException;

public class DataElementRegistryParser implements Parser<DataElementRegistry> {

    @Override
    public DataElementRegistry parse(@NotNull Element element) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        var registry = new DataElementRegistry();
        build(element, registry, "table_6-1");
        build(element, registry, "table_7-1");
        build(element, registry, "table_8-1");
        build(element, registry, "table_9-1");
        return registry;
    }

    private void build(@NotNull Element root, @NotNull DataElementRegistry registry, @NotNull String tableId) throws XPathExpressionException, IllegalArgumentException, ParserException {
        // Find table by id.
        final Node table = TableHelper.findById(root, tableId);
        // Extract table rows and iterate the rows.
        final NodeList rows = TableHelper.getRows(table);
        final var nrows = rows.getLength();
        // VR parser
        final var vrParser = new VRParser();
        for (int r = 0; r < nrows; r++) {

            // Extract row columns and iterate them
            var tds = TableHelper.getColumns(rows.item(r));
            if (tds.getLength() != 6) {
                throw new ParserException(String.format("Expected a table row[%d] with 6 columns but the actual column count is %d", r, tds.getLength()));
            }
            var tag = RangedTag.create(InnerText.get(tds.item(0)));
            var name = InnerText.get(tds.item(1));
            var keyword = InnerText.get(tds.item(2));
            VR vr;
            if (name.isBlank() || keyword.isBlank()) {
                continue;
            }
            var lvr = vrParser.parse((Element) tds.item(3));
            if (lvr.size() == 0) {
                vr = VR.NONE;
            } else {
                vr = lvr.get(0);
                lvr.remove(0);
            }
            var vm = InnerText.get(tds.item(4));
            var desc = InnerText.get(tds.item(5));
            registry.add(
                    new DataElementRegistry.Item(
                            tag, name, keyword, vr, lvr, vm, desc
                    ));
        }
    }


}
