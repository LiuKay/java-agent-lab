package com.kay.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.returns;
import static org.assertj.core.api.Assertions.assertThat;

class ByteBuddyTest {

    @Test
    @DisplayName("delegate Foo#foo() to Bar#bar method")
    void methodDelegation() throws InstantiationException, IllegalAccessException {
        final String foo = new ByteBuddy()
                .subclass(Foo.class)
                .method(ElementMatchers.named("foo")
                                       .and(isDeclaredBy(Foo.class))
                                       .and(returns(String.class)))
                .intercept(MethodDelegation.to(Bar.class))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance()
                .foo();

        assertThat(foo).isEqualTo(Bar.bar());
    }

    @Test
    @DisplayName("add new method and field")
    void defineMethodAndField() throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchFieldException {
        final Class<?> loaded = new ByteBuddy()
                .subclass(Object.class)
                .name("MyClassName")
                .defineMethod("custom", String.class, Visibility.PUBLIC)
                .intercept(MethodDelegation.to(Bar.class))
                .defineField("x", String.class, Visibility.PUBLIC)
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        final Method method = loaded.getDeclaredMethod("custom");

        assertThat(method.invoke(loaded.newInstance())).isEqualTo(Bar.bar());
        assertThat(loaded.getDeclaredField("x")).isNotNull();
    }

    @Test
    @DisplayName("redefine existing class")
    void redefineClass() {
        ByteBuddyAgent.install();
        new ByteBuddy()
                .redefine(Foo.class)
                .method(named("foo"))
                .intercept(MethodDelegation.to(Bar.class))
                .make()
                .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

        Foo foo = new Foo();
        assertThat(foo.foo()).isEqualTo(Bar.bar());
    }

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
        System.out.println(instance.getClass().getClassLoader());
        Method method = loaded.getDeclaredMethod("sayHello", String.class);
        Object ret = method.invoke(instance, "kaybee");
        System.out.println(ret);
    }
}