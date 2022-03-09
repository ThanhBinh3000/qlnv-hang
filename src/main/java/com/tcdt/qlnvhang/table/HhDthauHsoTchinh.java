package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = HhDthauHsoTchinh.TABLE_NAME)
@Data
public class HhDthauHsoTchinh implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_DTHAU_HSO_TCHINH";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhDthauHsoTchinh.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhDthauHsoTchinh.TABLE_NAME
			+ "_SEQ", allocationSize = 1, name = HhDthauHsoTchinh.TABLE_NAME + "_SEQ")
	private Long id;

	String ten;
	Long diem;
	Long xepHang;
	BigDecimal dgiaDthau;
	BigDecimal giaDthau;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_gt_hdr")
	@JsonBackReference
	private HhDthauGthau parent;

}
