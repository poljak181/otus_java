package ru.otus.homework05;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyUnit {
    private static int failuresCount = 0;

    public static void run(Class<?> testClass) {

        failuresCount = 0;
        List<Method> beforeAllMethods = new ArrayList<>();
        List<Method> afterAllMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<Method> beforeEachMethods = new ArrayList<>();
        List<Method> afterEachMethods = new ArrayList<>();

        final var methods = testClass.getDeclaredMethods();

        for (var method : methods) {
            final boolean methodIsStatic = Modifier.isStatic(method.getModifiers());

            final var annotations = method.getDeclaredAnnotations();
            for (var annotation: annotations) {
                boolean annotationIsForStaticMethod = false;

                if (annotation.annotationType() == BeforeAll.class) {
                    beforeAllMethods.add(method);
                    annotationIsForStaticMethod = true;
                } else if (annotation.annotationType() == AfterAll.class) {
                    afterAllMethods.add(method);
                    annotationIsForStaticMethod = true;
                } else if (annotation.annotationType() == Test.class) {
                    testMethods.add(method);
                } else if (annotation.annotationType() == BeforeEach.class) {
                    beforeEachMethods.add(method);
                } else if (annotation.annotationType() == AfterEach.class) {
                    afterEachMethods.add(method);
                }

                if (methodIsStatic != annotationIsForStaticMethod) {
                    System.out.println("Incorrect using of annotations for static or not static methods");
                    return;
                }
            }
        }

        Collections.shuffle(beforeAllMethods);
        Collections.shuffle(afterAllMethods);
        Collections.shuffle(testMethods);

        try {
            executeStaticMethods(testClass, beforeAllMethods);
            executeMethods(testClass, testMethods, beforeEachMethods, afterEachMethods);
            executeStaticMethods(testClass, afterAllMethods);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        System.out.println("\nTests run: " + testMethods.size() + ", Failures: " + failuresCount);
    }

    private static void executeStaticMethods(Class<?> testClass, List<Method> methods) throws InvocationTargetException {
        for (var method : methods) {
            ReflectionHelper.callMethod(testClass, null, method.getName());
        }
    }

    private static void executeMethods(Class<?> testClass, List<Method> testMethods, List<Method> beforeEachMethods,
                                       List<Method> afterEachMethods) throws InvocationTargetException {
        for (var method : testMethods) {
            final var object = ReflectionHelper.instantiate(testClass);

            Collections.shuffle(beforeEachMethods);
            for (var beforeEachMethod : beforeEachMethods) {
                ReflectionHelper.callMethod(testClass, object, beforeEachMethod.getName());
            }

            try {
                ReflectionHelper.callMethod(testClass, object, method.getName());
            } catch (InvocationTargetException e) {
                System.out.println("[ERROR]: " + e.getCause());
                failuresCount++;
            }

            Collections.shuffle(afterEachMethods);
            for (var afterEachMethod : afterEachMethods) {
                ReflectionHelper.callMethod(testClass, object, afterEachMethod.getName());
            }
        }
    }
}
