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

package org.apache.geronimo.gshell.vfs;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.geronimo.gshell.command.Variables;

/**
 * Provides access to VFS file systems.
 *
 * @version $Rev$ $Date$
 */
public interface FileSystemAccess
{
    String CWD = "vfs.cwd";

    FileSystemManager getManager();

    FileObject getCurrentDirectory(Variables vars) throws FileSystemException;

    FileObject getCurrentDirectory() throws FileSystemException;

    void setCurrentDirectory(Variables vars, FileObject dir) throws FileSystemException;

    FileObject resolveFile(FileObject baseFile, String name) throws FileSystemException;

    //
    // TODO: Consider renaming this puppy, as it resolved relative to CWD, to resolve w/o use ^^^ and pass null to the first, bit me a few times already
    //
    
    FileObject resolveFile(String name) throws FileSystemException;
}
