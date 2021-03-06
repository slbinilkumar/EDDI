package ai.labs.parser.correction;

import ai.labs.expressions.Expression;
import ai.labs.expressions.ExpressionFactory;
import ai.labs.expressions.utilities.ExpressionProvider;
import ai.labs.expressions.utilities.IExpressionProvider;
import ai.labs.parser.model.IDictionary;
import ai.labs.parser.model.Phrase;
import ai.labs.parser.model.Word;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author ginccc
 */
public class DamerauLevenshteinCorrectionTest {
    private IExpressionProvider expressionUtilities;

    @Before
    public void setup() {
        expressionUtilities = new ExpressionProvider(new ExpressionFactory());
    }

    @Test
    public void testLookupTermDistanceOfOne() throws Exception {
        //setup
        TestDictionary testDictionary = new TestDictionary();
        DamerauLevenshteinCorrection levenshteinCorrection = new DamerauLevenshteinCorrection(true); // max distance of 2
        levenshteinCorrection.init(Arrays.asList(new IDictionary[]{testDictionary}));

        //test
        IDictionary.IFoundWord[] foundWords = levenshteinCorrection.correctWord("helo");

        //assert
        Assert.assertEquals(1, foundWords.length);
    }

    @Test
    public void testLookupTermDistanceOfTwo() throws Exception {
        //setup
        TestDictionary testDictionary = new TestDictionary();
        DamerauLevenshteinCorrection levenshteinCorrection = new DamerauLevenshteinCorrection(true); // max distance of 2
        levenshteinCorrection.init(Arrays.asList(new IDictionary[]{testDictionary}));

        //test
        IDictionary.IFoundWord[] foundWords = levenshteinCorrection.correctWord("heo");

        //assert
        Assert.assertEquals(1, foundWords.length);
    }

    @Test
    public void testLookupTermDistanceOfThree() throws Exception {
        //setup
        TestDictionary testDictionary = new TestDictionary();
        DamerauLevenshteinCorrection levenshteinCorrection = new DamerauLevenshteinCorrection(true); // max distance of 2
        levenshteinCorrection.init(Arrays.asList(new IDictionary[]{testDictionary}));

        //test
        IDictionary.IFoundWord[] foundWords = levenshteinCorrection.correctWord("he");

        //assert
        Assert.assertEquals(0, foundWords.length);
    }

    private class TestDictionary implements IDictionary {
        @Override
        public List<IWord> getWords() {
            return Arrays.asList(new IWord[]
                    {
                            new Word("hello", Arrays.asList(new Expression[0]), "", 0, false),
                            new Word("world", Arrays.asList(new Expression[0]), "", 0, false)
                    });
        }

        @Override
        public List<IPhrase> getPhrases() {
            return Arrays.asList(new IPhrase[]
                    {
                            new Phrase("good morning", expressionUtilities.parseExpressions("good morning"), ""),
                            new Phrase("day after tomorrow", expressionUtilities.parseExpressions("day after tomorrow"), "")
                    });
        }

        @Override
        public IFoundWord[] lookupTerm(String value) {
            return new IFoundWord[0];
        }

        @Override
        public String getLanguage() {
            return "";
        }

        @Override
        public boolean lookupIfKnown() {
            return false;
        }
    }
}
