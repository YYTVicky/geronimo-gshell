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

package org.apache.geronimo.gshell.remote.server.handler;

import org.apache.geronimo.gshell.command.Variables;
import org.apache.geronimo.gshell.remote.server.RemoteIO;
import org.apache.geronimo.gshell.spring.BeanContainer;
import org.apache.geronimo.gshell.whisper.util.SessionAttributeBinder;
import org.apache.geronimo.gshell.shell.Shell;
import org.apache.geronimo.gshell.shell.ShellContext;

import java.security.PublicKey;

/**
 * Container for server-side session state.
 *
 * @version $Rev$ $Date$
 */
public class ServerSessionContext
{
    public static final SessionAttributeBinder<ServerSessionContext> BINDER = new SessionAttributeBinder<ServerSessionContext>(ServerSessionContext.class);

    public PublicKey pk;

    public BeanContainer container;

    public RemoteIO io;

    public Variables variables;

    public Shell shell;

    public ShellContext shellContext;

    public void close() {
        shell.close();
        shell = null;

        container = null;
        io = null;
        variables = null;
        shellContext = null;
        pk = null;
    }
}
