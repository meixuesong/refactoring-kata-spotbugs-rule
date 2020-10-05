package refactoring.kata.rule;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import refactoring.kata.scanner.CallGraph;
import refactoring.kata.scanner.JavaScanner;
import refactoring.kata.scanner.MethodObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author meixuesong
 */
public class FeatureEnvyDetector extends BytecodeScanningDetector {
    private static Logger logger = LoggerFactory.getLogger(FeatureEnvyDetector.class);
    private final BugReporter bugReporter;
    private List<MethodObject> smellMethods = new ArrayList<>();
    private JavaClass javaClass;

    public FeatureEnvyDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visit(JavaClass obj) {
        smellMethods = scan(obj.getClassName());
        this.javaClass = obj;

        super.visit(obj);
    }

    @Override
    public void visit(Method obj) {
        super.visit(obj);
        for (MethodObject smellMethod : smellMethods) {
            if (smellMethod.getMethodName().equalsIgnoreCase(obj.getName())) {
                BugInstance bug = new BugInstance(this, "FEATURE_ENVY_SMELL", NORMAL_PRIORITY).addClassAndMethod(javaClass, obj);
                bugReporter.reportBug(bug);
            }
        }
    }

    public List<MethodObject> scan(String className) {
        logger.debug("Scan: {}", className);

        JavaScanner scanner = new JavaScanner(Arrays.asList("java."));
        CallGraph graph = scanner.scan(className);

        List<FeatureEnvyMetrics> envyMetrics = graph.getClasses().stream()
                .filter(c -> c.getClassName().equals(className))
                .flatMap(c -> c.getMethods().stream())
                .filter(m -> !m.getMethodName().equals("main"))
                .map(m -> new FeatureEnvyMetrics(m))
                .collect(Collectors.toList());

        List<MethodObject> results = envyMetrics.stream().filter(FeatureEnvyMetrics::isFeatureEnvy)
                .map(e -> e.getMethodObject())
                .collect(Collectors.toList());

        return results;
    }

}
