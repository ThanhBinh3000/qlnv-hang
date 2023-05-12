package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbKeHoachDcDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbKeHoachDcHdrRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbQuyetDinhDcCDtlRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.DcnbQuyetDinhDcCHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbQuyetDinhDcCHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbQuyetDinhDcC;
import com.tcdt.qlnvhang.service.feign.LuuKhoClient;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DcnbQuyetDinhDcCDtlService extends BaseServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(DcnbQuyetDinhDcCDtlService.class);

    @Autowired
    private DcnbQuyetDinhDcCHdrRepository dcnbQuyetDinhDcCHdrRepository;

    @Autowired
    private DcnbQuyetDinhDcCDtlRepository dcnbQuyetDinhDcCDtlRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;
    @Autowired
    private DcnbKeHoachDcHdrRepository dcnbKeHoachDcHdrRepository;
    @Autowired
    private LuuKhoClient luuKhoClient;

    public Page<DcnbQuyetDinhDcCHdr> searchPage(CustomUserDetails currentUser, SearchDcnbQuyetDinhDcC req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbQuyetDinhDcCHdr> search = dcnbQuyetDinhDcCHdrRepository.search(req, pageable);
        return search;
    }

    @Transactional
    public DcnbQuyetDinhDcCHdr save(CustomUserDetails currentUser, DcnbQuyetDinhDcCHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbQuyetDinhDcCHdr> optional = dcnbQuyetDinhDcCHdrRepository.findFirstBySoQdinh(objReq.getSoQdinh());
        if (optional.isPresent() && objReq.getSoQdinh().split("/").length == 1) {
            throw new Exception("số quyết định đã tồn tại");
        }
        DcnbQuyetDinhDcCHdr data = new DcnbQuyetDinhDcCHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getDvql());
        data.setTrangThai(Contains.DUTHAO);
        for (DcnbQuyetDinhDcCDtl e : objReq.getDanhSachQuyetDinh()) {
            if (Contains.DCNB.equals(data.getLoaiDc()) && Contains.CAP_CUC.equals(currentUser.getUser().getCapDvi())) {
                // được phép thêm mới kế hoạch và update kế hoạch (ngầm)
                if (e.getKeHoachDcHdrId() == null) {
                    if (e.getDanhSachKeHoach() != null) {
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdr = new DcnbKeHoachDcHdr();
                        dcnbKeHoachDcHdr.setType(Contains.NHAN_DIEU_CHUYEN_TS);
                        dcnbKeHoachDcHdr.setMaDviPq(e.getDanhSachKeHoach().get(0).getMaChiCucNhan());
                        dcnbKeHoachDcHdr.setPhuongAnDieuChuyen(new ArrayList<>());
                        BigDecimal total = e.getDanhSachKeHoach().stream().map(DcnbKeHoachDcDtl::getDuToanKphi)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        dcnbKeHoachDcHdr.setTongDuToanKp(total);
                        e.getDanhSachKeHoach().forEach(e1 -> {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                            e1.setDaXdinhDiemNhap(true);
                        });
                        dcnbKeHoachDcHdr.setDaXdinhDiemNhap(true);
                        dcnbKeHoachDcHdr.setDanhSachHangHoa(e.getDanhSachKeHoach());
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdrNew = dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr);
                        e.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdrNew);
                        e.setKeHoachDcHdrId(dcnbKeHoachDcHdrNew.getId());
                    }
                } else {
                    throw new Exception("dcnbKeHoachDcHdr.id phải là null!");
                }
            }
            e.setDcnbQuyetDinhDcCHdr(data);
        }
        DcnbQuyetDinhDcCHdr created = dcnbQuyetDinhDcCHdrRepository.save(data);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU");
        List<FileDinhKem> quyetDinh = fileDinhKemService.saveListFileDinhKem(objReq.getQuyetDinh(), created.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH");
        created.setCanCu(canCu);
        created.setQuyetDinh(quyetDinh);
        return created;
    }

    @Transactional
    public DcnbQuyetDinhDcCHdr update(CustomUserDetails currentUser, DcnbQuyetDinhDcCHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbQuyetDinhDcCHdr> optional = dcnbQuyetDinhDcCHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<DcnbQuyetDinhDcCHdr> soDxuat = dcnbQuyetDinhDcCHdrRepository.findFirstBySoQdinh(objReq.getSoQdinh());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoQdinh())) {
            if (soDxuat.isPresent() && objReq.getSoQdinh().split("/").length == 1) {
                if (!soDxuat.get().getId().equals(objReq.getId())) {
                    throw new Exception("số quyết định đã tồn tại");
                }
            }
        }

        DcnbQuyetDinhDcCHdr data = optional.get();
        objReq.setMaDvi(data.getMaDvi());
        BeanUtils.copyProperties(objReq, data);
        data.setDanhSachQuyetDinh(objReq.getDanhSachQuyetDinh());
        for (DcnbQuyetDinhDcCDtl e : objReq.getDanhSachQuyetDinh()) {
            if (Contains.DCNB.equals(data.getLoaiDc()) && Contains.CAP_CUC.equals(currentUser.getUser().getCapDvi())) {
                // được phép thêm mới kế hoạch và update kế hoạch (ngầm)
                if (e.getKeHoachDcHdrId() != null) {
                    if (e.getDanhSachKeHoach() != null) {
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdr = new DcnbKeHoachDcHdr();
                        dcnbKeHoachDcHdr.setType(Contains.NHAN_DIEU_CHUYEN_TS);
                        dcnbKeHoachDcHdr.setMaDviPq(e.getDanhSachKeHoach().get(0).getMaChiCucNhan());
                        dcnbKeHoachDcHdr.setPhuongAnDieuChuyen(new ArrayList<>());
                        Optional<DcnbKeHoachDcHdr> keHoachDcHdrOpt = dcnbKeHoachDcHdrRepository.findById(e.getKeHoachDcHdrId());
                        if (!keHoachDcHdrOpt.isPresent()) {
                            throw new Exception("dcnbKeHoachDcHdr.id không tìm thấy trong hệ thống!");
                        }
                        dcnbKeHoachDcHdr.setId(keHoachDcHdrOpt.get().getId());
                        dcnbKeHoachDcHdr.setType(Contains.NHAN_DIEU_CHUYEN_TS);
                        dcnbKeHoachDcHdr.setMaDviPq(e.getDcnbKeHoachDcHdr().getMaDvi());

                        BigDecimal total = e.getDanhSachKeHoach().stream().map(DcnbKeHoachDcDtl::getDuToanKphi)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        dcnbKeHoachDcHdr.setTongDuToanKp(total);
                        e.getDanhSachKeHoach().forEach(e1 -> {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                            e1.setDaXdinhDiemNhap(true);
                        });
                        dcnbKeHoachDcHdr.setDaXdinhDiemNhap(true);
                        dcnbKeHoachDcHdr.setDanhSachHangHoa(e.getDanhSachKeHoach());
                        dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr);
                    }
                } else {
                    if (e.getDanhSachKeHoach() != null) {
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdr = new DcnbKeHoachDcHdr();
                        dcnbKeHoachDcHdr.setType(Contains.NHAN_DIEU_CHUYEN_TS);
                        dcnbKeHoachDcHdr.setMaDviPq(e.getDanhSachKeHoach().get(0).getMaDiemKhoNhan());
                        dcnbKeHoachDcHdr.setPhuongAnDieuChuyen(new ArrayList<>());

                        BigDecimal total = e.getDanhSachKeHoach().stream().map(DcnbKeHoachDcDtl::getDuToanKphi)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        dcnbKeHoachDcHdr.setTongDuToanKp(total);
                        e.getDanhSachKeHoach().forEach(e1 -> {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                            e1.setDaXdinhDiemNhap(true);
                        });
                        dcnbKeHoachDcHdr.setDaXdinhDiemNhap(true);
                        dcnbKeHoachDcHdr.setDanhSachHangHoa(e.getDanhSachKeHoach());
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdrNew = dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr);
                        e.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdrNew);
                        e.setKeHoachDcHdrId(dcnbKeHoachDcHdrNew.getId());
                    }
                }
            } else if (Contains.GIUA_2_CUC_DTNN_KV.equals(data.getLoaiDc()) && Contains.QD_NHAP.equals(data.getLoaiQdinh()) && Contains.CAP_CHI_CUC.equals(currentUser.getUser().getCapDvi())) {
                // được phép update kế hoạch (ngầm)
                DcnbKeHoachDcHdr dcnbKeHoachDcHdr = e.getDcnbKeHoachDcHdr();
                if (dcnbKeHoachDcHdr.getId() != null) {
                    Optional<DcnbKeHoachDcHdr> keHoachDcHdrOpt = dcnbKeHoachDcHdrRepository.findById(dcnbKeHoachDcHdr.getId());
                    if (!keHoachDcHdrOpt.isPresent()) {
                        throw new Exception("dcnbKeHoachDcHdr.id không tìm thấy trong hệ thống!");
                    }
                    if (e.getDanhSachKeHoach() != null) {
                        e.getDanhSachKeHoach().forEach(e1 -> {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                            e1.setDaXdinhDiemNhap(true);
                        });
                    }
                    dcnbKeHoachDcHdr.setDanhSachHangHoa(e.getDanhSachKeHoach());
                    dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr);
                } else {
                    throw new Exception("dcnbKeHoachDcHdr.id phải khác null!");
                }
            }
            e.setDcnbQuyetDinhDcCHdr(data);
        }
        DcnbQuyetDinhDcCHdr created = dcnbQuyetDinhDcCHdrRepository.save(data);

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU");

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH"));
        List<FileDinhKem> quyetDinh = fileDinhKemService.saveListFileDinhKem(objReq.getQuyetDinh(), created.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH");
        created.setCanCu(canCu);
        created.setQuyetDinh(quyetDinh);
        return created;
    }


    public List<DcnbQuyetDinhDcCHdr> detail(List<Long> ids) throws Exception {
        if (DataUtils.isNullOrEmpty(ids))
            throw new Exception("Tham số không hợp lệ.");
        List<DcnbQuyetDinhDcCHdr> optional = dcnbQuyetDinhDcCHdrRepository.findByIdIn(ids);
        if (DataUtils.isNullOrEmpty(optional)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        optional.forEach(data -> {
            List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU"));
            data.setCanCu(canCu);
            List<FileDinhKem> quyetDinh = fileDinhKemService.search(data.getId(), Arrays.asList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH"));
            data.setQuyetDinh(quyetDinh);

            List<DcnbQuyetDinhDcCDtl> sachQuyetDinh = data.getDanhSachQuyetDinh();
            sachQuyetDinh.forEach(data1 -> {
                if (data1.getKeHoachDcHdrId() != null) {
                    Optional<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcnbKeHoachDcHdrRepository.findById(data1.getKeHoachDcHdrId());
                    if (dcnbKeHoachDcHdr.isPresent()) {
                        data1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr.get());
                    }
                    List<DcnbKeHoachDcDtl> khs = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrId(data1.getKeHoachDcHdrId());
                    data1.setDanhSachKeHoach(khs);
                }
            });
        });
        return optional;
    }

    public DcnbQuyetDinhDcCHdr detail(Long id) throws Exception {
        List<DcnbQuyetDinhDcCHdr> details = detail(Arrays.asList(id));
        return details.isEmpty() ? null : details.get(0);
    }

    @Transient
    public void delete(IdSearchReq idSearchReq) throws Exception {
        Optional<DcnbQuyetDinhDcCHdr> optional = dcnbQuyetDinhDcCHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        DcnbQuyetDinhDcCHdr data = optional.get();
        List<DcnbQuyetDinhDcTcDtl> list = dcnbQuyetDinhDcCDtlRepository.findByHdrId(data.getId());
        dcnbQuyetDinhDcCDtlRepository.deleteAll(list);
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.delete(data.getId(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH"));
        dcnbQuyetDinhDcCHdrRepository.delete(data);
    }

    @Transient
    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        List<DcnbQuyetDinhDcCHdr> list = dcnbQuyetDinhDcCHdrRepository.findAllByIdIn(idSearchReq.getIdList());

        if (list.isEmpty()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        List<Long> listId = list.stream().map(DcnbQuyetDinhDcCHdr::getId).collect(Collectors.toList());
        List<DcnbQuyetDinhDcTcDtl> listDtl = dcnbQuyetDinhDcCDtlRepository.findByHdrIdIn(listId);
        dcnbQuyetDinhDcCDtlRepository.deleteAll(listDtl);
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU"));
        fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH"));
        dcnbQuyetDinhDcCHdrRepository.deleteAll(list);
    }

    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
        if (StringUtils.isEmpty(statusReq.getId())) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        DcnbQuyetDinhDcCHdr details = detail(Long.valueOf(statusReq.getId()));
        Optional<DcnbQuyetDinhDcCHdr> optional = Optional.of(details);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
    }

    public DcnbQuyetDinhDcCHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbQuyetDinhDcCHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.CHODUYET_TP:
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_TP:
                optional.get().setNgayGduyet(LocalDate.now());
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
                optional.get().setNgayDuyetTc(LocalDate.now());
                optional.get().setNguoiDuyetTcId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDC + Contains.BAN_HANH:
                optional.get().setNgayDuyetTc(LocalDate.now());
                optional.get().setNguoiDuyetTcId(currentUser.getUser().getId());

                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbQuyetDinhDcCHdr created = dcnbQuyetDinhDcCHdrRepository.save(optional.get());
        return created;
    }

    public void export(CustomUserDetails currentUser, SearchDcnbQuyetDinhDcC objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<DcnbQuyetDinhDcCHdr> page = this.searchPage(currentUser, objReq);
        List<DcnbQuyetDinhDcCHdr> data = page.getContent();

        String title = "Danh sách phương án xuất cứu trợ, viện trợ ";
        String[] rowsName = new String[]{"STT", "Năm kH", "Số công văn/đề xuất", "Ngày duyệt LĐ Cục", "Loại điều chuyển", "Đơn vị đề xuất", "Trạng thái",};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbQuyetDinhDcCHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNam();
            objs[2] = dx.getSoQdinh();
            objs[3] = dx.getNgayDuyetTc();
            objs[4] = dx.getLoaiDc();
            objs[5] = dx.getTenDvi();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }
}
