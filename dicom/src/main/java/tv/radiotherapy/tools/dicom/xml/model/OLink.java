package tv.radiotherapy.tools.dicom.xml.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OLink {
    // Specifies the URI of the document in which the link target appears
    private String document = "";
    // Specifies the location of the link target in the document
    private String ptr = "";
    // Specifies a keyword or keywords identifying additional style information
    private String style = "";
    // Identifies application-specific customization of the link behavior
    private String type = "";
    // Holds additional information that may be used by the applicatoin when resolving the link
    private String localInfo = "";

    public OLink(String document, String ptr, String style) {
        this.document = document;
        this.ptr = ptr;
        this.style = style;
    }

    /**
     * Check if this instance contains a link to another document.
     * @return True if a link to an external document is present.
     */
    public boolean hasLink() {
        return !document.isBlank() && !ptr.isBlank();
    }
}
