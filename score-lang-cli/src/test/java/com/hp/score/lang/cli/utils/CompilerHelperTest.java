package com.hp.score.lang.cli.utils;


import com.google.common.collect.Lists;
import com.hp.score.lang.compiler.SlangCompiler;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

/*
 * Licensed to Hewlett-Packard Development Company, L.P. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CompilerHelperTest.Config.class)
public class CompilerHelperTest {

    @Autowired
    private CompilerHelper compilerHelper;

    @Autowired
    private SlangCompiler slangCompiler;

    @Test(expected = IllegalArgumentException.class)
    public void testFilePathWrong() throws Exception {
        compilerHelper.compile(null,null,null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilePathNotFile() throws Exception {
        compilerHelper.compile("xxx",null,null);
    }

    @Test
    public void testFilePathValid() throws Exception {
        String flowFilePath = getClass().getResource("/flow.yaml").getPath();
        String opFilePath = getClass().getResource("/operation.yaml").getPath();
        compilerHelper.compile(flowFilePath,null,null);
        Mockito.verify(slangCompiler,times(1)).compile(new File(flowFilePath),null, Lists.newArrayList(new File(flowFilePath),new File(opFilePath)));
    }

    @Test
     public void testFilePathValidWithOtherPathForDepdencies() throws Exception {
        String flowFilePath = getClass().getResource("/flow.yaml").getPath();
        String folderPath = getClass().getResource("/flowsdir/").getPath();
        String flow2FilePath = getClass().getResource("/flowsdir/flow2.yaml").getPath();
        compilerHelper.compile(flowFilePath,null,folderPath);
        Mockito.verify(slangCompiler,times(1)).compile(new File(flowFilePath),null, Lists.newArrayList(new File(flow2FilePath)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDirPathForDepdencies() throws Exception {
        String flowFilePath = getClass().getResource("/flow.yaml").getPath();
        String invalidDirPath = getClass().getResource("").getPath().concat("xxx");
        compilerHelper.compile(flowFilePath,null,invalidDirPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDirPathForDepdencies2() throws Exception {
        String flowFilePath = getClass().getResource("/flow.yaml").getPath();
        compilerHelper.compile(flowFilePath,null,flowFilePath);
    }


    @Configuration
    static class Config{

        @Bean
        public SlangCompiler compiler(){
            return mock(SlangCompiler.class);
        }

        @Bean
        public CompilerHelper compilerHelper(){
            return new CompilerHelper();
        }

    }



}