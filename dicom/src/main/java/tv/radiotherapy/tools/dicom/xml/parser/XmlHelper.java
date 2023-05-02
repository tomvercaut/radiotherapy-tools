package tv.radiotherapy.tools.dicom.xml.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Helper class for XML documents, nodes, ...
 */
public class XmlHelper {
    private static final Logger logger = LogManager.getLogger(TableHelper.class);
    private static final XPath xPath = XPathFactory.newInstance().newXPath();

    /**
     * Find a (nested) element by the `id` XML attribute.
     *
     * @param element XML element
     * @param id      unique identifier
     * @return XML node
     * @throws XPathExpressionException If the expression cannot be evaluated.
     * @throws IllegalArgumentException If {@code returnType} is not one of the types defined in {@link XPathConstants}.
     * @throws NullPointerException     If {@code returnType} is {@code null}.
     */
    public static Node findById(@NotNull Element element, @NotNull String id) throws XPathExpressionException, IllegalArgumentException, NullPointerException {
        return findById(element, id, xPath);
    }

    /**
     * Find a (nested) element by the `id` XML attribute.
     *
     * @param element XML element
     * @param tableId unique identifier
     * @param xPath   provides access to the XPath evaluation environment
     * @return XML node
     * @throws XPathExpressionException If the expression cannot be evaluated.
     * @throws IllegalArgumentException If {@code returnType} is not one of the types defined in {@link XPathConstants}.
     * @throws NullPointerException     If {@code returnType} is {@code null}.
     */
    @NotNull
    public static Node findById(@NotNull Element element, @NotNull String tableId, XPath xPath) throws XPathExpressionException, IllegalArgumentException, NullPointerException {
        final var expression = ".//*[@id='" + tableId + "']";
        logger.debug(String.format("Looking up table with XML id: %s", tableId));
        return (Node) xPath.compile(expression).evaluate(element, XPathConstants.NODE);
    }

    /**
     * Find all nested XML tables.
     *
     * @param element XML element
     * @return XML node
     * @throws XPathExpressionException If the expression cannot be evaluated.
     * @throws IllegalArgumentException If {@code returnType} is not one of the types defined in {@link XPathConstants}.
     * @throws NullPointerException     If {@code returnType} is {@code null}.
     */
    public static NodeList findTables(@NotNull Element element) throws XPathExpressionException, IllegalArgumentException, NullPointerException {
        return findTables(element, xPath);
    }

    /**
     * Find all nested XML tables.
     *
     * @param element XML element
     * @param xPath   provides access to the XPath evaluation environment
     * @return XML node
     * @throws XPathExpressionException If the expression cannot be evaluated.
     * @throws IllegalArgumentException If {@code returnType} is not one of the types defined in {@link XPathConstants}.
     * @throws NullPointerException     If {@code returnType} is {@code null}.
     */
    public static NodeList findTables(@NotNull Element element, @NotNull XPath xPath) throws XPathExpressionException, IllegalArgumentException, NullPointerException {
        final var expression = ".//table";
        logger.debug("Looking up tables");
        return (NodeList) xPath.compile(expression).evaluate(element, XPathConstants.NODESET);
    }
}
