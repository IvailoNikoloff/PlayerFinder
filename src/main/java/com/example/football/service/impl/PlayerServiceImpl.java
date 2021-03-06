package com.example.football.service.impl;

import com.example.football.models.dto.xmlDto.PlayerSeedRootDto;
import com.example.football.models.entity.Player;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final String PLAYERS_FILE_PATH = "src/main/resources/files/xml/players.xml";

    private final PlayerRepository playerRepository;
    private final TownService townService;
    private final TeamService teamService;
    private final StatService statService;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public PlayerServiceImpl(PlayerRepository playerRepository, TownService townService, TeamService teamService, StatService statService, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.playerRepository = playerRepository;
        this.townService = townService;
        this.teamService = teamService;
        this.statService = statService;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(PLAYERS_FILE_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(PLAYERS_FILE_PATH, PlayerSeedRootDto.class)
                .getPlayers()
                .stream()
                .filter(playerSeedDto -> {
                    boolean isValid = validationUtil.isValid(playerSeedDto)
                            && !isEntityExist(playerSeedDto.getEmail());

                    sb
                            .append(isValid ? String.format(
                                    "Successfully imported Player %s %s - %s",
                                    playerSeedDto.getFirstName(),
                                    playerSeedDto.getLastName(),
                                    playerSeedDto.getPosition().toString())
                                    : "Invalid Player")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(playerSeedDto -> {
                    Player player = modelMapper.map(playerSeedDto, Player.class);
                    player.setTown(townService.findByTownName(playerSeedDto.getTownName().getName()));
                    player.setTeam(teamService.findByTeamName(playerSeedDto.getTeamName().getName()));
                    player.setStat(statService.findByStatId(playerSeedDto.getStatId().getId()));

                    return player;
                })
                .forEach(playerRepository::save);


        return sb.toString();
    }

    private boolean isEntityExist(String email) {
        return playerRepository.existsByEmail(email);
    }

    @Override
    public String exportBestPlayers() {
        StringBuilder sb = new StringBuilder();

        LocalDate start = LocalDate.of(1995,01,01);
        LocalDate end = LocalDate.of(2003,01,01);

        playerRepository
                .findAllOrderByShootingDescThen( start, end)
                .forEach(player -> {
                    sb
                            .append(String.format("""
                                            Player - %s %s
                                            \tPosition - %s
                                            \tTeam - %s
                                            \tStadium - %s
                                            """,
                                    player.getFirstName(), player.getLastName(),
                                    player.getPosition().toString(), player.getTeam().getName(),
                                    player.getTeam().getStadiumName()))
                            .append(System.lineSeparator());
                });
        return sb.toString();
    }
}
