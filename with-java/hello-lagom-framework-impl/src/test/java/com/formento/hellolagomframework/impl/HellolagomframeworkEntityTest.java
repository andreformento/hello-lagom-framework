package com.formento.hellolagomframework.impl;

import akka.Done;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver.Outcome;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import com.formento.hellolagomframework.impl.HellolagomframeworkCommand.Hello;
import com.formento.hellolagomframework.impl.HellolagomframeworkCommand.UseGreetingMessage;
import com.formento.hellolagomframework.impl.HellolagomframeworkEvent.GreetingMessageChanged;

import static org.junit.Assert.assertEquals;

public class HellolagomframeworkEntityTest {
    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("HellolagomframeworkEntityTest");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testHellolagomframeworkEntity() {
        PersistentEntityTestDriver<HellolagomframeworkCommand, HellolagomframeworkEvent, HellolagomframeworkState> driver = new PersistentEntityTestDriver<>(system,
                new HellolagomframeworkEntity(), "world-1");

        Outcome<HellolagomframeworkEvent, HellolagomframeworkState> outcome1 = driver.run(new Hello("Alice"));
        assertEquals("Hello, Alice!", outcome1.getReplies().get(0));
        assertEquals(Collections.emptyList(), outcome1.issues());

        Outcome<HellolagomframeworkEvent, HellolagomframeworkState> outcome2 = driver.run(new UseGreetingMessage("Hi"),
                new Hello("Bob"));
        assertEquals(1, outcome2.events().size());
        assertEquals(new GreetingMessageChanged("world-1", "Hi"), outcome2.events().get(0));
        assertEquals("Hi", outcome2.state().message);
        assertEquals(Done.getInstance(), outcome2.getReplies().get(0));
        assertEquals("Hi, Bob!", outcome2.getReplies().get(1));
        assertEquals(2, outcome2.getReplies().size());
        assertEquals(Collections.emptyList(), outcome2.issues());
    }
}
