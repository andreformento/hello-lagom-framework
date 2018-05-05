package com.formento.hellolagomframework.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

import com.formento.hellolagomframework.api.HellolagomframeworkService;

/**
 * The module that binds the HellolagomframeworkService so that it can be served.
 */
public class HellolagomframeworkModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(HellolagomframeworkService.class, HellolagomframeworkServiceImpl.class);
    }
}
