package tv.radiotherapy.tools.dicom.xml.parser.module.attribute;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tv.radiotherapy.tools.dicom.xml.model.RangedTag;
import tv.radiotherapy.tools.dicom.xml.model.module.AttributeType;
import tv.radiotherapy.tools.dicom.xml.model.module.attribute.*;
import tv.radiotherapy.tools.dicom.xml.parser.*;
import tv.radiotherapy.tools.dicom.xml.parser.module.AttributeTypeParser;
import tv.radiotherapy.tools.dicom.xml.parser.module.NoIncludeException;
import tv.radiotherapy.tools.dicom.xml.parser.module.IncludeRecordSelectionKeysException;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.Optional;

public class AttributeTableRowParser implements TableRowParser<Item> {
    private static final Logger logger = LogManager.getLogger(AttributeTableRowParser.class);
    private static final XRefParser xRefParser = new XRefParser();
    private static final AttributeTypeParser attributeTypeParser = new AttributeTypeParser();

    @Override
    public Optional<Item> parseRow(@NotNull Node row) throws XPathExpressionException, IllegalArgumentException, ParserException {
        if (row.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException("XML node must be of an XML element");
        }
        var tds = TableHelper.getColumns(row);
        var ntds = tds.getLength();
        if (ntds == 0) {
            throw new ParserException("Expected a table row with at least one column.");
        }
        if (ntds >= 5) {
            throw new ParserException(String.format("Expected a table row with a maximum of 4 columns but the actual count is %d", ntds));
        }

        if (ntds == 1) {
            return Optional.of(new Description(InnerText.get(tds.item(0))));
        } else if (ntds == 2) {
            // In case of two columns:
            // The first column contains an xref element
            // The second column contains a description
            var col0 = tds.item(0);
            var sc0 = InnerText.get(col0);
            var sid = SequenceItemDepth.get(sc0);
            int depth;
            if (sid.isEmpty()) {
                depth = 0;
            } else {
                depth = sid.get().getKey();

            }
            var xrefElement = (Element) xrefExpression().evaluate(col0, XPathConstants.NODE);
            if (xrefElement == null && sc0.contains("Include Record Selection Keys")) {
                throw new IncludeRecordSelectionKeysException("First row contains \"Include Record Selection Keys\" and doesn't contain an xref");
            }
            if (xrefElement == null && sc0.contains("Include")) {
                // there are tables that contain an ">Include" but that don't have an XRef element. At this moment the text in this row / column is not processed.
                throw new NoIncludeException("First column contains the word \"Include\" but doesn't have an xref element");
            }
            if (xrefElement == null || !sc0.contains("Include")) {
                // The first column doesn't contain a xref element or the text Include
                throw new ParserException("Expected the first column to contain the word \"Include\" and an xref element.");
            }
            var xref = xRefParser.parse(xrefElement);
            var sc1 = InnerText.get(tds.item(1));
            return Optional.of(new Include(depth, xref, sc1));
        } else if (ntds == 3) {
            // In case of three columns:
            // The first column contains a name
            // The second column contains a tag
            // The third column contains a description
            var col2 = tds.item(2);
            var depthNameTag = getDepthNameTag(tds);
            int depth = depthNameTag.getLeft();
            var name = depthNameTag.getMiddle();
            var tag = depthNameTag.getRight();
            var desc = InnerText.get(col2);
            if (tag == null) {
                // special case
                if (name.contains("Any Attribute from the top level Data Set that was modified or removed")) {
                    var tDepthNameAttrType = getDepthNameAttrType(tds);
                    depth = tDepthNameAttrType.getLeft();
                    name = tDepthNameAttrType.getMiddle();
                    var type = tDepthNameAttrType.getRight();
                    return Optional.of(new ParentItem(depth, name, type, desc));
                } else if (name.startsWith("Any DA, DT, or TM Attributes")) {
                    var tDepthNameAttrType = getDepthNameAttrType(tds);
                    depth = tDepthNameAttrType.getLeft();
                    var type = tDepthNameAttrType.getRight();
                    return Optional.of(new AnyDateTime(depth, type, desc));
                } else if (name.startsWith("Any UI Key Attribute")) {
                    var tDepthNameAttrType = getDepthNameAttrType(tds);
                    depth = tDepthNameAttrType.getLeft();
                    var type = tDepthNameAttrType.getRight();
                    return Optional.of(new AnyUid(depth, type, desc));
                } else if (name.startsWith("Any Attributes")) {
                    var tDepthNameAttrType = getDepthNameAttrType(tds);
                    depth = tDepthNameAttrType.getLeft();
                    var type = tDepthNameAttrType.getRight();
                    return Optional.of(new Any(depth, type, desc));
                }
                throw new ParserException("RangedTag was null and a special case was not met.");
            } else {
                return Optional.of(new NameTagDescItem(depth, name, tag, desc));
            }
        } else if (ntds == 4) {
            // In case of three columns:
            // The first column contains a name
            // The second column contains a tag
            // The third column contains an attribute / macro type
            // The fourth column contains a description
            var depthNameTag = getDepthNameTag(tds);
            var type = attributeTypeParser.parse((Element) tds.item(2));
            var desc = InnerText.get(tds.item(3));
            return Optional.of(new NameTagTypeDescItem(depthNameTag.getLeft(), depthNameTag.getMiddle(), depthNameTag.getRight(), type, desc));
        }

        return Optional.empty();
    }

    /**
     * Extract these common properties from the XML table row columns:
     *
     * <ol>
     *    <li>sequence item depth [extracted from column 0]</li>
     *    <li>attribute / macro name [extracted from column 0]</li>
     *    <li>ranged DICOM tag [extracted from column 1]</li>
     * </ol>
     *
     * @param columns XML table row columns
     * @return An element's depth in a sequence, it's name and DICOM tag.
     */
    private Triple<Integer, String, RangedTag> getDepthNameTag(NodeList columns) {
        var col0 = columns.item(0);
        var col1 = columns.item(1);
        var sc0 = InnerText.get(col0);
        var depthParam = SequenceItemDepth.get(sc0);
        int depth = 0;
        String name;
        if (depthParam.isEmpty()) {
            name = sc0;
        } else {
            var p = depthParam.get();
            depth = p.getKey();
            name = sc0.substring(p.getValue());
            name = name.trim();
        }
        var stag = InnerText.get(col1);

        RangedTag tag;
        try {
            tag = RangedTag.create(stag);
        } catch (IllegalArgumentException exception) {
            tag = null;
            logger.debug(String.format("Unable to create a RangedTag from {%s}.\nException: {%s}\nThe calling function should investigate how to resolve this.", stag, exception));
        }
        return new ImmutableTriple<>(depth, name, tag);
    }

    private Triple<Integer, String, AttributeType> getDepthNameAttrType(NodeList columns) throws ParserException, XPathExpressionException {
        var col0 = columns.item(0);
        var col1 = columns.item(1);
        var sc0 = InnerText.get(col0);
        var depthParam = SequenceItemDepth.get(sc0);
        int depth = 0;
        String name;
        if (depthParam.isEmpty()) {
            name = sc0;
        } else {
            var p = depthParam.get();
            depth = p.getKey();
            name = sc0.substring(p.getValue());
            name = name.trim();
        }
        var type = attributeTypeParser.parse((Element) col1);
        return new ImmutableTriple<>(depth, name, type);
    }


    private static XPathExpression xrefExpression() throws XPathExpressionException {
        return XPathFactory.newInstance().newXPath().compile(".//xref");
    }
}
