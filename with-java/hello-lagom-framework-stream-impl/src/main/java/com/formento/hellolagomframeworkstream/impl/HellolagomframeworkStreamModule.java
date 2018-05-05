package com.formento.hellolagomframeworkstream.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

import com.formento.hellolagomframework.api.HellolagomframeworkService;
import com.formento.hellolagomframeworkstream.api.HellolagomframeworkStreamService;

/**
 * The module that binds the HellolagomframeworkStreamService so that it can be served.
 */
public class HellolagomframeworkStreamModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        // Bind the HellolagomframeworkStreamService service
        bindService(HellolagomframeworkStreamService.class, HellolagomframeworkStreamServiceImpl.class);
        // Bind the HellolagomframeworkService client
        bindClient(HellolagomframeworkService.class);
        // Bind the subscriber eagerly to ensure it starts up
        bind(HellolagomframeworkStreamSubscriber.class).asEagerSingleton();
    }
}
