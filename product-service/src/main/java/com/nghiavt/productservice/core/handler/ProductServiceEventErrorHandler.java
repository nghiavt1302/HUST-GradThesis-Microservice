package com.nghiavt.productservice.core.handler;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;

import javax.annotation.Nonnull;

public class ProductServiceEventErrorHandler implements ListenerInvocationErrorHandler {
    @Override
    public void onError(@Nonnull Exception exception,
                        @Nonnull EventMessage<?> event,
                        @Nonnull EventMessageHandler eventHandler) throws Exception {
        throw exception;
    }
}
