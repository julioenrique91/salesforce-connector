package org.mule.modules.salesforce.connection.strategy;

import org.mule.api.callback.SourceCallback;

public class Subscription {
    private String topic;
    private SourceCallback callback;
    private boolean subscribed;

    public Subscription(String topic, SourceCallback callback, boolean subscribed) {
        this.topic = topic;
        this.callback = callback;
        this.subscribed = subscribed;
    }

    public SourceCallback getCallback() {
        return callback;
    }

    public String getTopic() {
        return topic;
    }

    public boolean isSubscribed() {
        return subscribed;
    }
}
