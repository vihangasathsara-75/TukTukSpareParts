package com.tuktuk.tuktukspareparts;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class DataParserTest {

    @Test
    public void testParserLoadsAllParts() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        assertEquals(10, parts.size());
    }

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

    @Test
    public void testParserHandlesMissingBrand() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getCode().equals("P003")) {
                assertEquals("Unknown", parts.get(i).getBrand());
            }
        }
    }

    @Test
    public void testParserCleansPriceWithRsPrefix() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getCode().equals("P001")) {
                assertEquals(4500.0, parts.get(i).getPrice());
            }
        }
    }

    @Test
    public void testParserCleansPriceRsNoDot() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getCode().equals("P004")) {
                assertEquals(850.0, parts.get(i).getPrice());
            }
        }
    }

    @Test
    public void testParserFixesUppercaseCategory() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getCode().equals("P005")) {
                assertEquals("Engine", parts.get(i).getCategory());
            }
        }
    }

    @Test
    public void testParserFixesLowercaseCategory() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getCode().equals("P004")) {
                assertEquals("Electrical", parts.get(i).getCategory());
            }
        }
    }

    @Test
    public void testParserHandlesMixedDelimiters() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        Part p005 = null;
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i).getCode().equals("P005")) {
                p005 = parts.get(i);
            }
        }
        assertNotNull(p005);
        assertEquals("Clutch Cable Bajaj RE", p005.getName());
    }

    @Test
    public void testAllPricesAreNonNegative() {
        ArrayList<Part> parts = DataParser.loadParts("inventory_legacy.txt");
        for (int i = 0; i < parts.size(); i++) {
            assertTrue(parts.get(i).getPrice() >= 0,
                    "Price should not be negative for part: " + parts.get(i).getCode());
        }
    }

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

    @Test
    public void testParserLoadsAllDealers() {
        ArrayList<Dealer> dealers = DataParser.loadDealers("dealers_legacy.txt");
        assertEquals(8, dealers.size());
    }

    @Test
    public void testParserHandlesMissingDealerPhone() {
        ArrayList<Dealer> dealers = DataParser.loadDealers("dealers_legacy.txt");
        for (int i = 0; i < dealers.size(); i++) {
            if (dealers.get(i).getCode().equals("D103")) {
                assertEquals("No contact", dealers.get(i).getContactNumber());
            }
        }
    }
}