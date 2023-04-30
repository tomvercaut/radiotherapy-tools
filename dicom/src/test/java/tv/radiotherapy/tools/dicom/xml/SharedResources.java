package tv.radiotherapy.tools.dicom.xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Thread-safe singleton implementation to load shared test resources.
 * <p>
 * Shared resources are loaded concurrently using an executor service.
 */
public class SharedResources {
    private static volatile SharedResources instance;
    private final static Object mutex = new Object();
    private final Document part03;
    private final Document part06;

    /**
     * Load shared resources concurrently.
     *
     * @throws CancellationException if the computation was cancelled
     * @throws ExecutionException    if the computation threw an
     *                               exception
     * @throws InterruptedException  if the current thread was interrupted
     *                               while waiting
     */
    private SharedResources() throws ExecutionException, CancellationException, InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        var f03 = executor.submit(this::readPart03);
        var f06 = executor.submit(this::readPart06);
        part03 = f03.get();
        part06 = f06.get();
    }

    /**
     * Create a new thread-safe instance if it wasn't already created.
     *
     * @return Singleton instance
     * @throws CancellationException if the computation was cancelled
     * @throws ExecutionException    if the computation threw an
     *                               exception
     * @throws InterruptedException  if the current thread was interrupted
     *                               while waiting
     */
    public static SharedResources getInstance() throws ExecutionException, CancellationException, InterruptedException {
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

    /**
     * Read part 03 of the DICOM standard.
     *
     * @return XML document of part 03 of the standard
     * @throws ParserConfigurationException if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws IOException                  If any IO errors occur.
     * @throws SAXException                 If any parse error occurs.
     *                                      IOException – If any IO errors occur.
     *                                      IllegalArgumentException – When the internal XML input stream is null.
     */
    private Document readPart03() throws ParserConfigurationException, IOException, SAXException {
        var classLoader = getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream("dicom_standard/part03/part03.xml");
        assertNotNull(inputStream);
        return DocumentReader.read(inputStream);
    }


    /**
     * Read part 06 of the DICOM standard.
     *
     * @return XML document of part 06 of the standard
     * @throws ParserConfigurationException if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws IOException                  If any IO errors occur.
     * @throws SAXException                 If any parse error occurs.
     *                                      IOException – If any IO errors occur.
     *                                      IllegalArgumentException – When the internal XML input stream is null.
     */
    private Document readPart06() throws ParserConfigurationException, IOException, SAXException {
        var classLoader = getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream("dicom_standard/part06/part06.xml");
        assertNotNull(inputStream);
        return DocumentReader.read(inputStream);
    }

    /**
     * Get a part 03 of the DICOM standard as an XML document.
     *
     * @return XML document
     */
    public Document getPart03() {
        return part03;
    }

    /**
     * Get a part 06 of the DICOM standard as an XML document.
     *
     * @return XML document
     */
    public Document getPart06() {
        return part06;
    }
}
