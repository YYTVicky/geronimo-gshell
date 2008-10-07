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

package org.apache.geronimo.gshell.vfs.provider.meta.data.support;

import org.apache.geronimo.gshell.vfs.provider.meta.data.MetaDataRegistry;
import org.apache.geronimo.gshell.vfs.provider.meta.data.MetaData;
import org.apache.geronimo.gshell.vfs.provider.meta.data.MetaDataContent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Installs {@link MetaData} into the {@link MetaDataRegistry}.
 *
 * @version $Rev$ $Date$
 */
public class MetaDataInstaller
{
    @Autowired
    private MetaDataRegistry metaRegistry;

    private Map<String,MetaDataContent> contentNodes;

    public void setContentNodes(final Map<String, MetaDataContent> nodes) {
        this.contentNodes = nodes;
    }

    @PostConstruct
    public void init() throws Exception {
        assert metaRegistry != null;
        MetaDataRegistryConfigurer metaConfig = new MetaDataRegistryConfigurer(metaRegistry);

        if (contentNodes != null && !contentNodes.isEmpty()) {
            for (Map.Entry<String,MetaDataContent> entry : contentNodes.entrySet()) {
                metaConfig.addContent(entry.getKey(), entry.getValue());
            }
        }
    }

    //
    // TODO: Merge this guy with MetaDataRegistryConfigurer, allow for spring+direct usage.
    //
}