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
package ojxu4t.orderful.work.orderFulfillment;

import ojxu4t.orderful.OrderFulfillmentTestSupport;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import ru.lanit.bpm.common.junit.category.UnitTests;
import ru.lanit.bpm.ninja.unit.client.NinjaRemoteRunner;
import ru.lanit.bpm.ninja.unit.client.cp.PreparePage;

import static ojxu4t.orderful.data.bandwidth.Bandwidth.C_OJXU4T_ORDERFUL_DATA_BANDWIDTH;
import static ojxu4t.orderful.data.location.Location.C_OJXU4T_ORDERFUL_DATA_LOCATION;
import static ojxu4t.orderful.data.project.Project.C_OJXU4_T_ORDER_FUL_DATA_PROJECT;
import static ojxu4t.orderful.work.orderFulfillment.OrderFulfillment.*;
import static ru.lanit.bpm.ninja.pega.constants.PegaConstants.P_PY_ID;
import static ru.lanit.bpm.ninja.pega.constants.PegaConstants.P_PY_LABEL;

/**
 * todo Document type ValidateOrderDetails_Validate
 */
@Category(UnitTests.class)
@RunWith(NinjaRemoteRunner.class)
public class ValidateOrderDetails_Validate extends OrderFulfillmentTestSupport {

    public static final String G_PRIMARY_PAGE = "Ninja_UT";
    public static final String R_VALIDATE_ORDER_DETAILS = "ValidateOrderDetails";

    @Test
    public void valid() {
        preparePrimaryPage();
        setValidProject("My Project Id", "My Project Label");
        setValidBandwidth("My Bandwidth Id", "My Bandwidth Label");
        appendLocation();

        invokeRule();

        assertPage(G_PRIMARY_PAGE).messages().size(0);
    }

    @Test
    public void locations_empty() {
        preparePrimaryPage();
        setValidProject("some id", "some label");
        setValidBandwidth("2mbs-10mbs", "some label");

        invokeRule();

        assertPage(G_PRIMARY_PAGE).messages().string().messageEquals(".pxListSize: Please add at least one location");
    }

    @Test
    public void project_blank() {
        preparePrimaryPage().prop(".ProjectInfo.pyID", "");
        setValidBandwidth("bla-bla-bla", "Some label");
        appendLocation();

        invokeRule();

        assertPage(G_PRIMARY_PAGE).messages().string().messageContains("ProjectId: This field may not be blank.")
                .messageContains("Please select project from the list");
    }

    @Test
    public void project_modifiedLabel() {
        preparePrimaryPage();
        setValidProject("VPN4Acme", "VPN for ACME Corporation");
        preparePage(G_PRIMARY_PAGE).prop(".ProjectInfo.pyLabel", "Custom label");
        setValidBandwidth("2mbs-2mbs", "Upstream: 2mbs, downstream: 2mbs");
        appendLocation();

        invokeRule();

        assertPage(G_PRIMARY_PAGE).messages().string().messageEquals(".ProjectId: Please select project from the list");
    }

    @Test
    public void bandwidth_blank() {
        preparePrimaryPage().prop(".BandwidthInfo.pyID", "");
        setValidProject("My proj", "My proj label");
        appendLocation();

        invokeRule();

        assertPage(G_PRIMARY_PAGE).messages().string().messageContains(".Bandwidth: This field may not be blank.")
                .messageContains("Please select bandwidth from the list");
    }

    @Test
    public void bandwidth_modifiedLabel() {
        preparePrimaryPage();
        setValidProject("Proj-Id", "Proj Label");
        setValidBandwidth("Band-id", "Bandwidth description");
        preparePage(G_PRIMARY_PAGE).prop(".BandwidthInfo.pyLabel", "Custom label");
        appendLocation();

        invokeRule();

        assertPage(G_PRIMARY_PAGE).messages().string().messageEquals(".Bandwidth: Please select bandwidth from the list");
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void invokeRule() {
        invoke().validate().name(R_VALIDATE_ORDER_DETAILS).primaryPage(G_PRIMARY_PAGE);
    }

    private PreparePage preparePrimaryPage() {
        return preparePage(G_PRIMARY_PAGE).create(C_OJXU4T_ORDERFUL_WORK_ORDERFULFILLMENT);
    }

    private void appendLocation() {
        preparePage(G_PRIMARY_PAGE).pageList(P_LOCATIONS).append(C_OJXU4T_ORDERFUL_DATA_LOCATION);
    }

    private void setValidProject(final String pyId, final String pyLabel) {
        preparePage(G_PRIMARY_PAGE).page(P_PROJECT_INFO).create(C_OJXU4_T_ORDER_FUL_DATA_PROJECT).prop(P_PY_ID, pyId).prop(P_PY_LABEL, pyLabel);
        expectDictLookup(C_OJXU4_T_ORDER_FUL_DATA_PROJECT, pyId, pyLabel);
    }

    private void setValidBandwidth(String pyId, String pyLabel) {
        preparePage(G_PRIMARY_PAGE).page(P_BANDWIDTH_INFO).create(C_OJXU4T_ORDERFUL_DATA_BANDWIDTH).prop(P_PY_ID, pyId).prop(P_PY_LABEL, pyLabel);
        expectDictLookup(C_OJXU4T_ORDERFUL_DATA_BANDWIDTH, pyId, pyLabel);
    }
}
