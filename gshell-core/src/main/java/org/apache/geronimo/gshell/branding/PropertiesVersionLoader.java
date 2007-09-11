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

package org.apache.geronimo.gshell.branding;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.codehaus.plexus.component.annotations.Component;

/**
 * Loads a version number from a properties file.
 *
 * @version $Rev$ $Date$
 */
@Component(role=VersionLoader.class, hint="default") // HACK: hint="properties")
public class PropertiesVersionLoader
    implements VersionLoader
{
    private Properties props;

    // @Configuraton(key="resourceName", value="version.properties")
    private String resourceName = "version.properties";

    public String getVersion() {
        if (props == null) {
            InputStream input = getClass().getResourceAsStream(resourceName);
            assert input != null;

            try {
                props = new Properties();
                props.load(input);
                input.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Failed to load " + resourceName, e);
            }
        }
        
        return props.getProperty("version");
    }
}