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

import ojxu4t.intt.lastMile.quoteService.QuoteService;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import ru.lanit.bpm.common.junit.category.UnitTests;
import ru.lanit.bpm.ninja.unit.client.NinjaRemoteRunner;
import ru.lanit.bpm.ninja.unit.client.NinjaTestSupport;

/**
 * todo Document type MapRegisterQuoteResponse_DataTransformTest
 */
@Category(UnitTests.class)
@RunWith(NinjaRemoteRunner.class)
public class MapRegisterQuoteResponse_DataTransformTest extends NinjaTestSupport {

    public static final String G_PRIMARY_PAGE = "Ninja_UT";
    public static final String V_QUOTE_ID = "quote-id";
    public static final String G_INT_PAGE = "Ninja_UT_IntPage";

    @Test
    public void mapping() {
        preparePage(G_PRIMARY_PAGE).create(LastMile.C_OJXU4T_ORDERFUL_WORK_LAST_MILE);
        preparePage(G_INT_PAGE).create(QuoteService.C_OJXU4T_INT_LASTMILE_QUOTESERVICE).prop(".AddQuoteParametersResponse.quoteId", V_QUOTE_ID);
        prepareParameter("IntPage").value(G_INT_PAGE);

        invoke().dataTransform().name("MapRegisterQuoteResponse").primaryPage(G_PRIMARY_PAGE);

        assertPage(G_PRIMARY_PAGE).prop("QuoteId", V_QUOTE_ID);
    }
}
