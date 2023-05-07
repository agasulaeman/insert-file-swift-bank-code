package com.permatabank.insertbatchfile;

import com.permatabank.insertbatchfile.service.BatchInsert;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class InsertbatchfileApplication implements CommandLineRunner {

	final
	BatchInsert batchInsert;

	@Value("${filePath}")
	private String filePath;

	public InsertbatchfileApplication(BatchInsert batchInsert) {
		this.batchInsert = batchInsert;
	}

	public static void main(String[] args) {
		SpringApplication.run(InsertbatchfileApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		batchInsert.processData(filePath);
	}


}
