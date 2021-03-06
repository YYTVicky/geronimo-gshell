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

package org.apache.geronimo.gshell.commands.builtin;

import org.apache.commons.vfs.FileObject;
import org.apache.geronimo.gshell.clp.Argument;
import org.apache.geronimo.gshell.command.CommandAction;
import org.apache.geronimo.gshell.command.CommandContext;
import org.apache.geronimo.gshell.commandline.CommandLineExecutor;
import org.apache.geronimo.gshell.io.IO;
import org.apache.geronimo.gshell.shell.ShellContextHolder;
import org.apache.geronimo.gshell.vfs.FileSystemAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Read and execute commands from a file in the current shell environment.
 *
 * @version $Rev$ $Date$
 */
public class SourceAction
    implements CommandAction
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CommandLineExecutor executor;

    private final FileSystemAccess fileSystemAccess;

    @Argument(required=true)
    private String path;

    public SourceAction(final CommandLineExecutor executor, final FileSystemAccess fileSystemAccess) {
        assert executor != null;
        this.executor = executor;
        assert fileSystemAccess != null;
        this.fileSystemAccess = fileSystemAccess;
    }

    public Object execute(final CommandContext context) throws Exception {
        assert context != null;
        IO io = context.getIo();

        FileObject cwd = fileSystemAccess.getCurrentDirectory(context.getVariables());
        FileObject file = fileSystemAccess.resolveFile(cwd, path);

        if (!file.exists()) {
            io.error("File not found: {}", file.getName());
            return Result.FAILURE;
        }
        else if (!file.getType().hasContent()) {
            io.error("File has no content: {}", file.getName());
            return Result.FAILURE;
        }

        log.debug("Sourcing file: {}", file.getName());
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getContent().getInputStream()));
        
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String tmp = line.trim();
                
                // Ignore empty lines and comments
                if (tmp.length() == 0 || tmp.startsWith("#")) {
                    continue;
                }

                executor.execute(ShellContextHolder.get(), line);
            }
        }
        finally {
            reader.close();
            file.close();
        }

        return Result.SUCCESS;
    }
}
