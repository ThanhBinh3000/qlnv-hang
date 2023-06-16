package com.tcdt.qlnvhang.table.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
public class HhDxKhlcntDsgthauReport {
	private static int currentStt = 0;
	private int stt;
	private String goiThau;
	private int soLuong;
	private BigDecimal thanhTien;
	private String diaDiemNhapKho;
	private String ghiChu;

	@Override
	public String toString() {
		currentStt = 0;
		return
				stt + "," + goiThau +"," + soLuong + "," + thanhTien + "," + diaDiemNhapKho + "," + ghiChu;
	}

	public HhDxKhlcntDsgthauReport() {
		this.stt = ++currentStt;
	}

}