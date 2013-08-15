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
                region = "Guangzhou";
                break;
            case 3:
                region = "Shenzhen";
                break;
            case 4:
                region = "Hangzhou";
                break;
            case 5:
                region = "Region 6";
                break;
            case 6:
                region = "Others";
                break;
        }

        return region;
    }
}
