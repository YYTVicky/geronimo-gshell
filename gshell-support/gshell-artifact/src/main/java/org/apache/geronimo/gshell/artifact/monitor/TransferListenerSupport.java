/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.geronimo.gshell.artifact.monitor;

import org.apache.maven.wagon.events.TransferEvent;
import org.apache.maven.wagon.events.TransferListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Support for Wagon {@link TransferListener} implementations.
 *
 * @version $Rev$ $Date$
 */
public class TransferListenerSupport
    implements TransferListener
{
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public void transferInitiated(final TransferEvent event) {
        log.trace("Transfer initiated: {}", event);
    }

    public void transferStarted(final TransferEvent event) {
        log.trace("Transfer started: {}", event);
    }

    public void transferProgress(final TransferEvent event, final byte[] buffer, final int length) {
        log.trace("Transfer progress: {}, {}, {}", new Object[] { event, buffer, length });
    }

    public void transferCompleted(final TransferEvent event) {
        log.trace("Transfer completed: {}", event);
    }

    public void transferError(final TransferEvent event) {
        log.trace("Transfer error: {}", event);

        assert event != null;

        Throwable cause = event.getException();
        assert cause != null;
        
        log.error("Transfer failure: " + cause, cause);
    }

    public void debug(final String message) {
        assert message != null;
        
        log.debug(message);
    }
}