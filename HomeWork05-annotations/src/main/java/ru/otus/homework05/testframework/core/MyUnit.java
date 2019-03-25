package ru.otus.homework05.testframework.core;

import ru.otus.homework05.testframework.api.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyUnit {
    private static class TestingContext {
        public int failuresCount;
        public int runCount;
        public List<Method> beforeAllMethods = new ArrayList<>();
        public List<Method> afterAllMethods = new ArrayList<>();
        public List<Method> testMethods = new ArrayList<>();
        public List<Method> beforeEachMethods = new ArrayList<>();
        public List<Method> afterEachMethods = new ArrayList<>();
    }

    public static void run(Class<?> testClass) {
        var testingContext = new TestingContext();

        if (!distributeMethods(testClass.getDeclaredMethods(), testingContext)) {
            return;
        }

        shuffleMethods(testingContext);
        execute(testClass, testingContext);

        System.out.println("\nTests run: " + testingContext.runCount + ", Failures: " + testingContext.failuresCount);
    }

    private static boolean distributeMethods(Method[] methods, TestingContext context) {
        for (var method : methods) {
            final boolean methodIsStatic = Modifier.isStatic(method.getModifiers());

            final var annotations = method.getDeclaredAnnotations();
            for (var annotation: annotations) {
                boolean annotationIsForStaticMethod = false;

                if (annotation.annotationType() == BeforeAll.class) {
                    context.beforeAllMethods.add(method);
                    annotationIsForStaticMethod = true;
                } else if (annotation.annotationType() == AfterAll.class) {
                    context.afterAllMethods.add(method);
                    annotationIsForStaticMethod = true;
                } else if (annotation.annotationType() == Test.class) {
                    context.testMethods.add(method);
                } else if (annotation.annotationType() == BeforeEach.class) {
                    context.beforeEachMethods.add(method);
                } else if (annotation.annotationType() == AfterEach.class) {
                    context.afterEachMethods.add(method);
                }

                if (methodIsStatic != annotationIsForStaticMethod) {
                    System.out.println("Incorrect using of annotations for static or not static methods");
                    return false;
                }
            }
        }
        return true;
    }

    private static void shuffleMethods(TestingContext context) {
        Collections.shuffle(context.beforeAllMethods);
        Collections.shuffle(context.afterAllMethods);
        Collections.shuffle(context.testMethods);
    }

    private static void execute(Class<?> testClass, TestingContext context) {
        if (executeStaticMethods(context.beforeAllMethods)) {
            executeMethods(testClass, context);
        }
        executeStaticMethods(context.afterAllMethods);
    }

    private static boolean executeStaticMethods(List<Method> methods) {
        for (var method : methods) {
            if (!ReflectionHelper.callMethod(null, method)) {
                return false;
            }
        }
        return true;
    }

    private static void executeMethods(Class<?> testClass, TestingContext context) {
        for (var method : context.testMethods) {
            final var object = ReflectionHelper.instantiate(testClass);

            Collections.shuffle(context.beforeEachMethods);
            boolean beforeEachSuccess = true;
            for (var beforeEachMethod : context.beforeEachMethods) {
                if (!ReflectionHelper.callMethod(object, beforeEachMethod)) {
                    beforeEachSuccess = false;
                    break;
                }
            }

            if (beforeEachSuccess) {
                if (!ReflectionHelper.callMethod(object, method)) {
                    context.failuresCount++;
                }
                context.runCount++;
            }

            Collections.shuffle(context.afterEachMethods);
            for (var afterEachMethod : context.afterEachMethods) {
                ReflectionHelper.callMethod(object, afterEachMethod);
            }
        }
    }
}
