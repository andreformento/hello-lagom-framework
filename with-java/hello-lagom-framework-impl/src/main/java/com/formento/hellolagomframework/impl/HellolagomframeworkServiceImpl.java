package com.formento.hellolagomframework.impl;

import akka.Done;
import akka.NotUsed;
import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;

import com.formento.hellolagomframework.api.GreetingMessage;
import com.formento.hellolagomframework.api.HellolagomframeworkService;
import com.formento.hellolagomframework.impl.HellolagomframeworkCommand.*;

/**
 * Implementation of the HellolagomframeworkService.
 */
public class HellolagomframeworkServiceImpl implements HellolagomframeworkService {
    private final PersistentEntityRegistry persistentEntityRegistry;

    @Inject
    public HellolagomframeworkServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        persistentEntityRegistry.register(HellolagomframeworkEntity.class);
    }

    @Override
    public ServiceCall<NotUsed, String> hello(String id) {
        return request -> {
            // Look up the hello world entity for the given ID.
            PersistentEntityRef<HellolagomframeworkCommand> ref = persistentEntityRegistry.refFor(HellolagomframeworkEntity.class, id);
            // Ask the entity the Hello command.
            return ref.ask(new Hello(id));
        };
    }

    @Override
    public ServiceCall<GreetingMessage, Done> useGreeting(String id) {
        return request -> {
            // Look up the hello world entity for the given ID.
            PersistentEntityRef<HellolagomframeworkCommand> ref = persistentEntityRegistry.refFor(HellolagomframeworkEntity.class, id);
            // Tell the entity to use the greeting message specified.
            return ref.ask(new UseGreetingMessage(request.message));
        };
    }

    @Override
    public Topic<com.formento.hellolagomframework.api.HellolagomframeworkEvent> helloEvents() {
        // We want to publish all the shards of the hello event
        return TopicProducer.taggedStreamWithOffset(HellolagomframeworkEvent.TAG.allTags(), (tag, offset) ->
                // Load the event stream for the passed in shard tag
                persistentEntityRegistry.eventStream(tag, offset).map(eventAndOffset -> {
                    // Now we want to convert from the persisted event to the published event.
                    // Although these two events are currently identical, in future they may
                    // change and need to evolve separately, by separating them now we save
                    // a lot of potential trouble in future.
                    com.formento.hellolagomframework.api.HellolagomframeworkEvent eventToPublish;

                    if (eventAndOffset.first() instanceof HellolagomframeworkEvent.GreetingMessageChanged) {
                        HellolagomframeworkEvent.GreetingMessageChanged messageChanged = (HellolagomframeworkEvent.GreetingMessageChanged) eventAndOffset.first();
                        eventToPublish = new com.formento.hellolagomframework.api.HellolagomframeworkEvent.GreetingMessageChanged(
                                messageChanged.getName(), messageChanged.getMessage()
                        );
                    } else {
                        throw new IllegalArgumentException("Unknown event: " + eventAndOffset.first());
                    }

                    // We return a pair of the translated event, and its offset, so that
                    // Lagom can track which offsets have been published.
                    return Pair.create(eventToPublish, eventAndOffset.second());
                })
        );
    }
}
