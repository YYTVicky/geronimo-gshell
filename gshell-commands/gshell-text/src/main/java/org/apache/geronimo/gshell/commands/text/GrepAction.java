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

package org.apache.geronimo.gshell.commands.text;

import org.apache.commons.vfs.FileObject;
import org.apache.geronimo.gshell.clp.Argument;
import org.apache.geronimo.gshell.clp.Option;
import org.apache.geronimo.gshell.command.CommandAction;
import org.apache.geronimo.gshell.command.CommandContext;
import org.apache.geronimo.gshell.io.Closer;
import org.apache.geronimo.gshell.io.IO;
import org.apache.geronimo.gshell.vfs.FileObjects;
import org.apache.geronimo.gshell.vfs.support.VfsActionSupport;
import org.apache.oro.text.MatchAction;
import org.apache.oro.text.MatchActionInfo;
import org.apache.oro.text.MatchActionProcessor;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import java.io.BufferedInputStream;

/**
 * Displays lines matching a pattern.
 *
 * @version $Rev$ $Date$
 */
public class GrepAction
    extends VfsActionSupport
{
    /** Return value when matches are found. */
    public static final int FOUND = 0;

    /** Return value when no matches are found. */
    public static final int NOT_FOUND = 1;

    //
    // TODO: Add --pattern option (in addition to this argument) to allow patterns to start with "-"
    //

    @Argument(index=0, required=true)
    private String pattern;

    @Argument(index=1, required=false)
    private String path;

    @Option(name="-c", aliases={"--count"})
    private boolean count;

    @Option(name="-i", aliases={"--ignore-case"})
    private boolean ignoreCase;

    @Option(name="-n", aliases={"--line-number"})
    private boolean lineNumbers;

    @Option(name="-v", aliases={"--invert-match"})
    private boolean invertMatch;

    /** Tracks the number of matches. */
    private int matches = 0;

    public Object execute(final CommandContext context) throws Exception {
        assert context != null;
        final IO io = context.getIo();

        PatternCompiler compiler = new Perl5Compiler();
        InvertableMatcher matcher = new InvertableMatcher(new Perl5Matcher());
        MatchActionProcessor processor = new MatchActionProcessor(compiler, matcher);

        try {
            int options = Perl5Compiler.DEFAULT_MASK;

            if (ignoreCase) {
                options = Perl5Compiler.CASE_INSENSITIVE_MASK;
            }

            processor.addAction(pattern, options, new MatchAction() {
                public void processMatch(final MatchActionInfo info) {
                    matches++;

                    // Render output unless --count was configured
                    if (!count) {
                        StringBuilder buff = new StringBuilder();

                        if (lineNumbers) {
                            buff.append(info.lineNumber);
                            buff.append(":");
                        }

                        buff.append(info.line);

                        io.info(buff.toString());
                    }
                }
            });
        }
        catch (MalformedPatternException e) {
            io.error("Invalid pattern: " + e, e);
            return CommandAction.Result.FAILURE;
        }

        if (path != null) {
            FileObject file = resolveFile(context, path);

            try {
                grep(context, processor, file);
            }
            finally {
                FileObjects.close(file);
            }
        }
        else {
            processor.processMatches(context.getIo().inputStream, context.getIo().outputStream);
        }

        if (count) {
            io.info("{}", matches);
        }

        return matches != 0 ? FOUND: NOT_FOUND;
    }

    private void grep(final CommandContext context, final MatchActionProcessor processor, final FileObject file) throws Exception {
        assert context != null;
        assert processor != null;
        assert file != null;
        
        ensureFileExists(file);
        ensureFileHasContent(file);
        ensureFileIsReadable(file);

        BufferedInputStream input = new BufferedInputStream(file.getContent().getInputStream());
        try {
            processor.processMatches(input, context.getIo().outputStream);
        }
        finally {
            Closer.close(input);
        }
    }

    /**
     * Delegating {@link PatternMatcher} which allows the match/contains results to be
     * inverted based on the {@link GrepAction#invertMatch} field for --invert-match support.
     */
    private final class InvertableMatcher
        implements PatternMatcher
    {
        private final PatternMatcher delegate;

        public InvertableMatcher(final PatternMatcher delegate) {
            this.delegate = delegate;
        }

        public boolean matchesPrefix(char[] input, Pattern pattern, int offset) {
            boolean result = delegate.matchesPrefix(input, pattern, offset);
            return invertMatch ? !result : result;
        }

        public boolean matchesPrefix(String input, Pattern pattern) {
            boolean result = delegate.matchesPrefix(input, pattern);
            return invertMatch ? !result : result;
        }

        public boolean matchesPrefix(char[] input, Pattern pattern) {
            boolean result = delegate.matchesPrefix(input, pattern);
            return invertMatch ? !result : result;
        }

        public boolean matchesPrefix(PatternMatcherInput input, Pattern pattern) {
            boolean result = delegate.matchesPrefix(input, pattern);
            return invertMatch ? !result : result;
        }

        public boolean matches(String input, Pattern pattern) {
            boolean result = delegate.matches(input, pattern);
            return invertMatch ? !result : result;
        }

        public boolean matches(char[] input, Pattern pattern) {
            boolean result = delegate.matches(input, pattern);
            return invertMatch ? !result : result;
        }

        public boolean matches(PatternMatcherInput input, Pattern pattern) {
            boolean result = delegate.matches(input, pattern);
            return invertMatch ? !result : result;
        }

        public boolean contains(String input, Pattern pattern) {
            boolean result = delegate.contains(input, pattern);
            return invertMatch ? !result : result;
        }

        public boolean contains(char[] input, Pattern pattern) {
            boolean result = delegate.contains(input, pattern);
            return invertMatch ? !result : result;
        }

        public boolean contains(PatternMatcherInput input, Pattern pattern) {
            boolean result = delegate.contains(input, pattern);
            return invertMatch ? !result : result;
        }

        public MatchResult getMatch() {
            return delegate.getMatch();
        }
    }
}
