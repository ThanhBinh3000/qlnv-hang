package com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "QLNV_10.QLHDMVT_DIA_DIEM_NHAP_VT")
public class QlhdmvtDiaDiemNhapVt implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", nullable = false)
	private String ID;

	@Column(name = "STT")
	private String STT;

	@Column(name = "DON_VI_ID")
	private String donViId;

	@Column(name = "MA_DON_VI")
	private String maDonVi;

	@Column(name = "SO_LUONG_NHAP")
	private String soLuongNhap;

	@Column(name = "QLHDMVT_DS_GOI_THAU_ID")
	private String qlhdmvtDsGoiThauId;

}
