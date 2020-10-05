package refactoring.kata.scanner;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.List;

/**
 * @author meixuesong
 */
public class CallGraphClassVisitor extends ClassVisitor {
    private final MethodCallVisitor methodVisitor;
    private CallGraph graph = new CallGraph();
    private String classOrInterfaceName;

    public CallGraphClassVisitor(List<String> ignorePackages) {
        super(Opcodes.ASM5);
        this.methodVisitor = new MethodCallVisitor(graph, ignorePackages);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        classOrInterfaceName = Type.getObjectType(name).getClassName();
        graph.addClass(classOrInterfaceName);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodObject methodObject = new MethodObject(classOrInterfaceName, name, descriptor);
        methodVisitor.setCaller(methodObject);
        if (! MethodCallVisitor.CLASS_INIT.equals(name)) {
            graph.addMethod(methodObject);
        }

        return methodVisitor;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        graph.addField(new FieldObject(classOrInterfaceName, access, name, signature, value));
        return super.visitField(access, name, descriptor, signature, value);
    }

    public CallGraph getCallGraph() {
        return graph;
    }
}

class MethodCallVisitor extends MethodVisitor {
    public static final String CLASS_INIT = "<init>";
    private MethodObject caller;
    private CallGraph graph;
    private List<String> ignorePackages;

    public MethodCallVisitor(CallGraph graph, List<String> ignorePackages) {
        super(Opcodes.ASM5);
        this.graph = graph;
        this.ignorePackages = ignorePackages;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);

        if (! CLASS_INIT.equals(name)) {
            String className = Type.getObjectType(owner).getClassName();
            if (shouldNotIgnore(className)) {
                MethodObject methodObject = new MethodObject(className, name, descriptor);
                graph.addMethodCall(caller, methodObject);
            }
        }
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        String className = Type.getObjectType(owner).getClassName();
        if (shouldNotIgnore(className)) {
            FieldObject fieldObject = new FieldObject(className, name);
            graph.addFieldAccess(caller, fieldObject);
        }

        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    public void setCaller(MethodObject caller) {
        this.caller = caller;
    }

    private boolean shouldNotIgnore(String classOrInterfaceName) {
        for (String ignorePackage : ignorePackages) {
            if (classOrInterfaceName.startsWith(ignorePackage)) {
                return false;
            }
        }

        return true;
    }
}
