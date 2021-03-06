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

import org.apache.geronimo.gshell.ansi.AnsiRenderer;
import org.apache.geronimo.gshell.application.Application;
import org.apache.geronimo.gshell.command.Variables;
import org.apache.geronimo.gshell.console.Console;
import org.apache.geronimo.gshell.interpolation.VariablesValueSource;
import org.apache.geronimo.gshell.shell.ShellContextHolder;
import org.codehaus.plexus.interpolation.InterpolationException;
import org.codehaus.plexus.interpolation.Interpolator;
import org.codehaus.plexus.interpolation.PrefixedObjectValueSource;
import org.codehaus.plexus.interpolation.StringSearchInterpolator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link Console.Prompter} component.
 *
 * @version $Rev$ $Date$
 */
public class ConsolePrompterImpl
    implements Console.Prompter
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Interpolator interp = new StringSearchInterpolator("%{", "}");

    private final VariablesValueSource variablesValueSource = new VariablesValueSource();

    private final AnsiRenderer renderer = new AnsiRenderer();

    private final String defaultPrompt;

    public ConsolePrompterImpl(final Application application) {
        assert application != null;
        
        interp.addValueSource(new PrefixedObjectValueSource("application", application));
        interp.addValueSource(new PrefixedObjectValueSource("branding", application.getModel().getBranding()));
        interp.addValueSource(variablesValueSource);

        defaultPrompt = application.getModel().getBranding().getPrompt();
    }

    public String prompt() {
        String prompt = null;

        Variables vars = ShellContextHolder.get().getVariables();
        String pattern = vars.get("gshell.prompt", String.class);

        if (pattern != null) {
            assert variablesValueSource != null;
            variablesValueSource.setVariables(vars);

            try {
                assert interp != null;
                prompt = interp.interpolate(pattern);
            }
            catch (InterpolationException e) {
                log.error("Failed to render prompt pattern: " + pattern, e);
            }
        }

        // Use a default prompt if we don't have anything here
        if (prompt == null) {
            prompt = defaultPrompt;
        }

        // Encode ANSI muck if it looks like there are codes encoded
        if (AnsiRenderer.test(prompt)) {
            prompt = renderer.render(prompt);
        }

        return prompt;
    }
}