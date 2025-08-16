package org.group4.dvdshopbackend.core.api;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PagedRes<T> {
	private List<T> content;
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	private boolean hasPrev;
	private boolean hasNext;
	private boolean isFirst;
	private boolean isLast;

	public PagedRes(Page<T> page) {
		this.content = page.getContent();
		this.page = page.getNumber() + 1;
		this.size = page.getSize();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();

		this.hasPrev = page.hasPrevious();
		this.hasNext = page.hasNext();
		this.isFirst = page.isFirst();
		this.isLast = page.isLast();
	}
}
