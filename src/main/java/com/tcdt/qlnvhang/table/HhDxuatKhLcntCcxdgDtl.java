package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "HH_DXUAT_KH_LCNT_CCXDG_DTL")
@Data
public class HhDxuatKhLcntCcxdgDtl implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_DXUAT_KH_LCNT_CCXDG_DTL";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DXUAT_KH_LCNT_CCXDG_DTL_SEQ")
	@SequenceGenerator(sequenceName = "HH_DXUAT_KH_LCNT_CCXDG_DTL_SEQ", allocationSize = 1, name = "HH_DXUAT_KH_LCNT_CCXDG_DTL_SEQ")
	private Long id;

	String tenTlieu;
	String loaiCanCu;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dataId")
	@JsonManagedReference
	@Where(clause = "data_type='" + HhDxuatKhLcntCcxdgDtl.TABLE_NAME + "'")
	private List<FileDKemJoinDxKhLcntCcxdg> children = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private HhDxuatKhLcntHdr parent;

}
