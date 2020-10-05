package refactoring.kata.scanner;

public class FieldObject {
    private final String className;
    private final String name;
    private final int opCodes;
    private final String signature;
    private final Object initialValue;

    public FieldObject(String className, String name) {
        this.className = className;
        this.name = name;
        this.opCodes = Integer.MIN_VALUE;
        this.signature = "";
        this.initialValue = null;
    }

    public FieldObject(String className, int opCodes, String name, String signature, Object initialValue) {
        this.className = className;
        this.opCodes = opCodes;
        this.name = name;
        this.signature = signature;
        this.initialValue = initialValue;
    }

    public String getClassName() {
        return className;
    }

    public String getName() {
        return name;
    }

    public int getOpCodes() {
        return opCodes;
    }

    public String getSignature() {
        return signature;
    }

    public Object getInitialValue() {
        return initialValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldObject that = (FieldObject) o;

        if (!className.equals(that.className)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = className.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FieldObject{" +
                "className='" + className + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    boolean hasFullDefinition() {
        return getOpCodes() != Integer.MIN_VALUE;
    }
}
