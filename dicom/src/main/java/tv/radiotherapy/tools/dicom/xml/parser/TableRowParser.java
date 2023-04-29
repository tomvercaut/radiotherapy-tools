package tv.radiotherapy.tools.dicom.xml.parser;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;
import java.util.Optional;

public interface TableRowParser<RowModel> {
    Optional<RowModel> parseRow(@NotNull Node row) throws XPathExpressionException, IllegalArgumentException, ParserException;
}
