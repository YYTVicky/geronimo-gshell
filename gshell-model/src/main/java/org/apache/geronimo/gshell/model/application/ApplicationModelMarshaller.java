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

package org.apache.geronimo.gshell.model.application;

import com.thoughtworks.xstream.XStream;
import org.apache.geronimo.gshell.model.layout.AliasNode;
import org.apache.geronimo.gshell.model.layout.CommandNode;
import org.apache.geronimo.gshell.model.marshal.MarshallerSupport;

/**
 * Marshaller for {@link ApplicationModel} models.
 *
 * @version $Rev$ $Date$
 */
public class ApplicationModelMarshaller
    extends MarshallerSupport<ApplicationModel>
{
    public ApplicationModelMarshaller() {
        super(ApplicationModel.class);
    }

    @Override
    protected void configure(final XStream xs) {
        super.configure(xs);

        // Need to provide some additional configuration to tell XStream about the Layout's node sub-classes
        configureAnnotations(xs, CommandNode.class, AliasNode.class);
    }
}