import com.kay.transform.Person;
import org.objectweb.asm.Type;

public class Test {
    public static void main(String[] args) {
        System.out.println(Type.getDescriptor(String.class));

        System.out.println(Type.getDescriptor(Person.class));

        System.out.println(Type.getMethodDescriptor(Type.getType(String.class), Type.INT_TYPE, Type.INT_TYPE));

        System.out.println(Type.getReturnType("(II)V").getInternalName());
    }
}
