package tv.radiotherapy.tools.dicom.xml.parser.module;

import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

public class NoIncludeException extends ParserException {
    public NoIncludeException() {
    }

    public NoIncludeException(String message) {
        super(message);
    }

    public NoIncludeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoIncludeException(Throwable cause) {
        super(cause);
    }

    public NoIncludeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
