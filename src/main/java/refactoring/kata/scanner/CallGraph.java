package refactoring.kata.scanner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author meixuesong
 */
public class CallGraph {
    private Map<String, ClassObject> classNodes = new HashMap<>();

    public Collection<ClassObject> getClasses() {
        return classNodes.values();
    }

    public ClassObject findClass(String className) {
        return classNodes.get(className);
    }

    public synchronized void addClass(String className) {
        if (!classNodes.containsKey(className)) {
            ClassObject classObject = new ClassObject(className);
            classNodes.put(className, classObject);
        }
    }

    public void addMethod(MethodObject methodObject) {
        addClass(methodObject.getClassName());

        classNodes.get(methodObject.getClassName()).addMethod(methodObject);
    }

    public void addMethodCall(MethodObject caller, MethodObject callee) {
        caller.addCallee(callee);

        addMethod(caller);
        addMethod(callee);
    }

    public void addFieldAccess(MethodObject caller, FieldObject fieldObject) {
        addMethod(caller);
        caller.addCallee(fieldObject);
    }

    public void addField(FieldObject fieldObject) {
        addClass(fieldObject.getClassName());
        classNodes.get(fieldObject.getClassName()).addField(fieldObject);
    }
}
