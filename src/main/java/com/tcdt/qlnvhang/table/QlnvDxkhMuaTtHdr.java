package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "QLNV_DXKH_MUA_TT_HDR")
@Data
public class QlnvDxkhMuaTtHdr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_DXKH_MUA_TT_HDR_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_DXKH_MUA_TT_HDR_SEQ", allocationSize = 1, name = "QLNV_DXKH_MUA_TT_HDR_SEQ")
	private Long id;
	String soDxuat;
	String maDvi;
	String soQdKhoach;
	Date ngayLap;
	String maHhoa;
	String tenHhoa;
	Date tuNgayThien;
	Date denNgayThien;
	String diaDiem;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	String ldoTuchoi;
	Date ngayPduyet;
	String nguoiPduyet;
	String trangThai;
	@OneToMany(mappedBy = "header", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QlnvDxkhMuaTtDtl> detailList = new ArrayList<QlnvDxkhMuaTtDtl>();

	public void setDetailList(List<QlnvDxkhMuaTtDtl> detail) {
		if (this.detailList == null) {
			this.detailList = detail;
		} else if (this.detailList != detail) {
			this.detailList.clear();
			if (detail != null) {
				this.detailList.addAll(detail);
			}
		}
	}

	public QlnvDxkhMuaTtHdr addDetail(QlnvDxkhMuaTtDtl dt) {
		detailList.add(dt);
		dt.setHeader(this);
		return this;
	}

	public QlnvDxkhMuaTtHdr removeDetail(QlnvDxkhMuaTtDtl dt) {
		detailList.remove(dt);
		dt.setHeader(null);
		return this;
	}
}
