package com.gameloft.profile.matcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gameloft.profile.matcher.model.Campaign;
import com.gameloft.profile.matcher.model.Player;
import com.gameloft.profile.matcher.repository.PlayerRepository;
import com.gameloft.profile.matcher.service.CampaignService;
import com.gameloft.profile.matcher.service.ProfileMatcherService;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.statements.ProfileValueChecker;
import org.springframework.util.SocketUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

@SpringBootTest

@EnableAutoConfiguration(exclude={EmbeddedMongoAutoConfiguration.class})
class ProfileMatcherApplicationTests {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ProfileMatcherService profileMatcherService;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CampaignService campaignService;

    private static MongodExecutable mongodExecutable;
    private static final String PLAYER_JSON = "{\"player_id\":\"97983be2-98b7-11e7-90cf-082e5f28d836\",\"credential\":\"apple_credential\",\"created\":\"2021-01-10T13:37:17Z\",\"modified\":\"2021-01-23T13:37:17Z\",\"last_session\":\"2021-01-23T13:37:17Z\",\"total_spent\":400,\"total_refund\":0,\"total_transactions\":5,\"last_purchase\":\"2021-01-22T13:37:17Z\",\"active_campaigns\":[],\"devices\":[{\"id\":1,\"model\":\"apple iphone 11\",\"carrier\":\"vodafone\",\"firmware\":\"123\"}],\"level\":3,\"xp\":1000,\"total_playtime\":144,\"country\":\"CA\",\"language\":\"fr\",\"birthdate\":\"2000-01-10T13:37:17Z\",\"gender\":\"MALE\",\"inventory\":{\"cash\":123,\"coins\":123,\"item_1\":1,\"item_34\":3,\"item_55\":2},\"clan\":{\"id\":\"123456\",\"name\":\"Hello world clan\"},\"_customfield\":\"mycustom\"}";
    private static final String CAMPAIGN_JSON = "{\"game\":\"mygame\",\"name\":\"mycampaign\",\"priority\":10.5,\"matchers\":{\"level\":{\"min\": 1,\"max\": 3},\"has\":{\"country\":[\"US\",\"RO\",\"CA\"],\"items\":[\"item_1\"]},\"does_not_have\":{\"items\":[\"item_4\", \"item_34\"]}},\"start_date\":\"2022-01-25T00:00:00Z\",\"end_date\":\"2022-02-25T00:00:00Z\",\"enabled\":true,\"last_updated\":\"2021-07-13T11:46:58Z\"}";
    private static final String NOT_MATCHING_CAMPAIGN_JSON = "{\"game\":\"mygame\",\"name\":\"mycampaign\",\"priority\":10.5,\"matchers\":{\"level\":{\"min\": 1,\"max\": 3},\"has\":{\"country\":[\"US\",\"RO\",\"CA\"],\"items\":[\"item_1\", \"item_4\"]},\"does_not_have\":{\"items\":[\"item_4\", \"item_34\"]}},\"start_date\":\"2022-01-25T00:00:00Z\",\"end_date\":\"2022-02-25T00:00:00Z\",\"enabled\":true,\"last_updated\":\"2021-07-13T11:46:58Z\"}";

    @BeforeAll
    public static void before() throws IOException {
        String ip = "localhost";
        int port = 27017;

        ImmutableMongodConfig mongodConfig = MongodConfig
                .builder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(ip, port, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
    }

    @AfterAll
    public static void cleanUp() {
        mongodExecutable.stop();
    }

    @Test
    void testCampaignMatcherHappyCase() throws JsonProcessingException {
        playerRepository.save(createPlayer());
        when(campaignService.getCurrentCampaign()).thenReturn(mockedCampaign(CAMPAIGN_JSON));
        Player playerExisting = profileMatcherService.getClientConfig("97983be2-98b7-11e7-90cf-082e5f28d836");
        assertTrue(playerExisting.getActiveCampaigns().contains("mycampaign"));
    }

    @Test
    void testCampaignMatcherUnhappyCase() throws JsonProcessingException {
        playerRepository.save(createPlayer());
        when(campaignService.getCurrentCampaign()).thenReturn(mockedCampaign(NOT_MATCHING_CAMPAIGN_JSON));
        Player playerExisting = profileMatcherService.getClientConfig("97983be2-98b7-11e7-90cf-082e5f28d836");
        assertFalse(playerExisting.getActiveCampaigns().contains("mycampaign"));
    }

    private Player createPlayer() throws JsonProcessingException {
        return mapper.readValue(PLAYER_JSON, Player.class);
    }
    private Campaign mockedCampaign(String campaignJson) throws JsonProcessingException {
        return mapper.readValue(campaignJson, Campaign.class);
    }

}
