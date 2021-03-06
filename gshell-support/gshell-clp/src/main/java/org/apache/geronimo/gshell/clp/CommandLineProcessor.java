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

package org.apache.geronimo.gshell.clp;

import org.apache.geronimo.gshell.clp.handler.Handler;
import org.apache.geronimo.gshell.clp.handler.Handlers;
import org.apache.geronimo.gshell.clp.handler.Parameters;
import org.apache.geronimo.gshell.clp.setter.CollectionFieldSetter;
import org.apache.geronimo.gshell.clp.setter.FieldSetter;
import org.apache.geronimo.gshell.clp.setter.MethodSetter;
import org.apache.geronimo.gshell.clp.setter.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Processes an object for command-line configuration annotations.
 *
 * @version $Rev$ $Date$
 */
public class CommandLineProcessor
{
    private final List<Handler> optionHandlers = new ArrayList<Handler>();

    private final List<Handler> argumentHandlers = new ArrayList<Handler>();

    private boolean stopAtNonOption = false;

    public CommandLineProcessor() {}
    
    public CommandLineProcessor(final Object bean) {
        assert bean != null;

        addBean(bean);
    }

    public List<Handler> getOptionHandlers() {
        return Collections.unmodifiableList(optionHandlers);
    }

    public List<Handler> getArgumentHandlers() {
        return Collections.unmodifiableList(argumentHandlers);
    }

    public boolean getStopAtNonOption() {
        return stopAtNonOption;
    }

    public void setStopAtNonOption(final boolean flag) {
        stopAtNonOption = flag;
    }

    public void addBean(final Object bean) {
        assert bean != null;

        discoverDescriptors(bean);
    }

    //
    // Discovery
    //
    
    private void discoverDescriptors(final Object bean) {
        assert bean != null;

        // Recursively process all the methods/fields.
        for (Class type=bean.getClass(); type!=null; type=type.getSuperclass()) {
            // Discover methods
            for (Method method : type.getDeclaredMethods()) {
                Option option = method.getAnnotation(Option.class);
                if (option != null) {
                    addOption(new MethodSetter(bean, method), option);
                }

                Argument argument = method.getAnnotation(Argument.class);
                if (argument != null) {
                    addArgument(new MethodSetter(bean, method), argument);
                }
            }

            // Discover fields
            for (Field field : type.getDeclaredFields()) {
                Option option = field.getAnnotation(Option.class);
                if (option != null) {
                    addOption(createFieldSetter(bean, field), option);
                }

                Argument argument = field.getAnnotation(Argument.class);
                if (argument != null) {
                    addArgument(createFieldSetter(bean, field), argument);
                }
            }
        }

        // Sanity check the argument indexes
        for (int i=0; i< argumentHandlers.size(); i++) {
            if (argumentHandlers.get(i) == null) {
                throw new IllegalAnnotationError("No argument annotation for index: " + i);
            }
        }
    }
    
    private Setter createFieldSetter(final Object bean, final Field field) {
        assert bean != null;
        assert field != null;

        if (Collection.class.isAssignableFrom(field.getType())) {
            return new CollectionFieldSetter(bean, field);
        }
        else {
            return new FieldSetter(bean, field);
        }
    }

    private void addArgument(final Setter setter, final Argument argument) {
        Handler handler = Handlers.create(new ArgumentDescriptor(setter.getName(), argument, setter.isMultiValued()), setter);
    	int index = argument.index();

        // Make sure the argument will fit in the list
    	while (index >= argumentHandlers.size()) {
            argumentHandlers.add(null);
    	}

        if (argumentHandlers.get(index) != null) {
            throw new IllegalAnnotationError("Duplicate argument index: " + index);
        }

        argumentHandlers.set(index, handler);
    }

    private void addOption(final Setter setter, final Option option) {
        Handler handler = Handlers.create(new OptionDescriptor(setter.getName(), option, setter.isMultiValued()), setter);
        checkOptionNotInMap(option.name());

        for (String alias : option.aliases()) {
            checkOptionNotInMap(alias);
        }

        optionHandlers.add(handler);

    }

    private void checkOptionNotInMap(final String name) throws IllegalAnnotationError {
        if (findOptionByName(name) != null) {
            throw new IllegalAnnotationError("Duplicate option name: " + name);
        }
    }

    //
    // Processing
    //
    
    private class ParametersImpl
        implements Parameters
    {
        private final String[] args;
        
        private int pos = 0;

        private Handler handler;

        public ParametersImpl(final String[] args) {
            assert args != null;
            
            this.args = args;
        }

        private boolean hasMore() {
            return pos < args.length;
        }

        private String current() {
            return args[pos];
        }

        private void skip(final int n) {
            pos += n;
        }

        private String getOptionName() {
            return handler.descriptor.toString();
        }

        public String get(final int idx) throws ProcessingException {
            if (pos + idx >= args.length) {
                throw new ProcessingException(Messages.MISSING_OPERAND.format(getOptionName()));
            }

            return args[pos + idx];
        }
    }

    public void process(final String... args) throws ProcessingException {
        ParametersImpl params = new ParametersImpl(args);
        Set<Handler> present = new HashSet<Handler>();
        int argIndex = 0;
        boolean processOptions = true;
        boolean requireOverride = false;

        //
        // TODO: Need to rewrite some of this to allow more posix-style argument processing, like --foo=bar and --foo bar, and -vvvv
        //
        
        while (params.hasMore()) {
            String arg = params.current();
            Handler handler;

            if (processOptions && arg.startsWith("-")) {
            	boolean isKeyValuePair = arg.indexOf('=') != -1;

                // parse this as an option.
                handler = isKeyValuePair ? findOptionHandler(arg) : findOptionByName(arg);

                if (handler == null) {
                    if (stopAtNonOption) {
                        // Slurp up the remaining bits as arguments (including the option we just looked at)
                        processOptions = false;
                        continue;
                    }
                    else {
                        // Unknown option, complain
                        throw new ProcessingException(Messages.UNDEFINED_OPTION.format(arg));
                    }
                }
                else if (isKeyValuePair){
                	// known option, but further processing is required in the handler.
                	handler.isKeyValuePair = isKeyValuePair;
                }
                else {
                    // known option; skip its name
                    params.skip(1);
                }
            }
            else {
                // Complain if we have more arguments than we have handlers configured
                if (argIndex >= argumentHandlers.size()) {
                    Messages msg = argumentHandlers.size() == 0 ? Messages.NO_ARGUMENT_ALLOWED : Messages.TOO_MANY_ARGUMENTS;
                    throw new ProcessingException(msg.format(arg));
            	}

            	// known argument
            	handler = argumentHandlers.get(argIndex);
            	if (!handler.descriptor.isMultiValued()) {
                    argIndex++;
                }
            }

            try {
                //Hook up the current handler to the params for error message rendering
                params.handler = handler;

                // If this is an option which overrides requirements track it
                if (!requireOverride && handler.descriptor instanceof OptionDescriptor) {
                    OptionDescriptor d = (OptionDescriptor) handler.descriptor;
                    requireOverride = d.isRequireOverride();
                }
                
                // Invoker the handler and then skip arguments which it has eatten up
                int consumed = handler.handle(params);
                params.skip(consumed);
            }
            catch (StopProcessingOptionsNotification n) {
                processOptions = false;
            }

            // Keep a list of the handlers which have been processed (for required validation below)
            present.add(handler);
        }
        
        // Ensure that all required option handlers are present, unless a processed option has overridden requirments
        if (!requireOverride) {
	        for (Handler handler : optionHandlers) {
	            if (handler.descriptor.isRequired() && !present.contains(handler)) {
                    //
                    // FIXME: This needs to be i18n aware
                    //

	                throw new ProcessingException(Messages.REQUIRED_OPTION_MISSING.format(handler.descriptor.toString()));
	            }
	        }

	        // Ensure that all required argument handlers are present
	        for (Handler handler : argumentHandlers) {
	            if (handler.descriptor.isRequired() && !present.contains(handler)) {
                    //
                    // FIXME: This needs to be i18n aware
                    //
                    
	                throw new ProcessingException(Messages.REQUIRED_ARGUMENT_MISSING.format(handler.descriptor.toString()));
	            }
	        }
        }
    }

    //
    // Option Handler Lookup
    //
    
    private Handler findOptionHandler(String name) {
        Handler handler = findOptionByName(name);

        if (handler == null) {
            // Have not found by its name, maybe its a property?
            // Search for parts of the name (=prefix) - most specific first 
            for (int i=name.length(); i>1; i--) {
                String prefix = name.substring(0, i);
                Map<String, Handler> possibleHandlers = filter(optionHandlers, prefix);
                handler = possibleHandlers.get(prefix);

                if (handler != null) {
                    return handler;
                }
            }
        }

        return handler;
    }

    private Map<String, Handler> filter(List<Handler> handlers, String keyFilter) {
        Map<String, Handler> map = new TreeMap<String, Handler>();

        for (Handler handler : handlers) {
        	if (keyFilter.contains("--")) {
        		for (String alias : ((OptionDescriptor)handler.descriptor).getAliases()) {
        			if (alias.startsWith(keyFilter)) {
        				map.put(alias, handler);
        			}
        		}
        	} else {
        		if (((OptionDescriptor)handler.descriptor).getName().startsWith(keyFilter)) {
                    map.put(((OptionDescriptor)handler.descriptor).getName(), handler);
                }
        	}
        }

        return map;
    }
    
    private Handler findOptionByName(String name) {
        for (Handler handler : optionHandlers) {
            OptionDescriptor option = (OptionDescriptor)handler.descriptor;

            if (name.equals(option.getName())) {
                return handler;
            }

            for (String alias : option.getAliases()) {
                if (name.equals(alias)) {
                    return handler;
                }
            }
        }

        return null;
    }
}
