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
package ninja.demo;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import ru.lanit.bpm.common.junit.category.UnitTests;
import ru.lanit.bpm.ninja.unit.client.NinjaRemoteRunner;
import ru.lanit.bpm.ninja.unit.client.NinjaTestSupport;
import ru.lanit.bpm.ninja.unit.client.cp.PreparePage;
import ru.lanit.bpm.ninja.unit.client.r.MockBehaviour;
import ru.lanit.bpm.ninja.unit.client.r.act.MockActivityContext;
import ru.lanit.bpm.ninja.unit.client.r.decision.InvokeDecisionTable;

/**
 * Tests CalculateRiskCategory Decision Table
 */
@Category(UnitTests.class)
@RunWith(NinjaRemoteRunner.class)
public class CalculateRiskCategory_DecisionTable extends NinjaTestSupport {
    public static final String P_NINJA_DEMO = "NinjaDemo";
    public static final String C_NINJA_DEMO = "Ninja-Demo";
    public static final String PROP_AGE = "Age";
    public static final String PROP_AMOUNT = "Amount";
    public static final String PROP_CURRENCY = "Currency";
    public static final String DT_CALCULATE_RISK_CATEGORY = "CalculateRiskCategory";
    public static final String RESULT_UNDEFINED = "UNDEFINED";
    public static final String RESULT_MIDDLE = "MIDDLE";
    public static final String RESULT_HIGH = "HIGH";
    public static final String RESULT_LOW = "LOW";

    @Test
    public void calculateRisk_17_200_Galleon() {
        primaryPageSetup("17", "200", "Galleon");
        invokeDt();
        assertInvocationResult().string(RESULT_UNDEFINED);
    }

    @Test
    public void calculateRisk_21_2000_USD() throws Exception {
        runScenario("21", "2000", "USD", "1.0", RESULT_MIDDLE);
    }

    @Test
    public void calculateRisk_18_4002_Copper() throws Exception {
        runScenario("18", "4002", "Copper", "0.5", RESULT_HIGH);
    }

    @Test
    public void calculateRisk_40_6250_Silver() throws Exception {
        runScenario("40", "6250", "Silver", "0.8", RESULT_LOW);
    }

    @Test
    public void calculateRisk_30_8000_Gold() throws Exception {
        runScenario("30", "8000", "Gold", "1.25", RESULT_MIDDLE);
    }

    @Test
    public void calculateRisk_22_50005_Lion() throws Exception {
        runScenario("22", "50005", "Lion", "0.2", RESULT_HIGH);
    }

    private void runScenario(String age, String amount, final String currency, final String exchangeRate, String expectedResult) throws Exception {
        primaryPageSetup(age, amount, currency);
        expect().activity().name("LoadLatestExchangeRates").className("Ninja-Demo-ExchangeRates").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.assertParameter("baseCurrency").value(currency);
                context.preparePrimaryPage().prop("Rates(USD)", exchangeRate);
            }
        });
        invokeDt();
        assertInvocationResult().string(expectedResult);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================
    private InvokeDecisionTable invokeDt() {
        return invoke().decisionTable().name(DT_CALCULATE_RISK_CATEGORY).primaryPage(P_NINJA_DEMO);
    }

    private PreparePage primaryPageSetup(String age, String amount, String currency) {
        return preparePage(P_NINJA_DEMO).create(C_NINJA_DEMO).prop(PROP_AGE, age).prop(PROP_AMOUNT, amount).prop(PROP_CURRENCY, currency);
    }
}