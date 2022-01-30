package com.gameloft.profile.matcher.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameloft.profile.matcher.model.Campaign;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Builder
@Slf4j
public class CampaignService {

    private final ObjectMapper objectMapper;

    private static final String CAMPAIGN_JSON = "{\"game\":\"mygame\",\"name\":\"mycampaign\",\"priority\":10.5,\"matchers\":{\"level\":{\"min\": 1,\"max\": 3},\"has\":{\"country\":[\"US\",\"RO\",\"CA\"],\"items\":[\"item_1\"]},\"does_not_have\":{\"items\":[\"item_4\"]}},\"start_date\":\"2022-01-25T00:00:00Z\",\"end_date\":\"2022-02-25T00:00:00Z\",\"enabled\":true,\"last_updated\":\"2021-07-13T11:46:58Z\"}";

    //TODO Integrate with campaign API, mocked at the moment
    public Campaign getCurrentCampaign() {
        try {
            return objectMapper.readValue(CAMPAIGN_JSON, Campaign.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse campaign JSON", e);
            return Campaign.builder().build();
        }
    }
}
