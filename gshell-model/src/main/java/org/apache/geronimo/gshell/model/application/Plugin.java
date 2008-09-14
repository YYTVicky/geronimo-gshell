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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.geronimo.gshell.model.common.Artifact;

/**
 * Plugin artifact configuration.
 *
 * @version $Rev$ $Date$
 */
@XStreamAlias("plugin")
public class Plugin
    extends Artifact
{
    public static final String DEFAULT_TYPE = "jar";

    private transient PluginGroup pluginGroup;

    public PluginGroup getPluginGroup() {
        return pluginGroup;
    }

    public void setPluginGroup(final PluginGroup group) {
        this.pluginGroup = group;
    }

    // Return configuration detals from the group if not directly configured

    @Override
    public String getGroupId() {
        String tmp = super.getGroupId();

        if (tmp == null && pluginGroup != null) {
            tmp = pluginGroup.getGroupId();
        }

        return tmp;
    }

    @Override
    public String getArtifactId() {
        String tmp = super.getArtifactId();

        if (tmp == null && pluginGroup != null) {
            tmp = pluginGroup.getArtifactId();
        }

        return tmp;
    }

    @Override
    public String getClassifier() {
        String tmp = super.getClassifier();

        if (tmp == null && pluginGroup != null) {
            tmp = pluginGroup.getClassifier();
        }

        return tmp;
    }

    @Override
    public String getType() {
        String tmp = super.getType();

        if (tmp == null && pluginGroup != null) {
            tmp = pluginGroup.getType();
        }

        if (tmp == null) {
            tmp = DEFAULT_TYPE;
        }

        return tmp;
    }

    @Override
    public String getVersion() {
        String tmp = super.getVersion();

        if (tmp == null && pluginGroup != null) {
            tmp = pluginGroup.getVersion();
        }

        return tmp;
    }
}