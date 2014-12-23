/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/

package org.openscore.lang.tests.operation;

import com.google.common.collect.Sets;
import org.openscore.lang.entities.CompilationArtifact;
import org.openscore.events.EventConstants;
import org.openscore.events.ScoreEvent;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * Created by orius123 on 12/11/14.
 */
public class SubFlowSystemTests extends SystemsTestsParent {

    @Test
    public void testCompileAndRunSubFlowBasic() throws Exception {
        URI resource = getClass().getResource("/yaml/sub-flow/parent_flow.yaml").toURI();
        URI subFlow = getClass().getResource("/yaml/sub-flow/child_flow.yaml").toURI();
        URI operations = getClass().getResource("/yaml/simple_operations.yaml").toURI();

        Set<File> path = Sets.newHashSet(new File(subFlow), new File(operations));
        CompilationArtifact compilationArtifact = slang.compile(new File(resource), path);

        Map<String, Serializable> userInputs = new HashMap<>();
        userInputs.put("input1", "value1");
        ScoreEvent event = trigger(compilationArtifact, userInputs);
        Assert.assertEquals(EventConstants.SCORE_FINISHED_EVENT, event.getEventType());
    }
}
