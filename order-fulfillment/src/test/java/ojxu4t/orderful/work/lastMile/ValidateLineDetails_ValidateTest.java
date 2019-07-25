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
package ojxu4t.orderful.work.lastMile;

import ojxu4t.orderful.OrderFulfillmentTestSupport;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import ru.lanit.bpm.common.junit.category.UnitTests;
import ru.lanit.bpm.ninja.unit.client.NinjaRemoteRunner;
import ru.lanit.bpm.ninja.unit.client.cp.PreparePage;

import static ojxu4t.orderful.data.lineDetails.LineDetails.C_OJXU4T_ORDERFUL_DATA_LINE_DETAILS;
import static ojxu4t.orderful.work.lastMile.LastMile.C_OJXU4T_ORDERFUL_WORK_LAST_MILE;
import static ru.lanit.bpm.ninja.common.utils.PegaConstants.P_PY_ID;
import static ru.lanit.bpm.ninja.common.utils.PegaConstants.P_PY_LABEL;

/**
 * todo Document type ValidateLineDetails_ValidateTest
 */
@Category(UnitTests.class)
@RunWith(NinjaRemoteRunner.class)
public class ValidateLineDetails_ValidateTest extends OrderFulfillmentTestSupport {

    public static final String G_PRIMARY_PAGE = "Ninja_UT";
    public static final String R_VALIDATE_LINE_DETAILS = "ValidateLineDetails";

    @Test
    public void valid() {
        preparePrimaryPage();
        final String pyId = "Line-id";
        final String pyLabel = "Line Label";
        setValidLineDetails(pyId, pyLabel);

        invokeRule();

        assertPage(G_PRIMARY_PAGE).messages().size(0);
    }

    @Test
    public void blank() {
        preparePrimaryPage().prop(".LineDetails.pyID", "");

        invokeRule();

        assertPage(G_PRIMARY_PAGE).messages().string().messageContains(".LineId: This field may not be blank")
                .messageContains("Please select line type from the list");
    }

    @Test
    public void modifiedLabel() {
        preparePrimaryPage();
        setValidLineDetails("MyLineId", "My Line Desc");
        preparePage(G_PRIMARY_PAGE).prop(".LineDetails.pyLabel", "Custom Label");

        invokeRule();

        assertPage(G_PRIMARY_PAGE).messages().string().messageEquals(".LineId: Please select line type from the list");
    }


    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================


    private PreparePage preparePrimaryPage() {
        return preparePage(G_PRIMARY_PAGE).create(C_OJXU4T_ORDERFUL_WORK_LAST_MILE);
    }

    private void invokeRule() {
        invoke().validate().name(R_VALIDATE_LINE_DETAILS).primaryPage(G_PRIMARY_PAGE);
    }

    private void setValidLineDetails(String pyId, String pyLabel) {
        preparePage(G_PRIMARY_PAGE).page("LineDetails").create(C_OJXU4T_ORDERFUL_DATA_LINE_DETAILS)
                .prop(P_PY_ID, pyId)
                .prop(P_PY_LABEL, pyLabel);
        expectDictLookup(C_OJXU4T_ORDERFUL_DATA_LINE_DETAILS, pyId, pyLabel);
    }
}
