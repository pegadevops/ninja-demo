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
package ojxu4t.intt.lastMile.quoteService;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import ru.lanit.bpm.common.junit.category.UnitTests;
import ru.lanit.bpm.ninja.unit.client.NinjaRemoteRunner;
import ru.lanit.bpm.ninja.unit.client.NinjaTestSupport;

/**
 * todo Document type AddQuoteParametersResponse_ParseXmlTest
 */
@Category(UnitTests.class)
@RunWith(NinjaRemoteRunner.class)
public class AddQuoteParametersResponse_ParseXmlTest extends NinjaTestSupport {

    public static final String G_PRIMARY_PAGE = "Ninja_UT";
    public static final String XML = "<peg:AddQuoteResponse xmlns:peg=\"http://pegadevops.com\"><peg:quoteId>%1$s</peg:quoteId></peg:AddQuoteResponse>";
    public static final String V_QUOTE_ID = "My-Quote-Id";

    @Test
    public void parse() {
        preparePage(G_PRIMARY_PAGE).create(QuoteService.C_OJXU4T_INT_LASTMILE_QUOTESERVICE);

        invoke().parseXml().primaryPage(G_PRIMARY_PAGE).elementName("AddQuoteParametersResponse").namespace("Default").xml(String.format(XML, V_QUOTE_ID));
        preparePage(G_PRIMARY_PAGE).dump();

        assertPage(G_PRIMARY_PAGE).prop(".AddQuoteParametersResponse.quoteId", V_QUOTE_ID);
    }
}
