package it.albertus.util;

import java.util.TimeZone;

public interface Jsonable {

	TimeZone defaultTimeZone = TimeZone.getDefault();

	String toJson();

}
