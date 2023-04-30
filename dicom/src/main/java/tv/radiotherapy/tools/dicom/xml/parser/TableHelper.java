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
 * A helper class with functions that are linked to XML tables.
 */
public class TableHelper {
    private static final Logger logger = LogManager.getLogger(TableHelper.class);
    private static final XPath xPath = XPathFactory.newInstance().newXPath();

    /**
     * Test if the XML node an element.
     *
     * @param node XML node
     * @return True if the node is an XML element has a tag name 'table'.
     */
    public static boolean isTable(@NotNull Node node) {
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return false;
        }
        return ((Element) node).getTagName().equals("table");
    }

    /**
     * Find a table by the `id` XML attribute.
     *
     * @param element XML element
     * @param tableId unique table identifier
     * @return XML node refering to the table.
     * @throws XPathExpressionException If the expression cannot be evaluated.
     * @throws IllegalArgumentException If {@code returnType} is not one of the types defined in {@link XPathConstants}.
     * @throws NullPointerException     If {@code returnType} is {@code null}.
     */
    public static Node findById(@NotNull Element element, @NotNull String tableId) throws XPathExpressionException, IllegalArgumentException, NullPointerException {
        var xPath = XPathFactory.newInstance().newXPath();
        return findById(element, tableId, xPath);
    }

    /**
     * Find a table by the `id` XML attribute.
     *
     * @param element XML element
     * @param tableId unique table identifier
     * @param xPath   provides access to the XPath evaluation environment
     * @return XML node refering to the table.
     * @throws XPathExpressionException If the expression cannot be evaluated.
     * @throws IllegalArgumentException If {@code returnType} is not one of the types defined in {@link XPathConstants}.
     * @throws NullPointerException     If {@code returnType} is {@code null}.
     */
    @NotNull
    public static Node findById(@NotNull Element element, @NotNull String tableId, XPath xPath) throws XPathExpressionException, IllegalArgumentException, NullPointerException {
        var expression = ".//table[@id='" + tableId + "']";
        logger.debug(String.format("Looking up table with XML id: %s", tableId));
        return (Node) xPath.compile(expression).evaluate(element, XPathConstants.NODE);
    }

    /**
     * Get the rows in an XML table.
     *
     * @param table XML table element
     * @return List of table rows.
     * @throws XPathExpressionException If the expression cannot be evaluated.
     * @throws IllegalArgumentException If {@code returnType} is not one of the types defined in {@link XPathConstants}.
     * @throws NullPointerException     If {@code returnType} is {@code null}.
     */
    public static NodeList getRows(@NotNull Node table) throws XPathExpressionException, IllegalArgumentException, NullPointerException {
        return getRows(table, xPath);
    }

    /**
     * Get the rows in an XML table.
     *
     * @param table XML table element
     * @param xPath provides access to the XPath evaluation environment
     * @return List of table rows.
     * @throws XPathExpressionException If the expression cannot be evaluated.
     * @throws IllegalArgumentException If {@code returnType} is not one of the types defined in {@link XPathConstants}.
     * @throws NullPointerException     If {@code returnType} is {@code null}.
     */
    @NotNull
    public static NodeList getRows(@NotNull Node table, @NotNull XPath xPath) throws XPathExpressionException, IllegalArgumentException, NullPointerException {
        if (table.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException("XML node must be of an XML element");
        }
        if (!((Element) table).getTagName().equals("table")) {
            throw new IllegalArgumentException("Expected an XML table element but got " + ((Element) table).getTagName());
        }
        final String expression = "tbody/tr";
        return (NodeList) xPath.compile(expression).evaluate(table, XPathConstants.NODESET);
    }

    /**
     * Get the columns of a row in an XML table.
     *
     * @param row XML table row element
     * @return List of columns in a table row.
     * @throws XPathExpressionException If the expression cannot be evaluated.
     * @throws IllegalArgumentException If {@code returnType} is not one of the types defined in {@link XPathConstants}.
     * @throws NullPointerException     If {@code returnType} is {@code null}.
     */
    @NotNull
    public static NodeList getColumns(@NotNull Node row) throws XPathExpressionException {
        return getColumns(row, xPath);
    }

    /**
     * Get the columns of a row in an XML table.
     *
     * @param row   XML table row element
     * @param xPath provides access to the XPath evaluation environment
     * @return List of columns in a table row.
     * @throws XPathExpressionException If the expression cannot be evaluated.
     * @throws IllegalArgumentException If {@code returnType} is not one of the types defined in {@link XPathConstants}.
     * @throws NullPointerException     If {@code returnType} is {@code null}.
     */
    @NotNull
    public static NodeList getColumns(@NotNull Node row, @NotNull XPath xPath) throws XPathExpressionException, IllegalArgumentException, NullPointerException {
        if (row.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException("XML node must be of an XML element");
        }
        if (!((Element) row).getTagName().equals("tr")) {
            throw new IllegalArgumentException("Expected an XML table row element but got " + ((Element) row).getTagName());
        }
        final String expression = ".//td";
        return (NodeList) xPath.compile(expression).evaluate(row, XPathConstants.NODESET);
    }
}
