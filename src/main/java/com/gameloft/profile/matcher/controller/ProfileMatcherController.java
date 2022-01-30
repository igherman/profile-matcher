package com.gameloft.profile.matcher.controller;

import com.gameloft.profile.matcher.model.Player;
import com.gameloft.profile.matcher.service.ProfileMatcherService;
import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Builder
public class ProfileMatcherController {
    private final ProfileMatcherService profileMatcherService;

    @GetMapping(path = "/get_client_config/{player_id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<Player> getClientConfig(@PathVariable(name = "player_id") String playerId) {
      return ResponseEntity.ok(profileMatcherService.getClientConfig(playerId));
    }
}
