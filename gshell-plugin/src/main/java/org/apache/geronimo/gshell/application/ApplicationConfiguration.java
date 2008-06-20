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

package org.apache.geronimo.gshell.application;

import org.apache.geronimo.gshell.yarn.ReflectionToStringBuilder;
import org.apache.geronimo.gshell.yarn.ToStringStyle;
import org.apache.geronimo.gshell.io.IO;
import org.apache.geronimo.gshell.model.application.Application;
import org.apache.geronimo.gshell.shell.ShellContext;

/**
 * Container for application configuration.
 *
 * @version $Rev$ $Date$
 */
public class ApplicationConfiguration
{
    private IO io;

    private ShellContext context;

    private Application application;

    private IO createIo() {
        return new IO();
    }

    public IO getIo() {
        if (io == null) {
            io = createIo();
        }
        return io;
    }

    public void setIo(final IO io) {
        this.io = io;
    }

    private ShellContext createEnvironment() {
        return new DefaultShellContext(getIo());
    }

    public ShellContext getEnvironment() {
        if (context == null) {
            context = createEnvironment();
        }

        return context;
    }

    public void setEnvironment(final ShellContext context) {
        this.context = context;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(final Application application) {
        this.application = application;
    }

    public void validate() {
        if (application == null) {
            throw new IllegalStateException("Missing application configuration");
        }    
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}