package com.miso.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal.hornak on 3/1/2017.
 */

public class Abilities {

    public static class Ability{
        public String name;
        public String description;
        public int price;
        public Ability(String name, String description, int price) {
            this.name = name;
            this.description = description;
            this.price = price;
        }
    }

    private static List<Ability> abilityList = null;

    public static List<Ability> getAbilityList(){
        if (abilityList == null){
            abilityList = new ArrayList<>();
            abilityList.add(new Ability("shockwave", "Powerfull shockwave that kills surounding enemies", 100));
            abilityList.add(new Ability("timestop", "Stops time for all enemies", 500));
            abilityList.add(new Ability("freezing projectiles", "Enemy hit will be frozen in place", 500));
            abilityList.add(new Ability("mock_ability", "Mock description", 100));
            abilityList.add(new Ability("mock_ability2", "Mock description", 100));
            return abilityList;
        } else {
            return abilityList;
        }
    }

}
