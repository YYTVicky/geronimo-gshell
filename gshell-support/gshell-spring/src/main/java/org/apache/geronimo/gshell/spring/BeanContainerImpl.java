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

package org.apache.geronimo.gshell.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.classworlds.realm.DuplicateRealmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link BeanContainer} implementation.
 *
 * @version $Rev$ $Date$
 */
public class BeanContainerImpl
    implements BeanContainer
{
    private static final String REALM_ID = "gshell";

    private static final String[] CONFIG_LOCATIONS = {
        "classpath*:META-INF/spring/components.xml",
        "classpath*:META-INF/gshell/commands.xml"
    };

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ClassPathXmlApplicationContext context;

    private ClassWorld classWorld;

    private ClassRealm classRealm;
    
    public BeanContainerImpl(final ClassLoader classLoader) {
        assert classLoader != null;

        // Setup classworlds
        classWorld = new ClassWorld();
        try {
            classRealm = classWorld.newRealm(REALM_ID, classLoader);
        } catch (DuplicateRealmException e) {
            // Should never happen
            throw new Error(e);
        }

        // Construct the container and add customizations
        context = new ClassPathXmlApplicationContext(CONFIG_LOCATIONS, false);
        context.registerShutdownHook();
        context.setClassLoader(classRealm);
        addBeanPostProcessor(new BeanContainerAwareProcessor(this));

        // Refresh to load things up
        context.refresh();
    }

    private void addBeanPostProcessor(final BeanPostProcessor processor) {
        assert processor != null;

        context.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor()
        {
            public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
                beanFactory.addBeanPostProcessor(processor);
            }
        });
    }

    public <T> T getBean(final Class<T> type) throws BeansException {
        assert type != null;

        String[] names = context.getBeanNamesForType(type);
        if (names.length == 0) {
            throw new NoSuchBeanDefinitionException(type, "No bean defined for type: " + type);
        }
        if (names.length > 1) {
            throw new NoSuchBeanDefinitionException(type, "Duplicate bean defined for type: " + type);
        }

        return getBean(names[0], type);
    }

    @SuppressWarnings({"unchecked"})
    public <T> T getBean(final String name, final Class<T> requiredType) throws BeansException {
        assert name != null;
        assert requiredType != null;

        return (T) context.getBean(name, requiredType);
    }
}