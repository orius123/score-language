package org.openscore.lang.cli.utils;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.openscore.lang.api.Slang;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

import static org.mockito.Mockito.mock;

/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CompilerHelperTest.Config.class)
public class CompilerHelperTest {

    @Autowired
    private CompilerHelper compilerHelper;

    @Autowired
    private Slang slang;

    @Test(expected = IllegalArgumentException.class)
    public void testFilePathWrong() throws Exception {
        compilerHelper.compile(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilePathNotFile() throws Exception {
        compilerHelper.compile("xxx", null, null);
    }

    @Before
    public void resetMocks() {
        Mockito.reset(slang);
    }

    @Test
    public void testFilePathValid() throws Exception {
        String flowFilePath = getClass().getResource("/flow.yaml").getPath();
        String opFilePath = getClass().getResource("/operation.yaml").getPath();
        compilerHelper.compile(flowFilePath, null, null);
        Mockito.verify(slang).compile(new File(flowFilePath), Sets.newHashSet(new File(flowFilePath), new File(opFilePath)));
    }

    @Test
    public void testFilePathValidWithOtherPathForDependencies() throws Exception {
        String flowFilePath = getClass().getResource("/flow.yaml").getPath();
        String folderPath = getClass().getResource("/flowsdir/").getPath();
        String flow2FilePath = getClass().getResource("/flowsdir/flow2.yaml").getPath();
        compilerHelper.compile(flowFilePath, null, Lists.newArrayList(folderPath));
        Mockito.verify(slang).compile(new File(flowFilePath), Sets.newHashSet(new File(flow2FilePath)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDirPathForDependencies() throws Exception {
        String flowFilePath = getClass().getResource("/flow.yaml").getPath();
        String invalidDirPath = getClass().getResource("").getPath().concat("xxx");
        compilerHelper.compile(flowFilePath, null, Lists.newArrayList(invalidDirPath));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDirPathForDependencies2() throws Exception {
        String flowFilePath = getClass().getResource("/flow.yaml").getPath();
        compilerHelper.compile(flowFilePath, null, Lists.newArrayList(flowFilePath));
    }


    @Configuration
    static class Config {

        @Bean
        public Slang slang() {
            return mock(Slang.class);
        }

        @Bean
        public CompilerHelper compilerHelper() {
            return new CompilerHelperImpl();
        }

    }

}