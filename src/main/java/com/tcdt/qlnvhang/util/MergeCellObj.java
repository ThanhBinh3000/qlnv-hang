package com.tcdt.qlnvhang.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MergeCellObj {
	private CellRangeAddress cellAddresses;
	private String value;
	private Row row;
	private int firstRow;
	private int lastRow;
	private int firstCol;
	private int lastCol;

	public CellRangeAddress getCellAddresses() {
		return new CellRangeAddress(this.firstRow, this.lastRow, this.firstCol, this.lastCol);
	}
}
