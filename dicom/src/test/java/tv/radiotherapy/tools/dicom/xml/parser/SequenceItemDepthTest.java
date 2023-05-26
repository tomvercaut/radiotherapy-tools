package tv.radiotherapy.tools.dicom.xml.parser;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class SequenceItemDepthTest {

    @Test
    void get() {
        final var ls = List.of(
                "",
                "abc",
                ">abc",
                "  >> abc",
                " >\u200B>abc",
                " >\u200B>\r>\nabc",
                " >\u200B>\r>\n>abc"
        );
        final var expected = List.of(
                Optional.empty(),
                Optional.empty(),
                Optional.of(Pair.of(1, 1)),
                Optional.of(Pair.of(2, 4)),
                Optional.of(Pair.of(2, 4)),
                Optional.of(Pair.of(3, 7)),
                Optional.of(Pair.of(4, 8))
        );
        final var actuals = ls.stream().map(SequenceItemDepth::get).collect(Collectors.toList());
        Assertions.assertIterableEquals(expected, actuals);
    }
}