package org.redhatchallenge.rhc2013.client;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class LocaleUtil {

    public static String getCountryFromIndex(int index) {

        String country = null;

        switch(index) {
            case 0:
                country = "Singapore";
                break;
            case 1:
                country = "Malaysia";
                break;
            case 2:
                country = "Thailand";
                break;
            case 3:
                country = "China";
                break;
            case 4:
                country = "Hong Kong";
                break;
            case 5:
                country = "Taiwan";
                break;
        }
        return country;
    }

    public static int getIndexFromCountry(String country) {
        if(country.equalsIgnoreCase("singapore")) {
            return 0;
        }

        else if(country.equalsIgnoreCase("malaysia")) {
            return 1;
        }

        else if(country.equalsIgnoreCase("thailand")) {
            return 2;
        }

        else if(country.equalsIgnoreCase("china")) {
            return 3;
        }

        else if(country.equalsIgnoreCase("hong kong")) {
            return 4;
        }

        else if(country.equalsIgnoreCase("taiwan")) {
            return 5;
        }

        else {
            return -1;
        }
    }

    public static String getLanguageFromIndex(int index) {

        String language = null;

        switch(index) {
            case 0:
                language = "English";
                break;
            case 1:
                language = "Chinese (Simplified)";
                break;
            case 2:
                language = "Chinese (Traditional)";
                break;
        }

        return language;
    }

    public static int getIndexFromLanguage(String language) {
        if(language.equalsIgnoreCase("english")) {
            return 0;
        }

        else if(language.equalsIgnoreCase("chinese (simplified)")) {
            return 1;
        }

        else if(language.equalsIgnoreCase("chinese (traditional)")) {
            return 2;
        }

        else {
            return -1;
        }
    }

    public static String getRegionFromIndex(int index) {

        String region = null;

        switch(index) {
            case 0:
                region = "Beijing";
                break;
            case 1:
                region = "Shanghai";
                break;
            case 2:
                region = "Wuhan";
                break;
            case 3:
                region = "Dalian";
                break;
            case 4:
                region = "Jinan";
                break;
            case 5:
                region = "Others";
                break;
        }

        return region;
    }

    public static int getIndexFromRegion(String region) {
        if(region.equalsIgnoreCase("Beijing")) {
            return 0;
        }

        else if(region.equalsIgnoreCase("Shanghai")) {
            return 1;
        }

        else if(region.equalsIgnoreCase("Wuhan")) {
            return 2;
        }

        else if(region.equalsIgnoreCase("Dalian")) {
            return 3;
        }

        else if(region.equalsIgnoreCase("Jinan")) {
            return 4;
        }

        else if(region.equalsIgnoreCase("others")) {
            return 6;
        }

        else {
            return -1;
        }
    }
}
