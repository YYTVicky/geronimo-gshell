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

package org.apache.geronimo.gshell.wisdom.meta;

import org.apache.geronimo.gshell.vfs.provider.meta.data.support.MapMetaDataContentSupport;

import java.util.Map;

/**
 * {@link org.apache.geronimo.gshell.vfs.provider.meta.data.MetaDataContent} to return the contents of {@link System#getProperties}.
 *
 * @version $Rev$ $Date$
 */
public class SystemPropertiesContent
    extends MapMetaDataContentSupport<Object,Object>
{
    protected Map<Object, Object> getMap() {
        return System.getProperties();
    }
}