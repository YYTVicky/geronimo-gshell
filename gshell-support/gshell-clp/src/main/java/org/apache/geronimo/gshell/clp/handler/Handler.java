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

package org.apache.geronimo.gshell.clp.handler;

import org.apache.geronimo.gshell.clp.Descriptor;
import org.apache.geronimo.gshell.clp.ProcessingException;
import org.apache.geronimo.gshell.clp.setter.Setter;

/**
 * Provides the basic mechanism to handle custom option and argument processing.
 *
 * @version $Rev$ $Date$
 */
public abstract class Handler<T>
{
    public final Descriptor descriptor;
    
    public Boolean isKeyValuePair = false;

    public final Setter<? super T> setter;

    protected Handler(final Descriptor descriptor, final Setter<? super T> setter) {
        assert descriptor != null;
        assert setter != null;
        
        this.descriptor = descriptor;
        this.setter = setter;
    }

    public abstract int handle(Parameters params) throws ProcessingException;

    public abstract String getDefaultToken();
}
