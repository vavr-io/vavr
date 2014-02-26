package javaslang.json;

import static javaslang.json.JSON.JSONArray;
import static javaslang.json.JSON.JSONBoolean;
import static javaslang.json.JSON.JSONNull;
import static javaslang.json.JSON.JSONNumber;
import static javaslang.json.JSON.JSONObject;
import static javaslang.json.JSON.JSONString;
import static javaslang.json.JSON.JSONUndefined;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

public class JSONTest {

	@Test
	public void testName() throws Exception {
		
		JSONBoolean(true);
		JSONNumber(1);
		JSONNumber(1.0d);
		JSONNumber(new BigDecimal("1.0"));
		JSONString("hello");
		
		final JSON json = JSONObject()//
				.bind("firstName", "John")//
				.bind("lastName", "Smith")//
				.bind("address", JSONObject()//
						.bind("streetAddress", "21 2nd Street")//
						.bind("state", "NY")//
						.bind("postalCode", 10021)//
				)//
				.bind("phoneNumbers", JSONArray(//
						JSONObject().bind("type", "home").bind("number", "212 555-1234"),//
						JSONObject().bind("type", "fax").bind("number", "646 555-4567")//
						))//
				.bind("notes", JSONArray(1, "hello", true, new BigDecimal("1E-10"), new Date()))// date is undefined in JSON
				.bind("bool", true)//
				.bind("null", null)//
				.bind("JSONNull", JSONNull)//
				.bind("undefined", JSONUndefined);//

		System.out.println(json.stringify());

	}
}
