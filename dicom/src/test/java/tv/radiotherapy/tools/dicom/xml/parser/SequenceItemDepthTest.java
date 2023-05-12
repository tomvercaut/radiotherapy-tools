package tv.radiotherapy.tools.dicom.xml.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

class SequenceItemDepthTest {

    @Test
    void get() {
        final var ls = List.of(
                "",
                "abc",
                ">abc",
                "  >> abc",
                " >\u200B> abc",
                " >\u200B>\r>\n abc",
                " >\u200B>\r>\n> abc"
        );
        final var expected = List.of(
                0, 0, 1, 2, 2, 3, 4
        );
        final var actuals = ls.stream().map(SequenceItemDepth::get).collect(Collectors.toList());
        Assertions.assertIterableEquals(expected, actuals);
    }
}