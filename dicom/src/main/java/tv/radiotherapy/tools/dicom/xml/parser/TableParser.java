package tv.radiotherapy.tools.dicom.xml.parser;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

public interface TableParser<TableModel, RowParser extends TableRowParser<?>> {
    /**
     * Parse a table create a row model from it.
     * Each row in the table is parsed by a RowParser.
     *
     * @param element   table row XML element
     * @param rowParser parser for the XML table row
     * @return Type RowModel
     * @throws ParserException          Thrown in case something goes wrong while parsing the XML element.
     * @throws XPathExpressionException If an XPath expression cannot be evaluated.
     * @throws IllegalArgumentException If {@code returnType} is not one of the types defined in {@link XPathConstants}.
     * @throws NullPointerException     If {@code returnType} is {@code null}.
     */
    TableModel parse(@NotNull Element element, @NotNull RowParser rowParser) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException;
}
