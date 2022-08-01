package com.tcdt.qlnvhang.entities.nhaphang.bbanlaymau;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "NH_BB_BAN_GIAO_MAU")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BienBanBanGiaoMau extends TrangThaiBaseEntity implements Serializable {
	private static final long serialVersionUID = 6093365068005372524L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_BAN_GIAO_MAU_SEQ")
	@SequenceGenerator(sequenceName = "BB_BAN_GIAO_MAU_SEQ", allocationSize = 1, name = "BB_BAN_GIAO_MAU_SEQ")
	private Long id;
	private Long qdgnvnxId;
	private Long bbLayMauId;
	private String soBienBan;
	private LocalDate ngayBanGiaoMau;

	private String maVatTuCha;
	private String maVatTu;
	private String tenDviBenNhan;
	private Integer soLuongMau;
	private String chiTieuKiemTra;
	private String ttNiemPhongMauHang;
	private String diaDiemBanGiao;

	private Long nguoiGuiDuyetId;
	private LocalDate ngayGuiDuyet;
	private Long nguoiPduyetId;
	private LocalDate ngayPduyet;
	private String trangThai;
	private String maDvi;
	private String capDvi;

	private Integer so;
	private Integer nam;
	private String loaiVthh;
	private Long hopDongId;
	@Transient
	private List<BienBanBanGiaoMauCt> chiTiets = new ArrayList<>();
}
