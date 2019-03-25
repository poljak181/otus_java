package ru.otus.homework05.testframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionHelper {

    private ReflectionHelper() {
    }

    static <T> T instantiate(Class<T> type, Object... args) {
        boolean isAccessible = true;
        Constructor<T> constructor = null;
        try {
            if (args.length == 0) {
                constructor = type.getDeclaredConstructor();
                isAccessible = constructor.canAccess(null);
                constructor.setAccessible(true);
                return constructor.newInstance();
            } else {
                final Class<?>[] classes = toClasses(args);
                constructor = type.getDeclaredConstructor(classes);
                return constructor.newInstance(args);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (constructor != null && !isAccessible) {
                constructor.setAccessible(false);
            }
        }
        return null;
    }

    static <T> Object callMethod(Object object, Method method, Object... args) throws InvocationTargetException {
        boolean isAccessible = true;

        try {
            isAccessible = method.canAccess(object);
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw e;
        } finally {
            if (method != null && !isAccessible) {
                method.setAccessible(false);
            }
        }
        return null;
    }

    private static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }
}
