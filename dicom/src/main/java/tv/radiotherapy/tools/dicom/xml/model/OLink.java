package tv.radiotherapy.tools.dicom.xml.model;

/**
 * @param document  Specifies the URI of the document in which the link target appears
 * @param ptr       Specifies the location of the link target in the document
 * @param style     Specifies a keyword or keywords identifying additional style information
 * @param type      Identifies application-specific customization of the link behavior
 * @param localInfo Holds additional information that may be used by the applicatoin when resolving the link
 */
public record OLink(
        String document,
        String ptr,
        String style,
        String type,
        String localInfo) {

    public OLink(String document, String ptr, String style) {
        this(document, ptr, style, "", "");
    }

    /**
     * Check if this instance contains a link to another document.
     *
     * @return True if a link to an external document is present.
     */
    public boolean hasLink() {
        return !document.isBlank() && !ptr.isBlank();
    }
}
