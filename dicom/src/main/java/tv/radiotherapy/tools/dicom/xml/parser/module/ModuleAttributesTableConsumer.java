package tv.radiotherapy.tools.dicom.xml.parser.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import tv.radiotherapy.tools.dicom.xml.model.module.ModuleAttributes;
import tv.radiotherapy.tools.dicom.xml.parser.ElementConsumer;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.module.attribute.AttributeTableRowParser;

import javax.xml.xpath.XPathExpressionException;
import java.util.*;

public class ModuleAttributesTableConsumer implements ElementConsumer<ModuleAttributes> {
    private static final Logger logger = LogManager.getLogger(ModuleAttributesTableConsumer.class);
    private final Queue<Element> queue = new LinkedList<>();
    private final ModuleAttributesTableParser tableParser = new ModuleAttributesTableParser();
    private final AttributeTableRowParser rowParser = new AttributeTableRowParser();
    private final List<ModuleAttributes> result = new ArrayList<>();
    private final Map<String, Throwable> errors = new HashMap<>();

    @Override
    public void add(@NotNull Element element) {
        queue.add(element);
    }

    @Override
    public void clearResults() {
        this.result.clear();
    }

    @Override
    @NotNull
    public List<ModuleAttributes> getResults() {
        return result;
    }

    public Map<String, Throwable> getErrors() {
        return errors;
    }

    @Override
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public void run() {
        Element table;
        while ((table = queue.poll()) != null) {
            final var id = table.getAttribute("xml:id");
            try {
                final ModuleAttributes item;
                try {
                    item = tableParser.parse(table, rowParser);
                    if (item != null) result.add(item);
                } catch (NoModuleAttributesTableException | XPathExpressionException e) {
                    errors.put(id, e);
                }
            } catch (ParserException ex) {
                logger.warn("Table [" + id + "] could not be converted to a ModuleAttributes.");
            }
        }
    }
}
