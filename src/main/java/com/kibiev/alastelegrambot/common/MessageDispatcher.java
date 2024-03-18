package org.kibiev.config.common;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.reflections.Reflections;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@Configuration
public class MessageDispatcher {
    public Map<String, Pair<Class<?>, Method>> messageMappingMethods;

    @PostConstruct
    protected void setup() {
        Set<Class<? extends MessageController>> messageControllers = new Reflections("org.kibiev")
                .getSubTypesOf(MessageController.class);
        for (var clazz : messageControllers) {
            Arrays.stream(clazz.getMethods())
                    .filter(method -> method.isAnnotationPresent(MessageMapping.class))
                    .forEach(method -> {
                        Pair<Class<?>, Method> pair = new ImmutablePair<>(clazz, method);
                        String methodMapping = method.getAnnotation(MessageMapping.class).mapping();
                        messageMappingMethods.put(methodMapping, pair);
                    });
        }
    }
    public void delegateUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            String callback = update.getCallbackQuery().getData();
            if (messageMappingMethods.containsKey(callback)) {
                try {
                    var pair = messageMappingMethods.get(callback);
                    Method method = pair.getRight();
                    var clazz = pair.getLeft();
                    method.invoke(clazz, update);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
