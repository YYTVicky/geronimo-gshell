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

import java.net.URI;

import org.apache.geronimo.gshell.clp.Argument;
import org.apache.geronimo.gshell.command.CommandSupport;
import org.apache.geronimo.gshell.command.annotation.CommandComponent;
import org.codehaus.plexus.component.annotations.Requirement;

/**
 * Command to start a remote shell server.
 *
 * @version $Rev$ $Date$
 */
@CommandComponent(id="rsh-server")
public class RshServerCommand
    extends CommandSupport
{
    @Argument(required=true)
    private URI location;

    @Requirement
    private RshServerFactory factory;

    private RshServer server;

    protected Object doExecute() throws Exception {
        server = factory.connect(location);

        io.out.println("Listening on: " + location);
        
        return SUCCESS;
    }
}