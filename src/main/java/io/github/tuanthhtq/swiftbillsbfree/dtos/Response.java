package io.github.tuanthhtq.swiftbillsbfree.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author io.github.tuanthhtq
 */

@Getter
@Setter
public class Response<T> {
	private String message;
	private Set<String> errors;
	private int statusCode;
	private T data;
}
