package tv.radiotherapy.tools.dicom.xml;

/**
 * A cross-reference to another part of the XML document.
 * @param link Points to the internal link target by identifying the value of the xml:id attribute.
 * @param style Specifies a keyword or keywords identifying additional style information.
 */
public record XRef(String link, String style) {
}
