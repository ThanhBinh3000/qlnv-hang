package com.tcdt.qlnvhang.service.dieuchuyennoibo.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcdt.qlnvhang.enums.EnumResponse;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbQuyetDinhDcCHdrReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.SearchDcnbQuyetDinhDcC;
import com.tcdt.qlnvhang.request.feign.TrangThaiHtReq;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.response.BaseResponse;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbQuyetDinhDcCHdrDTO;
import com.tcdt.qlnvhang.response.feign.KtMlk;
import com.tcdt.qlnvhang.response.feign.TrangThaiHtResponce;
import com.tcdt.qlnvhang.service.feign.KhoClient;
import com.tcdt.qlnvhang.service.feign.LuuKhoClient;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


@Service
public class DcnbQuyetDinhDcCHdrServiceImpl extends BaseServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(DcnbQuyetDinhDcCHdrServiceImpl.class);

    @Autowired
    private DcnbQuyetDinhDcCHdrRepository dcnbQuyetDinhDcCHdrRepository;
    @Autowired
    private DcnbQuyetDinhDcTcHdrRepository dcnbQuyetDinhDcTcHdrRepository;
    @Autowired
    private DcnbQuyetDinhDcCDtlRepository dcnbQuyetDinhDcCDtlRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;
    @Autowired
    private DcnbKeHoachDcDtlRepository dcnbKeHoachDcDtlRepository;
    @Autowired
    private DcnbPhuongAnDcRepository dcnbPhuongAnDcRepository;
    @Autowired
    private DcnbKeHoachDcHdrRepository dcnbKeHoachDcHdrRepository;
    @Autowired
    private KhoClient khoClient;
    @Autowired
    private DcnbKeHoachDcHdrServiceImpl dcnbKeHoachDcHdrServiceImpl;
    @Autowired
    private QlnvDmDonviRepository qlnvDmDonviRepository;
    @Autowired
    private DcnbDataLinkHdrRepository dcnbDataLinkHdrRepository;

    public Page<DcnbQuyetDinhDcCHdr> searchPage(CustomUserDetails currentUser, SearchDcnbQuyetDinhDcC req) throws Exception {
        String dvql = currentUser.getDvql();
        req.setMaDvi(dvql);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<DcnbQuyetDinhDcCHdr> search = null;
        req.setTypes(Arrays.asList(Contains.NHAN_DIEU_CHUYEN, Contains.DIEU_CHUYEN));
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            search = dcnbQuyetDinhDcCHdrRepository.searchChiCuc(req, pageable);
        } else {
            search = dcnbQuyetDinhDcCHdrRepository.searchCuc(req, pageable);
        }

        search.forEach(item -> {
            if (item.getDanhSachQuyetDinh() != null && !item.getDanhSachQuyetDinh().isEmpty()) {
                List<DcnbQuyetDinhDcCDtl> danhSachQuyetDinh = item.getDanhSachQuyetDinh();
                danhSachQuyetDinh.forEach(i -> {
                    if (!Objects.isNull(i.getKeHoachDcHdrId())) {
                        Optional<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcnbKeHoachDcHdrRepository.findById(i.getKeHoachDcHdrId());
                        if (dcnbKeHoachDcHdr.isPresent()) {
                            i.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr.get());
                        }
                    }
                });
            }
        });
        return search;
    }

    public DcnbQuyetDinhDcCHdr save(CustomUserDetails currentUser, DcnbQuyetDinhDcCHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
            throw new Exception("Chức năng thêm mới chỉ dành cho cấp cục");
        }
        List<DcnbQuyetDinhDcCHdr> optionalList = dcnbQuyetDinhDcCHdrRepository.findBySoQdinh(objReq.getSoQdinh());
        if (!optionalList.isEmpty()) {
            throw new Exception("số quyết định đã tồn tại");
        }
        DcnbQuyetDinhDcCHdr data = new DcnbQuyetDinhDcCHdr();
        BeanUtils.copyProperties(objReq, data);
        data.setMaDvi(currentUser.getDvql());
        data.setTrangThai(Contains.DUTHAO);
        data.setType(null);
        BigDecimal total = new BigDecimal(0l);
        BigDecimal totalPd = new BigDecimal(0l);
        if (data.getDanhSachQuyetDinh() == null || data.getDanhSachQuyetDinh().isEmpty()) {
            throw new Exception("DanhSachQuyetDinh không được để trống!");
        }
        for (DcnbQuyetDinhDcCDtl e : data.getDanhSachQuyetDinh()) {
            if (Contains.DCNB.equals(data.getLoaiDc()) && Contains.CAP_CUC.equals(currentUser.getUser().getCapDvi())) {
                // được phép thêm mới kế hoạch và update kế hoạch (ngầm)
                if (e.getKeHoachDcHdrId() == null) {
                    if (e.getDcnbKeHoachDcHdr().getDanhSachHangHoa() != null && !e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().isEmpty()) {
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdr = new DcnbKeHoachDcHdr();
                        dcnbKeHoachDcHdr.setType(Contains.NHAN_DIEU_CHUYEN_TS);
                        dcnbKeHoachDcHdr.setLoaiDc(Contains.DCNB);
                        dcnbKeHoachDcHdr.setMaDviPq(e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().get(0).getMaChiCucNhan());
                        dcnbKeHoachDcHdr.setPhuongAnDieuChuyen(new ArrayList<>());
                        for (DcnbKeHoachDcDtl e1 : e.getDcnbKeHoachDcHdr().getDanhSachHangHoa()) {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                            e1.setDaXdinhDiemNhap(true);
                            e1.setDuToanKphi(e1.getDuToanKphi() == null ? new BigDecimal(0) : e1.getDuToanKphi());
                            if (e1.getDonViTinh() == null) {
                                throw new Exception("Đơn vị tính không được để trống!");
                            }
                        }

                        BigDecimal totalDuT = e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().stream()
                                .map(DcnbKeHoachDcDtl::getDuToanKphi)
                                .map(kphi -> kphi != null ? kphi : BigDecimal.ZERO)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        dcnbKeHoachDcHdr.setTongDuToanKp(totalDuT);
                        total = total.add(totalDuT);
                        dcnbKeHoachDcHdr.setDaXdinhDiemNhap(true);
                        // check hàng hóa trong kho xuất, kho nhận
                        // Check số lượng hiện thời từng lo kho - (tổng đề kế hoạch xuất - tổng xuất trong thực tế)> 0 ;
                        for (DcnbKeHoachDcDtl hh : e.getDcnbKeHoachDcHdr().getDanhSachHangHoa()) {
                            hh.setCoLoKho(!StringUtils.isEmpty(hh.getMaLoKho()));
                            if (!StringUtils.isEmpty(hh.getMaNganKho())) {
                                KtMlk tinKho = getThongTinKho(hh.getMaLoKho(), hh.getTenLoKho(), hh.getMaNganKho(), hh.getTenNganKho());
                                hh.setLoaiVthh(tinKho.getObject().getLoaiVthh());
                                hh.setCloaiVthh(tinKho.getObject().getCloaiVthh());
                            }
                            hh.setCoLoKhoNhan(!StringUtils.isEmpty(hh.getMaLoKhoNhan()));
                            if (!StringUtils.isEmpty(hh.getMaNganKhoNhan())) {
                                KtMlk tinKho = getThongTinKho(hh.getMaLoKhoNhan(), hh.getTenLoKhoNhan(), hh.getMaNganKhoNhan(), hh.getTenNganKhoNhan());
                                if (!hh.getCloaiVthh().equals(tinKho.getObject().getCloaiVthh())) {
                                    throw new Exception("Chủng loại hàng hóa kho xuất và nhận không giống nhau!");
                                }
                            }
                        }
                        dcnbKeHoachDcHdr.setDanhSachHangHoa(e.getDcnbKeHoachDcHdr().getDanhSachHangHoa());
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdrNew = dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr);
                        e.setKeHoachDcHdrId(dcnbKeHoachDcHdrNew.getId());

                    } else {
                        throw new Exception("DanhSachKeHoach không được để trống! ");
                    }
                } else {
                    throw new Exception("dcnbKeHoachDcHdr.id phải là null!");
                }
            } else {
                if (e.getKeHoachDcHdrId() == null) {
                    if (e.getDcnbKeHoachDcHdr().getDanhSachHangHoa() != null && !e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().isEmpty()) {
                        Optional<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcnbKeHoachDcHdrRepository.findById(e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().get(0).getHdrId());
                        if (!dcnbKeHoachDcHdr.isPresent()) {
                            throw new Exception("Không tìm thấy kế hoạch id = " + e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().get(0).getHdrId());
                        }
                        for (DcnbKeHoachDcDtl e1 : e.getDcnbKeHoachDcHdr().getDanhSachHangHoa()) {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr.get());
                            e1.setThayDoiThuKho(true);
                            Optional<DcnbKeHoachDcDtl> dcnbKeHoachDcDtl = dcnbKeHoachDcDtlRepository.findById(e1.getHdrId());
                            if (dcnbKeHoachDcDtl.isPresent()) {
                                e1.setParentId(dcnbKeHoachDcDtl.get().getParentId());
                            }
                            if (e1.getDonViTinh() == null) {
                                throw new Exception("Đơn vị tính không được để trống!");
                            }
                            BigDecimal totalDuT = e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().stream()
                                    .map(DcnbKeHoachDcDtl::getDuToanKphi)
                                    .map(kphi -> kphi != null ? kphi : BigDecimal.ZERO)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            total = total.add(totalDuT);

                            BigDecimal totalDuTPd = e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().stream()
                                    .map(DcnbKeHoachDcDtl::getDuToanKphiPd)
                                    .map(kphi -> kphi != null ? kphi : BigDecimal.ZERO)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            totalPd=  totalPd.add(totalDuTPd);
                        }
                        // check hàng hóa trong kho xuất, kho nhận
                        // Check số lượng hiện thời từng lo kho - (tổng đề kế hoạch xuất - tổng xuất trong thực tế)> 0 ;
                        for (DcnbKeHoachDcDtl hh : e.getDcnbKeHoachDcHdr().getDanhSachHangHoa()) {
                            hh.setCoLoKho(!StringUtils.isEmpty(hh.getMaLoKho()));
                            if (!StringUtils.isEmpty(hh.getMaNganKho())) {
                                KtMlk tinKho = getThongTinKho(hh.getMaLoKho(), hh.getTenLoKho(), hh.getMaNganKho(), hh.getTenNganKho());
                                hh.setLoaiVthh(tinKho.getObject().getLoaiVthh());
                                hh.setCloaiVthh(tinKho.getObject().getCloaiVthh());
                            }
                            hh.setCoLoKhoNhan(!StringUtils.isEmpty(hh.getMaLoKhoNhan()));
                            if (!StringUtils.isEmpty(hh.getMaNganKhoNhan())) {
                                KtMlk tinKho = getThongTinKho(hh.getMaLoKhoNhan(), hh.getTenLoKhoNhan(), hh.getMaNganKhoNhan(), hh.getTenNganKhoNhan());
                                if (!hh.getCloaiVthh().equals(tinKho.getObject().getCloaiVthh())) {
                                    throw new Exception("Chủng loại hàng hóa kho xuất và nhận không giống nhau!");
                                }
                            }
                        }
                        dcnbKeHoachDcHdr.get().setDanhSachHangHoa(e.getDcnbKeHoachDcHdr().getDanhSachHangHoa());
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdrNew = dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr.get());
                        e.setKeHoachDcHdrId(dcnbKeHoachDcHdrNew.getId());

                    } else {
                        throw new Exception("DanhSachKeHoach không được để trống! ");
                    }
                } else {
                    throw new Exception("dcnbKeHoachDcHdr.id phải là null!");
                }
            }
            e.setDcnbQuyetDinhDcCHdr(data);
        }
        data.setTongDuToanKp(total);
//        data.setTongDuToanKpPd(totalPd);
        List<Long> longs = data.getDanhSachQuyetDinh().stream().map(DcnbQuyetDinhDcCDtl::getKeHoachDcHdrId).collect(Collectors.toList());

        List<Long> duplicates = longs.stream()
                .filter(number -> Collections.frequency(longs, number) > 1)
                .distinct()
                .collect(Collectors.toList());
        if (!duplicates.isEmpty()) {
            throw new Exception("Mỗi danhSachQuyetDinh chỉ có 1 keHoachDcHdrId!");
        }
        DcnbQuyetDinhDcCHdr created = dcnbQuyetDinhDcCHdrRepository.save(data);
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU");
        List<FileDinhKem> quyetDinh = fileDinhKemService.saveListFileDinhKem(objReq.getQuyetDinh(), created.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH");
        created.setCanCu(canCu);
        created.setQuyetDinh(quyetDinh);
        return created;
    }

    private KtMlk getThongTinKho(String maLoKho, String tenLoKho, String maNganKho, String tenNganKho) throws Exception {
        TrangThaiHtReq trangThaiHtReq = new TrangThaiHtReq();
        trangThaiHtReq.setMaDvi(!StringUtils.isEmpty(maLoKho) ? maLoKho : maNganKho);
        trangThaiHtReq.setTenDvi(!StringUtils.isEmpty(maLoKho) ? tenLoKho : tenNganKho);
        ResponseEntity<BaseResponse> responseNhan = khoClient.infoMlk(trangThaiHtReq);
        BaseResponse body = responseNhan.getBody();
        if (body != null && EnumResponse.RESP_SUCC.getDescription().equals(body.getMsg())) {
            logger.debug(body.toString());
            Gson gson = new Gson();
            KtMlk res = gson.fromJson(gson.toJson(body.getData()), KtMlk.class);
            if (res == null) {
                throw new Exception("Không tìm thấy thông tin kho! Ngăn/Lô: " + trangThaiHtReq.getTenDvi());
            }
            if (res.getObject().getCloaiVthh() == null) {
                throw new Exception("Chưa khởi tạo kho đầu kì! Ngăn/Lô: " + trangThaiHtReq.getTenDvi());
            }
            return res;
        } else {
            throw new Exception("Không tìm thấy thông tin kho! Ngăn/Lô: " + trangThaiHtReq.getTenDvi());
        }
    }

    public List<DcnbQuyetDinhDcCHdrDTO> danhSachQuyetDinhChiCuc(CustomUserDetails currentUser, SearchDcnbQuyetDinhDcC req) throws Exception {
        // 2 trường hợp
        // trường 1: chi cục lấy ra quyết định của cục
        // trường 2: cục lấy ra quyết định của cục nhưng phải được bh biên bản lấy mẫu
        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            String dvql = currentUser.getDvql();
            req.setMaDvi(dvql);
            req.setTypes(Arrays.asList(Contains.NHAN_DIEU_CHUYEN, Contains.DIEU_CHUYEN));
            if (req.getIsVatTu() != null) {
                if (req.getIsVatTu()) {
                    req.setDsLoaiHang(Arrays.asList("VT"));
                } else {
                    req.setDsLoaiHang(Arrays.asList("LT", "M"));
                }
            }
            if ("00".equals(req.getType())) {
                req.setType(Contains.DIEU_CHUYEN);
            } else if ("01".equals(req.getType())) {
                if (Contains.DCNB.equals(req.getLoaiDc())) {
                    req.setType(Contains.DIEU_CHUYEN);
                } else {
                    req.setType(Contains.NHAN_DIEU_CHUYEN);
                }
            }
            List<DcnbQuyetDinhDcCHdrDTO> danhSachSoQdDieuChuyen = dcnbQuyetDinhDcCHdrRepository.searchListChiCuc(req);
            return danhSachSoQdDieuChuyen;
        }
        return null;
    }

    public DcnbQuyetDinhDcCHdr update(CustomUserDetails currentUser, DcnbQuyetDinhDcCHdrReq objReq) throws Exception {
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        Optional<DcnbQuyetDinhDcCHdr> optional = dcnbQuyetDinhDcCHdrRepository.findById(objReq.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }
        Optional<DcnbQuyetDinhDcCHdr> soDxuat = dcnbQuyetDinhDcCHdrRepository.findFirstBySoQdinhAndTypeIsNull(objReq.getSoQdinh());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(objReq.getSoQdinh())) {
            if (soDxuat.isPresent() && objReq.getSoQdinh().split("/").length == 1) {
                if (!soDxuat.get().getId().equals(objReq.getId())) {
                    throw new Exception("số quyết định đã tồn tại");
                }
            }
        }

        DcnbQuyetDinhDcCHdr data = optional.get();
        objReq.setMaDvi(data.getMaDvi());
        objReq.setType(data.getType());
        BigDecimal total = new BigDecimal(0l);
        for (DcnbQuyetDinhDcCDtl e : objReq.getDanhSachQuyetDinh()) {
            if (Contains.DCNB.equals(objReq.getLoaiDc()) && Contains.CAP_CUC.equals(currentUser.getUser().getCapDvi())) {
                // được phép thêm mới kế hoạch và update kế hoạch (ngầm)
                if (e.getKeHoachDcHdrId() != null) {
                    if (e.getDcnbKeHoachDcHdr().getDanhSachHangHoa() != null && !e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().isEmpty()) {
                        Optional<DcnbKeHoachDcHdr> keHoachDcHdrOpt = dcnbKeHoachDcHdrRepository.findById(e.getKeHoachDcHdrId());
                        if (!keHoachDcHdrOpt.isPresent()) {
                            throw new Exception("dcnbKeHoachDcHdr.id không tìm thấy trong hệ thống!");
                        }
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdr = keHoachDcHdrOpt.get();
                        dcnbKeHoachDcHdr.setType(Contains.NHAN_DIEU_CHUYEN_TS);
                        dcnbKeHoachDcHdr.setLoaiDc(Contains.DCNB);
                        dcnbKeHoachDcHdr.setMaDviPq(e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().get(0).getMaChiCucNhan());
                        dcnbKeHoachDcHdr.setPhuongAnDieuChuyen(new ArrayList<>());

                        BigDecimal totalDuT = e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().stream()
                                .map(DcnbKeHoachDcDtl::getDuToanKphi)
                                .map(kphi -> kphi != null ? kphi : BigDecimal.ZERO)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        dcnbKeHoachDcHdr.setTongDuToanKp(totalDuT);
                        total = total.add(totalDuT);
                        for (DcnbKeHoachDcDtl e1 : e.getDcnbKeHoachDcHdr().getDanhSachHangHoa()) {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                            e1.setDaXdinhDiemNhap(true);
                            e1.setDuToanKphi(e1.getDuToanKphi() == null ? new BigDecimal(0) : e1.getDuToanKphi());
                            if (e1.getDonViTinh() == null) {
                                throw new Exception("Đơn vị tính không được để trống!");
                            }
                        }
                        // check hàng hóa trong kho xuất, kho nhận
                        // Check số lượng hiện thời từng lo kho - (tổng đề kế hoạch xuất - tổng xuất trong thực tế)> 0 ;
                        for (DcnbKeHoachDcDtl hh : e.getDcnbKeHoachDcHdr().getDanhSachHangHoa()) {
                            hh.setCoLoKho(!StringUtils.isEmpty(hh.getMaLoKho()));
                            if (!StringUtils.isEmpty(hh.getMaNganKho())) {
                                KtMlk tinKho = getThongTinKho(hh.getMaLoKho(), hh.getTenLoKho(), hh.getMaNganKho(), hh.getTenNganKho());
                                hh.setLoaiVthh(tinKho.getObject().getLoaiVthh());
                                hh.setCloaiVthh(tinKho.getObject().getCloaiVthh());
                            }
                            hh.setCoLoKhoNhan(!StringUtils.isEmpty(hh.getMaLoKhoNhan()));
                            if (!StringUtils.isEmpty(hh.getMaNganKhoNhan())) {
                                KtMlk tinKho = getThongTinKho(hh.getMaLoKhoNhan(), hh.getTenLoKhoNhan(), hh.getMaNganKhoNhan(), hh.getTenNganKhoNhan());
                                if (!hh.getCloaiVthh().equals(tinKho.getObject().getCloaiVthh())) {
                                    throw new Exception("Chủng loại hàng hóa kho xuất và nhận không giống nhau!");
                                }
                            }
                        }
                        dcnbKeHoachDcHdr.setDanhSachHangHoa(e.getDcnbKeHoachDcHdr().getDanhSachHangHoa());
                        dcnbKeHoachDcHdr.setDaXdinhDiemNhap(true);
                        dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr);
                    } else {
                        throw new Exception("DanhSachKeHoach không được để trống! ");
                    }
                } else {
                    if (e.getDcnbKeHoachDcHdr().getDanhSachHangHoa() != null && !e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().isEmpty()) {
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdr = new DcnbKeHoachDcHdr();
                        dcnbKeHoachDcHdr.setType(Contains.NHAN_DIEU_CHUYEN_TS);
                        dcnbKeHoachDcHdr.setMaDviPq(e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().get(0).getMaDiemKhoNhan().substring(0, 8));
                        dcnbKeHoachDcHdr.setPhuongAnDieuChuyen(new ArrayList<>());

                        BigDecimal totalDuT = e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().stream()
                                .map(DcnbKeHoachDcDtl::getDuToanKphi)
                                .map(kphi -> kphi != null ? kphi : BigDecimal.ZERO)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        dcnbKeHoachDcHdr.setTongDuToanKp(totalDuT);
                        total = total.add(totalDuT);
                        for (DcnbKeHoachDcDtl e1 : e.getDcnbKeHoachDcHdr().getDanhSachHangHoa()) {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr);
                            e1.setDaXdinhDiemNhap(true);
                            if (e1.getDonViTinh() == null) {
                                throw new Exception("Đơn vị tính không được để trống!");
                            }
                        }
                        dcnbKeHoachDcHdr.setDaXdinhDiemNhap(true);
                        // check hàng hóa trong kho xuất, kho nhận
                        // Check số lượng hiện thời từng lo kho - (tổng đề kế hoạch xuất - tổng xuất trong thực tế)> 0 ;
                        for (DcnbKeHoachDcDtl hh : e.getDcnbKeHoachDcHdr().getDanhSachHangHoa()) {
                            hh.setCoLoKho(!StringUtils.isEmpty(hh.getMaLoKho()));
                            if (!StringUtils.isEmpty(hh.getMaNganKho())) {
                                KtMlk tinKho = getThongTinKho(hh.getMaLoKho(), hh.getTenLoKho(), hh.getMaNganKho(), hh.getTenNganKho());
                                hh.setLoaiVthh(tinKho.getObject().getLoaiVthh());
                                hh.setCloaiVthh(tinKho.getObject().getCloaiVthh());
                            }
                            hh.setCoLoKhoNhan(!StringUtils.isEmpty(hh.getMaLoKhoNhan()));
                            if (!StringUtils.isEmpty(hh.getMaNganKhoNhan())) {
                                KtMlk tinKho = getThongTinKho(hh.getMaLoKhoNhan(), hh.getTenLoKhoNhan(), hh.getMaNganKhoNhan(), hh.getTenNganKhoNhan());
                                if (!hh.getCloaiVthh().equals(tinKho.getObject().getCloaiVthh())) {
                                    throw new Exception("Chủng loại hàng hóa kho xuất và nhận không giống nhau!");
                                }
                            }
                        }
                        dcnbKeHoachDcHdr.setDanhSachHangHoa(e.getDcnbKeHoachDcHdr().getDanhSachHangHoa());
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdrNew = dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr);
                        e.setKeHoachDcHdrId(dcnbKeHoachDcHdrNew.getId());
                    } else {
                        throw new Exception("DanhSachKeHoach không được để trống! ");
                    }
                }
            } else if (Contains.GIUA_2_CUC_DTNN_KV.equals(objReq.getLoaiDc()) && Contains.QD_NHAP.equals(objReq.getLoaiQdinh()) && Contains.CAP_CHI_CUC.equals(currentUser.getUser().getCapDvi())) {
                // được phép update kế hoạch (ngầm)
                if (e.getKeHoachDcHdrId() != null) {
                    Optional<DcnbKeHoachDcHdr> keHoachDcHdrOpt = dcnbKeHoachDcHdrRepository.findById(e.getKeHoachDcHdrId());
                    if (!keHoachDcHdrOpt.isPresent()) {
                        throw new Exception("dcnbKeHoachDcHdr.id không tìm thấy trong hệ thống!");
                    }
                    DcnbKeHoachDcHdr dcnbKeHoachDcHdr = keHoachDcHdrOpt.get();
                    if (e.getDcnbKeHoachDcHdr().getDanhSachHangHoa() != null && !e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().isEmpty()) {
                        for (DcnbKeHoachDcDtl e1 : e.getDcnbKeHoachDcHdr().getDanhSachHangHoa()) {
                            Optional<DcnbKeHoachDcDtl> dcnbKeHoachDcDtl = dcnbKeHoachDcDtlRepository.findById(e1.getId());
                            if (dcnbKeHoachDcDtl.isPresent()) {
                                e1.setParentId(dcnbKeHoachDcDtl.get().getParentId());
                            }
                            if (e1.getDonViTinh() == null) {
                                throw new Exception("Đơn vị tính không được để trống!");
                            }
                            e1.setDaXdinhDiemNhap(true);
                            BigDecimal totalDuT = e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().stream()
                                    .map(DcnbKeHoachDcDtl::getDuToanKphi)
                                    .map(kphi -> kphi != null ? kphi : BigDecimal.ZERO)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            total = total.add(totalDuT);
                        }
                        // check hàng hóa trong kho xuất, kho nhận
                        // Check số lượng hiện thời từng lo kho - (tổng đề kế hoạch xuất - tổng xuất trong thực tế)> 0 ;
                        for (DcnbKeHoachDcDtl hh : e.getDcnbKeHoachDcHdr().getDanhSachHangHoa()) {
                            hh.setCoLoKho(!StringUtils.isEmpty(hh.getMaLoKho()));
                            if (!StringUtils.isEmpty(hh.getMaNganKho())) {
                                KtMlk tinKho = getThongTinKho(hh.getMaLoKho(), hh.getTenLoKho(), hh.getMaNganKho(), hh.getTenNganKho());
                                hh.setLoaiVthh(tinKho.getObject().getLoaiVthh());
                                hh.setCloaiVthh(tinKho.getObject().getCloaiVthh());
                            }
                            hh.setCoLoKhoNhan(!StringUtils.isEmpty(hh.getMaLoKhoNhan()));
                            if (!StringUtils.isEmpty(hh.getMaNganKhoNhan())) {
                                KtMlk tinKho = getThongTinKho(hh.getMaLoKhoNhan(), hh.getTenLoKhoNhan(), hh.getMaNganKhoNhan(), hh.getTenNganKhoNhan());
                                if (!hh.getCloaiVthh().equals(tinKho.getObject().getCloaiVthh())) {
                                    throw new Exception("Chủng loại hàng hóa kho xuất và nhận không giống nhau!");
                                }
                            }
                        }
                    } else {
                        throw new Exception("DanhSachKeHoach không được để trống! ");
                    }
                    dcnbKeHoachDcHdr.setDanhSachHangHoa(e.getDcnbKeHoachDcHdr().getDanhSachHangHoa());
                    dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr);
                } else {
                    throw new Exception("dcnbKeHoachDcHdr.id phải khác null!");
                }
            } else {
                if (e.getKeHoachDcHdrId() != null) {
                    if (e.getDcnbKeHoachDcHdr().getDanhSachHangHoa() != null && !e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().isEmpty()) {
                        Optional<DcnbKeHoachDcHdr> dcnbKeHoachDcHdr = dcnbKeHoachDcHdrRepository.findById(e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().get(0).getHdrId());
                        if (!dcnbKeHoachDcHdr.isPresent()) {
                            throw new Exception("Không tìm thấy kế hoạch id = " + e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().get(0).getHdrId());
                        }
                        for (DcnbKeHoachDcDtl e1 : e.getDcnbKeHoachDcHdr().getDanhSachHangHoa()) {
                            e1.setDcnbKeHoachDcHdr(dcnbKeHoachDcHdr.get());
                            Optional<DcnbKeHoachDcDtl> dcnbKeHoachDcDtl = dcnbKeHoachDcDtlRepository.findById(e1.getId());
                            if (dcnbKeHoachDcDtl.isPresent()) {
                                e1.setParentId(dcnbKeHoachDcDtl.get().getParentId());
                            }
                            if (e1.getDonViTinh() == null) {
                                throw new Exception("Đơn vị tính không được để trống!");
                            }
                        }
                        // check hàng hóa trong kho xuất, kho nhận
                        // Check số lượng hiện thời từng lo kho - (tổng đề kế hoạch xuất - tổng xuất trong thực tế)> 0 ;
                        for (DcnbKeHoachDcDtl hh : e.getDcnbKeHoachDcHdr().getDanhSachHangHoa()) {
                            hh.setCoLoKho(!StringUtils.isEmpty(hh.getMaLoKho()));
                            if (!StringUtils.isEmpty(hh.getMaNganKho())) {
                                KtMlk tinKho = getThongTinKho(hh.getMaLoKho(), hh.getTenLoKho(), hh.getMaNganKho(), hh.getTenNganKho());
                                hh.setLoaiVthh(tinKho.getObject().getLoaiVthh());
                                hh.setCloaiVthh(tinKho.getObject().getCloaiVthh());
                            }
                            hh.setCoLoKhoNhan(!StringUtils.isEmpty(hh.getMaLoKhoNhan()));
                            if (!StringUtils.isEmpty(hh.getMaNganKhoNhan())) {
                                KtMlk tinKho = getThongTinKho(hh.getMaLoKhoNhan(), hh.getTenLoKhoNhan(), hh.getMaNganKhoNhan(), hh.getTenNganKhoNhan());
                                if (!hh.getCloaiVthh().equals(tinKho.getObject().getCloaiVthh())) {
                                    throw new Exception("Chủng loại hàng hóa kho xuất và nhận không giống nhau!");
                                }
                            }
                        }
                        dcnbKeHoachDcHdr.get().setDanhSachHangHoa(e.getDcnbKeHoachDcHdr().getDanhSachHangHoa());
                        DcnbKeHoachDcHdr dcnbKeHoachDcHdrNew = dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdr.get());
                        e.setKeHoachDcHdrId(dcnbKeHoachDcHdrNew.getId());
                        BigDecimal totalDuT = e.getDcnbKeHoachDcHdr().getDanhSachHangHoa().stream()
                                .map(DcnbKeHoachDcDtl::getDuToanKphi)
                                .map(kphi -> kphi != null ? kphi : BigDecimal.ZERO)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        total = total.add(totalDuT);
                    } else {
                        throw new Exception("dcnbKeHoachDcHdr.id phải khác null!");
                    }
                } else {
                    throw new Exception("dcnbKeHoachDcHdr.id phải khác null!");
                }
            }
            e.setDcnbQuyetDinhDcCHdr(data);
        }
        data.setDanhSachQuyetDinh(objReq.getDanhSachQuyetDinh());
        BeanUtils.copyProperties(objReq, data);
        data.setTongDuToanKp(total);
        List<Long> longs = data.getDanhSachQuyetDinh().stream().map(DcnbQuyetDinhDcCDtl::getKeHoachDcHdrId).collect(Collectors.toList());

        List<Long> duplicates = longs.stream()
                .filter(number -> Collections.frequency(longs, number) > 1)
                .distinct()
                .collect(Collectors.toList());
        if (!duplicates.isEmpty()) {
            throw new Exception("Mỗi danhSachQuyetDinh chỉ có 1 keHoachDcHdrId!");
        }
        DcnbQuyetDinhDcCHdr created = dcnbQuyetDinhDcCHdrRepository.save(data);

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU");

        fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH"));
        List<FileDinhKem> quyetDinh = fileDinhKemService.saveListFileDinhKem(objReq.getQuyetDinh(), created.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_QUYET_DINH");
        created.setCanCu(canCu);
        created.setQuyetDinh(quyetDinh);
        data.setDanhSachQuyetDinh(objReq.getDanhSachQuyetDinh());
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
        List<DcnbQuyetDinhDcCDtl> list = dcnbQuyetDinhDcCDtlRepository.findByHdrId(data.getId());
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
        List<DcnbQuyetDinhDcCDtl> listDtl = dcnbQuyetDinhDcCDtlRepository.findByHdrIdIn(listId);
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
        // check đang là điều chuyển hay là nhận điều chuyển
        if (optional.get().getType() == null) {
            this.approve(currentUser, statusReq, optional); // Truyền giá trị của optional vào
        } else if (Contains.NHAN_DIEU_CHUYEN.equals(optional.get().getType())) {
            this.approveNhanDieuChuyen(currentUser, statusReq, optional); // Luồng nhận điều chuyển quyết định
        }
    }

    private DcnbQuyetDinhDcCHdr approveNhanDieuChuyen(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbQuyetDinhDcCHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.YC_CHICUC_PHANBO_DC + Contains.CHODUYET_TBP_TVQT:
            case Contains.TUCHOI_TBP_TVQT + Contains.CHODUYET_TBP_TVQT:
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_TBP_TVQT:
                optional.get().setNgayGduyet(LocalDate.now());
                optional.get().setNguoiGduyetId(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_TBP_TVQT + Contains.TUCHOI_TBP_TVQT:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_TBP_TVQT + Contains.CHODUYET_LDCC:
                optional.get().setNgayPduyet(LocalDate.now());
                optional.get().setNguoiPduyetId(currentUser.getUser().getId());
                break;
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                optional.get().setNgayDuyetLdcc(LocalDate.now());
                optional.get().setNguoiDuyetLdccId(currentUser.getUser().getId());
                optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                optional.get().setNgayDuyetLdcc(LocalDate.now());
                optional.get().setNguoiDuyetLdccId(currentUser.getUser().getId());

                List<DcnbQuyetDinhDcCDtl> danhSachQuyetDinh = optional.get().getDanhSachQuyetDinh();
                for (DcnbQuyetDinhDcCDtl hh : danhSachQuyetDinh) {
                    Optional<DcnbKeHoachDcHdr> keHoachDcHdr = dcnbKeHoachDcHdrRepository.findById(hh.getKeHoachDcHdrId());
                    if (keHoachDcHdr.isPresent()) {
                        keHoachDcHdr.get().setDaXdinhDiemNhap(true);
                        dcnbKeHoachDcHdrRepository.save(keHoachDcHdr.get());
                    } else {
                        throw new Exception("Không tìm thấy kế hoạch! id: " + hh.getKeHoachDcHdrId());
                    }
                    hh.setDcnbKeHoachDcHdr(keHoachDcHdr.get());
                    List<DcnbKeHoachDcDtl> danhSachKeHoach = hh.getDcnbKeHoachDcHdr().getDanhSachHangHoa();

                    Set<Long> parentIds = new HashSet<>();
                    for (DcnbKeHoachDcDtl kh : danhSachKeHoach) {
                        Optional<DcnbKeHoachDcDtl> parentDtl = dcnbKeHoachDcDtlRepository.findById(kh.getParentId());
                        boolean contains = parentIds.contains(hh.getParentId());
                        if (parentDtl.isPresent() && !contains) {
                            parentIds.add(hh.getParentId());
                            parentDtl.get().setMaDiemKhoNhan(kh.getMaDiemKhoNhan());
                            parentDtl.get().setTenDiemKhoNhan(kh.getTenDiemKhoNhan());
                            parentDtl.get().setMaNhaKhoNhan(kh.getMaNhaKhoNhan());
                            parentDtl.get().setTenNhaKhoNhan(kh.getTenNhaKhoNhan());
                            parentDtl.get().setMaNganKhoNhan(kh.getMaNganKhoNhan());
                            parentDtl.get().setTenNganKhoNhan(kh.getTenNganKhoNhan());
                            parentDtl.get().setCoLoKhoNhan(kh.getCoLoKhoNhan());
                            parentDtl.get().setMaLoKhoNhan(kh.getMaLoKhoNhan());
                            parentDtl.get().setTenLoKhoNhan(kh.getTenLoKhoNhan());
                            parentDtl.get().setTichLuongKd(kh.getTichLuongKd());
                            parentDtl.get().setSoLuongPhanBo(kh.getSoLuongPhanBo());
                            parentDtl.get().setSlDcConLai(kh.getSlDcConLai());
                            parentDtl.get().setThuKhoNhan(kh.getThuKhoNhan());
                            parentDtl.get().setThuKhoNhanId(kh.getThuKhoNhanId());
                            parentDtl.get().setThayDoiThuKho(kh.getThayDoiThuKho());
                            parentDtl.get().setDaXdinhDiemNhap(true);
                            dcnbKeHoachDcDtlRepository.save(parentDtl.get());

                            DcnbDataLinkHdr dataLink = new DcnbDataLinkHdr();
                            dataLink.setKeHoachDcDtlId(kh.getId());
                            dataLink.setKeHoachDcHdrId(kh.getHdrId());
                            dataLink.setKeHoachDcDtlParentId(parentDtl.get().getId());
                            dataLink.setKeHoachDcHdrParentId(parentDtl.get().getHdrId());
                            dataLink.setQdCcId(optional.get().getId());
                            dataLink.setQdCcParentId(optional.get().getParentId());
                            dataLink.setQdCtcId(optional.get().getCanCuQdTc());
                            dataLink.setType(Contains.NHAN_DIEU_CHUYEN);
                            dcnbDataLinkHdrRepository.save(dataLink);
                        } else if (parentDtl.isPresent()) {
                            DcnbKeHoachDcDtl clone = SerializationUtils.clone(parentDtl.get());
                            clone.setId(null);
                            clone.setMaDiemKhoNhan(kh.getMaDiemKhoNhan());
                            clone.setTenDiemKhoNhan(kh.getTenDiemKhoNhan());
                            clone.setMaNhaKhoNhan(kh.getMaNhaKhoNhan());
                            clone.setTenNhaKhoNhan(kh.getTenNhaKhoNhan());
                            clone.setMaNganKhoNhan(kh.getMaNganKhoNhan());
                            clone.setTenNganKhoNhan(kh.getTenNganKhoNhan());
                            clone.setCoLoKhoNhan(kh.getCoLoKhoNhan());
                            clone.setMaLoKhoNhan(kh.getMaLoKhoNhan());
                            clone.setTenLoKhoNhan(kh.getTenLoKhoNhan());
                            clone.setTichLuongKd(kh.getTichLuongKd());
                            clone.setSoLuongPhanBo(kh.getSoLuongPhanBo());
                            clone.setSlDcConLai(kh.getSlDcConLai());
                            clone.setThuKhoNhan(kh.getThuKhoNhan());
                            clone.setThuKhoNhanId(kh.getThuKhoNhanId());
                            clone.setThayDoiThuKho(kh.getThayDoiThuKho());
                            clone.setDaXdinhDiemNhap(true);
                            clone = dcnbKeHoachDcDtlRepository.save(clone);

                            DcnbDataLinkHdr dataLink = new DcnbDataLinkHdr();
                            dataLink.setKeHoachDcDtlId(kh.getId());
                            dataLink.setKeHoachDcHdrId(kh.getHdrId());
                            dataLink.setKeHoachDcDtlParentId(clone.getId());
                            dataLink.setKeHoachDcHdrParentId(clone.getHdrId());
                            dataLink.setQdCcId(optional.get().getId());
                            dataLink.setQdCcParentId(optional.get().getParentId());
                            dataLink.setQdCtcId(optional.get().getCanCuQdTc());
                            dataLink.setType(Contains.NHAN_DIEU_CHUYEN);
                            dcnbDataLinkHdrRepository.save(dataLink);
                        }
                        kh.setDaXdinhDiemNhap(true);
                        dcnbKeHoachDcDtlRepository.save(kh);
                    }
                }
                for (DcnbQuyetDinhDcCDtl hh : danhSachQuyetDinh) {
                    Optional<DcnbKeHoachDcHdr> hdr = dcnbKeHoachDcHdrRepository.findById(hh.getKeHoachDcHdrId());
                    Optional<DcnbKeHoachDcHdr> parentHdr = dcnbKeHoachDcHdrRepository.findById(hdr.get().getParentId());
                    if (parentHdr.isPresent()) {
                        boolean allMatch = parentHdr.get().getDanhSachHangHoa().stream().allMatch(n -> (n.getDaXdinhDiemNhap() != null && n.getDaXdinhDiemNhap()));
                        if (allMatch) {
                            parentHdr.get().setDaXdinhDiemNhap(true);
                            dcnbKeHoachDcHdrRepository.save(parentHdr.get());
                        }
                    }
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbQuyetDinhDcCHdr created = dcnbQuyetDinhDcCHdrRepository.save(optional.get());
        return created;
    }

    public DcnbQuyetDinhDcCHdr approve(CustomUserDetails currentUser, StatusReq statusReq, Optional<DcnbQuyetDinhDcCHdr> optional) throws Exception {
        String status = optional.get().getTrangThai() + statusReq.getTrangThai();
        switch (status) {
            case Contains.DUTHAO + Contains.YC_CHICUC_PHANBO_DC:
                cloneQuyetDinhDcCNhan(statusReq, optional, false);
                break;
            case Contains.YC_CHICUC_PHANBO_DC + Contains.CHODUYET_TP:
            case Contains.DUTHAO + Contains.CHODUYET_TP:
            case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
            case Contains.TUCHOI_LDC + Contains.CHODUYET_TP:
                // validate kế hoạch hdr đã xác điịnh điểm nhập hết chưa?
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
                Optional<DcnbQuyetDinhDcTcHdr> dcnbQuyetDinhDcTcHdr = Optional.empty();
                if (optional.get().getCanCuQdTc() != null) {
                    dcnbQuyetDinhDcTcHdr = dcnbQuyetDinhDcTcHdrRepository.findById(optional.get().getCanCuQdTc());
                }
                if (dcnbQuyetDinhDcTcHdr.isPresent()) {
                    if ("00".equals(optional.get().getLoaiQdinh())) {
                        String qdinhNhapCucId = dcnbQuyetDinhDcTcHdr.get().getQdinhNhapCucId();
                        if (qdinhNhapCucId == null) {
                            qdinhNhapCucId = "";
                        } else {
                            qdinhNhapCucId += "," + optional.get().getId();
                        }
                        dcnbQuyetDinhDcTcHdr.get().setQdinhNhapCucId(qdinhNhapCucId);
                        String soQdinh = dcnbQuyetDinhDcTcHdr.get().getSoQdinhNhapCuc();
                        if (soQdinh == null) {
                            soQdinh = "";
                        } else {
                            soQdinh += "," + optional.get().getSoQdinh();
                        }
                        dcnbQuyetDinhDcTcHdr.get().setSoQdinhNhapCuc(soQdinh);
                    }
                    if ("01".equals(optional.get().getLoaiQdinh())) {
                        String qdinhXuatCucId = dcnbQuyetDinhDcTcHdr.get().getQdinhXuatCucId();
                        if (qdinhXuatCucId == null) {
                            qdinhXuatCucId = "";
                        } else {
                            qdinhXuatCucId += "," + optional.get().getId();
                        }
                        dcnbQuyetDinhDcTcHdr.get().setQdinhXuatCucId(qdinhXuatCucId);
                        String soQdinh = dcnbQuyetDinhDcTcHdr.get().getSoQdinhXuatCuc();
                        if (soQdinh == null) {
                            soQdinh = "";
                        } else {
                            soQdinh += "," + optional.get().getSoQdinh();
                        }
                        dcnbQuyetDinhDcTcHdr.get().setSoQdinhXuatCuc(soQdinh);
                    }
                }
                if (Contains.GIUA_2_CHI_CUC_TRONG_1_CUC.equals(optional.get().getLoaiDc())) {
                    // clone chi cục nhận
                    cloneQuyetDinhDcCNhan(statusReq, optional, true);
                    // clone chi cục xuat
                    cloneQuyetDinhDcCXuat(statusReq, optional);
                } else {
                    // clone chi cục xuat
                    cloneQuyetDinhDcCXuat(statusReq, optional);
                }
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        optional.get().setTrangThai(statusReq.getTrangThai());
        DcnbQuyetDinhDcCHdr created = dcnbQuyetDinhDcCHdrRepository.save(optional.get());
        return created;
    }

    public void cloneQuyetDinhDcCXuat(StatusReq statusReq, Optional<DcnbQuyetDinhDcCHdr> optional) throws Exception {
        // xử lý clone cho chi cục với TYPE là DC
        List<DcnbQuyetDinhDcCDtl> danhSachQuyetDinh = optional.get().getDanhSachQuyetDinh();
        Map<String, List<DcnbKeHoachDcDtl>> groupedByMaCc = new HashMap<>();
        for (DcnbQuyetDinhDcCDtl qdd : danhSachQuyetDinh) {
            Optional<DcnbKeHoachDcHdr> keHoachDcHdr = dcnbKeHoachDcHdrRepository.findById(qdd.getKeHoachDcHdrId());
            if (keHoachDcHdr.isPresent()) {
                qdd.setDcnbKeHoachDcHdr(keHoachDcHdr.get());
            } else {
                throw new Exception("Không tìm thấy kế hoạch! id: " + qdd.getKeHoachDcHdrId());
            }
            List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = groupedByMaCc.get(keHoachDcHdr.get().getMaDviPq());
            if (dcnbKeHoachDcDtls == null) {
                groupedByMaCc.put(keHoachDcHdr.get().getMaDviPq(), qdd.getDcnbKeHoachDcHdr().getDanhSachHangHoa());
            } else {
                dcnbKeHoachDcDtls.addAll(qdd.getDcnbKeHoachDcHdr().getDanhSachHangHoa());
                groupedByMaCc.put(keHoachDcHdr.get().getMaDviPq(), dcnbKeHoachDcDtls);
            }
        }

        AtomicBoolean loiData = new AtomicBoolean(false);
        groupedByMaCc.forEach((maChiCucThue, khList) -> {
            DcnbQuyetDinhDcCHdr dcnbQuyetDinhDcCHdrCloned = SerializationUtils.clone(optional.get());
            dcnbQuyetDinhDcCHdrCloned.setParentId(dcnbQuyetDinhDcCHdrCloned.getId());
            dcnbQuyetDinhDcCHdrCloned.setId(null);
            dcnbQuyetDinhDcCHdrCloned.setMaDvi(maChiCucThue);
            QlnvDmDonvi byMaDvi = qlnvDmDonviRepository.findByMaDvi(maChiCucThue);
            if (byMaDvi != null) {
                dcnbQuyetDinhDcCHdrCloned.setTenDvi(byMaDvi.getTenDvi());
            } else {
                dcnbQuyetDinhDcCHdrCloned.setTenDvi(khList.get(0).getDcnbKeHoachDcHdr().getTenDvi());
            }
            dcnbQuyetDinhDcCHdrCloned.setType(Contains.DIEU_CHUYEN);
            dcnbQuyetDinhDcCHdrCloned.setTrangThai(statusReq.getTrangThai());
            Map<Long, List<DcnbKeHoachDcDtl>> groupedByKhh = khList.stream()
                    .collect(Collectors.groupingBy(DcnbKeHoachDcDtl::getHdrId));
            List<DcnbQuyetDinhDcCDtl> quyetDinhDcCDtlsClone = new ArrayList<>();

            groupedByKhh.forEach((dcnbKeHoachDcHdrId, dcnbKeHoachDcDtlList) -> {
                List<DcnbQuyetDinhDcCDtl> quyetDinhDcCDtls = danhSachQuyetDinh.stream().filter(item -> dcnbKeHoachDcHdrId.equals(item.getKeHoachDcHdrId())).map(itemMap -> {
                    DcnbQuyetDinhDcCDtl dcnbQuyetDinhDcCDtlClone = SerializationUtils.clone(itemMap);
                    dcnbQuyetDinhDcCDtlClone.setParentId(dcnbQuyetDinhDcCDtlClone.getId());
                    dcnbQuyetDinhDcCDtlClone.setId(null);
                    Optional<DcnbKeHoachDcHdr> dcnbKeHoachDcHdrDetail = null;
                    try {
                        dcnbKeHoachDcHdrDetail = Optional.ofNullable(dcnbKeHoachDcHdrServiceImpl.details(dcnbKeHoachDcHdrId));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    DcnbKeHoachDcHdr dcnbKeHoachDcHdrClone = SerializationUtils.clone(dcnbKeHoachDcHdrDetail.get());
                    List<DcnbKeHoachDcDtl> keHoachDcDtls = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrId(dcnbKeHoachDcHdrClone.getId());
                    List<DcnbPhuongAnDc> phuongAnDcs = dcnbPhuongAnDcRepository.findByKeHoachDcHdrId(dcnbKeHoachDcHdrClone.getId());
                    List<DcnbKeHoachDcDtl> keHoachDcDtlsClone = new ArrayList<>();
                    List<DcnbPhuongAnDc> phuongAnDcsClone = new ArrayList<>();
                    for (DcnbKeHoachDcDtl keHoachDcDtl : keHoachDcDtls) {
                        try {
                            keHoachDcDtlsClone.add((DcnbKeHoachDcDtl) keHoachDcDtl.clone());
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                    for (DcnbPhuongAnDc phuongAnDc : phuongAnDcs) {
                        try {
                            phuongAnDcsClone.add((DcnbPhuongAnDc) phuongAnDc.clone());
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }

                    dcnbKeHoachDcHdrClone.setDanhSachHangHoa(keHoachDcDtlsClone);
                    dcnbKeHoachDcHdrClone.setPhuongAnDieuChuyen(phuongAnDcsClone);
                    dcnbKeHoachDcHdrClone.setParentId(dcnbKeHoachDcHdrClone.getId());
                    dcnbKeHoachDcHdrClone.setId(null);
                    dcnbKeHoachDcHdrClone.setMaDviPq(maChiCucThue);
                    dcnbKeHoachDcHdrClone.setType(Contains.DIEU_CHUYEN_TS);
                    dcnbKeHoachDcHdrClone.setTrangThai(statusReq.getTrangThai());
                    DcnbKeHoachDcHdr finalDcnbKeHoachDcHdrClone = dcnbKeHoachDcHdrClone;
                    dcnbKeHoachDcHdrClone.setDanhSachHangHoa(dcnbKeHoachDcHdrClone.getDanhSachHangHoa().stream()
                            .filter(item -> item.getMaDiemKho().substring(0, 8).equals(maChiCucThue)).map(itemMap1 -> {
                                itemMap1.setParentId(itemMap1.getId());
                                itemMap1.setId(null);
                                itemMap1.setHdrId(finalDcnbKeHoachDcHdrClone.getId());
                                itemMap1.setDcnbKeHoachDcHdr(finalDcnbKeHoachDcHdrClone);
                                return itemMap1;
                            }).collect(Collectors.toList()));
                    dcnbKeHoachDcHdrClone.setPhuongAnDieuChuyen(dcnbKeHoachDcHdrClone.getPhuongAnDieuChuyen().stream().map(itemMap1 -> {
                        itemMap1.setParentId(itemMap1.getId());
                        itemMap1.setId(null);
                        itemMap1.setKeHoachDcHdrId(finalDcnbKeHoachDcHdrClone.getId());
                        itemMap1.setDcnbKeHoachDcHdr(finalDcnbKeHoachDcHdrClone);
                        return itemMap1;
                    }).collect(Collectors.toList()));
                    dcnbKeHoachDcHdrClone.setCanCu(dcnbKeHoachDcHdrClone.getCanCu().stream().map(itemMap1 -> {
                        itemMap1.setId(null);
                        itemMap1.setDataId(null);
                        return itemMap1;
                    }).collect(Collectors.toList()));
                    BigDecimal total = dcnbKeHoachDcHdrClone.getDanhSachHangHoa().stream()
                            .map(DcnbKeHoachDcDtl::getDuToanKphi)
                            .map(kphi -> kphi != null ? kphi : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    dcnbKeHoachDcHdrClone.setTongDuToanKp(total);

                    dcnbKeHoachDcHdrClone = dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdrClone);

                    dcnbQuyetDinhDcCDtlClone.setKeHoachDcHdrId(dcnbKeHoachDcHdrClone.getId());

                    fileDinhKemService.delete(dcnbKeHoachDcHdrClone.getId(), Lists.newArrayList(DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU"));
                    List<FileDinhKemReq> fileDinhKemReqs = dcnbKeHoachDcHdrClone.getCanCu().stream()
                            .map(person -> new ModelMapper().map(person, FileDinhKemReq.class))
                            .collect(Collectors.toList());
                    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(fileDinhKemReqs, dcnbKeHoachDcHdrClone.getId(), DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU");
                    dcnbKeHoachDcHdrClone.setCanCu(canCu);

                    return dcnbQuyetDinhDcCDtlClone;
                }).collect(Collectors.toList());
                if (quyetDinhDcCDtls.size() > 1) {
                    loiData.set(true);
                } else {
                    DcnbQuyetDinhDcCDtl quyetDinhDcCDtl = quyetDinhDcCDtls.get(0);
                    quyetDinhDcCDtlsClone.add(quyetDinhDcCDtl);
                }
            });
            dcnbQuyetDinhDcCHdrCloned.setDanhSachQuyetDinh(quyetDinhDcCDtlsClone);
            dcnbQuyetDinhDcCHdrCloned = dcnbQuyetDinhDcCHdrRepository.save(dcnbQuyetDinhDcCHdrCloned);
            fileDinhKemService.delete(dcnbQuyetDinhDcCHdrCloned.getId(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU"));
            List<FileDinhKemReq> fileDinhKemReqs = dcnbQuyetDinhDcCHdrCloned.getCanCu().stream()
                    .map(person -> new ModelMapper().map(person, FileDinhKemReq.class))
                    .collect(Collectors.toList());
            List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(fileDinhKemReqs, dcnbQuyetDinhDcCHdrCloned.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU");
            dcnbQuyetDinhDcCHdrCloned.setCanCu(canCu);
        });

        if (loiData.get()) {
            throw new Exception("Mỗi DcnbQuyetDinhDcCDtl chỉ có 1 dcnbKeHoachDcHdrId!");
        }
    }

    public void cloneQuyetDinhDcCNhan(StatusReq statusReq, Optional<DcnbQuyetDinhDcCHdr> optional, boolean isClone) throws Exception {
        // xử lý clone cho chi cục với TYPE là NDC
        List<DcnbQuyetDinhDcCDtl> danhSachQuyetDinh = optional.get().getDanhSachQuyetDinh();
        List<DcnbKeHoachDcDtl> danhSachKeHoachs = new ArrayList<>();
        for (DcnbQuyetDinhDcCDtl qdd : danhSachQuyetDinh) {
            Optional<DcnbKeHoachDcHdr> keHoachDcHdr = dcnbKeHoachDcHdrRepository.findById(qdd.getKeHoachDcHdrId());
            if (keHoachDcHdr.isPresent()) {
                qdd.setDcnbKeHoachDcHdr(keHoachDcHdr.get());
            } else {
                throw new Exception("Không tìm thấy kế hoạch! id: " + qdd.getKeHoachDcHdrId());
            }
            danhSachKeHoachs.addAll(qdd.getDcnbKeHoachDcHdr().getDanhSachHangHoa());
        }
        Map<String, List<DcnbKeHoachDcDtl>> groupedByMaCc = danhSachKeHoachs.stream()
                .collect(Collectors.groupingBy(DcnbKeHoachDcDtl::getMaChiCucNhan));

        AtomicBoolean loiData = new AtomicBoolean(false);
        groupedByMaCc.forEach((maChiCucThue, khList) -> {
            DcnbQuyetDinhDcCHdr dcnbQuyetDinhDcCHdrCloned = SerializationUtils.clone(optional.get());
            dcnbQuyetDinhDcCHdrCloned.setParentId(dcnbQuyetDinhDcCHdrCloned.getId());
            dcnbQuyetDinhDcCHdrCloned.setId(null);
            dcnbQuyetDinhDcCHdrCloned.setMaDvi(maChiCucThue);
            dcnbQuyetDinhDcCHdrCloned.setTenDvi(khList.get(0).getTenChiCucNhan());
            dcnbQuyetDinhDcCHdrCloned.setType(Contains.NHAN_DIEU_CHUYEN);
            dcnbQuyetDinhDcCHdrCloned.setTrangThai(statusReq.getTrangThai());
            Map<Long, List<DcnbKeHoachDcDtl>> groupedByKhh = khList.stream()
                    .collect(Collectors.groupingBy(DcnbKeHoachDcDtl::getHdrId));
            List<DcnbQuyetDinhDcCDtl> quyetDinhDcCDtlsClone = new ArrayList<>();

            groupedByKhh.forEach((dcnbKeHoachDcHdrId, dcnbKeHoachDcDtlList) -> {
                List<DcnbQuyetDinhDcCDtl> quyetDinhDcCDtls = danhSachQuyetDinh.stream().filter(item -> dcnbKeHoachDcHdrId.equals(item.getKeHoachDcHdrId())).map(itemMap -> {
                    DcnbQuyetDinhDcCDtl dcnbQuyetDinhDcCDtlCloned = SerializationUtils.clone(itemMap);
                    dcnbQuyetDinhDcCDtlCloned.setParentId(dcnbQuyetDinhDcCDtlCloned.getId());
                    dcnbQuyetDinhDcCDtlCloned.setId(null);
                    Optional<DcnbKeHoachDcHdr> keHoachDcHdrOpt = null;
                    try {
                        keHoachDcHdrOpt = Optional.ofNullable(dcnbKeHoachDcHdrServiceImpl.details(dcnbKeHoachDcHdrId));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    DcnbKeHoachDcHdr dcnbKeHoachDcHdrClone = SerializationUtils.clone(keHoachDcHdrOpt.get());
                    List<DcnbKeHoachDcDtl> keHoachDcDtls = dcnbKeHoachDcDtlRepository.findByDcnbKeHoachDcHdrId(dcnbKeHoachDcHdrClone.getId());
                    List<DcnbPhuongAnDc> phuongAnDcs = dcnbPhuongAnDcRepository.findByKeHoachDcHdrId(dcnbKeHoachDcHdrClone.getId());
                    List<DcnbKeHoachDcDtl> keHoachDcDtlsClone = new ArrayList<>();
                    List<DcnbPhuongAnDc> phuongAnDcsClone = new ArrayList<>();
                    for (DcnbKeHoachDcDtl keHoachDcDtl : keHoachDcDtls) {
                        try {
                            keHoachDcDtlsClone.add((DcnbKeHoachDcDtl) keHoachDcDtl.clone());
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                    for (DcnbPhuongAnDc phuongAnDc : phuongAnDcs) {
                        try {
                            phuongAnDcsClone.add((DcnbPhuongAnDc) phuongAnDc.clone());
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }

                    dcnbKeHoachDcHdrClone.setDanhSachHangHoa(keHoachDcDtlsClone);
                    dcnbKeHoachDcHdrClone.setPhuongAnDieuChuyen(phuongAnDcsClone);
                    dcnbKeHoachDcHdrClone.setParentId(dcnbKeHoachDcHdrClone.getId());
                    dcnbKeHoachDcHdrClone.setId(null);
                    dcnbKeHoachDcHdrClone.setMaDviPq(maChiCucThue);
                    dcnbKeHoachDcHdrClone.setType(Contains.NHAN_DIEU_CHUYEN_TS);
                    dcnbKeHoachDcHdrClone.setTrangThai(statusReq.getTrangThai());
                    DcnbKeHoachDcHdr finalDcnbKeHoachDcHdrClone = dcnbKeHoachDcHdrClone;
                    dcnbKeHoachDcHdrClone.setDanhSachHangHoa(dcnbKeHoachDcHdrClone.getDanhSachHangHoa().stream()
                            .filter(item -> item.getMaChiCucNhan().equals(maChiCucThue)).map(itemMap1 -> {
                                itemMap1.setParentId(itemMap1.getId());
                                itemMap1.setId(null);
                                itemMap1.setHdrId(finalDcnbKeHoachDcHdrClone.getId());
                                itemMap1.setDcnbKeHoachDcHdr(finalDcnbKeHoachDcHdrClone);
                                return itemMap1;
                            }).collect(Collectors.toList()));
                    dcnbKeHoachDcHdrClone.setPhuongAnDieuChuyen(dcnbKeHoachDcHdrClone.getPhuongAnDieuChuyen().stream()
                            .filter(item -> item.getMaChiCucNhan().equals(maChiCucThue)).map(itemMap1 -> {
                                itemMap1.setParentId(itemMap1.getId());
                                itemMap1.setId(null);
                                itemMap1.setKeHoachDcHdrId(finalDcnbKeHoachDcHdrClone.getId());
                                itemMap1.setDcnbKeHoachDcHdr(finalDcnbKeHoachDcHdrClone);
                                return itemMap1;
                            }).collect(Collectors.toList()));
                    dcnbKeHoachDcHdrClone.setCanCu(dcnbKeHoachDcHdrClone.getCanCu().stream().map(itemMap1 -> {
                        itemMap1.setId(null);
                        itemMap1.setDataId(null);
                        return itemMap1;
                    }).collect(Collectors.toList()));
                    BigDecimal total = dcnbKeHoachDcHdrClone.getDanhSachHangHoa().stream().map(DcnbKeHoachDcDtl::getDuToanKphi)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    dcnbKeHoachDcHdrClone.setTongDuToanKp(total);

                    dcnbKeHoachDcHdrClone = dcnbKeHoachDcHdrRepository.save(dcnbKeHoachDcHdrClone);

                    dcnbQuyetDinhDcCDtlCloned.setKeHoachDcHdrId(dcnbKeHoachDcHdrClone.getId());

                    fileDinhKemService.delete(dcnbKeHoachDcHdrClone.getId(), Lists.newArrayList(DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU"));
                    List<FileDinhKemReq> fileDinhKemReqs = dcnbKeHoachDcHdrClone.getCanCu().stream()
                            .map(person -> new ModelMapper().map(person, FileDinhKemReq.class))
                            .collect(Collectors.toList());
                    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(fileDinhKemReqs, dcnbKeHoachDcHdrClone.getId(), DcnbKeHoachDcHdr.TABLE_NAME + "_CAN_CU");
                    dcnbKeHoachDcHdrClone.setCanCu(canCu);

                    return dcnbQuyetDinhDcCDtlCloned;
                }).collect(Collectors.toList());
                if (quyetDinhDcCDtls.size() > 1) {
                    loiData.set(true);
                } else {
                    DcnbQuyetDinhDcCDtl quyetDinhDcCDtl = quyetDinhDcCDtls.get(0);
                    quyetDinhDcCDtlsClone.add(quyetDinhDcCDtl);
                }
            });
            dcnbQuyetDinhDcCHdrCloned.setDanhSachQuyetDinh(quyetDinhDcCDtlsClone);
            if (!isClone) {
                dcnbQuyetDinhDcCHdrCloned.setTrangThai(Contains.YC_CHICUC_PHANBO_DC);
            }
            dcnbQuyetDinhDcCHdrCloned = dcnbQuyetDinhDcCHdrRepository.save(dcnbQuyetDinhDcCHdrCloned);

            fileDinhKemService.delete(dcnbQuyetDinhDcCHdrCloned.getId(), Lists.newArrayList(DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU"));
            List<FileDinhKemReq> fileDinhKemReqs = dcnbQuyetDinhDcCHdrCloned.getCanCu().stream()
                    .map(person -> new ModelMapper().map(person, FileDinhKemReq.class))
                    .collect(Collectors.toList());
            List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(fileDinhKemReqs, dcnbQuyetDinhDcCHdrCloned.getId(), DcnbQuyetDinhDcCHdr.TABLE_NAME + "_CAN_CU");
            dcnbQuyetDinhDcCHdrCloned.setCanCu(canCu);
        });

        if (loiData.get()) {
            throw new Exception("Mỗi DcnbQuyetDinhDcCDtl chỉ có 1 dcnbKeHoachDcHdrId!");
        }
    }

    public void export(CustomUserDetails currentUser, SearchDcnbQuyetDinhDcC objReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<DcnbQuyetDinhDcCHdr> page = this.searchPage(currentUser, objReq);
        List<DcnbQuyetDinhDcCHdr> data = page.getContent();

        String title = "Danh sách quyết định cục ";
        String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số quyết định", "Ngày ký quyết định", "Loại điều chuyển", "Loại quyết định", "Trích yếu", "Số đề xuất/công văn", "Trạng thái"};
        String fileName = "danh-sach-ke-hoach-dieu-chuyen-noi-bo-hang-dtqg.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            DcnbQuyetDinhDcCHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getNam();
            objs[2] = dx.getSoQdinh();
            objs[3] = dx.getNgayKyQdinh();
            objs[4] = dx.getLoaiDc();
            objs[5] = dx.getLoaiQdinh();
            objs[6] = dx.getTrichYeu();
            objs[7] = dx.getSoDxuat();
            objs[8] = TrangThaiAllEnum.getLabelById(dx.getTrangThai());
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    public List<DcnbQuyetDinhDcCHdrDTO> danhSachQuyetDinhCuc(CustomUserDetails currentUser, SearchDcnbQuyetDinhDcC req) {
        // 2 trường hợp
        // trường 1: chi cục lấy ra quyết định của cục
        // trường 2: cục lấy ra quyết định của cục nhưng phải được bh biên bản lấy mẫu
        if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            String dvql = currentUser.getDvql();
            req.setMaDvi(dvql);
            req.setTypes(Arrays.asList(Contains.NHAN_DIEU_CHUYEN, Contains.DIEU_CHUYEN));
            if (req.getIsVatTu() != null) {
                if (req.getIsVatTu()) {
                    req.setDsLoaiHang(Arrays.asList("VT"));
                } else {
                    req.setDsLoaiHang(Arrays.asList("LT", "M"));
                }
            }
            List<DcnbQuyetDinhDcCHdrDTO> danhSachSoQdDieuChuyen = dcnbQuyetDinhDcCHdrRepository.searchListCuc(req);
            return danhSachSoQdDieuChuyen;
        }
        return null;
    }
}
