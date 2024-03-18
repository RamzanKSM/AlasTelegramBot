package com.kibiev.alastelegrambot.common;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageDispatcher {
    private final Map<String, Pair<Class<?>, Method>> messageMappingMethods = new HashMap<>();
    private final ApplicationContext applicationContext;

    @PostConstruct
    protected void setup() {
        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(MessageController.class));
        var controllers = scanner.findCandidateComponents("")
                .stream()
                .map(beanDefinition -> {
                    try {
                        return Class.forName(beanDefinition.getBeanClassName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        for (var clazz : controllers) {
            Arrays.stream(clazz.getMethods())
                    .filter(method -> method.isAnnotationPresent(MessageMapping.class))
                    .forEach(method -> {
                        Pair<Class<?>, Method> pair = new ImmutablePair<>(clazz, method);
                        String methodMapping = method.getAnnotation(MessageMapping.class).value();
                        messageMappingMethods.put(methodMapping, pair);
                    });
        }
    }
    public void delegateUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            String callback = update.getCallbackQuery().getData();

            if (messageMappingMethods.containsKey(callback)) {
                invokeMethod(messageMappingMethods.get(callback), update);
            }
        } else if (update.hasMessage()) {
            String message = update.getMessage().getText();

            if (messageMappingMethods.containsKey(message)) {
                invokeMethod(messageMappingMethods.get(message), update);
            }
        }
    }

    private void invokeMethod(Pair<Class<?>, Method> pair, Update update) {
        try {
            Method method = pair.getRight();
            Class<?> clazz = pair.getLeft();
            Object instance = applicationContext.getBean(
                    makeClassNameToBeanName(clazz.getSimpleName())
            );
            method.invoke(instance, update);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    private String makeClassNameToBeanName(String className) {
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
}
