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
@Table(name = "HH_PHU_LUC_HD")
@Data
public class HhPhuLucHd implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_PHU_LUC_HD_SEQ")
	@SequenceGenerator(sequenceName = "HH_PHU_LUC_HD_SEQ", allocationSize = 1, name = "HH_PHU_LUC_HD_SEQ")
	private Long id;

	String soPluc;
	Date ngayKy;
	Date ngayHluc;
	String veViec;
	Date tuNgayTrDc;
	Date tuNgaySauDc;
	Double soNgayTrcDc;
	Double soNgaySauDc;
	Date denNgayTrDc;
	Date tuNgayTrcDc;
	String soHd;
	String noiDungDc;

}
