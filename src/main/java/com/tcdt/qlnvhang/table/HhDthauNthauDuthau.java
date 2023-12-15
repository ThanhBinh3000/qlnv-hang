package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.models.auth.In;
import lombok.Data;

@Entity
@Table(name = HhDthauNthauDuthau.TABLE_NAME)
@Data
public class HhDthauNthauDuthau implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_DTHAU_NTHAU_DUTHAU";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhDthauNthauDuthau.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhDthauNthauDuthau.TABLE_NAME
			+ "_SEQ", allocationSize = 1, name = HhDthauNthauDuthau.TABLE_NAME + "_SEQ")
	private Long id;
	private Long idNhaThau;
	String tenNhaThau;
	String mst;
	String diaChi;
	String sdt;
	BigDecimal soLuong;
	//Gia du thau
	BigDecimal donGia;
	String trangThai;
	@Transient
	String tenTrangThai;
	@Transient
	String giaDuThau;
	String lyDo;

	private Long idDtGt;
	private String type;

}
