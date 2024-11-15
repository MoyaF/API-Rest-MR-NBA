package com.apinba.restapi.controllers;

import com.apinba.restapi.controllers.model.CreateTeamRequest;
import com.apinba.restapi.controllers.model.CreateTeamResponse;
import com.apinba.restapi.controllers.model.FindAllTeamResponse;
import com.apinba.restapi.controllers.model.FindTeamByIdResponse;
import com.apinba.restapi.controllers.model.UpdateTeamRequest;
import com.apinba.restapi.controllers.model.UpdateTeamResponse;
import com.apinba.restapi.services.TeamService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams")
public class TeamController {

  private final TeamService teamService;

  public TeamController(TeamService teamService) {
    this.teamService = teamService;
  }

  @GetMapping
  public ResponseEntity<List<FindAllTeamResponse>> getTeams() {
    var teams = this.teamService.getTeams()
            .map(FindAllTeamResponse::fromTeamModel)
            .toList();

    return ResponseEntity.ok(teams);
  }

  @PostMapping
  public ResponseEntity<CreateTeamResponse> saveTeam(@RequestBody CreateTeamRequest requestBody) {
    var createdTeam = teamService.createTeam(requestBody.toCreateTeam());
    return ResponseEntity.ok(CreateTeamResponse.fromTeamModel(createdTeam));
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<FindTeamByIdResponse> findById(@PathVariable UUID id) {
    var response = teamService.getById(id).map(FindTeamByIdResponse::fromTeamModel);
    return ResponseEntity.of(response);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<UpdateTeamResponse> updateTeamById(
      @RequestBody UpdateTeamRequest request, @PathVariable UUID id) {
    var updatedTeam = teamService.updateById(request.toUpdateTeam(), id);
    return ResponseEntity.ok(UpdateTeamResponse.fromTeamModel(updatedTeam));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<String> deleteTeamById(@PathVariable UUID id) {
    teamService.deleteTeamById(id);
    return ResponseEntity.ok("Team with id " + id + "DELETED!");
  }
}
