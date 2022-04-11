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

import lombok.Data;

@Entity
@Table(name = "HH_DVI_LQUAN")
@Data
public class HhDviLquan implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DVI_LQUAN_SEQ")
	@SequenceGenerator(sequenceName = "HH_DVI_LQUAN_SEQ", allocationSize = 1, name = "HH_DVI_LQUAN_SEQ")
	private Long id;

	String ma;
	String ten;
	String diaChi;
	String sdt;
	String stk;
	String mst;
	String tenNguoiDdien;
	String chucVu;
	String type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private HhHopDongHdr parent;

}
