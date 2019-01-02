package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AddConditionalFormatRuleRequest;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.BooleanCondition;
import com.google.api.services.sheets.v4.model.BooleanRule;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.ConditionValue;
import com.google.api.services.sheets.v4.model.ConditionalFormatRule;
import com.google.api.services.sheets.v4.model.FindReplaceRequest;
import com.google.api.services.sheets.v4.model.FindReplaceResponse;
import com.google.api.services.sheets.v4.model.GridCoordinate;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.PivotGroup;
import com.google.api.services.sheets.v4.model.PivotTable;
import com.google.api.services.sheets.v4.model.PivotValue;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.TextFormat;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.UpdateSpreadsheetPropertiesRequest;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.collect.Lists;

public class MySpreadsheetsService {
	private Sheets service;

	public MySpreadsheetsService(Sheets service) {
		this.service = service;
	}

	public BatchGetValuesResponse batchGetValues(String spreadsheetId, List<String> ranges) throws IOException {
		Sheets service = this.service;
		BatchGetValuesResponse result = service.spreadsheets().values().batchGet(spreadsheetId).setRanges(ranges)
				.execute();
		System.out.printf("%d ranges retrieved.", result.getValueRanges().size());
		return result;
	}

	public UpdateValuesResponse updateValues(String spreadsheetId, String range, String valueInputOption,
			List<List<Object>> values) throws IOException {
		Sheets service = this.service;
		
		ValueRange body = new ValueRange().setValues(values);
		UpdateValuesResponse result = service.spreadsheets().values().update(spreadsheetId, range, body)
				.setValueInputOption(valueInputOption).execute();
		return result;
	}
}
