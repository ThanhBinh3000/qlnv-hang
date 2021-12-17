package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "QLNV_SO_THEODOI")
@Data
public class QlnvSoTheoDoi implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_SO_THEODOI_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_SO_THEODOI_SEQ", allocationSize = 1, name = "QLNV_SO_THEODOI_SEQ")
	private Long id;
	String soTheodoi;
	Date ngayLap;
	String maHhoa;
	String tenSo;
	String maKho;
	String maNgan;
	String maLo;
	String maDvi;
	String hthucBquan;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;

	
}
