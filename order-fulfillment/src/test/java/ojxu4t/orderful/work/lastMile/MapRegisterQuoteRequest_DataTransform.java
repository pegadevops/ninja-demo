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
import ojxu4t.orderful.OrderFulfillmentTestSupport;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;
import ru.lanit.bpm.common.junit.category.UnitTests;
import ru.lanit.bpm.ninja.unit.client.NinjaRemoteRunner;
import ru.lanit.bpm.ninja.unit.client.cp.PreparePage;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * todo Document type MapRegisterQuoteRequest_DataTransform
 */
@Category(UnitTests.class)
@RunWith(NinjaRemoteRunner.class)
public class MapRegisterQuoteRequest_DataTransform extends OrderFulfillmentTestSupport {

    @Test
    public void mapping() throws IOException, SAXException, ParserConfigurationException {
        PreparePage workPage = preparePage(G_PRIMARY_PAGE).create(LastMile.C_OJXU4T_ORDERFUL_WORK_LAST_MILE);
        preparePageFromXml(workPage, file("work-page.xml"));
        prepareParameter("IntPage").value(G_INT_PAGE);
        preparePage(G_INT_PAGE).create(QuoteService.C_OJXU4T_INT_LASTMILE_QUOTESERVICE);

        invoke().dataTransform().name("MapRegisterQuoteRequest").primaryPage(G_PRIMARY_PAGE);

        assertPageAsXml(assertPage(G_INT_PAGE), file("int-page.xml"));
    }
}
