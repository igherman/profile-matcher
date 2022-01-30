package com.gameloft.profile.matcher.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.OffsetDateTime;

@Data
@Jacksonized
@Builder
public class Campaign {
    private String game;
    private String name;
    private double priority;
    private Matchers matchers;
    @JsonProperty("start_date")
    private OffsetDateTime startDate;
    @JsonProperty("end_date")
    private OffsetDateTime endDate;
    private boolean enabled;
    @JsonProperty("last_updated")
    private OffsetDateTime lastUpdated;
}
