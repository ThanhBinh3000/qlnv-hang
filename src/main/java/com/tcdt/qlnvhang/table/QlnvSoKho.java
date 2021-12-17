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
@Table(name = "QLNV_SO_KHO")
@Data
public class QlnvSoKho implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_SO_KHO_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_SO_KHO_SEQ", allocationSize = 1, name = "QLNV_SO_KHO_SEQ")
	private Long id;
	String soKho;
	Date ngayLap;
	String maHhoa;
	String tenSo;
	String maKho;
	String maNgan;
	String maLo;
	String maDvi;
	String dviTinh;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;

	
}
