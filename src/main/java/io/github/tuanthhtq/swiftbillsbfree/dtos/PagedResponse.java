package io.github.tuanthhtq.swiftbillsbfree.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author io.github.tuanthhtq
 */

@Getter
@Setter
public class PagedResponse<T> extends Response<T> {
	public PagedResponse() {
		super();
	}

	private int pageNo;
	private int pageSize;
	private int totalPages;
}
