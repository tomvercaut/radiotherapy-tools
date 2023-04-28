package tv.radiotherapy.tools.dicom.xml.parser;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

public interface Parser<Model> {
    /**
     * Parse an XML element and create a model from it.
     * @param element XML element
     * @return Type Model
     * @throws ParserException Thrown in case something goes wrong while parsing the XML element.
     * @throws XPathExpressionException If an XPath expression cannot be evaluated.
     * @throws IllegalArgumentException If {@code returnType} is not one of the types defined in {@link XPathConstants}.
     * @throws NullPointerException     If {@code returnType} is {@code null}.
     */
    Model parse(@NotNull Element element) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException;
}
