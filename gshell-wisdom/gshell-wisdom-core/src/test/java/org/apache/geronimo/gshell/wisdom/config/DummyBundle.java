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

package org.apache.geronimo.gshell.wisdom.config;

import org.apache.geronimo.gshell.spring.LoggingProcessor;
import org.apache.geronimo.gshell.spring.SpringTestSupport;
import org.apache.geronimo.gshell.command.CommandAction;
import org.apache.geronimo.gshell.command.CommandContext;
import org.apache.geronimo.gshell.notification.Notification;
import org.apache.geronimo.gshell.application.plugin.bundle.Bundle;

/**
 * ???
 *
 * @version $Rev$ $Date$
 */
public class DummyBundle
    implements Bundle
{
    private String name;

    public DummyBundle(final String name) {
        this.name = name;
    }

    public void disable() throws Exception {

    }

    public void enable() throws Exception {
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return false;
    }
}