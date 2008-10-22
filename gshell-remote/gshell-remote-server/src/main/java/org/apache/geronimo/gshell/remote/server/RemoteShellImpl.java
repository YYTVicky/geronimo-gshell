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

package org.apache.geronimo.gshell.remote.server;

import org.apache.geronimo.gshell.commandline.CommandLineExecutor;
import org.apache.geronimo.gshell.shell.Shell;
import org.apache.geronimo.gshell.shell.ShellContext;
import org.apache.geronimo.gshell.shell.ShellContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides the server-side encapsulation of the basic shell bits to allow remote clients to invoke commands.
 *
 * @version $Rev$ $Date$
 */
public class RemoteShellImpl
    implements Shell
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CommandLineExecutor executor;

    private boolean opened = true;

    public RemoteShellImpl(final CommandLineExecutor executor) {
        assert executor != null;
        this.executor = executor;
    }

    private void ensureOpened() {
        if (!opened) {
            throw new IllegalStateException("Not opened");
        }
    }

    public boolean isOpened() {
        return opened;
    }

    public void close() {
        log.debug("Closing");
        
        opened = false;
    }

    public ShellContext getContext() {
        ensureOpened();

        return ShellContextHolder.get();
    }

    public Object execute(final String line) throws Exception {
        assert line != null;

        ensureOpened();

        return executor.execute(getContext(), line);
    }

    public Object execute(final String command, final Object[] args) throws Exception {
        assert command != null;
        assert args != null;

        ensureOpened();

        return executor.execute(getContext(), command, args);
    }

    public Object execute(final Object... args) throws Exception {
        assert args != null;

        ensureOpened();

        return executor.execute(getContext(), args);
    }

    public Object execute(final Object[][] commands) throws Exception {
        assert commands != null;

        ensureOpened();

        return executor.execute(getContext(), commands);
    }

    public boolean isInteractive() {
        return false;
    }

    public void run(final Object... args) throws Exception {
        throw new UnsupportedOperationException();
    }

    //
    // TODO: Hookup profile script processing bits
    //
}