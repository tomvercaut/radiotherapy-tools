package tv.radiotherapy.tools.dicom.xml.parser.module;

import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

public class NoModuleAttributesTableException extends ParserException {
    public NoModuleAttributesTableException() {
    }

    public NoModuleAttributesTableException(String message) {
        super(message);
    }

    public NoModuleAttributesTableException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoModuleAttributesTableException(Throwable cause) {
        super(cause);
    }

    public NoModuleAttributesTableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
