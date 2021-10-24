package io.github.albertus82.util;

import java.util.TimeZone;

public interface Jsonable {

	TimeZone defaultTimeZone = TimeZone.getDefault();

	String toJson();

}
