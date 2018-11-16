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
package ninja.demo.exchangeRates;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import ru.lanit.bpm.common.junit.category.UnitTests;
import ru.lanit.bpm.ninja.unit.client.NinjaRemoteRunner;
import ru.lanit.bpm.ninja.unit.client.NinjaTestSupport;

import static ninja.demo.exchageRates.ExchangeRates.C_NINJA_DEMO_EXCHANGE_RATES;

/**
 * todo Document type LoadLatestExchangeRates_Activity
 */
@Category(UnitTests.class)
@RunWith(NinjaRemoteRunner.class)
public class LoadLatestExchangeRates_Activity extends NinjaTestSupport {
    public static final String G_PRIMARY_PAGE = "UT_PrimaryPage";

    @Test
    public void loadExchangeRates() {
        preparePage(G_PRIMARY_PAGE).create(C_NINJA_DEMO_EXCHANGE_RATES);
        prepareParameter("baseCurrency").value("EUR");
        expect().activity().name("Invoke").andMock(context -> preparePage(G_PRIMARY_PAGE).prop("PlainResponse", file("success.json")));

        invoke().activity().primaryPage(G_PRIMARY_PAGE).name("LoadLatestExchangeRates").className(C_NINJA_DEMO_EXCHANGE_RATES);

        assertPage(G_PRIMARY_PAGE).prop("Rates(USD)", "1.15");
    }
}
