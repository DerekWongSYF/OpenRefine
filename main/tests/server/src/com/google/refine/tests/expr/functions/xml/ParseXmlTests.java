package com.google.refine.tests.expr.functions.xml;

import org.testng.annotations.Test;

import java.util.Properties;

import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.google.refine.expr.EvalError;
import com.google.refine.expr.functions.xml.ParseXml;
import com.google.refine.grel.ControlFunctionRegistry;
import com.google.refine.grel.Function;
import com.google.refine.tests.RefineTest;
import com.google.refine.tests.util.TestUtils;


public class ParseXmlTests extends RefineTest {
    
    static Properties bindings;
    static String x =   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<root xmlns:foaf=\"http://xmlns.com/foaf/0.1/\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n" +
                        "    <foaf:Person>\n" +
                        "        <foaf:name>John Doe</foaf:name>\n" +
                        "        <head>head1</head>\n" +
                        "        <head>head2</head>\n" +
                        "        <BODY>body1</BODY>\n" +
                        "        <foaf:homepage rdf:resource=\"http://www.example.com\"/>\n" +
                        "    </foaf:Person>\n" +
                        "    <foaf:Person>\n" +
                        "        <foaf:name>Héloïse Dupont</foaf:name>\n" +
                        "        <head>head3</head>\n" +
                        "        <BODY>body2</BODY>\n" +
                        "        <foaf:title/>\n" +
                        "    </foaf:Person>\n" +
                        "</root>";
    
    @Override
    @BeforeTest
    public void init() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @BeforeMethod
    public void SetUp() {
        bindings = new Properties();
    }

    @AfterMethod
    public void TearDown() {
        bindings = null;
    }

    /**
     * Lookup a control function by name and invoke it with a variable number of args
     */
    private static Object invoke(String name,Object... args) {
        // registry uses static initializer, so no need to set it up
        Function function = ControlFunctionRegistry.getFunction(name);
        if (function == null) {
            throw new IllegalArgumentException("Unknown function "+name);
        }
        if (args == null) {
            return function.call(bindings,new Object[0]);
        } else {
            return function.call(bindings,args);
        }
    }
    
    @Test
    public void serializeParseXml() {
        String json = "{\"description\":\"Parses a string as XML\",\"params\":\"string s\",\"returns\":\"XML object\"}";
        TestUtils.isSerializedTo(new ParseXml(), json);
    }
    
    @Test
    public void testParseXml() {
        Assert.assertTrue(invoke("parseXml") instanceof EvalError);
        Assert.assertTrue(invoke("parseXml","x") instanceof org.jsoup.nodes.Document);
    }
}

