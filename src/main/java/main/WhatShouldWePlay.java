package main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;

import service.MySpreadsheetsService;
import utils.SheetsServiceUtil;

public class WhatShouldWePlay {
	private static Sheets sheetsService;
	private static String spreadSheetId;
	private static MySpreadsheetsService service;

	private static final Logger logger = LoggerFactory.getLogger(WhatShouldWePlay.class);

	private static final String FIRST_RANGE = "H2:I"; // https://developers.google.com/sheets/api/guides/concepts#a1_notation
	private static final String SECOND_RANGE = "A2:A";

	private static final String PATH = "conf.txt";
	private static final int STARTINGROW = 2;
	private static final int RNG_BOUND = 10000;

	public static void main(String[] args) {
		try {
			sheetsService = SheetsServiceUtil.getSheetsService();

			service = new MySpreadsheetsService(sheetsService);
			List<String> ranges = Arrays.asList(FIRST_RANGE, SECOND_RANGE);
			spreadSheetId = readFromFile(PATH);

			BatchGetValuesResponse result = service.batchGetValues(spreadSheetId, ranges);

			List<List<Object>> valuesFirstRange = result.getValueRanges().get(0).getValues();
			List<List<Object>> valuesSecondRange = result.getValueRanges().get(1).getValues();

			Map<Integer, String> lineNumberAndNames = getLineNumberWithNames(valuesSecondRange);

			Map<Integer, String> writingValues = new HashMap<>();

			boolean lookingForWinner = true;
			int roll = 1;
			while (lookingForWinner) {
				Random rnd = new Random();
				int randomNumber = rnd.nextInt(RNG_BOUND);

				int line = STARTINGROW;

				for (List row : valuesFirstRange) {
					if (writingValues.get(line) == null) {
						writingValues.put(line, "");
					}

					int valueOfColumnH = Integer.valueOf((String) row.get(0));

					if (randomNumber <= valueOfColumnH) {
						writingValues.put(line, writingValues.get(line).concat("|"));
						logger.debug("Wurf: " + roll + " - " + randomNumber);
						logger.debug("Zeile " + line + " " + writingValues.get(line));

						if (writingValues.get(line) != null && isWinner(writingValues.get(line))) {
							writingValues.put(line, winningOutputString(lineNumberAndNames, writingValues, line));
							logger.debug("Winner winner chicken dinner row: " + line);
							lookingForWinner = false;
						}
						++roll;
						break;
					}
					line++;
				}
				Thread.sleep(1000); //for the excitement
				write(writingValues);
			}
		} catch (GeneralSecurityException | IOException | InterruptedException e) {
			logger.error("", e);
		}
	}

	private static boolean isWinner(String s) {
		if (s.equals("|||")) {
			return true;
		} else {
			return false;
		}
	}

	private static void write(Map<Integer, String> writingValues) throws IOException {
		List<List<Object>> values = new ArrayList<>();
		for (int i = 0; i < writingValues.size(); ++i) {
			values.add(Arrays.asList(writingValues.get(i + STARTINGROW)));
		}

		service.updateValues(spreadSheetId, "I2", "USER_ENTERED", values);
	}

	private static String winningOutputString(Map<Integer, String> nameLines, Map<Integer, String> writingValues,
			int line) {
		return writingValues.get(line).concat(" - " + nameLines.get(line - 1));
	}

	private static Map<Integer, String> getLineNumberWithNames(List<List<Object>> names) {
		Map<Integer, String> nameLines = new HashMap<>();
		int i = 0;
		for (List name : names) {
			i++;
			try {
				nameLines.put(i, (String) name.get(0));
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		}
		return nameLines;
	}

	private static String readFromFile(String path) throws IOException {
		Path p = Paths.get(path);
		try {
			return Files.lines(p).findFirst().get();

		} catch (IOException e) {
			logger.error("spreadsheet ID needed.");
			logger.error("Copy your spreadsheet ID in here: " + p.toAbsolutePath());
			Files.writeString(p, "<your spreadsheet ID here>", Charset.forName("UTF-8"));
			e.printStackTrace();
		}
		return null;
	}
}
