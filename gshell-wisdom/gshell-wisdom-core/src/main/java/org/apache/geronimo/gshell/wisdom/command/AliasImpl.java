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


package org.apache.geronimo.gshell.wisdom.command;

import org.apache.geronimo.gshell.command.Alias;

/**
 * Simpe implementation of {@link Alias} interface.
 *
 * @version $Rev: 706889 $ $Date: 2008-10-22 10:12:04 +0200 (Wed, 22 Oct 2008) $
 */
public class AliasImpl
    implements Alias
{
    private final String name;

    private final String alias;

    public AliasImpl(final String name, final String alias) {
        assert name != null;
        this.name = name;

        assert alias != null;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }
}
