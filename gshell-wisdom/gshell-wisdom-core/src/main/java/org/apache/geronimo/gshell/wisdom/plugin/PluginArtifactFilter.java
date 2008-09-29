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

package org.apache.geronimo.gshell.wisdom.plugin;

import org.apache.geronimo.gshell.wisdom.application.ApplicationArtifactFilter;
import org.apache.geronimo.gshell.application.Application;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.AndArtifactFilter;
import org.apache.maven.artifact.Artifact;

import java.util.Set;
import java.util.HashSet;

/**
 * Artifact filter for plugins.
 *
 * @version $Rev$ $Date$
 */
public class PluginArtifactFilter
    extends ApplicationArtifactFilter
{
    private final Application application;

    public PluginArtifactFilter(final Application application) {
        assert application != null;

        this.application = application;
    }

    @Override
    protected AndArtifactFilter createFilter() {
        AndArtifactFilter filter = super.createFilter();
        
        // Filter out application artifacts, need to use gid:aid to make sure we don't clober anything which has the same artifactId, but different groupId
        final Set<String> excludes = new HashSet<String>();
        for (org.apache.geronimo.gshell.model.common.Artifact a : application.getArtifacts()) {
            String id = a.getGroupId() + ":" + a.getArtifactId();
            excludes.add(id);
        }

        filter.add(new ArtifactFilter() {
            public boolean include(final Artifact artifact) {
                assert artifact != null;
                String id = artifact.getGroupId() + ":" + artifact.getArtifactId();
                return !excludes.contains(id);
            }
        });

        return filter;
    }
}