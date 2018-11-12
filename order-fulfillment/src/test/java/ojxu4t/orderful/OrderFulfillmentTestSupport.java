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
package ojxu4t.orderful;

import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.lanit.bpm.ninja.unit.client.NinjaTestSupport;
import ru.lanit.bpm.ninja.unit.client.cp.AssertPage;
import ru.lanit.bpm.ninja.unit.client.cp.PreparePage;
import ru.lanit.bpm.ninja.unit.client.r.MockBehaviour;
import ru.lanit.bpm.ninja.unit.client.r.act.MockActivityContext;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

import static ru.lanit.bpm.ninja.common.utils.PegaConstants.P_PY_ID;
import static ru.lanit.bpm.ninja.common.utils.PegaConstants.P_PY_LABEL;

/**
 * todo Document type OrderFulfillmentTestSupport
 */
public class OrderFulfillmentTestSupport extends NinjaTestSupport {

    public static final String G_INT_PAGE = "Ninja_UT_IntPage";
    public static final String G_PRIMARY_PAGE = "Ninja_UT";

    protected void expectDictLookup(String clazz, String pyId, String pyLabel) {
        expect().lookup().className(clazz).andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext mockActivityContext) {
                mockActivityContext.assertParameter(P_PY_ID).value(pyId);
                mockActivityContext.preparePrimaryPage().prop(P_PY_LABEL, pyLabel);
            }
        });
    }

    @SuppressWarnings("UnusedReturnValue")
    protected PreparePage preparePageFromXml(PreparePage preparePage, String xml) throws ParserConfigurationException, SAXException, IOException {
        Element rootElement = readXml(xml);
        preparePageFromXml(preparePage, rootElement);
        return preparePage;
    }

    @SuppressWarnings("UnusedReturnValue")
    protected PreparePage preparePageFromXml(PreparePage parentPage, String embeddedPageName, String xml)
            throws ParserConfigurationException, SAXException, IOException {
        Element rootElement = readXml(xml);
        Assert.assertEquals("Page name doesn't match", rootElement.getTagName(), embeddedPageName);
        preparePageFromXml(parentPage.page(embeddedPageName), rootElement);
        return parentPage;
    }

    @SuppressWarnings("UnusedReturnValue")
    protected AssertPage assertPageAsXml(AssertPage assertPage, String xml) throws ParserConfigurationException, SAXException, IOException {
        Element rootElement = readXml(xml);
        assertPageAsXml(assertPage, rootElement);
        return assertPage;
    }

    @SuppressWarnings("UnusedReturnValue")
    protected AssertPage assertPageAsXml(AssertPage parentPage, String embeddedPageName, String xml)
            throws ParserConfigurationException, SAXException, IOException {
        Element rootElement = readXml(xml);
        Assert.assertEquals("Page name doesn't match", rootElement.getTagName(), embeddedPageName);
        assertPageAsXml(parentPage.page(embeddedPageName), rootElement);
        return parentPage;
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private Element readXml(String xml) throws ParserConfigurationException, SAXException, IOException {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            return document.getDocumentElement();
        }

        private void assertPageAsXml(AssertPage assertPage, Element pageElement) {
            NodeList nodeList = pageElement.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String elementName = element.getTagName();
                    if (element.hasAttribute("REPEATINGTYPE")) {
                        assertRepeatingPageAsXml(assertPage, elementName, element);
                    } else if (isPageNode(element)) {
                        assertPageAsXml(assertPage.page(elementName), element);
                    } else if (element.hasChildNodes()) {
                        assertPage.prop(elementName, element.getTextContent());
                    }
                }
            }
        }

        private void assertRepeatingPageAsXml(AssertPage parentPage, String repeatingProperty, Element repeatingElement) {
            NodeList nodeList = repeatingElement.getChildNodes();
            String type = repeatingElement.getAttribute("REPEATINGTYPE");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String reference = String.format("%s(%s)", repeatingProperty, element.getAttribute("REPEATINGINDEX"));
                    if (type.contains("Page")) {
                        assertPageAsXml(parentPage.page(reference), element);
                    } else {
                        parentPage.prop(reference, element.getTextContent());
                    }
                }
            }
        }

        private void preparePageFromXml(PreparePage page, Element rootElement) {
            NodeList nodeList = rootElement.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String elementName = element.getTagName();
                    if (element.hasAttribute("REPEATINGTYPE")) {
                        prepareRepeatingPageFromXml(page, elementName, element);
                    } else if (isPageNode(element)) {
                        preparePageFromXml(page.page(elementName).create("@baseclass"), element);
                    } else if (element.hasChildNodes()) {
                        page.prop(elementName, element.getTextContent());
                    }
                }
            }
        }

        private boolean isPageNode(Element element) {
            NodeList nodeList = element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    return true;
                }
            }
            return false;
        }

        private void prepareRepeatingPageFromXml(PreparePage page, String repeatingProperty, Element repeatingElement) {
            NodeList nodeList = repeatingElement.getChildNodes();
            String type = repeatingElement.getAttribute("REPEATINGTYPE");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String reference = String.format("%s(%s)", repeatingProperty, element.getAttribute("REPEATINGINDEX"));
                    if (type.contains("Page")) {
                        preparePageFromXml(page.page(reference), element);
                    } else {
                        page.prop(reference, element.getTextContent());
                    }
                }
            }
        }
}
