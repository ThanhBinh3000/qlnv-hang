package com.tcdt.qlnvhang.table;

import java.io.Serializable;

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

	String tenDvi;
	String mst;
	String diaChi;
	String sdt;
	private Integer version;

	private Long idDtGt;

}
