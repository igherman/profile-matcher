package com.gameloft.profile.matcher.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameloft.profile.matcher.model.Campaign;
import com.gameloft.profile.matcher.model.Inventory;
import com.gameloft.profile.matcher.model.Matchers;
import com.gameloft.profile.matcher.model.Player;
import com.gameloft.profile.matcher.repository.PlayerRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Builder
@Slf4j
public class ProfileMatcherService {

    private final CampaignService campaignService;
    private final PlayerRepository playerRepository;
    private final ObjectMapper objectMapper;

    private static final Field[] allFields = Inventory.class.getDeclaredFields();
    private static final String PLAYER_JSON = "{\"player_id\":\"97983be2-98b7-11e7-90cf-082e5f28d836\",\"credential\":\"apple_credential\",\"created\":\"2021-01-10T13:37:17Z\",\"modified\":\"2021-01-23T13:37:17Z\",\"last_session\":\"2021-01-23T13:37:17Z\",\"total_spent\":400,\"total_refund\":0,\"total_transactions\":5,\"last_purchase\":\"2021-01-22T13:37:17Z\",\"active_campaigns\":[],\"devices\":[{\"id\":1,\"model\":\"apple iphone 11\",\"carrier\":\"vodafone\",\"firmware\":\"123\"}],\"level\":3,\"xp\":1000,\"total_playtime\":144,\"country\":\"CA\",\"language\":\"fr\",\"birthdate\":\"2000-01-10T13:37:17Z\",\"gender\":\"MALE\",\"inventory\":{\"cash\":123,\"coins\":123,\"item_1\":1,\"item_34\":3,\"item_55\":2},\"clan\":{\"id\":\"123456\",\"name\":\"Hello world clan\"},\"_customfield\":\"mycustom\"}";

    //TODO remove after the player will be properly created in db
    @PostConstruct
    public void createPlayer() throws JsonProcessingException {
        if (playerRepository.findById("97983be2-98b7-11e7-90cf-082e5f28d836").isEmpty()) {
            playerRepository.save(objectMapper.readValue(PLAYER_JSON, Player.class));
            log.debug("Mocked player was added to db during startup");
        }
    }

    public Player getClientConfig(String playerId) {
        Campaign currentCampaign = campaignService.getCurrentCampaign();
        return playerRepository.findById(playerId)
                .map(player -> addCampaignToPlayer(currentCampaign, player))
                .orElseThrow(() -> new RuntimeException("Player not found"));
    }

    private Player addCampaignToPlayer(Campaign currentCampaign, Player player) {
        if (playerMatchCampaign(currentCampaign.getMatchers(), player)) {
            log.debug("Player {} matched the current campaign {}.", player.getPlayerId(), currentCampaign.getName());
            return playerRepository.save(player.toBuilder()
                    .activeCampaign(currentCampaign.getName())
                    .modified(OffsetDateTime.now(ZoneOffset.UTC))
                    .build());
        }
        log.debug("Player {} did not match the current campaign {}.", player.getPlayerId(), currentCampaign.getName());
        return player;
    }

    private boolean playerMatchCampaign(Matchers matchers, Player player) {
        Set<String> inventoryFieldsList = Arrays.stream(allFields).map(Field::getName).collect(Collectors.toSet());
        return matchers.levelMatch(player.getLevel())
                && matchers.has(player.getCountry(), inventoryFieldsList)
                && matchers.doesNotHave(inventoryFieldsList);
    }
}
