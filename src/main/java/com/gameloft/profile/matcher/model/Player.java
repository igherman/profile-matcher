package com.gameloft.profile.matcher.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@Jacksonized
@Builder(toBuilder = true)
public class Player {
    @Id
    @JsonProperty("player_id")
    private String playerId;
    private String credential;
    private OffsetDateTime created;
    private OffsetDateTime modified;
    @JsonProperty("last_session")
    private OffsetDateTime lastSession;
    @JsonProperty("total_spent")
    private int totalSpent;
    @JsonProperty("total_refund")
    private int totalRefund;
    @JsonProperty("total_transactions")
    private int totalTransactions;
    @JsonProperty("last_purchase")
    private OffsetDateTime lastPurchase;
    @JsonProperty("active_campaigns")
    @Singular
    private Set<String> activeCampaigns;
    private Set<Device> devices;
    private int level;
    private int xp;
    @JsonProperty("total_playtime")
    private int totalPlaytime;
    private Country country;
    private String language;
    private OffsetDateTime birthdate;
    private Gender gender;
    private Inventory inventory;
    private Clan clan;
    @JsonProperty("_customfield")
    private String customField;
}
