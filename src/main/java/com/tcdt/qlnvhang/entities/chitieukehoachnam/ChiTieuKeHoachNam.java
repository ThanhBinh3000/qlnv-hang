package com.tcdt.qlnvhang.entities.chitieukehoachnam;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = ChiTieuKeHoachNam.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChiTieuKeHoachNam extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 6093365068005372524L;
	public static final String TABLE_NAME = "KH_CHI_TIEU_KE_HOACH_NAM";
	public static final String CAN_CU = "CAN_CU";
	public static final String FILE_DINH_KEM_DATA_TYPE_CAN_CU = String.format("%s_%s",TABLE_NAME, CAN_CU);

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHI_TIEU_KE_HOACH_NAM_SEQ")
	@SequenceGenerator(sequenceName = "CHI_TIEU_KE_HOACH_NAM_SEQ", allocationSize = 1, name = "CHI_TIEU_KE_HOACH_NAM_SEQ")
	private Long id;
	private String soQuyetDinh;
	private LocalDate ngayKy;
	private LocalDate ngayHieuLuc;
	private Integer namKeHoach;
	private String trichYeu;
	private String trangThai;
	private Long donViId;
	private LocalDate ngayGuiDuyet;
	private Long nguoiGuiDuyetId;
	private LocalDate ngayPheDuyet;
	private Long nguoiPheDuyetId;
	private String lyDoTuChoi;
	private String loaiQuyetDinh;
	private boolean latest;

	/**
	 * 	Cuc tao chi tieu ke hoach nam -> chi tieu ke hoach nam cua tong cuc
	 * 	Tong Cuc tao dieu chinh chi tieu ke hoach nam -> chi tieu ke hoach nam cua tong cuc
	 * 	Cuc tao dieu chinh chi tieu ke hoach nam -> chi tieu ke hoach nam cua cuc
	 */
	private Long qdGocId; //ChiTieuKeHoachNam
	private String capDvi;
	private String maDvi;

	/**
	 * Cục tạo điều chỉnh -> điều chỉnh của tổng cục
	 */
	private Long dcChiTieuId;

	@Lob
	private String ghiChu;
}