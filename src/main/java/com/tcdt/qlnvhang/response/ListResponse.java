package com.tcdt.qlnvhang.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ListResponse<T> {
	private int total;
	private int count;
	private List<T> list = new ArrayList<>();

	public int getCount() {
		return this.list.size();
	}
}
