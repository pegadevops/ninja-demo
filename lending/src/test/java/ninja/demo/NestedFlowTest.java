/*
 * Copyright (c) 2008-2018
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
package ninja.demo;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import ru.lanit.bpm.common.junit.category.UnitTests;
import ru.lanit.bpm.ninja.unit.client.NinjaRemoteRunner;
import ru.lanit.bpm.ninja.unit.client.NinjaTestSupport;

import static ninja.demo.Demo.C_NINJA_DEMO;

/**
 * todo Document type NestedFlowTest
 */
@Category(UnitTests.class)
@RunWith(NinjaRemoteRunner.class)
public class NestedFlowTest extends NinjaTestSupport {

    public static final String G_PRIMARY_PAGE = "Ninja_UT";
    public static final String C_FLOW1ACTIVITY_EXECUTED = "Flow1Activity executed";
    public static final String C_FLOW2ACTIVITY_EXECUTED = "Flow2Activity executed";
    public static final String C_FLOW3ACTIVITY_EXECUTED = "Flow3Activity executed";
    public static final String C_FLOW2ACTIVITY_MOCKED = "Flow2Activity mocked";

    @Test
    public void flow1() {
        preparePage(G_PRIMARY_PAGE).create(C_NINJA_DEMO);
        prepareParameter("flowType").value("Flow1");

        invoke().activity().name("startFlow").primaryPage(G_PRIMARY_PAGE);

        assertPage(G_PRIMARY_PAGE).prop("pxPages(Flow1Activity).pyLabel", C_FLOW1ACTIVITY_EXECUTED);
        assertPage(G_PRIMARY_PAGE).prop("pxPages(Flow2Activity).pyLabel", C_FLOW2ACTIVITY_EXECUTED);
        assertPage(G_PRIMARY_PAGE).prop("pxPages(Flow3Activity).pyLabel", C_FLOW3ACTIVITY_EXECUTED);
    }

    @Test
    public void flow1_mockFlow2Activity() {
        preparePage(G_PRIMARY_PAGE).create(C_NINJA_DEMO);
        prepareParameter("flowType").value("Flow1");
        expect().activity().name("Flow2Activity").className(C_NINJA_DEMO).andMock(context -> {
            context.preparePrimaryPage().prop("pxPages(Flow2Activity).pyLabel", C_FLOW2ACTIVITY_MOCKED);
        });

        invoke().activity().name("startFlow").primaryPage(G_PRIMARY_PAGE);

        assertPage(G_PRIMARY_PAGE).prop("pxPages(Flow1Activity).pyLabel", C_FLOW1ACTIVITY_EXECUTED);
        assertPage(G_PRIMARY_PAGE).prop("pxPages(Flow2Activity).pyLabel", C_FLOW2ACTIVITY_MOCKED);
        assertPage(G_PRIMARY_PAGE).prop("pxPages(Flow3Activity).pyLabel", C_FLOW3ACTIVITY_EXECUTED);
    }

    @Test
    public void flow2() {
        preparePage(G_PRIMARY_PAGE).create(C_NINJA_DEMO);
        prepareParameter("flowType").value("Flow2");

        invoke().activity().name("startFlow").primaryPage(G_PRIMARY_PAGE);

        assertPage(G_PRIMARY_PAGE).prop("pxPages(Flow2Activity).pyLabel", C_FLOW2ACTIVITY_EXECUTED);
        assertPage(G_PRIMARY_PAGE).prop("pxPages(Flow3Activity).pyLabel", C_FLOW3ACTIVITY_EXECUTED);
    }
}
