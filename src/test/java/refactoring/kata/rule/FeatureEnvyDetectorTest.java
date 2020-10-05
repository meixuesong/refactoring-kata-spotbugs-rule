package refactoring.kata.rule;

import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.test.SpotBugsRule;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcher;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcherBuilder;
import org.junit.Rule;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static edu.umd.cs.findbugs.test.CountMatcher.containsExactly;
import static org.hamcrest.MatcherAssert.assertThat;

public class FeatureEnvyDetectorTest {
    @Rule
    public SpotBugsRule spotbugs = new SpotBugsRule();

    @Test
    public void testBadCase() {
        Path path = Paths.get("target/test-classes", "refactoring.kata.rule.feature_envy".replace('.', '/'), "Customer.class");
        Path path2 = Paths.get("target/test-classes", "refactoring.kata.rule.feature_envy".replace('.', '/'), "Phone.class");
        BugCollection bugCollection = spotbugs.performAnalysis(path, path2);

        BugInstanceMatcher bugTypeMatcher = new BugInstanceMatcherBuilder()
                .bugType("FEATURE_ENVY_SMELL").build();
        assertThat(bugCollection, containsExactly(1, bugTypeMatcher));
    }

    @Test
    public void shouldIgnoreMain() {
        String packageName = "refactoring.kata.rule.alternative_classes_with_different_interfaces";
        Path path = Paths.get("target/test-classes", packageName.replace('.', '/'), "Cat.class");
        Path path2 = Paths.get("target/test-classes", packageName.replace('.', '/'), "Dog.class");
        Path path3 = Paths.get("target/test-classes", packageName.replace('.', '/'), "MyPets.class");
        BugCollection bugCollection = spotbugs.performAnalysis(path, path2, path3);

        BugInstanceMatcher bugTypeMatcher = new BugInstanceMatcherBuilder().bugType("FEATURE_ENVY_SMELL").build();
        assertThat(bugCollection, containsExactly(0, bugTypeMatcher));
    }
}
