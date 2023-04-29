package tv.radiotherapy.tools.dicom.xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SharedResources {
    private static volatile SharedResources instance;
    private final static Object mutex = new Object();
    private final Document part06;

    private SharedResources() throws ParserConfigurationException, IOException, SAXException {
            part06 = readPart06();
    }

    public static SharedResources getInstance() throws ParserConfigurationException, IOException, SAXException {
        SharedResources result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) {
                    instance = result = new SharedResources();
                }
            }
        }
        return result;
    }

    private Document readPart06() throws ParserConfigurationException, IOException, SAXException {
        var classLoader = getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream("dicom_standard/part06/part06.xml");
        assertNotNull(inputStream);
        return DocumentReader.read(inputStream);
    }

    public Document getPart06() {
        return part06;
    }
}
