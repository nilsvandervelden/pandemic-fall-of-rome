package com.groep6.pfor.factories;

import com.groep6.pfor.Config;
import com.groep6.pfor.models.City;
import com.groep6.pfor.util.parsers.CityParser;

import java.text.ParseException;

/**
 * The CityFactory constructs the list of cities from a json file
 *
 * @author Owen Elderbroek
 */
public class CityFactory {
    private static final CityFactory SINGLE_INSTANCE = createCityFactory();
    private City[] cities;

    private static CityFactory createCityFactory() {
        return new CityFactory();
    }

    private CityParser createCityParser() {
        return new CityParser();
    }

    private void addCitiesToCityList(CityParser cityParser) {
        try {
            cities = cityParser.parseFile("/cities.json");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /** Creates the CityFactory instance */
    private CityFactory() {
        CityParser cityParser = createCityParser();
        addCitiesToCityList(cityParser);
    }

    /**
     * Returns the instance of the CityFactory
     * @return The CityFactory instance
     */
    public static CityFactory getInstance() {
        return SINGLE_INSTANCE;
    }

    /**
     * Get an array of all the cities currently loaded into the game
     * @return An array of available cities
     */
    public City[] getAllCities() {
        return cities;
    }

    /**
     * Get the amount of cities in the game
     * @return The amount of cities in the game
     */
    public int cityCount() {
        return cities.length;
    }

    private boolean requestedCityNameEqualsCityName(String requestedCityName, City city) {
        return city.getCityName().toUpperCase().equals(requestedCityName.toUpperCase());
    }

    public City getCityByName(String requestedCityName) {
        for (City city : cities) {
            if (requestedCityNameEqualsCityName(requestedCityName, city)) return city;
        }
        if (inDebugMode()) printCityWasNotFound(requestedCityName);
        return null;
    }

    private void printCityWasNotFound(String requestedCityName) {
        System.out.printf("[WARNING] No city was found with the name '%s'\n", requestedCityName);
    }

    private boolean inDebugMode() {
        return Config.DEBUG;
    }
}
