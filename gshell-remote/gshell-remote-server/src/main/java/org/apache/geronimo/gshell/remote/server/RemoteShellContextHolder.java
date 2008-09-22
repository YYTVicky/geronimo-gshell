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

import org.apache.geronimo.gshell.shell.ShellContext;

/**
 * Remote {@link ShellContext} holder.
 *
 * @version $Rev$ $Date$
 */
public class RemoteShellContextHolder
{
    private static final InheritableThreadLocal<ShellContext> holder = new InheritableThreadLocal<ShellContext>();

    public static void clearContext() {
        holder.set(null);
    }

    public static void setContext(final ShellContext context) {
        assert context != null;

        holder.set(context);
    }

    public static ShellContext getContext() {
        return holder.get();
    }
}