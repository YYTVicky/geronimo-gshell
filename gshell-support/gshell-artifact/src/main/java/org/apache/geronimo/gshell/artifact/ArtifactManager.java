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

package org.apache.geronimo.gshell.artifact;

import org.apache.maven.artifact.InvalidRepositoryException;
import org.apache.maven.artifact.UnknownRepositoryLayoutException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.wagon.events.TransferListener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

/**
 * Provides a facade over the artifact + repository subsystem.
 *
 * @version $Rev$ $Date$
 */
public interface ArtifactManager
{
    ArtifactRepository getLocalRepository();

    void setLocalRepository(ArtifactRepository repository);

    void setLocalRepository(File dir) throws MalformedURLException, InvalidRepositoryException;

    List<ArtifactRepository> getRemoteRepositories();

    void addRemoteRepository(ArtifactRepository repository);

    void addRemoteRepository(String id, URI location) throws MalformedURLException, UnknownRepositoryLayoutException;

    ArtifactFactory getArtifactFactory();

    void setDownloadMonitor(TransferListener listener);

    ArtifactResolutionResult resolve(ArtifactResolutionRequest request);
}