package com.callfailures.commontests.utils;

import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONException;
import org.junit.Ignore;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.google.gson.JsonObject;

@Ignore
public class JsonTestUtils {
	public static final String BASE_JSON_DIR = "json/";

	private JsonTestUtils() {
	}

	public static String readJsonFile(final String relativePath) {
		final InputStream inputStream = JsonTestUtils.class.getClassLoader().getResourceAsStream(BASE_JSON_DIR + relativePath);
		try (Scanner scanner = new Scanner(inputStream)) {
			return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
		}
	}

	public static void assertJsonMatchesFileContent(final String actualJson, final String fileNameWithExpectedJson) {
		assertJsonMatchesExpectedJson(actualJson, readJsonFile(fileNameWithExpectedJson));
	}

	public static void assertJsonMatchesExpectedJson(final String actualJson, final String expectedJson) {
		try {
			JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.NON_EXTENSIBLE);
		} catch (final JSONException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Long getIdFromJson(final String json) {
		final JsonObject jsonObject = JsonReader.readAsJsonObject(json);
		return JsonReader.getLongOrNull(jsonObject, "id");
	}

}