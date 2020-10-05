package refactoring.kata.scanner;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author meixuesong
 */
public class MethodObject {
    private String className;
    private String methodName;
    private String methodDesc;
    private Set<MethodObject> callees = new HashSet<>();
    private Set<FieldObject> usedFields = new HashSet<>();

    public MethodObject(String className, String methodName, String methodDesc) {
        this.className = className;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    @Override
    public String toString() {
        return className + "." + methodName + "_" + methodDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MethodObject that = (MethodObject) o;
        return className.equals(that.className) &&
                methodName.equals(that.methodName) &&
                methodDesc.equals(that.methodDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, methodName, methodDesc);
    }

    public Set<MethodObject> getCallees() {
        return callees;
    }

    public Set<FieldObject> getUsedFields() {
        return usedFields;
    }

    public void addCallee(MethodObject callee) {
        callees.add(callee);
    }

    public void addCallee(FieldObject fieldObject) {
        if (! fieldObject.getClassName().startsWith("java.")) {
            usedFields.add(fieldObject);
        }
    }

    public String getFullName() {
        return className + "." + methodName;
    }
}
