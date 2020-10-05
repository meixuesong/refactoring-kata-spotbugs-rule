package refactoring.kata.scanner;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author meixuesong
 */
public class ClassObject {
    private String packageName;
    private String className;
    private Set<MethodObject> methods = new HashSet<>();
    private Set<FieldObject> fields = new HashSet<>();

    public ClassObject(String className) {
        this.packageName = className.indexOf(".") > -1 ? className.substring(0, className.lastIndexOf(".")) : "";
        this.className = className;
    }

    public Collection<MethodObject> getMethods() {
        return methods;
    }

    public List<MethodObject> findMethod(String methodName) {
        return methods.stream().filter(m -> m.getMethodName().equals(methodName)).collect(Collectors.toList());
    }

    public MethodObject findMethod(MethodObject methodObject) {
        for (MethodObject method : methods) {
            if (method.equals(methodObject)) {
                return method;
            }
        }

        return null;
    }

    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return packageName;
    }

    public synchronized void addMethod(MethodObject methodObject) {
        MethodObject existingMethod = findMethod(methodObject);
        if (existingMethod != null) {
            methodObject.getCallees().addAll(existingMethod.getCallees());
            methods.remove(existingMethod);
        }

        methods.add(methodObject);
    }

    public synchronized void addField(FieldObject fieldObject) {
        if (fieldObject.hasFullDefinition()) {
            //remove old field, which is not a fully definition
            fields.remove(fieldObject);
        }
        fields.add(fieldObject);
    }

    public Set<FieldObject> getFields() {
        return fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassObject that = (ClassObject) o;
        return className.equals(that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }

    @Override
    public String toString() {
        return "ClassObject{" +
                "className='" + className + '\'' +
                '}';
    }

    public String getSimpleName() {
        return className.indexOf(".") > -1 ? className.substring(className.lastIndexOf(".") + 1) : className;
    }
}
