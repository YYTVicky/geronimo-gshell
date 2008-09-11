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

package org.apache.geronimo.gshell.wisdom.shell;

import jline.History;
import org.apache.geronimo.gshell.application.ApplicationManager;
import org.apache.geronimo.gshell.wisdom.application.event.ApplicationConfiguredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.io.File;
import java.io.IOException;

/**
 * Default implementation of the {@link jline.History} component.
 *
 * @version $Rev$ $Date$
 */
public class HistoryImpl
    extends History
    implements ApplicationListener
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationManager applicationManager;

    public HistoryImpl() {}

    public void onApplicationEvent(final ApplicationEvent event) {
        assert event != null;

        if (event instanceof ApplicationConfiguredEvent) {
            assert applicationManager != null;

            try {
                File file = applicationManager.getContext().getModel().getBranding().getHistoryFile();

                log.debug("Application configured, setting history file: {}", file);

                setHistoryFile(file);
            }
            catch (IOException e) {
                throw new RuntimeException("Failed to set history file", e);
            }
        }
    }

    public void setHistoryFile(final File file) throws IOException {
        assert file != null;

        File dir = file.getParentFile();

        if (!dir.exists()) {
            dir.mkdirs();

            log.debug("Created base directory for history file: {}", dir);
        }

        log.debug("Using history file: {}", file);

        super.setHistoryFile(file);
    }
}