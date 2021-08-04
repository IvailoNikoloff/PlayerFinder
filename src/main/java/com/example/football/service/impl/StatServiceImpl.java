package com.example.football.service.impl;

import com.example.football.models.dto.xmlDto.StatSeedRootDto;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class StatServiceImpl implements StatService {

    private static final String STATS_FILE_PATH = "src/main/resources/files/xml/stats.xml";

    private final StatRepository statRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public StatServiceImpl(StatRepository statRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.statRepository = statRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files.readString(Path.of(STATS_FILE_PATH));
    }

    @Override
    public String importStats() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(STATS_FILE_PATH, StatSeedRootDto.class)
                .getStats()
                .stream()
                .filter(statSeedDto -> {
                    boolean isValid = validationUtil.isValid(statSeedDto)
                            && !isEntityExist(
                            statSeedDto.getPassing(),
                            statSeedDto.getShooting(),
                            statSeedDto.getEndurance()
                    );

                    sb
                            .append(isValid ? String.format(
                                    "Successfully imported Stat %.2f - %.2f - %.2f",
                                    statSeedDto.getShooting(),
                                    statSeedDto.getPassing(),
                                    statSeedDto.getEndurance())
                                    : "Invalid Stat")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(statSeedDto -> modelMapper.map(statSeedDto, Stat.class))
                .forEach(statRepository::save);

        return sb.toString();
    }

    @Override
    public boolean isEntityExist(Long id) {
        return statRepository.existsById(id);
    }

    @Override
    public Stat findByStatId(Long id) {
        return statRepository.findById(id).orElse(null);
    }


    private boolean isEntityExist(Float passing, Float shooting, Float endurance) {
        return statRepository.existsByPassingAndShootingAndEndurance(passing, shooting, endurance);
    }
}
