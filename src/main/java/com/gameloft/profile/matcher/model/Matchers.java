package com.gameloft.profile.matcher.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Set;

@Jacksonized
@Data
public class Matchers {
    private Level level;
    private Has has;

    @JsonProperty("does_not_have")
    private DoesNotHave doesNotHave;

    @Jacksonized
    @Data
    public static class Level {
        private int min;
        private int max;
    }

    @Jacksonized
    @Data
    public static class Has {
        private List<Country> country;
        private Set<String> items;
    }

    @Jacksonized
    @Data
    public static class DoesNotHave {
        private Set<String> items;
    }

    public boolean levelMatch(int playerLevel) {
        return playerLevel >= this.level.getMin() && playerLevel <= this.level.getMax();
    }

    public boolean has(Country country, Set<String> inventoryFields) {
        return this.has.country.contains(country) && inventoryFields.containsAll(this.has.items);
    }

    public boolean doesNotHave(Set<String> inventoryFields) {
        return !inventoryFields.containsAll(this.doesNotHave.items);
    }
}
