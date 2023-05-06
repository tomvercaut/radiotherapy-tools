package tv.radiotherapy.tools.dicom.xml.parser.registry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tv.radiotherapy.tools.dicom.xml.ParallelAlgorithm;
import tv.radiotherapy.tools.dicom.xml.model.registry.ContextGroupUid;
import tv.radiotherapy.tools.dicom.xml.parser.ContextGroupUidTableRowConsumer;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.TableHelper;
import tv.radiotherapy.tools.dicom.xml.parser.TableParser;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ContextGroupUidTableParser implements TableParser<List<ContextGroupUid>, ContextGroupUidTableRowParser>, ParallelAlgorithm {
    private static final Logger logger = LogManager.getLogger(ContextGroupUidTableParser.class);
    private static boolean isThreaded = true;

    @Override
    public List<ContextGroupUid> parse(@NotNull Element element, @NotNull ContextGroupUidTableRowParser rowParser) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException, InterruptedException {
        var registry = new ArrayList<ContextGroupUid>();
        build(element, registry, rowParser, "table_A-3");
        return registry;
    }

    private void build(@NotNull Element element, @NotNull List<ContextGroupUid> registry, @NotNull ContextGroupUidTableRowParser rowParser, String tableId) throws XPathExpressionException, ParserException {
        // Find table by id.
        final Node table = TableHelper.findById(element, tableId);
        final NodeList rows = TableHelper.getRows(table);
        // Extract table rows and iterate the rows.
        final var nrows = rows.getLength();
        if (isThreaded) {
            Runtime runtime = Runtime.getRuntime();
            var nproc = runtime.availableProcessors();
            if (nrows < nproc) nproc = nrows;
            final List<ContextGroupUidTableRowConsumer> consumers = new ArrayList<>();
            for (int i = 0; i < nproc; i++) {
                consumers.add(new ContextGroupUidTableRowConsumer());
            }
            logger.debug("Adding tasks to the queue.");
            try (ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newCachedThreadPool()) {
                for (int i = 0; i < nrows; i++) {
                    var iproc = i % nproc;
                    final var row = rows.item(i);
                    consumers.get(iproc).add((Element) row);
                }
                logger.debug("Submitting tasks to the executor service.");
                for (int i = 0; i < nproc; i++) {
                    tpe.submit(consumers.get(i));
                }
            }
            logger.debug("Waiting for the tasks to be handled by the executor service.");

            for (var consumer : consumers) {
                if (consumer.hasErrors()) {
                    for (var entry : consumer.getErrors()) {
                        logger.error(String.format("row: %s", entry));
                    }
                    throw new ParserException("Failed to process all Context Group Uid rows in parallel.");
                }
                var results = consumer.getResults();
                if (!registry.addAll(results)) {
                    throw new ParserException("Failed to add all the results.");
                }
            }
        } else {
            for (int r = 0; r < nrows; r++) {
                var opt = rowParser.parseRow(rows.item(r));
                opt.ifPresent(registry::add);
            }
        }
    }

    @Override
    public void enableParallel(boolean enableParallelAlgorithm) {
        isThreaded = enableParallelAlgorithm;
    }

    @Override
    public boolean isParallel() {
        return isThreaded;
    }
}
