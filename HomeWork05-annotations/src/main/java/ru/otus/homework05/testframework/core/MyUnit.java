package ru.otus.homework05.testframework.core;

import ru.otus.homework05.testframework.api.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyUnit {
    private static int failuresCount = 0;
    private static int runCount = 0;
    private static List<Method> beforeAllMethods = new ArrayList<>();
    private static List<Method> afterAllMethods = new ArrayList<>();
    private static List<Method> testMethods = new ArrayList<>();
    private static List<Method> beforeEachMethods = new ArrayList<>();
    private static List<Method> afterEachMethods = new ArrayList<>();

    public static void run(Class<?> testClass) {
        clear();

        if (!distributeMethods(testClass.getDeclaredMethods())) {
            return;
        }

        shuffleMethods();
        execute(testClass);

        System.out.println("\nTests run: " + runCount + ", Failures: " + failuresCount);
    }

    private static void clear() {
        failuresCount = 0;
        runCount = 0;

        beforeAllMethods.clear();
        afterAllMethods.clear();
        testMethods.clear();
        beforeEachMethods.clear();
        afterEachMethods.clear();
    }

    private static boolean distributeMethods(Method[] methods) {
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
                    return false;
                }
            }
        }
        return true;
    }

    private static void shuffleMethods() {
        Collections.shuffle(beforeAllMethods);
        Collections.shuffle(afterAllMethods);
        Collections.shuffle(testMethods);
    }

    private static void execute(Class<?> testClass) {
        if (executeStaticMethods(beforeAllMethods)) {
            executeMethods(testClass, testMethods, beforeEachMethods, afterEachMethods);
        }
        executeStaticMethods(afterAllMethods);
    }

    private static boolean executeStaticMethods(List<Method> methods) {
        for (var method : methods) {
            if (!ReflectionHelper.callMethod(null, method)) {
                return false;
            }
        }
        return true;
    }

    private static void executeMethods(Class<?> testClass, List<Method> testMethods,
                                       List<Method> beforeEachMethods, List<Method> afterEachMethods) {
        for (var method : testMethods) {
            final var object = ReflectionHelper.instantiate(testClass);

            Collections.shuffle(beforeEachMethods);
            boolean beforeEachSuccess = true;
            for (var beforeEachMethod : beforeEachMethods) {
                if (!ReflectionHelper.callMethod(object, beforeEachMethod)) {
                    beforeEachSuccess = false;
                    break;
                }
            }

            if (beforeEachSuccess) {
                if (!ReflectionHelper.callMethod(object, method)) {
                    failuresCount++;
                }
                runCount++;
            }

            Collections.shuffle(afterEachMethods);
            for (var afterEachMethod : afterEachMethods) {
                ReflectionHelper.callMethod(object, afterEachMethod);
            }
        }
    }
}
