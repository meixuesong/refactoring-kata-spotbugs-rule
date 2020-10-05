package refactoring.kata.rule;


import refactoring.kata.scanner.MethodObject;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * The theory of how to identify feature envy bad smell:
 * http://www.jot.fm/issues/issue_2012_08/article5.pdf
 * https://www.simpleorientedarchitecture.com/how-to-identify-feature-envy-using-ndepend/
 */
public class FeatureEnvyMetrics {
    private MethodObject methodObject;
    public static final int FEW = 2;
    public static final double LAA_THRESHOLD = 0.33D;

    public FeatureEnvyMetrics(MethodObject methodObject) {
        this.methodObject = methodObject;
    }

    public MethodObject getMethodObject() {
        return methodObject;
    }

    /**
     * (ATFD > Few) AND (LAA < One Third) AND (FDP <= Few)
     *
     * ATFD and FDP use Generally-Accepted Meaning Thresholds. FEW is defined between 2 and 5.
     * LAA uses a Common Fraction Threshold. One Third is 0.33.
     * @return
     */
    public boolean isFeatureEnvy() {
        long atfd = getAtfd();
        double laa = getLaa();
        long fdp = getFdp();

        return (atfd > FEW) && (laa < LAA_THRESHOLD) && (fdp <= FEW);
    }

    /**
     * ATFD – Access To Foreign Data – to measure how many foreign attributes the method is using
     * @return
     */
    protected long getAtfd() {
        //TODO: need to consider the fields & methods of parent class
        long foreignMethodCount = methodObject.getCallees().stream()
                .filter(m -> !m.getClassName().equals(methodObject.getClassName()))
                .count();

        long foreignFieldCount = methodObject.getUsedFields().stream()
                .filter(f -> !f.getClassName().equals(methodObject.getClassName()))
                .count();

        return foreignFieldCount + foreignMethodCount;
    }

    /**
     * LAA – Locality of Attribute Accesses – to measure if the method uses more attributes from other classes than its own
     * @return
     */
    protected double getLaa() {
        long selfMethodCount = methodObject.getCallees().stream()
                .filter(m -> m.getClassName().equals(methodObject.getClassName()))
                .count();

        long selfFieldCount = methodObject.getUsedFields().stream()
                .filter(f -> f.getClassName().equals(methodObject.getClassName()))
                .count();

        return (selfFieldCount + selfMethodCount) * 1.0D / (methodObject.getCallees().size() + methodObject.getUsedFields().size());
    }

    /**
     * FDP – Foreign Data Providers – to measure the number of classes that the foreign attributes belong to
     * @return
     */
    protected long getFdp() {
        Set<String> classNames = methodObject.getCallees()
                .stream()
                .map(m -> m.getClassName())
                .collect(Collectors.toSet());

        classNames.addAll(methodObject.getUsedFields()
                .stream()
                .map(f -> f.getClassName())
                .collect(Collectors.toSet()));

        return classNames.size();
    }
}
