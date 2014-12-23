package org.openscore.lang.api;
/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/


import org.openscore.lang.entities.CompilationArtifact;
import org.openscore.events.ScoreEventListener;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * User: stoneo
 * Date: 03/12/2014
 * Time: 11:28
 */

/**
 * API for using slang
 */
public interface Slang {

    /**
     * Compile a flow written in slang
     * @param file the path of the slang file containing the flow
     * @param dependencies a set of paths of the slang files of all the flow's dependencies
     * @return the compiled artifact of the flow
     */
    public CompilationArtifact compile(File file, Set<File> dependencies);

    /**
     * Compile an operation written in slang
     * @param file the path of the slang file containing the operation
     * @param operationName the name of the operation to compile from the file
     * @param dependencies a set of paths of the slang files of all the operation's dependencies
     * @return the compiled artifact of the operation
     */
    public CompilationArtifact compileOperation(File file, String operationName, Set<File> dependencies);

    /**
     * Run a flow/operation written in slang already compiled to a compilationArtifact
     * @param compilationArtifact the compiled artifact of the flow
     * @param runInputs the inputs for the flow/operation run
     * @return the execution ID in score
     */
    public Long run(CompilationArtifact compilationArtifact, Map<String, Serializable> runInputs);

    /**
     * Compile & run a flow written in slang
     * @param file the path of the slang file containing the flow
     * @param dependencies a set of paths of the slang files of all the flow's dependencies
     * @param runInputs the inputs for the flow run
     * @return the execution ID in score
     */
    public Long compileAndRun(File file, Set<File> dependencies, Map<String, Serializable> runInputs);

    /**
     * Compile & run an operation written in slang
     * @param file the path of the slang file containing the operation
     * @param operationName the name of the operation to compile from the file
     * @param dependencies a set of paths of the slang files of all the operation's dependencies
     * @param runInputs the inputs for the operation run
     * @return the execution ID in score
     */
    public Long compileAndRunOperation(File file, String operationName, Set<File> dependencies, Map<String, Serializable> runInputs);

    /**
     * Subscribe on events of score or slang
     * @param eventListener listener for the events
     * @param eventTypes set of types of events to subscribe to
     */
    public void subscribeOnEvents(ScoreEventListener eventListener, Set<String> eventTypes);

    /**
     * Subscribe on events of score or slang
     * @param eventListener listener for the events
     */
    public void unSubscribeOnEvents(ScoreEventListener eventListener);

    /**
     * Subscribe on all of the events of score & slang
     * @param eventListener listener for the events
     */
    public void subscribeOnAllEvents(ScoreEventListener eventListener);
}
