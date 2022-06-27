package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = HhDthau.TABLE_NAME)
@Data
public class HhDthau implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_DTHAU";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhDthau.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhDthau.TABLE_NAME + "_SEQ", allocationSize = 1, name = HhDthau.TABLE_NAME
			+ "_SEQ")
	private Long id;

	String soHd;
	String tenHd;
	String soQd;
	String loaiVthh;
	String maDvi;
	String namKhoach;
	String trangThai;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	String ldoTuchoi;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	Date ngayPduyet;
	String nguoiPduyet;
	String ghiChu;

	@Transient
	private List<HhDthauGthau> children = new ArrayList<>();

}
