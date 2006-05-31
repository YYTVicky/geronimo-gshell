/*
 * Copyright 2006 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.geronimo.gshell.console;

/**
 * Interface to abstract creation of {@link Console} instances.
 *
 * @version $Id$
 */
public interface ConsoleFactory
{
    //
    // TODO: Need to hookup ConsoleFactory to allow instances to be created by components
    //       (like the script command) with out needing to know which is the right flavor
    //

    //
    // TODO: Re-eval if this needs to be an interface... might not
    //

    Console create(IO io) throws Exception;
}