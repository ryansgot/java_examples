package com.fsryan.examples.suggestion;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static com.fsryan.examples.suggestion.TestData.allWords;
import static com.fsryan.examples.suggestion.TestData.allWordsMatchingPrefix;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class SuggesterTest {

    protected final List<String> initWords;

    public SuggesterTest(List<String> initWords) {
        this.initWords = initWords;
    }

    @RunWith(Parameterized.class)
    public static class Init extends SuggesterTest {

        private Suggester suggesterUnderTest;

        public Init(List<String> initWords) {
            super(initWords);
        }

        @Parameterized.Parameters
        public static Iterable<Object[]> data() throws Exception {
            return Arrays.asList(new Object[][] {
                    {Lists.newArrayList("a", "aaron", "abide", "dude")},
                    {allWords()}
            });
        }

        @Before
        public void setUp() {
            suggesterUnderTest = new Suggester(initWords);
        }

        @Test
        public void allWordsShouldBeRepresented() {
            Set<String> expectedSet = new HashSet<>(initWords);
            Iterator<String> actualIterator = suggesterUnderTest.iterator();

            int pos = 0;
            while (actualIterator.hasNext()) {
                String actual = actualIterator.next();
                assertTrue("pos = " + pos + "; not expected word = " + actual, expectedSet.remove(actual));
                pos++;
            }
            assertFalse("unexpected continuation of actual = " + pos, actualIterator.hasNext());
            assertTrue("Actual did not contain words: " + expectedSet.toString(), expectedSet.isEmpty());
        }
    }

    @RunWith(Parameterized.class)
    public static class SuggestionFromIndex extends SuggesterTest {

        private final String prefix;
        private final List<String> expectedSuggestions;

        public SuggestionFromIndex(List<String> initWords, String prefix, List<String> expectedSuggestions) {
            super(initWords);
            this.prefix = prefix;
            this.expectedSuggestions = expectedSuggestions;
        }

        @Parameterized.Parameters
        public static Iterable<Object[]> data() throws Exception {
            return Arrays.asList(new Object[][] {
                    {   // 00: empty prefix should return none
                            Lists.newArrayList("a", "aa", "aaron"),
                            "",
                            Lists.newArrayList()
                    },
                    {   // 01: null prefix
                            Lists.newArrayList("a", "aa", "aaron"),
                            null,
                            Lists.newArrayList()
                    },
                    {   // 02: non-matching first character prefix
                            Lists.newArrayList("a", "aa", "aaron"),
                            "b",
                            Lists.newArrayList()
                    },
                    {   // 03: leaf-only matching prefix
                            Lists.newArrayList("a", "aa", "aaron"),
                            "aaron",
                            Lists.newArrayList("aaron")
                    },
                    {   // 04: results include match that is not a leaf
                            Lists.newArrayList("a", "aa", "aaron"),
                            "aa",
                            Lists.newArrayList("aa", "aaron")
                    },
                    {   // 05: all resuls matching "a"
                            Lists.newArrayList("a", "aa", "aaron"),
                            "a",
                            Lists.newArrayList("a", "aa", "aaron")
                    },
                    {   // 06: all resuls matching "a" when inputs have word starting with "b"
                            Lists.newArrayList("a", "aa", "aaron", "b"),
                            "a",
                            Lists.newArrayList("a", "aa", "aaron")
                    },
                    {   // 07: Large input with one letter least common
                            allWords(),
                            "x",
                            allWordsMatchingPrefix("x")
                    },
                    {   // 08: Large input with three letter prefix
                            allWords(),
                            "zeb",
                            allWordsMatchingPrefix("zeb")
                    },
                    {   // 09: Large input with one letter
                            allWords(),
                            "zebe",
                            allWordsMatchingPrefix("zebe")
                    },
                    {   // 10: Prefix with capital letters
                            allWords(),
                            "ZEBE",
                            allWordsMatchingPrefix("zebe")
                    }
            });
        }

        @Test
        public void shouldHaveCorrectSuggestions() {
            Iterator<String> actualSuggesitonIterator = new Suggester(initWords).suggest(prefix).iterator();
            Iterator<String> expectedSuggestionIterator = expectedSuggestions.iterator();

            int pos = 0;
            while (expectedSuggestionIterator.hasNext()) {
                assertTrue("early end to actual suggestions at pos = " + pos, actualSuggesitonIterator.hasNext());
                final String expected = expectedSuggestionIterator.next();
                final String actual = actualSuggesitonIterator.next();
                assertEqualsIgnoreCase("expected = " + expected + "; actual = " + actual + " at pos = " + pos, expected, actual);
                pos++;
            }

            final boolean extraElements = actualSuggesitonIterator.hasNext();
            assertFalse("had extra elements: " + itToStr(actualSuggesitonIterator), extraElements);
        }

        private String itToStr(Iterator<String> it) {
            StringBuilder buf = new StringBuilder();
            while (it.hasNext()) {
                buf.append(buf.length() != 0 ? ", " : "").append(it.next());
            }
            return buf.toString();
        }

        private void assertEqualsIgnoreCase(String message, String expected, String actual) {
            assertEquals(message, expected.toLowerCase(), actual.toLowerCase());
        }
    }
}
