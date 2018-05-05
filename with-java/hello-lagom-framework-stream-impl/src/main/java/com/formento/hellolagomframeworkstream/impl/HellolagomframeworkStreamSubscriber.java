package com.formento.hellolagomframeworkstream.impl;

import akka.Done;
import akka.stream.javadsl.Flow;

import com.formento.hellolagomframework.api.HellolagomframeworkEvent;
import com.formento.hellolagomframework.api.HellolagomframeworkService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * This subscribes to the HellolagomframeworkService event stream.
 */
public class HellolagomframeworkStreamSubscriber {
    @Inject
    public HellolagomframeworkStreamSubscriber(HellolagomframeworkService hellolagomframeworkService, HellolagomframeworkStreamRepository repository) {
        // Create a subscriber
        hellolagomframeworkService.helloEvents().subscribe()
                // And subscribe to it with at least once processing semantics.
                .atLeastOnce(
                        // Create a flow that emits a Done for each message it processes
                        Flow.<HellolagomframeworkEvent>create().mapAsync(1, event -> {
                            if (event instanceof HellolagomframeworkEvent.GreetingMessageChanged) {
                                HellolagomframeworkEvent.GreetingMessageChanged messageChanged = (HellolagomframeworkEvent.GreetingMessageChanged) event;
                                // Update the message
                                return repository.updateMessage(messageChanged.getName(), messageChanged.getMessage());
                            } else {
                                // Ignore all other events
                                return CompletableFuture.completedFuture(Done.getInstance());
                            }
                        })
                );
    }
}
