package com.permatabank.insertbatchfile.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.permatabank.insertbatchfile.entity.SwiftBank;
import com.permatabank.insertbatchfile.repository.SwiftBankRepository;
import com.permatabank.insertbatchfile.vo.SwiftBankVO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j

public class BatchInsert {

    private final DataSource dataSource;
    private final SwiftBankRepository swiftBankRepository;
    private final ModelMapper modelMapper;




    public BatchInsert(DataSource dataSource, SwiftBankRepository swiftBankRepository, ModelMapper modelMapper) {
        this.dataSource = dataSource;
        this.swiftBankRepository = swiftBankRepository;
        this.modelMapper = modelMapper;
    }

    public void processData (String filePath) throws IOException, FileNotFoundException, SQLException, CsvValidationException {

        int rowIndex = 0;
        List<SwiftBankVO> list = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                csvReader.skip(1);
                String[] line;
                while (null != (line = csvReader.readNext())) {
                    rowIndex++;
                    SwiftBankVO swiftBankVO = new SwiftBankVO();
                    swiftBankVO.setRecId(Long.valueOf(line[0]));
              //      swiftBankVO.setCreatedDate(line[1]);
                    swiftBankVO.setSwiftCode(line[2]);
                    swiftBankVO.setBranchCode(line[3]);
                    swiftBankVO.setBankName(line[4]);
                    swiftBankVO.setAddressLine1(line[5]);
                    swiftBankVO.setAddressLine2(line[6]);
                    swiftBankVO.setAddressLine3(line[7]);
                    swiftBankVO.setAddressLine4(line[8]);
                    swiftBankVO.setCityName(line[9]);
                    swiftBankVO.setCountryCode(line[10]);
                    swiftBankVO.setCountryName(line[11]);

                    list.add(swiftBankVO);

                    if (rowIndex % 10000 == 0) {
                        log.info("processing row {}", rowIndex);
                        batchInsert(list);
                        hibernateInsert(list);
                        list.clear();
                    }
                }
                if (!list.isEmpty()) {
                    batchInsert(list);
                    hibernateInsert(list);
                }
            }
        }
    }

    private void batchInsert(List<SwiftBankVO> list) throws SQLException {
        long start = System.currentTimeMillis();
        String insertSwiftBank = "INSERT INTO /*+append*/ PMOB.SWIFT_BANK(rec_id,created_date,swift_code,branch_code,bank_name,address_line1,address_line2,address_line3,address_line4,city_name,country_code,country_name) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection connection = dataSource.getConnection(); PreparedStatement insertSwift = connection.prepareStatement(insertSwiftBank)) {
            for (int i = 0; i < list.size(); i++) {
                insertSwift.setLong(1, list.get(i).getRecId());
                insertSwift.setDate(2, (Date) list.get(i).getCreatedDate());
                insertSwift.setString(3, list.get(i).getSwiftCode());
                insertSwift.setString(4, list.get(i).getBranchCode());
                insertSwift.setString(5, list.get(i).getBankName());
                insertSwift.setString(6, list.get(i).getAddressLine1());
                insertSwift.setString(7, list.get(i).getAddressLine2());
                insertSwift.setString(8, list.get(i).getAddressLine3());
                insertSwift.setString(9, list.get(i).getAddressLine4());
                insertSwift.setString(10, list.get(i).getCityName());
                insertSwift.setString(11, list.get(i).getCountryCode());
                insertSwift.setString(12,list.get(i).getCountryName());
                insertSwift.addBatch();
            }
            insertSwift.executeBatch();
        }
        long finish = System.currentTimeMillis();
        log.info("batchInsert method time = {} ms", finish - start);
    }

    private void hibernateInsert(List<SwiftBankVO> list) {
        long start = System.currentTimeMillis();
        swiftBankRepository.saveAll(list.stream().map(item -> modelMapper.map(item, SwiftBank.class)).collect(Collectors.toList()));
        long finish = System.currentTimeMillis();
        log.info("hibernateInsert method time = {} ms", finish - start);
    }
    @Configuration
    public class AppConfiguration {
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }
}
