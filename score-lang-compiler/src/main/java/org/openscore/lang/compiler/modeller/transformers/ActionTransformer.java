package org.openscore.lang.compiler.modeller.transformers;
/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/


/*
 * Created by orius123 on 05/11/14.
 */

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ActionTransformer implements Transformer<Map<String, Object>, String> {

    @Override
    public String transform(Map<String, Object> rawData) {
        return "";
    }

    @Override
    public List<Scope> getScopes() {
        return Arrays.asList();
    }

    @Override
    public String keyToTransform() {
        return null;
    }


}
