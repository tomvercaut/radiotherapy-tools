package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import tv.radiotherapy.tools.dicom.xml.model.registry.DataElement;
import tv.radiotherapy.tools.dicom.xml.model.RangedTag;
import tv.radiotherapy.tools.dicom.xml.model.VR;
import tv.radiotherapy.tools.dicom.xml.parser.*;

import javax.xml.xpath.XPathExpressionException;
import java.util.Optional;

public class DataElementTableRowParser implements TableRowParser<DataElement> {
    private final VRParser vrParser = new VRParser();
    @Override
    public Optional<DataElement> parseRow(@NotNull Node row) throws XPathExpressionException, IllegalArgumentException, ParserException {
        if (row.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException("XML node must be of an XML element");
        }
        // Extract row columns and iterate them
        var tds = TableHelper.getColumns(row);
        if (tds.getLength() != 6) {
            throw new ParserException(String.format("Expected a table row with 6 columns but the actual column count is %d", tds.getLength()));
        }
        var tag = RangedTag.create(InnerText.get(tds.item(0)));
        var name = InnerText.get(tds.item(1));
        var keyword = InnerText.get(tds.item(2));
        VR vr;
        if (name.isBlank() || keyword.isBlank()) {
            return Optional.empty();
        }
        var lvr = vrParser.parse((Element) tds.item(3));
        if (lvr.isEmpty()) {
            vr = VR.NONE;
        } else {
            vr = lvr.get(0);
            lvr.remove(0);
        }
        var vm = InnerText.get(tds.item(4));
        var desc = InnerText.get(tds.item(5));
        return Optional.of(new DataElement(
                tag, name, keyword, vr, lvr, vm, desc
        ));
    }
}
