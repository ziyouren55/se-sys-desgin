import java.lang.reflect.*;

public class SimpleRunner {
    public static void main(String[] args) throws Exception {
        int passed = 0;
        int failed = 0;

        String[] testClasses = {"PathUtilTest", "MkdirTest", "TouchTest", "LsTest", "InfoTest", "FindTest", "RmTest", "LinkTest", "IntegrationTest"};

        for (String className : testClasses) {
            Class<?> clazz = Class.forName(className);
            System.out.println("\n=== " + className + " ===");

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(org.junit.jupiter.api.Test.class)) {
                    try {
                        Object instance = clazz.getDeclaredConstructor().newInstance();

                        for (Method m : clazz.getDeclaredMethods()) {
                            if (m.isAnnotationPresent(org.junit.jupiter.api.BeforeEach.class)) {
                                m.invoke(instance);
                            }
                        }

                        method.invoke(instance);
                        System.out.println("  PASS: " + method.getName());
                        passed++;
                    } catch (InvocationTargetException e) {
                        Throwable cause = e.getCause();
                        System.out.println("  FAIL: " + method.getName() + " - " + cause);
                        failed++;
                    } catch (Exception e) {
                        System.out.println("  ERROR: " + method.getName() + " - " + e);
                        failed++;
                    }
                }
            }
        }

        System.out.println("\n================");
        System.out.println("Passed: " + passed + ", Failed: " + failed);
        if (failed > 0) System.exit(1);
    }
}