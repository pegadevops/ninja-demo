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
 * todo Document type NestedActivityTest
 */
@Category(UnitTests.class)
@RunWith(NinjaRemoteRunner.class)
public class NestedActivityTest extends NinjaTestSupport {

    public static final String G_PRIMARY_PAGE = "Ninja_UT";
    public static final String C_ACTIVITY1A_MOCK = "Activity1aMock";
    public static final String C_ACTIVITY1B_MOCK = "Activity1bMock";
    public static final String C_PARAM1A_ACTIVITY2 = "Param1a_Activity2";
    public static final String C_PARAM1_ACTIVITY1A_ACTIVITY2 = "Param1_Activity1a_Activity2";
    public static final String C_PARAM1 = "Param1";
    public static final String C_PARAM2 = "Param2";
    public static final String C_PARAM1_ACTIVITY_1A = "Param1_Activity1a";
    public static final String C_ACTIVITY2_MOCK = "Activity2Mock";
    public static final String C_PARAM1A = "Param1a";
    public static final String C_UT_PARAM1 = "UT_Param1";

    @Test
    public void activity1() {
        preparePage(G_PRIMARY_PAGE).create(C_NINJA_DEMO);

        invoke().activity().primaryPage(G_PRIMARY_PAGE).name("Activity1");

        assertPage(G_PRIMARY_PAGE).prop("pxPages(param1).pyLabel", C_PARAM1);
        assertPage(G_PRIMARY_PAGE).prop("pxPages(param1a).pyLabel", C_PARAM1A_ACTIVITY2);
        assertPage(G_PRIMARY_PAGE).prop("pxPages(param2).pyLabel", C_PARAM2);
    }

    @Test
    public void activity1_mockActivity1b() {
        preparePage(G_PRIMARY_PAGE).create(C_NINJA_DEMO);
        expect().activity().className(C_NINJA_DEMO).name("Activity1b").andMock();

        invoke().activity().primaryPage(G_PRIMARY_PAGE).name("Activity1");

        assertPage(G_PRIMARY_PAGE).prop("pxPages(param1).pyLabel", C_PARAM1_ACTIVITY1A_ACTIVITY2);
        assertPage(G_PRIMARY_PAGE).prop("pxPages(param1a).pyLabel", C_PARAM1A_ACTIVITY2);
        assertPage(G_PRIMARY_PAGE).prop("pxPages(param2).pyLabel", C_PARAM2);
    }


    @Test
    public void activity1_isolated() {
        preparePage(G_PRIMARY_PAGE).create(C_NINJA_DEMO);
        expect().activity().className(C_NINJA_DEMO).name("Activity1a").andMock(context -> {
            context.assertParameter("param1").value(C_PARAM1);
            context.preparePrimaryPage().prop("pxPages(param1).pyLabel", C_ACTIVITY1A_MOCK);
        });
        expect().activity().className(C_NINJA_DEMO).name("Activity1b").andMock(context -> {
            context.assertParameter("param1").value(C_PARAM1);
            context.preparePrimaryPage().prop("pxPages(param2).pyLabel", C_ACTIVITY1B_MOCK);

        });

        invoke().activity().primaryPage(G_PRIMARY_PAGE).name("Activity1");

        assertPage(G_PRIMARY_PAGE).prop("pxPages(param1).pyLabel", C_ACTIVITY1A_MOCK);
        assertPage(G_PRIMARY_PAGE).prop("pxPages(param2).pyLabel", C_ACTIVITY1B_MOCK);
    }

    @Test
    public void activity1_mockActivity2() {
        preparePage(G_PRIMARY_PAGE).create(C_NINJA_DEMO);
        expect().activity().className(C_NINJA_DEMO).name("Activity2").andMock(context -> {
            context.assertParameter("param1").value(C_PARAM1_ACTIVITY_1A);
            context.assertParameter("param1a").value("Param1a");
            context.preparePrimaryPage().prop("pxPages(Activity2).pyLabel", C_ACTIVITY2_MOCK);
            invoke().activity().primaryPage(G_PRIMARY_PAGE).name("Activity3");
        });

        invoke().activity().primaryPage(G_PRIMARY_PAGE).name("Activity1");

        assertPage(G_PRIMARY_PAGE).prop("pxPages(param1).pyLabel", C_PARAM1);
        assertPage(G_PRIMARY_PAGE).prop("pxPages(param1a).pyLabel", C_PARAM1A);
        assertPage(G_PRIMARY_PAGE).prop("pxPages(param2).pyLabel", "");
        assertPage(G_PRIMARY_PAGE).prop("pxPages(Activity2).pyLabel", C_ACTIVITY2_MOCK);
    }

    @Test
    public void activity1a() {
        preparePage(G_PRIMARY_PAGE).create(C_NINJA_DEMO);
        prepareParameter("param1").value(C_UT_PARAM1);

        invoke().activity().primaryPage(G_PRIMARY_PAGE).name("Activity1a");

        assertPage(G_PRIMARY_PAGE).prop("pxPages(param1).pyLabel", C_UT_PARAM1 + "_Activity1a_Activity2");
        assertPage(G_PRIMARY_PAGE).prop("pxPages(param1a).pyLabel", C_PARAM1A_ACTIVITY2);
        assertPage(G_PRIMARY_PAGE).prop("pxPages(param2).pyLabel", C_PARAM2);
    }
}
