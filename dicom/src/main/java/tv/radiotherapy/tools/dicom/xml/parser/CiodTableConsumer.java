package tv.radiotherapy.tools.dicom.xml.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import tv.radiotherapy.tools.dicom.xml.model.module.Ciod;
import tv.radiotherapy.tools.dicom.xml.parser.module.CiodTableParser;
import tv.radiotherapy.tools.dicom.xml.parser.module.CiodTableRowParser;

import javax.xml.xpath.XPathExpressionException;
import java.util.*;

public class CiodTableConsumer implements ElementConsumer<Ciod> {
    private static final Logger logger = LogManager.getLogger(CiodTableConsumer.class);
    private final Queue<Element> queue = new LinkedList<>();
    private final CiodTableParser tableParser = new CiodTableParser();
    private final CiodTableRowParser ciodRowParser = new CiodTableRowParser();
    private final List<Ciod> ciods = new ArrayList<>();
    private final Map<String, Throwable> xPathExpressionExceptionMap = new HashMap<>();

    @Override
    public void add(@NotNull Element element) {
        queue.add(element);
    }

    @Override
    public void clearResults() {
        this.ciods.clear();
    }

    @Override
    @NotNull
    public List<Ciod> getResults() {
        return ciods;
    }

    public Map<String, Throwable> getErrors() {
        return xPathExpressionExceptionMap;
    }

    @Override
    public boolean hasErrors() {
        return !xPathExpressionExceptionMap.isEmpty();
    }

    @Override
    public void run() {
        Element table;
        while ((table = queue.poll()) != null) {
            final var id = table.getAttribute("xml:id");
            try {
                final Ciod ciod;
                try {
                    ciod = tableParser.parse(table, ciodRowParser);
                    if (ciod != null) ciods.add(ciod);
                } catch (XPathExpressionException e) {
                    xPathExpressionExceptionMap.put(id, e);
                }
            } catch (ParserException ex) {
                logger.warn("Table [" + id + "] could not be converted to a CIOD.");
            }
        }
    }
}
