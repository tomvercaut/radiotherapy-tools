package tv.radiotherapy.tools.dicom.xml.parser.module;

import tv.radiotherapy.tools.dicom.xml.parser.ParserException;

public class IncludeRecordSelectionKeysException extends ParserException {
    public IncludeRecordSelectionKeysException() {
    }

    public IncludeRecordSelectionKeysException(String message) {
        super(message);
    }

    public IncludeRecordSelectionKeysException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncludeRecordSelectionKeysException(Throwable cause) {
        super(cause);
    }

    public IncludeRecordSelectionKeysException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
