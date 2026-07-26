package com.tuktuk.tuktukspareparts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataParser {

    // this method opens the inventory file and collects all parts into a list
    public static ArrayList<Part> loadParts(String filename) {

        ArrayList<Part> partList = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                Part part = parsePart(line);

                if (part != null) {
                    partList.add(part);
                }
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("couldnt open the file: " + filename);
        }

        return partList;
    } // loadParts ends here

    private static String formatDate(String rawDate) {

        String[] patterns = {
                "yyyy-MM-dd",
                "dd/MM/yyyy",
                "dd-MMM-yyyy",
                "MMM dd, yyyy",
                "MMM dd yyyy",
                "dd-MM-yyyy",
                "yyyy/MM/dd"
        };

        rawDate = rawDate.trim();

        for (int i = 0; i < patterns.length; i++) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                        patterns[i], Locale.ENGLISH);
                LocalDate parsed = LocalDate.parse(rawDate, formatter);
                return parsed.toString();
            } catch (Exception e) {
                // try next pattern
            }
        }

        // if nothing worked keep original
        return rawDate;
    }

    // this method takes one messy line and turns it into a clean Part object
    private static Part parsePart(String line) {

        // some lines use | and some use ; instead of commas
        line = line.replace("|", ",");
        line = line.replace(";", ",");

        // split by comma to get each field
        String[] fields = line.split(",");

        // trim spaces from every field
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }

        // need at least 6 fields to be a valid part
        if (fields.length < 6) {
            System.out.println("skipping this line, not enough fields: " + line);
            return null;
        }

        // read basic fields
        String code = fields[0];
        String name = fields[1];
        String brand = fields[2];

        // some parts dont have a brand so use Unknown
        if (brand.isEmpty()) {
            brand = "Unknown";
        }

        // clean the price - remove Rs. or Rs prefix
        String priceStr = fields[3];
        priceStr = priceStr.replace("Rs.", "");
        priceStr = priceStr.replace("Rs", "");
        priceStr = priceStr.trim();

        double price = 0.0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (Exception e) {
            System.out.println("bad price found: " + priceStr);
        }

        // read quantity
        int quantity = 0;
        try {
            quantity = Integer.parseInt(fields[4]);
        } catch (Exception e) {
            System.out.println("bad quantity found: " + fields[4]);
        }

        // fix category capitalisation - make first letter upper rest lower
        String category = fields[5];
        if (!category.isEmpty()) {
            category = category.substring(0, 1).toUpperCase()
                    + category.substring(1).toLowerCase();
        }

        // handle date - "Oct 15, 2023" has a comma so it gets split
        // if fields[7] is a 4 digit year it means date got split
        // so sjoin them back together
        String date = "";
        String imageFile = "";
        int threshold = 5;  

        if (fields.length > 6) {
            date = fields[6].trim();

            if (fields.length > 7 && fields[7].trim().matches("\\d{4}")) {
                date = date + ", " + fields[7].trim();

                if (fields.length > 8) {
                    try {
                        threshold = Integer.parseInt(fields[8].trim());
                        if (fields.length > 9) {
                            imageFile = fields[9].trim();
                        }                                   
                    } catch (Exception e) {
                        imageFile = fields[8].trim();
                    }
            } else {
                if (fields.length > 7) {
                    try {
                        threshold = Integer.parseInt(fields[7].trim());
                        if (fields.length > 8) {
                            imageFile = fields[8].trim();
                        }                                   
                    } catch (Exception e) {
                        imageFile = fields[7].trim();
                    }
                }
            }
            date = formatDate(date);
        }

        // create and return the clean Part object
        return new Part(code, name, brand, price, quantity,
                category, date, threshold, imageFile);

    } // parsePart ends here

    // this method opens the dealer file and collects all dealers into a list
    public static ArrayList<Dealer> loadDealers(String filename) {

        ArrayList<Dealer> dealerList = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                line = line.replace("|", ",");
                line = line.replace(";", ",");

                String[] fields = line.split(",");
                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim();
                }

                if (fields.length < 2) {
                    continue;
                }

                String code = fields[0];
                String name = fields[1];

                String phone = "";
                if (fields.length > 2) {
                    phone = fields[2];
                }
                if (phone.isEmpty()) {
                    phone = "No contact";
                }

                String location = "";
                if (fields.length > 3) {
                    location = fields[3];
                }
                if (location.isEmpty()) {
                    location = "Unknown";
                }

                dealerList.add(new Dealer(code, name, phone, location));
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("couldnt open the dealers file: " + filename);
        }

        return dealerList;

    } // loadDealers ends here

} // end of DataParser class