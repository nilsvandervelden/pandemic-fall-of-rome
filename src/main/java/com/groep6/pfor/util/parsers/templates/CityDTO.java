package com.groep6.pfor.util.parsers.templates;

import com.groep6.pfor.factories.FactionFactory;
import com.groep6.pfor.models.Barbarian;
import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Legion;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.models.factions.FactionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Represents a City in Firebase
 *
 * @author OwenElderbroek
 */
public class CityDTO {
    public String cityName;
    public boolean cityHasFort;
    public int amountOfLegionsInCity;
    public int amountOfBarbariansInCity;

    public CityDTO() {}

    private CityDTO(String cityName, boolean cityHasFort, int amountOfLegionsInCity, int amountOfBarbariansInCity) {
        this.cityName = cityName;
        this.cityHasFort = cityHasFort;
        this.amountOfLegionsInCity = amountOfLegionsInCity;
        this.amountOfBarbariansInCity = amountOfBarbariansInCity;
    }

    private static List<Faction> getFactions() {
        FactionFactory factionFactory = FactionFactory.getInstance();
        return factionFactory.getFactions();
    }

    private static void addBarbariansToGame(Map<String, Integer> barbarians) {
        for (Faction faction : getFactions()) {
            barbarians.put(getFactionTypeAsString(faction), 0);
        }
    }

    private static FactionType getFactionType(Faction faction) {
        return faction.getFactionType();
    }

    private static String getFactionTypeAsString(Faction faction) {
        return getFactionType(faction).toString();
    }

    private static FactionType getFactionTypeOfBarbarian(Barbarian barbarian) {
        return barbarian.getFactionTypeOfBarbarian();
    }

    private static  String getFactionTypeOfBarbarianAsString(Barbarian barbarian) {
        return getFactionTypeOfBarbarian(barbarian).toString();
    }

    private static int getNextBarbarianInteger(Map<String, Integer> barbarians, Barbarian barbarian) {
        return barbarians.get(getFactionTypeOfBarbarianAsString(barbarian)) + 1;
    }

    private static void addBarbariansToCities(Map<String, Integer> barbarians, City city) {
        for (Barbarian barbarian : city.getBarbariansInCity())
            barbarians.put(getFactionTypeOfBarbarianAsString(barbarian), getNextBarbarianInteger(barbarians, barbarian));
    }

    public static CityDTO parseFromModel(City city) {

        Map<String, Integer> barbarians = new HashMap<>();

        addBarbariansToGame(barbarians);

        addBarbariansToCities(barbarians, city);

        int amountOfBarbarians = barbarians.get(FactionType.VISIGOTHS.toString());
        amountOfBarbarians |= barbarians.get(FactionType.VANDALS.toString()) * 64;
        amountOfBarbarians |= (int) (barbarians.get(FactionType.ANGLO_SAXSONS_FRANKS.toString()) * Math.pow(64, 2));
        amountOfBarbarians |= (int) (barbarians.get(FactionType.HUNS.toString()) * Math.pow(64, 3));
        amountOfBarbarians |= (int) (barbarians.get(FactionType.OSTROGOTHS.toString()) * Math.pow(64, 4));

        return new CityDTO(city.getCityName(), city.hasFort(), city.getLegionCount(), amountOfBarbarians);
    }

    private void addLegion(Stack<Legion> legions) {
        legions.push(new Legion());
    }

    public City parseToModel() {
        Stack<Legion> legions = new Stack<>();
        for (int i = 0; i < this.amountOfLegionsInCity; i++) {
            addLegion(legions);
        }
        Stack<Barbarian> barbarians = new Stack<>();
        // Use fancy bitfields to save immense amounts of strain on the database
        addBarbariansToGame(FactionType.VISIGOTHS, this.amountOfBarbariansInCity & 63, barbarians);
        addBarbariansToGame(FactionType.VANDALS, (this.amountOfBarbariansInCity & (63 << 6)) >> 6, barbarians);
        addBarbariansToGame(FactionType.ANGLO_SAXSONS_FRANKS, (this.amountOfBarbariansInCity & (63 << 12)) >> 12, barbarians);
        addBarbariansToGame(FactionType.HUNS, (this.amountOfBarbariansInCity & (63 << 18)) >> 18, barbarians);
        addBarbariansToGame(FactionType.OSTROGOTHS, (this.amountOfBarbariansInCity & (63 << 24)) >> 24, barbarians);
        return new City(cityName, legions, barbarians, cityHasFort);
    }

    private void addBarbarian(Stack<Barbarian> barbarians, FactionType factionType) {
        barbarians.push(new Barbarian(factionType));
    }

    private void addBarbariansToGame(FactionType faction, int amountOfBarbariansInCity, Stack<Barbarian> barbarians) {
        for (int i = 0; i < amountOfBarbariansInCity; i++) {
            addBarbarian(barbarians, faction);
        }
    }
}
