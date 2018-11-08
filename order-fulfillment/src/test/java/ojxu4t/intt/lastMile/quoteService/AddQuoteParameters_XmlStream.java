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

import ojxu4t.intt.lastMile.addQuote.AddQuote;
import ojxu4t.intt.lastMile.lineDetails.LineDetails;
import ojxu4t.intt.lastMile.location.Location;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import ru.lanit.bpm.common.junit.category.UnitTests;
import ru.lanit.bpm.ninja.unit.client.NinjaRemoteRunner;
import ru.lanit.bpm.ninja.unit.client.NinjaTestSupport;
import ru.lanit.bpm.ninja.unit.client.cp.PreparePage;

import java.util.Date;

/**
 * todo Document type AddQuoteParameters_XmlStream
 */
@Category(UnitTests.class)
@RunWith(NinjaRemoteRunner.class)
public class AddQuoteParameters_XmlStream extends NinjaTestSupport {

    public static final String G_PRIMARY_PAGE = "Ninja_UT";
    public static final String V_PROJECT_ID = "My-Proj-Id";
    public static final String V_INTERNAL_COMMENT = "Some Internal Comment";
    public static final String V_SUPPLIER_COMMENT = "Some Supplier Comment";
    public static final String V_TECHNICAL_COMMENT = "Some Technical Comment";
    public static final String V_LOCATION_ID = "loc-id";
    public static final String V_CUSTOMER_LOCATION = "Customer location address";
    public static final String V_VENDOR_LOCATION = "Vendor location address";
    public static final Date V_PROVISION_DATE = new Date();
    public static final String V_LINE_TYPE_ID = "super-line";
    public static final String V_LINE_TYPE_NAME = "Super Stable Line";
    public static final String V_MINIMAL_LEASE_TIME = "12";
    public static final String XML = "<ns1:AddQuote xmlns:ns1=\"http://pegadevops.com\"> <ns1:projectId>My-Proj-Id</ns1:projectId> <ns1:lineDetails> " +
            "<ns1:lineTypeId>super-line</ns1:lineTypeId> <ns1:lineTypeName>Super Stable Line</ns1:lineTypeName> <ns1:minimalLeaseTime>12</ns1:minimalLeaseTime> " +
                    "</ns1:lineDetails> <ns1:location> <ns1:id>loc-id</ns1:id> <ns1:customerLocation>Customer location address</ns1:customerLocation>" +
                    " <ns1:vendorLocation>Vendor location address</ns1:vendorLocation> <ns1:provisionDate>%1$tY-%1$tm-%1$td</ns1:provisionDate> </ns1:location>" +
                    " <ns1:internalComment>Some Internal Comment</ns1:internalComment> <ns1:supplierComment>Some Supplier Comment</ns1:supplierComment>" +
                    " <ns1:technicalComment>Some Technical Comment</ns1:technicalComment> </ns1:AddQuote> ";

    @Test
    public void stream() {
        PreparePage addQuotePage = preparePage(G_PRIMARY_PAGE).create(QuoteService.C_OJXU4T_INT_LASTMILE_QUOTESERVICE)
                .page(QuoteService.P_ADDQUOTEPARAMETERS).create(AddQuote.C_OJXU4T_INTT_LASTMILE_ADDQUOTE);
        addQuotePage
                .prop(AddQuote.P_PROJECTID, V_PROJECT_ID)
                .prop(AddQuote.P_INTERNALCOMMENT, V_INTERNAL_COMMENT)
                .prop(AddQuote.P_SUPPLIERCOMMENT, V_SUPPLIER_COMMENT)
                .prop(AddQuote.P_TECHNICALCOMMENT, V_TECHNICAL_COMMENT);
        addQuotePage.page(AddQuote.P_LINEDETAILS).create(LineDetails.C_OJXU4T_INTT_LASTMILE_LINEDETAILS)
                .prop(LineDetails.P_LINETYPEID, V_LINE_TYPE_ID)
                .prop(LineDetails.P_LINETYPENAME, V_LINE_TYPE_NAME)
                .prop(LineDetails.P_MINIMALLEASETIME, V_MINIMAL_LEASE_TIME);
        addQuotePage.page(AddQuote.P_LOCATION).create(Location.C_OJXU4T_INTT_LASTMILE_LOCATION)
                .prop(Location.P_ID, V_LOCATION_ID)
                .prop(Location.P_CUSTOMERLOCATION, V_CUSTOMER_LOCATION)
                .prop(Location.P_VENDORLOCATION, V_VENDOR_LOCATION)
                .prop(Location.P_PROVISIONDATE, String.format("%1$tY%1$tm%1$td", V_PROVISION_DATE));

        invoke().xmlStream().name("AddQuoteParameters").type("MapFrom").primaryPage(G_PRIMARY_PAGE);

        assertInvocationResult().string(String.format(XML, V_PROVISION_DATE));
    }
}
