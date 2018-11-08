/*
 * Copyright (c) 2008-2017
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
package ninja.demo.creditbureau;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import ru.lanit.bpm.common.junit.category.UnitTests;
import ru.lanit.bpm.ninja.unit.client.NinjaRemoteRunner;
import ru.lanit.bpm.ninja.unit.client.NinjaTestSupport;
import ru.lanit.bpm.ninja.unit.client.r.MockBehaviour;
import ru.lanit.bpm.ninja.unit.client.r.db.MockObjSaveContext;

/**
 * Tests LogRequestToCreditBureau activity
 */
@Category(UnitTests.class)
@RunWith(NinjaRemoteRunner.class)
public class LogRequestToCreditBureau_Activity extends NinjaTestSupport {

    public static final String P_PY_WORK_PAGE = "pyWorkPage";
    public static final String C_NINJA_DEMO_CREDIT_BUREAU = "Ninja-Demo-CreditBureau";
    public static final String PROP_PY_ID = "pyID";
    public static final String PROP_PY_LABEL = "pyLabel";
    public static final String C_NINJA_DEMO_AUDIT = "Ninja-Demo-Audit";
    public static final String P_LOG_ITEM = "LogItem";
    public static final String PROP_PZ_INS_KEY = "pzInsKey";
    public static final String PROP_PX_COVER_INS_KEY = "pxCoverInsKey";
    public static final String P_PY_COVER_PAGE = "pyCoverPage";
    public static final String C_WORK = "Work-";
    public static final String R_LOG_REQUEST_TO_CREDIT_BUREAU = "LogRequestToCreditBureau";
    public static final String PROP_APPLICANT_FIRST_NAME = ".pyWorkParty(Applicant).pyFirstName";
    public static final String PROP_APPLICANT_LAST_NAME = ".pyWorkParty(Applicant).pyLastName";
    public static final String PROP_PY_FIRST_NAME = "pyFirstName";
    public static final String PROP_PY_LAST_NAME = "pyLastName";

    @Test
    public void topLevelCase() throws Exception {
        String pyId = "TopLevelCase-1";
        String pyLabel = "Top Level Case label";
        String firstName = "Alex";
        String lastName = "Black";
        // initialize page for activity invocation
        preparePage(P_PY_WORK_PAGE).create(C_NINJA_DEMO_CREDIT_BUREAU).prop(PROP_PY_ID, pyId).prop(PROP_PY_LABEL, pyLabel)
                .prop(PROP_APPLICANT_FIRST_NAME, firstName).prop(PROP_APPLICANT_LAST_NAME, lastName);
        // mock Obj-Save
        mockSavingLogItem(pyId, pyLabel, firstName, lastName);

        // invoke activity
        invoke().activity().className(C_NINJA_DEMO_CREDIT_BUREAU).name(R_LOG_REQUEST_TO_CREDIT_BUREAU).primaryPage(P_PY_WORK_PAGE);

        //assert results
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void coveredCase() throws Exception {
        final String pyId = "CoveredCase-1";
        final String pyLabel = "Covered Case label";
        String firstName = "Mike";
        String lastName = "Brown";
        // initialize page for activity invocation
        preparePage(P_PY_WORK_PAGE).create(C_NINJA_DEMO_CREDIT_BUREAU).prop(PROP_PX_COVER_INS_KEY, "TLC-1")
                .prop(PROP_APPLICANT_FIRST_NAME, firstName).prop(PROP_APPLICANT_LAST_NAME, lastName);;
        preparePage(P_PY_COVER_PAGE).create(C_WORK).prop(PROP_PY_ID, pyId).prop(PROP_PY_LABEL, pyLabel);
        // mock Obj-Save
        mockSavingLogItem(pyId, pyLabel, firstName, lastName);

        // invoke activity
        invoke().activity().className(C_NINJA_DEMO_CREDIT_BUREAU).name(R_LOG_REQUEST_TO_CREDIT_BUREAU).primaryPage(P_PY_WORK_PAGE);

        //assert results
        assertActivityStatus().good().messageAny();
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void mockSavingLogItem(String pyID, String pyLabel, String firstName, String lastName) throws Exception {
        expect().objSave().className(C_NINJA_DEMO_AUDIT).page(P_LOG_ITEM).writeNow(true).andMock(new MockBehaviour<MockObjSaveContext>() {
            @Override
            public void process(MockObjSaveContext mockObjSaveContext) {
                assertPage(P_LOG_ITEM).prop(PROP_PY_ID, pyID);
                assertPage(P_LOG_ITEM).prop(PROP_PY_LABEL, pyLabel);
                assertPage(P_LOG_ITEM).prop(PROP_PY_FIRST_NAME, firstName);
                assertPage(P_LOG_ITEM).prop(PROP_PY_LAST_NAME, lastName);
                preparePage(P_LOG_ITEM).prop(PROP_PZ_INS_KEY, String.format("%s %s", C_NINJA_DEMO_AUDIT, pyID));
            }
        });
    }
}
