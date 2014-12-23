/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/

package org.openscore.lang.tests.operation.flows;

import com.google.common.collect.Sets;
import org.openscore.lang.entities.CompilationArtifact;
import org.openscore.lang.tests.operation.SystemsTestsParent;

import org.openscore.events.EventConstants;
import org.openscore.events.ScoreEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.*;

/**
 * Date: 12/4/2014
 *
 * @author Bonczidai Levente
 */
public class NavigationTest extends SystemsTestsParent {

    @Before
    public void before() {
        startTaskMonitoring();
    }

    @Test
    public void testComplexNavigationEvenNumber() throws Exception {

        URI resource = getClass().getResource("/yaml/flow_complex_navigation.yaml").toURI();
        URI operationsPython = getClass().getResource("/yaml/simple_operations.yaml").toURI();

        Set<File> path = Sets.newHashSet(new File(operationsPython));
        CompilationArtifact compilationArtifact = slang.compile(new File(resource), path);

        Map<String, Serializable> userInputs = new HashMap<>();
        userInputs.put("userNumber", 12);
        userInputs.put("emailHost", "emailHost");
        userInputs.put("emailPort", "25");
        userInputs.put("emailSender", "user@host.com");
        userInputs.put("emailRecipient", "user@host.com");
        ScoreEvent event = trigger(compilationArtifact, userInputs);
        Assert.assertEquals(EventConstants.SCORE_FINISHED_EVENT, event.getEventType());

        List<String> expectedTasks = new ArrayList<>();
        expectedTasks.add("check_number");
        expectedTasks.add("process_even_number");
        verifyTaskOrder(expectedTasks);
    }

    @Test
    public void testComplexNavigationOddNumber() throws Exception {

        URI resource = getClass().getResource("/yaml/flow_complex_navigation.yaml").toURI();
        URI operationsPython = getClass().getResource("/yaml/simple_operations.yaml").toURI();

        Set<File> path = Sets.newHashSet(new File(operationsPython));
        CompilationArtifact compilationArtifact = slang.compile(new File(resource), path);

        Map<String, Serializable> userInputs = new HashMap<>();
        userInputs.put("userNumber", 13);
        userInputs.put("emailHost", "emailHost");
        userInputs.put("emailPort", "25");
        userInputs.put("emailSender", "user@host.com");
        userInputs.put("emailRecipient", "user@host.com");
        ScoreEvent event = trigger(compilationArtifact, userInputs);
        Assert.assertEquals(EventConstants.SCORE_FINISHED_EVENT, event.getEventType());

        List<String> expectedTasks = new ArrayList<>();
        expectedTasks.add("check_number");
        expectedTasks.add("process_odd_number");
        verifyTaskOrder(expectedTasks);
    }

    @Test
    public void testComplexNavigationFailure() throws Exception {

        URI resource = getClass().getResource("/yaml/flow_complex_navigation.yaml").toURI();
        URI operationsPython = getClass().getResource("/yaml/simple_operations.yaml").toURI();

        Set<File> path = Sets.newHashSet(new File(operationsPython));
        CompilationArtifact compilationArtifact = slang.compile(new File(resource), path);

        Map<String, Serializable> userInputs = new HashMap<>();
        userInputs.put("userNumber", 1024);
        userInputs.put("emailHost", "emailHost");
        userInputs.put("emailPort", "25");
        userInputs.put("emailSender", "user@host.com");
        userInputs.put("emailRecipient", "user@host.com");
        ScoreEvent event = trigger(compilationArtifact, userInputs);
        Assert.assertEquals(EventConstants.SCORE_FINISHED_EVENT, event.getEventType());

        List<String> expectedTasks = new ArrayList<>();
        expectedTasks.add("check_number");
        expectedTasks.add("send_error_mail");
        verifyTaskOrder(expectedTasks);
    }

    @Test
    public void testDefaultSuccessNavigation() throws Exception {

        URI resource = getClass().getResource("/yaml/flow_default_navigation.yaml").toURI();
        URI operationsPython = getClass().getResource("/yaml/simple_operations.yaml").toURI();

        Set<File> path = Sets.newHashSet(new File(operationsPython));
        CompilationArtifact compilationArtifact = slang.compile(new File(resource), path);

        Map<String, Serializable> userInputs = new HashMap<>();
        userInputs.put("navigationType", "success");
        userInputs.put("emailHost", "emailHost");
        userInputs.put("emailPort", "25");
        userInputs.put("emailSender", "user@host.com");
        userInputs.put("emailRecipient", "user@host.com");
        ScoreEvent event = trigger(compilationArtifact, userInputs);
        Assert.assertEquals(EventConstants.SCORE_FINISHED_EVENT, event.getEventType());

        List<String> expectedTasks = new ArrayList<>();
        expectedTasks.add("produce_default_navigation");
        expectedTasks.add("check_Weather");
        verifyTaskOrder(expectedTasks);
    }

    @Test
    public void testDefaultOnFailureNavigation() throws Exception {

        URI resource = getClass().getResource("/yaml/flow_default_navigation.yaml").toURI();
        URI operationsPython = getClass().getResource("/yaml/simple_operations.yaml").toURI();

        Set<File> path = Sets.newHashSet(new File(operationsPython));
        CompilationArtifact compilationArtifact = slang.compile(new File(resource), path);

        Map<String, Serializable> userInputs = new HashMap<>();
        userInputs.put("navigationType", "failure");
        userInputs.put("emailHost", "emailHost");
        userInputs.put("emailPort", "25");
        userInputs.put("emailSender", "user@host.com");
        userInputs.put("emailRecipient", "user@host.com");
        ScoreEvent event = trigger(compilationArtifact, userInputs);
        Assert.assertEquals(EventConstants.SCORE_FINISHED_EVENT, event.getEventType());

        List<String> expectedTasks = new ArrayList<>();
        expectedTasks.add("produce_default_navigation");
        expectedTasks.add("send_error_mail");
        verifyTaskOrder(expectedTasks);
    }
}