package tv.radiotherapy.tools.dicom.xml.parser.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tv.radiotherapy.tools.dicom.xml.ParallelAlgorithm;
import tv.radiotherapy.tools.dicom.xml.model.module.Ciod;
import tv.radiotherapy.tools.dicom.xml.parser.CiodTableConsumer;
import tv.radiotherapy.tools.dicom.xml.parser.Parser;
import tv.radiotherapy.tools.dicom.xml.parser.ParserException;
import tv.radiotherapy.tools.dicom.xml.parser.XmlHelper;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CiodTablesParser implements Parser<List<Ciod>>, ParallelAlgorithm {
    private static final Logger logger = LogManager.getLogger(CiodTablesParser.class);
    private static boolean isThreadedAlgorithm = false;

    @Override
    public List<Ciod> parse(@NotNull Element element) throws ParserException, XPathExpressionException, IllegalArgumentException, NullPointerException {
        final var chapterA = (Element) XmlHelper.findById(element, "chapter_A");
        final var tables = (NodeList) XmlHelper.findTables(chapterA);
        final int ntables = tables.getLength();
        if (ntables == 0) {
            throw new ParserException("Failed to find any CIOD tables, something went wrong.");
        }

        final List<Ciod> list = new ArrayList<>();
        if (isThreadedAlgorithm) {
            Runtime runtime = Runtime.getRuntime();
            var nproc = runtime.availableProcessors();
            if (ntables < nproc) {
                nproc = ntables;
            }
            List<CiodTableConsumer> consumers = new ArrayList<>();
            for (int i = 0; i < nproc; i++) {
                consumers.add(new CiodTableConsumer());
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
                    for (var entry : consumer.getErrors().entrySet()) {
                        logger.error(String.format("Table [%s]: %s", entry.getKey(), entry.getValue()));
                    }
                    throw new ParserException("Failed to process all CIOD tables in parallel.");
                }
                if (!list.addAll(consumer.getResults())) {
                    throw new ParserException("Failed to add all the results.");
                }
            }
        } else {
            final var tableParser = new CiodTableParser();
            final var ciodRowParser = new CiodTableRowParser();
            for (int i = 0; i < ntables; i++) {
                final var table = tables.item(i);
                try {
                    final var ciod = tableParser.parse((Element) table, ciodRowParser);
                    if (ciod != null) list.add(ciod);
                } catch (ParserException ex) {
                    final var et = (Element) table;
                    var id = et.getAttribute("xml:id");
                    logger.warn("Table [" + id + "] could not be converted to a CIOD.");
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
