package com.tcdt.qlnvhang.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tcdt.qlnvhang.entities.FileDKemJoinPhuLuc;
import com.tcdt.qlnvhang.util.Contains;

import lombok.Data;

@Entity
@Table(name = HhPhuLucHd.TABLE_NAME)
@Data
public class HhPhuLucHd implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "HH_PHU_LUC_HD";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhPhuLucHd.TABLE_NAME + "_SEQ")
	@SequenceGenerator(sequenceName = HhPhuLucHd.TABLE_NAME + "_SEQ", allocationSize = 1, name = HhPhuLucHd.TABLE_NAME
			+ "_SEQ")
	private Long id;
	String soPluc;
	@Temporal(TemporalType.DATE)
	Date ngayKy;
	@Temporal(TemporalType.DATE)
	Date ngayHluc;
	String veViec;
	Integer tgianThienHdTrc;
	Integer tgianThienHdDc;
	@Temporal(TemporalType.DATE)
	Date tuNgayHlucTrc;
	@Temporal(TemporalType.DATE)
	Date denNgayHlucTrc;
	@Temporal(TemporalType.DATE)
	Date tuNgayHlucDc;
	@Temporal(TemporalType.DATE)
	Date denNgayHlucDc;
	String soHd;
	String tenHd;
	String noiDung;
	String trangThai;
	@Temporal(TemporalType.DATE)
	Date ngayTao;
	String nguoiTao;
	@Temporal(TemporalType.DATE)
	Date ngaySua;
	String nguoiSua;
	String ldoTuchoi;
	@Temporal(TemporalType.DATE)
	Date ngayGuiDuyet;
	String nguoiGuiDuyet;
	@Temporal(TemporalType.DATE)
	Date ngayPduyet;
	String nguoiPduyet;
	String loaiVthh;
	String cloaiVthh;
	String maDvi;
	String ghiChu;
	String noiDungPl;
	@Temporal(TemporalType.DATE)
	Date ngayHlucDc;
	@Temporal(TemporalType.DATE)
	Date ngayHlucTrc;
	@Transient
	private List<FileDinhKem> fileDinhKems;

	@Transient
	private List<HhPhuLucHdDtl> HhPhuLucHdDtl = new ArrayList<>();
	@Transient
	String tenTrangThai;

	public String getTenTrangThai() {
		return TrangThaiAllEnum.getLabelById(trangThai);
	}

}
