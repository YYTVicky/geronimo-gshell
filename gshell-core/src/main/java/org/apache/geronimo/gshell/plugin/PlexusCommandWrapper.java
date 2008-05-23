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

package org.apache.geronimo.gshell.plugin;

import org.apache.geronimo.gshell.command.Command;
import org.apache.geronimo.gshell.command.CommandContext;
import org.apache.geronimo.gshell.descriptor.CommandDescriptor;
import org.codehaus.plexus.PlexusContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * A wrapper for Plexus-based commands.
 *
 * @version $Rev: 581061 $ $Date: 2007-10-01 22:18:31 +0200 (Mon, 01 Oct 2007) $
 */
public class PlexusCommandWrapper
    implements Command
{
    private Logger log = LoggerFactory.getLogger(getClass());

    private CommandDescriptor descriptor;

    private PlexusContainer container;

    public PlexusCommandWrapper(final PlexusContainer container, final CommandDescriptor descriptor) {
        this.container = container;
        this.descriptor = descriptor;
    }

    public String getId() {
        return descriptor.getId();
    }

    public String getDescription() {
        return descriptor.getDescription();
    }

    public Object execute(final CommandContext context, final Object... args) throws Exception {
        // Create a new child container for the invocation and lookup the command instance
        String realmId = "gshell:" + UUID.randomUUID();

        log.debug("Child container realm: {}", realmId);

        PlexusContainer childContainer = container.createChildContainer(realmId, container.getContainerRealm());

        Command command = (Command) childContainer.lookup(Command.class, descriptor.getId());

        Object result;

        try {
            result = command.execute(context, args);
        }
        finally {
            // Nuke the child container
            container.removeChildContainer(realmId);
        }

        return result;
    }
}
