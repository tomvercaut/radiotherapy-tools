package tv.radiotherapy.tools.dicom.xml;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * Helper class to read an XML String or input stream.
 */
public class DocumentReader {
    /**
     * Read an XML document from an InputStream.
     *
     * @param inputStream input stream
     * @return XML document instance
     * @throws ParserConfigurationException if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws IllegalArgumentException     If the input stream is null.
     * @throws IOException                  If any IO errors occur.
     * @throws SAXException                 If any parse error occurs.
     *                                      IOException – If any IO errors occur.
     *                                      IllegalArgumentException – When the internal XML input stream is null.
     */
    @NotNull
    public static Document read(@NotNull InputStream inputStream) throws ParserConfigurationException, IllegalArgumentException, IOException, SAXException {
        var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        var doc = builder.parse(inputStream);
        doc.getDocumentElement().normalize();
        return doc;
    }

    /**
     * Read an XML document from a String.
     *
     * @param xml XML String
     * @return XML document instance
     * @throws ParserConfigurationException if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws IllegalArgumentException     If the input stream is null.
     * @throws IOException                  If any IO errors occur.
     * @throws SAXException                 If any parse error occurs.
     *                                      IOException – If any IO errors occur.
     *                                      IllegalArgumentException – When the internal XML input stream is null.
     */
    public static Document readXmlString(@NotNull String xml) throws IOException, IllegalArgumentException, SAXException, ParserConfigurationException {
        var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        var inputSource = new InputSource(new StringReader(xml));
        var doc = builder.parse(inputSource);
        doc.getDocumentElement().normalize();
        return doc;
    }
}
