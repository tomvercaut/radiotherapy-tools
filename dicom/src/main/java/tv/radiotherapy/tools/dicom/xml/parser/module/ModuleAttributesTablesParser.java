package tv.radiotherapy.tools.dicom.xml.parser.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tv.radiotherapy.tools.dicom.xml.ParallelAlgorithm;
import tv.radiotherapy.tools.dicom.xml.model.module.ModuleAttributes;
import tv.radiotherapy.tools.dicom.xml.parser.Parser;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.XmlHelper;
import tv.radiotherapy.tools.dicom.xml.parser.module.attribute.AttributeTableRowParser;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModuleAttributesTablesParser implements Parser<List<ModuleAttributes>>, ParallelAlgorithm {
    private static final Logger logger = LogManager.getLogger(ModuleAttributesTablesParser.class);
    private boolean isThreadedAlgorithm = false;

    @Override
    public List<ModuleAttributes> parse(@NotNull Element root) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        final var tables = (NodeList) XmlHelper.findTables(root);
        final int ntables = tables.getLength();
        if (ntables == 0) {
            throw new ParserException("Failed to find any Module Attributes or Macro Attributes tables, something went wrong.");
        }
        final List<ModuleAttributes> list = new ArrayList<>();
        if (isThreadedAlgorithm) {
            Runtime runtime = Runtime.getRuntime();
            var nproc = runtime.availableProcessors();
            if (ntables < nproc) {
                nproc = ntables;
            }
            List<ModuleAttributesTableConsumer> consumers = new ArrayList<>();
            for (int i = 0; i < nproc; i++) {
                consumers.add(new ModuleAttributesTableConsumer());
            }
            try (ExecutorService es = Executors.newFixedThreadPool(nproc)) {
                // Add all the tasks to the queue
                logger.debug("Adding tasks to the queue.");
                for (int i = 0; i < ntables; i++) {
                    var iproc = i % nproc;
                    final var table = tables.item(i);
                    consumers.get(iproc).add((Element) table);
                }
                // add tasks to the executor service
                logger.debug("Submitting tasks to the executor service.");
                for (int i = 0; i < nproc; i++) {
                    es.submit(consumers.get(i));
                }
                logger.debug("Waiting for the tasks to be handled by the executor service.");
            }
            for (var consumer : consumers) {
                if (consumer.hasErrors()) {
                    boolean b = false;
                    for (var entry : consumer.getErrors().entrySet()) {
                        boolean isRealError = !(entry.getValue() instanceof NoModuleAttributesTableException);
                        if (isRealError) {
                            b = true;
                        }
                        logger.error(String.format("Table [%s]: %s", entry.getKey(), entry.getValue()));
                    }
                    if (b) {
                        throw new ParserException("Failed to process all Module Attributes or Macro Attributes tables in parallel.");
                    }
                }
                if (!list.addAll(consumer.getResults())) {
                    throw new ParserException("Failed to add all the results.");
                }
            }
        } else {
            final var tableParser = new ModuleAttributesTableParser();
            final var rowParser = new AttributeTableRowParser();
            for (int i = 0; i < ntables; i++) {
                final var table = tables.item(i);
                try {
                    final var item = tableParser.parse((Element) table, rowParser);
                    if (item != null) list.add(item);
                } catch (NoModuleAttributesTableException ex) {
                    final var et = (Element) table;
                    var id = et.getAttribute("xml:id");
                    logger.warn("Table [" + id + "] is not a Module Attributes or Macro Attributes table.");
                } catch (ParserException ex) {
                    final var et = (Element) table;
                    var id = et.getAttribute("xml:id");
                    logger.warn("Table [" + id + "] could not be converted to a ModuleAttributes.");
                    throw ex;
                }
            }
        }

        return list;
    }

    @Override
    public void enableParallel(boolean enableParallelAlgorithm) {
        isThreadedAlgorithm = enableParallelAlgorithm;
    }

    @Override
    public boolean isParallel() {
        return isThreadedAlgorithm;
    }
}
