/*
 * Copyright (c) 2008-2016
 * LANIT
 * All rights reserved.
 *
 * This product and related documentation are protected by copyright and
 * distributed under licenses restricting its use, copying, distribution, and
 * decompilation. No part of this product or related documentation may be
 * reproduced in any form by any means without prior written authorization of
 * LANIT and its licensors, if any.
 *
 * $Id$
 */
package com.pegadevops.ninja.demo;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import ru.lanit.bpm.common.junit.category.UnitTests;
import ru.lanit.bpm.ninja.unit.client.NinjaRemoteRunner;
import ru.lanit.bpm.ninja.unit.client.NinjaTestSupport;

@Category(UnitTests.class)
@RunWith(NinjaRemoteRunner.class)
public class HelloWorldTest extends NinjaTestSupport {
    @Test
    public void helloWorld() throws Exception {
        prepareParameter("param1").value("Hello World!");
        assertParameter("param1").value("Hello World!");
    }
}