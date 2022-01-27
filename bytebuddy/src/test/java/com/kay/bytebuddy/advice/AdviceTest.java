package com.kay.bytebuddy.advice;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class AdviceTest {

    @Test
    void visitMethod() throws Exception{
        Class<?> loaded = new ByteBuddy()
                .redefine(HelloReboot.class)
                .visit(Advice.to(HelloRebootAdvice.class).on(ElementMatchers.isMethod()))
                .make()
                //should use bootstrap loader
                .load(ClassLoadingStrategy.BOOTSTRAP_LOADER, ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        Object instance = loaded.newInstance();
        //        System.out.println(instance.getClass().getClassLoader());
        Method method = loaded.getDeclaredMethod("sayHello", String.class);
        Object ret = method.invoke(instance, "kaybee");

        assertThat(ret).isEqualTo("hello, kaybee");

        final Field field = loaded.getDeclaredField("name");
        assertThat(field).isNotNull();

        field.setAccessible(true);
        assertThat(field.get(instance)).isEqualTo("Kay");
    }

}
