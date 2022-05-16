package com.tcdt.qlnvhang.table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HH_SO_KHO_DTL")
@Data
public class HhSoKhoDtl extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_SO_KHO_DTL_SEQ")
	@SequenceGenerator(sequenceName = "HH_SO_KHO_DTL_SEQ", allocationSize = 1, name = "HH_SO_KHO_DTL_SEQ")
	private Long id;
	Long idHdr;
	Date ngayGhi;
	Date ngayChungTu;
	Integer soCtuNhap;
	Integer soCtuXuat;
	String dienGiai;
	Date ngayNhapXuat;
	BigDecimal slNhap;
	BigDecimal slXuat;
	BigDecimal slTon;
	String ghiChu;
/*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hdr")
	@JsonBackReference
	private HhSoKhoHdr parent;
	*/
}
