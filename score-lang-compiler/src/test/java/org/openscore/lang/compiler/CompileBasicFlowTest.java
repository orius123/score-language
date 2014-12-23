/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/

package org.openscore.lang.compiler;

import org.openscore.lang.compiler.configuration.SlangCompilerSpringConfig;
import org.openscore.lang.entities.CompilationArtifact;
import org.openscore.lang.entities.ResultNavigation;
import org.openscore.lang.entities.bindings.Input;
import org.openscore.lang.entities.bindings.Output;
import org.openscore.lang.entities.bindings.Result;
import org.openscore.api.ExecutionPlan;
import org.openscore.api.ExecutionStep;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.openscore.lang.entities.ScoreLangConstants;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*
 * Created by orius123 on 05/11/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SlangCompilerSpringConfig.class)
public class CompileBasicFlowTest {

    @Autowired
    private SlangCompiler compiler;

    @Test
    public void testCompileFlowBasic() throws Exception {
        URI flow = getClass().getResource("/flow.yaml").toURI();
        URI operation = getClass().getResource("/operation.yaml").toURI();

        Set<File> path = new HashSet<>();
        path.add(new File(operation));

        CompilationArtifact compilationArtifact = compiler.compileFlow(new File(flow), path);
        ExecutionPlan executionPlan = compilationArtifact.getExecutionPlan();
        Assert.assertNotNull("execution plan is null", executionPlan);
        Assert.assertEquals("there is a different number of steps than expected", 4, executionPlan.getSteps().size());
        Assert.assertEquals("execution plan name is different than expected", "basic_flow", executionPlan.getName());
        Assert.assertEquals("the dependencies size is not as expected", 1, compilationArtifact.getDependencies().size());
        Assert.assertEquals("the inputs size is not as expected", 1, compilationArtifact.getInputs().size());
    }

    @Test
    public void testCompileFlowWithData() throws Exception {
        URI flow = getClass().getResource("/flow_with_data.yaml").toURI();
        URI operation = getClass().getResource("/operation.yaml").toURI();

        Set<File> path = new HashSet<>();
        path.add(new File(operation));

        CompilationArtifact compilationArtifact = compiler.compileFlow(new File(flow), path);
        ExecutionPlan executionPlan = compilationArtifact.getExecutionPlan();
        Assert.assertNotNull("execution plan is null", executionPlan);
        Assert.assertEquals("there is a different number of steps than expected", 4, executionPlan.getSteps().size());
        Assert.assertEquals("execution plan name is different than expected", "SimpleFlow", executionPlan.getName());
        Assert.assertEquals("the dependencies size is not as expected", 1, compilationArtifact.getDependencies().size());

        ExecutionStep startStep = executionPlan.getStep(1L);
        @SuppressWarnings("unchecked") List<Input> inputs = (List<Input>) startStep.getActionData().get(ScoreLangConstants.OPERATION_INPUTS_KEY);
        Assert.assertNotNull("inputs doesn't exist", inputs);
        Assert.assertEquals("there is a different number of inputs than expected", 1, inputs.size());

        ExecutionStep beginTaskStep = executionPlan.getStep(2L);
        @SuppressWarnings("unchecked") List<Input> taskArguments = (List<Input>) beginTaskStep.getActionData().get(ScoreLangConstants.TASK_INPUTS_KEY);
        Assert.assertNotNull("arguments doesn't exist", taskArguments);
        Assert.assertEquals("there is a different number of arguments than expected", 2, taskArguments.size());
        Assert.assertEquals("city", taskArguments.get(0).getName());
        Assert.assertEquals("country", taskArguments.get(1).getName());
        Assert.assertEquals("CheckWeather", beginTaskStep.getActionData().get(ScoreLangConstants.NODE_NAME_KEY));

        ExecutionStep FinishTaskSteps = executionPlan.getStep(3L);
        @SuppressWarnings("unchecked") List<Output> publish = (List<Output>) FinishTaskSteps.getActionData().get(ScoreLangConstants.TASK_PUBLISH_KEY);
        @SuppressWarnings("unchecked") Map<String, ResultNavigation> navigate =
                (Map<String, ResultNavigation>) FinishTaskSteps.getActionData().get(ScoreLangConstants.TASK_NAVIGATION_KEY);
        Assert.assertEquals("CheckWeather", FinishTaskSteps.getActionData().get(ScoreLangConstants.NODE_NAME_KEY));

        Assert.assertNotNull("publish don't exist", publish);
        Assert.assertEquals("there is a different number of publish values than expected", 1, publish.size());
        Assert.assertNotNull("navigate don't exist", navigate);
        Assert.assertEquals("last step default success should go to flow success",
                ScoreLangConstants.SUCCESS_RESULT, navigate.get(ScoreLangConstants.SUCCESS_RESULT).getPresetResult());
        Assert.assertEquals("last step default failure should go to flow failure",
                ScoreLangConstants.FAILURE_RESULT, navigate.get(ScoreLangConstants.FAILURE_RESULT).getPresetResult());
        Assert.assertEquals("there is a different number of navigation values than expected", 2, navigate.size());


        ExecutionStep endStep = executionPlan.getStep(0L);
        @SuppressWarnings("unchecked") List<Output> outputs = (List<Output>) endStep.getActionData().get(ScoreLangConstants.EXECUTABLE_OUTPUTS_KEY);
        Assert.assertNotNull("outputs don't exist", outputs);
        Assert.assertEquals("there is a different number of outputs than expected", 1, outputs.size());

        @SuppressWarnings("unchecked") List<Result> results = (List<Result>) endStep.getActionData().get(ScoreLangConstants.EXECUTABLE_RESULTS_KEY);
        Assert.assertNotNull("results don't exist", results);
        Assert.assertEquals("there is a different number of results values than expected", 2, results.size());
    }

}