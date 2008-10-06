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

package org.apache.geronimo.gshell.notification;

import org.apache.geronimo.gshell.command.CommandAction.Result;

/**
 * Thrown to indicate a command failure state.
 *
 * Use of this notification won't cause an error to be propagated
 * only some details logged and the {@link Result#FAILURE} returned.
 *
 * @version $Rev$ $Date$
 */
public final class FailureNotification
    extends Notification
{
    private static final long serialVersionUID = 1;

    public FailureNotification(final String msg) {
        super(msg);
    }

    public FailureNotification(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public FailureNotification(final Throwable cause) {
        super(cause);
    }

    public FailureNotification() {
        super();
    }
}