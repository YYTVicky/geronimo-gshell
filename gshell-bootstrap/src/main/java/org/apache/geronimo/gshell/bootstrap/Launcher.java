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

package org.apache.geronimo.gshell.bootstrap;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * Bootstrap launcher.
 *
 * @version $Rev$ $Date$
 */
public class Launcher
{
    private final Configuration config;

    public Launcher() {
        this.config = new ConfigurationImpl();
    }

    public static void main(final String[] args) {
        assert args != null;

        Launcher launcher = new Launcher();
        launcher.run(args);
    }

    public void run(final String[] args) {
        assert args != null;

        Log.debug("Running");

        try {
            config.configure();

            launch(args);

            Log.debug("Exiting");

            System.exit(config.getSuccessExitCode());
        }
        catch (Throwable t) {
            Log.debug("Failure: " + t);
            
            t.printStackTrace(System.err);
            System.err.flush();

            System.exit(config.getFailureExitCode());
        }
    }

    public void launch(final String[] args) throws Exception {
        assert args != null;

        Log.debug("Launching");

        ClassLoader cl = getClassLoader();

        Class type = cl.loadClass(config.getMainClass());
        Method method = getMainMethod(type);

        Thread.currentThread().setContextClassLoader(cl);

        if (Log.DEBUG) {
            Log.debug("Invoking: " + method);
        }
        
        method.invoke(null, new Object[] { args });
    }

    private ClassLoader getClassLoader() throws Exception {
        List<URL> classPath = config.getClassPath();
        
        if (Log.DEBUG) {
            Log.debug("Classpath:");
            for (URL url : classPath) {
                Log.debug("    " + url);
            }
        }

        ClassLoader parent = getClass().getClassLoader();
        return new URLClassLoader(classPath.toArray(new URL[classPath.size()]), parent);
    }

    private Method getMainMethod(final Class type) throws Exception {
        assert type != null;

        Method method = type.getMethod("main", String[].class);
        int modifiers = method.getModifiers();

        if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
            Class returns = method.getReturnType();
            if (returns == Integer.TYPE || returns == Void.TYPE) {
                return method;
            }
        }

        throw new NoSuchMethodException("public static void main(String[] args) in " + type);
    }
}
