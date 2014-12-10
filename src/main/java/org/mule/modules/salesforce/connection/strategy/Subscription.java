/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

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
