package tv.radiotherapy.tools.dicom.xml.parser;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A helper class to compute the depth of a DICOM sequence item.
 */
public class SequenceItemDepth {

    /**
     * Compute the depth of a sequence item by counting the number of '>' characters at the start of a String.
     * <p>
     * The function ignores these characters after finding the first '>' character.
     * <ul>
     *     <li>&#92;u200B</li>
     *     <li>&#92;r</li>
     *     <li>&#92;n</li>
     * </ul>
     *
     * @param s input value
     * @return The number of '>' characters at the beginning of a String.
     */
    public static Optional<Pair<Integer, Integer>> get(@NotNull String s) {
        final var n = s.length();
        int i = s.indexOf('>');
        if (i == -1) {
            return Optional.empty();
        }
        ++i;
        int c = 1;
        char ch;
        while (i < n) {
            ch = s.charAt(i);
            if (ch == '>') {
                ++c;
                ++i;
            } else if (ch == '\u200B' || ch == '\n' || ch == '\r') {
                ++i;
            } else {
                break;
            }
        }
        return Optional.of(Pair.of(c, i));
    }
}
