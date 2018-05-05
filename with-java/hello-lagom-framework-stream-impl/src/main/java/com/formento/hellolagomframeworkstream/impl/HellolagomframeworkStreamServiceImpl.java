package com.formento.hellolagomframeworkstream.impl;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import com.formento.hellolagomframework.api.HellolagomframeworkService;
import com.formento.hellolagomframeworkstream.api.HellolagomframeworkStreamService;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Implementation of the HellolagomframeworkStreamService.
 */
public class HellolagomframeworkStreamServiceImpl implements HellolagomframeworkStreamService {
    private final HellolagomframeworkService hellolagomframeworkService;
    private final HellolagomframeworkStreamRepository repository;

    @Inject
    public HellolagomframeworkStreamServiceImpl(HellolagomframeworkService hellolagomframeworkService, HellolagomframeworkStreamRepository repository) {
        this.hellolagomframeworkService = hellolagomframeworkService;
        this.repository = repository;
    }

    @Override
    public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> directStream() {
        return hellos -> completedFuture(
                hellos.mapAsync(8, name -> hellolagomframeworkService.hello(name).invoke()));
    }

    @Override
    public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> autonomousStream() {
        return hellos -> completedFuture(
                hellos.mapAsync(8, name -> repository.getMessage(name).thenApply(message ->
                        String.format("%s, %s!", message.orElse("Hello"), name)
                ))
        );
    }
}
