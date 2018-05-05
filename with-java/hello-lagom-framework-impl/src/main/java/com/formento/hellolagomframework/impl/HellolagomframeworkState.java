package com.formento.hellolagomframework.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Value;

/**
 * The state for the {@link HellolagomframeworkEntity} entity.
 */
@SuppressWarnings("serial")
@Value
@JsonDeserialize
public final class HellolagomframeworkState implements CompressedJsonable {
    public final String message;
    public final String timestamp;

    @JsonCreator
    HellolagomframeworkState(String message, String timestamp) {
        this.message = Preconditions.checkNotNull(message, "message");
        this.timestamp = Preconditions.checkNotNull(timestamp, "timestamp");
    }
}
