package tv.radiotherapy.tools.dicom.xml.parser;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A helper class to extract a text representation of one or multiple XML nodes (elements, text, ...).
 */
public class InnerText {

    @SuppressWarnings({"EscapedSpace", "RegExpSimplifiable"})
    private static final Pattern REG_NEWLINES = Pattern.compile("\r?\n[\s]+");
    private static final Pattern REG_ZWSP = Pattern.compile("\u200b+");

    /**
     * Get the text representation of a `para` element.
     * <p>
     * The text is strip at the beginning and end of:
     * <ul>
     *     <li>\\n</li>
     *     <li>\\r</li>
     *     <li>unicode character:
     *          <ul><li>200b</li></ul>
     *     </li>
     *     <li>space character</li>
     * </ul>
     *
     * @param para XML element
     * @return A trimmed string with the text from the `para` element.
     * @throws IllegalArgumentException If the XML node is not of type ELEMENT (@see {@link Node#ELEMENT_NODE}) or if the tag name is not equal to `para`.
     * @see InnerText#get(Node)
     */
    public static String para(@NotNull Node para) throws IllegalArgumentException {
        if (para.getNodeType() != Node.ELEMENT_NODE || !((Element) para).getTagName().equals("para")) {
            throw new IllegalArgumentException("XML node is not an element node or doesn't have the name \"para\"");
        }
        return get(para);
    }

    /**
     * Get a text representation of an XML node.
     * <p>
     * After building an initial text representation of the XML node and its nested nodes, the String is
     * stripped from:
     * <ul>
     *     <li>spaces</li>
     *     <li>tabs</li>
     *     <li>newline characters</li>
     *     <li>carriage return characters</li>
     *     <li>U+200B: zero width whitespace</li>
     *     <li>backspace characters</li>
     * </ul>
     * <p>
     * Zero width spaces are removed within the text and replaced by a regular space.
     * An optional carriage return, a newline character followed by one or more spaces is replaced by a newline character.
     *
     * @param node XML node
     * @return A text representation of the input node and its nested nodes.
     */
    public static String get(@NotNull Node node) {
        var sb = new StringBuilder();
        get(node, sb);
        var s = sb.toString();
        var r = StringUtils.strip(s, "\t\n\r\u200b\u000B ");
        Matcher m2 = REG_ZWSP.matcher(r);
        var t = m2.replaceAll("");
//        var t = r;
        Matcher m3 = REG_NEWLINES.matcher(t);
        return m3.replaceAll("\n");
    }

    /**
     * Get a text representation of an XML document, element or text node and its nested nodes.
     * <p>
     * Other XML node types are ignored.
     *
     * @param node XML node
     * @param sb   builder to append the text
     */
    private static void get(@NotNull Node node, @NotNull StringBuilder sb) {
        var type = node.getNodeType();
        if (type == Node.ELEMENT_NODE || type == Node.DOCUMENT_NODE) {
            var cn = node.getChildNodes();
            var n = cn.getLength();
            for (int i = 0; i < n; i++) {
                var item = cn.item(i);
                get(item, sb);
            }
        } else if (type == Node.TEXT_NODE) {
            sb.append(node.getNodeValue());
        }
    }
}
