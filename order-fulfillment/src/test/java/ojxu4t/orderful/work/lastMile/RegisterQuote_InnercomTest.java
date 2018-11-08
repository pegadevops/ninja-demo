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
import org.xml.sax.SAXException;
import ru.lanit.bpm.common.junit.category.IntegrationTests;
import ru.lanit.bpm.ninja.unit.client.NinjaRemoteRunner;
import ru.lanit.bpm.ninja.unit.client.cp.PreparePage;
import ru.lanit.bpm.ninja.unit.common.LogLevel;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static ojxu4t.intt.lastMile.quoteService.QuoteService.C_OJXU4T_INT_LASTMILE_QUOTESERVICE;

/**
 * todo Document type RegisterQuote_InnercomTest
 */
@Category(IntegrationTests.class)
@RunWith(NinjaRemoteRunner.class)
public class RegisterQuote_InnercomTest extends OrderFulfillmentTestSupport {

    public static final String G_INT_PAGE = "Ninja_UT_IntPage";
    public static final String V_QUOTE_ID = "Quote-Id";
    public static final String V_SOAP_FAULT_REASON = "My SOAP Fault Reason";

    @Test
    public void success() throws IOException, SAXException, ParserConfigurationException {
        PreparePage workPage = preparePage(G_PRIMARY_PAGE).create(LastMile.C_OJXU4T_ORDERFUL_WORK_LAST_MILE);
        preparePageFromXml(workPage, file("work-page.xml"));
        expect().connectSoap().service("AddQuote").className(C_OJXU4T_INT_LASTMILE_QUOTESERVICE).andMock(context -> {
            context.assertRequestEnvelope().valueXml(file("request.xml"));
            context.prepareResponseEnvelope().value(String.format(file("success.xml"), V_QUOTE_ID));
            context.prepareResponseHttpCode().value(200);
        });

        invoke().activity().name("RegisterQuote").primaryPage(G_PRIMARY_PAGE);

        assertPage(G_PRIMARY_PAGE).prop("QuoteId", V_QUOTE_ID);
    }

    @Test
    public void fault() {
        preparePage(G_PRIMARY_PAGE).create(LastMile.C_OJXU4T_ORDERFUL_WORK_LAST_MILE);
        preparePage(G_INT_PAGE).create(C_OJXU4T_INT_LASTMILE_QUOTESERVICE);
        prepareParameter("IntPage").value(G_INT_PAGE);
        expect().connectSoap().service("AddQuote").className(C_OJXU4T_INT_LASTMILE_QUOTESERVICE).andMock(context -> {
            preparePage(G_INT_PAGE).prop("pySOAPFaultReason", V_SOAP_FAULT_REASON);
            context.prepareResult().exception("SOAP Fault");
        });
        expect().logMessage().level(LogLevel.ERROR).message("[RegisterQuote] AddQuote service invocation failed: " + V_SOAP_FAULT_REASON).andMock();

        invoke().activity().name("RegisterQuote").primaryPage(G_PRIMARY_PAGE);

        assertPage(G_PRIMARY_PAGE).messages().size(1).string().messageContains(V_SOAP_FAULT_REASON);
    }
}
