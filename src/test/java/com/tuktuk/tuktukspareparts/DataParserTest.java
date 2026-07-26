package com.tuktuk.tuktukspareparts;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class DataParserTest {

    // test 1 - parser loads all 10 parts from legacy file
    @Test
    public void testParserLoadsAllParts() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        // should load all 10 parts from the file
        assertEquals(10, parts.size());
    }

    // test 2 - parser handles pipe delimiter 
    @Test
    public void testParserHandlesPipeDelimiter() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        Part p002 = null;
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getCode().equals("P002")) {
                p002 = parts.get(i);
            }
        }
        assertNotNull(p002);
        assertEquals("TVS King Brake Pad", p002.getName());
        assertEquals("TVS", p002.getBrand());
    }

    // test 3 - parser handles semicolon delimiter 
    @Test
    public void testParserHandlesSemicolonDelimiter() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        Part p003 = null;
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getCode().equals("P003")) {
                p003 = parts.get(i);
            }
        }
        assertNotNull(p003);
        assertEquals("Bodywork", p003.getCategory());
        assertEquals(6500.0, p003.getPrice());
    }

    // test 4 - parser handles missing brand field 
    @Test
    public void testParserHandlesMissingBrand() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getCode().equals("P003")) {
                // missing brand should default to Unknown
                assertEquals("Unknown", parts.get(i).getBrand());
            }
        }
    }

    // test 5 - parser cleans price with Rs. prefix 
    @Test
    public void testParserCleansPriceWithRsPrefix() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getCode().equals("P001")) {
                // Rs. 4500.00 should become 4500.0
                assertEquals(4500.0, parts.get(i).getPrice());
            }
        }
    }

    // test 6 - parser cleans price without dot in Rs 
    @Test
    public void testParserCleansPriceRsNoDot() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getCode().equals("P004")) {
                // Rs850 should become 850.0
                assertEquals(850.0, parts.get(i).getPrice());
            }
        }
    }

    // test 7 - parser fixes uppercase category
    @Test
    public void testParserFixesUppercaseCategory() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getCode().equals("P005")) {
                // ENGINE should become Engine
                assertEquals("Engine", parts.get(i).getCategory());
            }
        }
    }

    // test 8 - parser fixes lowercase category
    @Test
    public void testParserFixesLowercaseCategory() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getCode().equals("P004")) {
                // electrical should become Electrical
                assertEquals("Electrical", parts.get(i).getCategory());
            }
        }
    }

    // test 9 - parser handles mixed delimiters in same line 
    @Test
    public void testParserHandlesMixedDelimiters() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        Part p005 = null;
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getCode().equals("P005")) {
                p005 = parts.get(i);
            }
        }
        // P005 mixes commas pipes and semicolons in same line
        assertNotNull(p005);
        assertEquals("Clutch Cable Bajaj RE", p005.getName());
    }

    // test 10 - all prices are non negative after parsing
    @Test
    public void testAllPricesAreNonNegative() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        for (int i = 0; i < parts.size(); i++) {
            assertTrue(parts.get(i).getPrice() >= 0,
                "Price should not be negative for part: " + parts.get(i).getCode());
        }
    }

    // test 11 - all categories start with uppercase after parsing
    @Test
    public void testAllCategoriesCorrectlyCapitalised() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        for (int i = 0; i < parts.size(); i++) {
            String cat = parts.get(i).getCategory();
            if (!cat.isEmpty()) {
                assertTrue(Character.isUpperCase(cat.charAt(0)),
                    "First letter of category should be uppercase: " + cat);
                assertEquals(cat.substring(1), cat.substring(1).toLowerCase(),
                    "Rest of category should be lowercase: " + cat);
            }
        }
    }

    // test 12 - parser loads all dealers from dealer file
    @Test
    public void testParserLoadsAllDealers() {
        ArrayList<Dealer> dealers = DataParser.loadDealers("dealers_legacy.txt");
        assertEquals(8, dealers.size());
    }

    // test 13 - parser handles missing phone in dealer file (D103)
    @Test
    public void testParserHandlesMissingDealerPhone() {
        ArrayList<Dealer> dealers = DataParser.loadDealers("dealers_legacy.txt");
        for (int i = 0; i < dealers.size(); i++) {
            if (dealers.get(i).getCode().equals("D103")) {
                // D103 has no phone - should default to No contact
                assertEquals("No contact", dealers.get(i).getContactNumber());
            }
        }
    }
}