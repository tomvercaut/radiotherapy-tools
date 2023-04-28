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
            var item = new DataElementRegistry.Item();
            var tag = new RangedTag();
            tag.set(InnerText.get(tds.item(0)));
            item.setTag(tag);
            var name = InnerText.get(tds.item(1));
            item.setName(name);
            var keyword = InnerText.get(tds.item(2));
            item.setKeyword(keyword);

            if (name.isBlank() || keyword.isBlank()) {
                continue;
            }
            var lvr = vrParser.parse((Element) tds.item(3));
            var nlvr = lvr.size();
            if (nlvr == 0) {
                item.setVr(VR.NONE);
            } else {
                item.setVr(lvr.get(0));
                if (nlvr > 1) {
                    lvr.remove(0);
                    item.getOvr().addAll(lvr);
                }
            }
            var vm = InnerText.get(tds.item(4));
            item.setVm(vm);
            var desc = InnerText.get(tds.item(5));
            item.setDesc(desc);
            registry.add(item);
        }
    }


}
