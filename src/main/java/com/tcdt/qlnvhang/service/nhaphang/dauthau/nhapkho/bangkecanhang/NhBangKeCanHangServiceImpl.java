package com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.bangkecanhang;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHang;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHangCt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKho;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNxDdiem;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoCtRepository;
import com.tcdt.qlnvhang.repository.quyetdinhgiaonhiemvunhapxuat.HhQdGiaoNvuNhapxuatRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeCanHangRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.bangkecanhang.NhBangKeChCtRepository;
import com.tcdt.qlnvhang.repository.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeCanHangLtReq;
import com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc.QlBangKeChCtLtReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.service.HhQdGiaoNvuNhapxuatService;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.nhapkho.phieunhapkho.NhPhieuNhapKhoService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class NhBangKeCanHangServiceImpl extends BaseServiceImpl implements NhBangKeCanHangService {

    @Autowired
    private NhBangKeCanHangRepository nhBangKeCanHangRepository;

    @Autowired
    private NhBangKeChCtRepository nhBangKeChCtRepository;

    @Autowired
    private HhQdGiaoNvuNhapxuatService hhQdGiaoNvuNhapxuatService;

    @Autowired
    private NhPhieuNhapKhoService nhPhieuNhapKhoService;

    @Autowired
    private HhQdGiaoNvuNhapxuatRepository hhQdGiaoNvuNhapxuatRepository;

    @Autowired
    private NhPhieuNhapKhoRepository nhPhieuNhapKhoRepository;

    @Autowired
    private NhPhieuNhapKhoCtRepository nhPhieuNhapKhoCtRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Override
    public Page<NhBangKeCanHang> searchPage(QlBangKeCanHangLtReq req) {
        return null;
    }

    @Override
    @Transactional
    public NhBangKeCanHang create(QlBangKeCanHangLtReq req) throws Exception {
        if (req == null){
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");

        }

        NhBangKeCanHang item = new NhBangKeCanHang();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgayTao(new Date());
        item.setNguoiTaoId(userInfo.getId());
        item.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        item.setMaDvi(userInfo.getDvql());
        item.setNam(LocalDate.now().getYear());
        item.setId(Long.valueOf(item.getSoBangKe().split("/")[0]));
        nhBangKeCanHangRepository.save(item);
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), "NH_BANG_KE_CAN_HANG");
        }
        this.saveCtiet(item.getId(),req);

        return item;
    }

    @Transactional
      public void saveCtiet(Long idHdr,QlBangKeCanHangLtReq req){
        nhBangKeChCtRepository.deleteByIdBangKeCanHangHdr(idHdr);
        for(QlBangKeChCtLtReq objCtiet : req.getChiTiets()){
            NhBangKeCanHangCt ctiet = new NhBangKeCanHangCt();
            BeanUtils.copyProperties(objCtiet,ctiet,"id");
            ctiet.setIdBangKeCanHangHdr(idHdr);
            nhBangKeChCtRepository.save(ctiet);
        }
    }

    @Override
    public NhBangKeCanHang update(QlBangKeCanHangLtReq req) throws Exception {
        if (req == null){
            return null;
        }

        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        Optional<NhBangKeCanHang> optional = nhBangKeCanHangRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Bảng kê không tồn tại.");
        }

        NhBangKeCanHang item = optional.get();
        BeanUtils.copyProperties(req, item, "id");
        item.setNgaySua(new Date());
        item.setNguoiSuaId(userInfo.getId());
        nhBangKeCanHangRepository.save(item);
        fileDinhKemService.delete(item.getId(), Lists.newArrayList("NH_BANG_KE_CAN_HANG"));
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), item.getId(), "NH_BANG_KE_CAN_HANG");
        }
        this.saveCtiet(item.getId(),req);
        return item;
    }

    @Override
    public NhBangKeCanHang detail(Long id) throws Exception {
        UserInfo userInfo = SecurityContextService.getUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        Optional<NhBangKeCanHang> optional = nhBangKeCanHangRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Bảng kê không tồn tại.");

        NhBangKeCanHang item = optional.get();
        Map<String, String> listDanhMucDvi = getListDanhMucDvi("", "", "01");
        List<NhBangKeCanHangCt> listCt = nhBangKeChCtRepository.findAllByIdBangKeCanHangHdr(item.getId());
        List<NhBangKeCanHangCt> chiTiets = new ArrayList<>();
        List<NhBangKeCanHangCt> chiTietGd = new ArrayList<>();
        List<NhBangKeCanHangCt> chiTietTb = new ArrayList<>();
        listCt.forEach(ct -> {
            if (ct.getPhanLoai() == null) {
                chiTiets.add(ct);
            } else if (ct.getPhanLoai().equals("GD")) {
                chiTietGd.add(ct);
            } else if (ct.getPhanLoai().equals("TB")) {
                chiTietTb.add(ct);
            }
        });
        item.setChiTiets(chiTiets);
        item.setChiTietGd(chiTietGd);
        item.setChiTietTb(chiTietTb);
        item.setTenDvi(listDanhMucDvi.get(item.getMaDvi()));
        item.setTenNguoiTao(ObjectUtils.isEmpty(item.getNguoiTaoId()) ? "" : userInfoRepository.findById(item.getNguoiTaoId()).get().getFullName());
        item.setTenNguoiPduyet(ObjectUtils.isEmpty(item.getNguoiPduyetId()) ? "" :userInfoRepository.findById(item.getNguoiPduyetId()).get().getFullName());
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(item.getId(), Collections.singletonList("NH_BANG_KE_CAN_HANG"));
        item.setListFileDinhKem(fileDinhKem);
        return item;
    }

    @Override
    public NhBangKeCanHang approve(QlBangKeCanHangLtReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if (!Contains.CAP_CHI_CUC.equals(userInfo.getCapDvi())){
            throw new Exception("Bad Request");
        }

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        Optional<NhBangKeCanHang> optional = nhBangKeCanHangRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu");
        }

        NhBangKeCanHang phieu = optional.get();

        String status = req.getTrangThai() + phieu.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDCC + Contains.DUTHAO:
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                phieu.setNguoiGuiDuyetId(userInfo.getId());
                phieu.setNgayGuiDuyet(new Date());
                break;
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                phieu.setNguoiPduyetId(userInfo.getId());
                phieu.setNgayPduyet(new Date());
                phieu.setLyDoTuChoi(req.getLyDoTuChoi());
                break;
            case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
                phieu.setNguoiPduyetId(userInfo.getId());
                phieu.setNgayPduyet(new Date());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        phieu.setTrangThai(req.getTrangThai());
        nhBangKeCanHangRepository.save(phieu);
        return phieu;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<NhBangKeCanHang> optional = nhBangKeCanHangRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu cần xoá");
        }
        if (!optional.get().getTrangThai().equals(Contains.DUTHAO)) {
            throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp.");
        }
        fileDinhKemService.delete(optional.get().getId(), Lists.newArrayList("NH_BANG_KE_CAN_HANG"));
        nhBangKeCanHangRepository.delete(optional.get());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) {

    }

    @Override
    public void export(QlBangKeCanHangLtReq req, HttpServletResponse response) throws Exception {
//        return false;
    }

    @Override
    public List<NhBangKeCanHang> findAllByIdQdGiaoNvNh(Long idQdGiaoNvNh) {
        return setDetailList(nhBangKeCanHangRepository.findAllByIdQdGiaoNvNh(idQdGiaoNvNh));

    }

    @Override
    public List<NhBangKeCanHang> findAllByIdDdiemGiaoNvNh(Long idDdiemGiaoNvNh) {
        return setDetailList(nhBangKeCanHangRepository.findByIdDdiemGiaoNvNh(idDdiemGiaoNvNh));
    }

    List<NhBangKeCanHang> setDetailList(List<NhBangKeCanHang> list){
        list.forEach( item -> {
//            item.setPhieuNhapKho(StringUtils.isEmpty(item.getPhieuNhapKho()) ? null : nhPhieuNhapKhoRepository.getOne(Long.valueOf(item.getSoPhieuNhapKho().split("/")[0])));
        });
        return list;
    }
    public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
        try {
            String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
            String fileTemplate = "nhapdauthau/nhapkho/" + fileName;
//            NhQdGiaoNvuNhapxuatHdr qd = objectMapper.readValue(objectMapper.writeValueAsString(body.get("children")), NhQdGiaoNvuNhapxuatHdr.class);
            FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
            NhBangKeCanHang detail  = this.detail(DataUtils.safeToLong(body.get("id")));
            NhQdGiaoNvuNhapxuatHdr qd = hhQdGiaoNvuNhapxuatService.detail(String.valueOf(detail.getIdQdGiaoNvNh()));
            NhQdGiaoNvuNxDdiem diaDiem = null;
            if (qd != null && qd.getDtlList() != null) {
                for (NhQdGiaoNvuNhapxuatDtl dtl : qd.getDtlList()) {
                    if (diaDiem==null && dtl.getMaDvi() != null && dtl.getMaDvi().equals(detail.getMaDvi())) {
                        diaDiem = dtl.getChildren().stream().filter(item -> item.getId().longValue() == detail.getIdDdiemGiaoNvNh().longValue()).findFirst().orElse(null);
                    }
                }
            }
            NhPhieuNhapKho pn = nhPhieuNhapKhoService.detail(Long.valueOf(detail.getSoPhieuNhapKho().split("/")[0]));
            return docxToPdfConverter.convertDocxToPdf(inputStream, detail, qd, diaDiem,pn);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void exportToExcel(HhQdNhapxuatSearchReq req, HttpServletResponse response) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        req.setMaDvi(userInfo.getDvql());
        Page<NhQdGiaoNvuNhapxuatHdr> page = hhQdGiaoNvuNhapxuatService.searchPage(req);
        List<NhQdGiaoNvuNhapxuatHdr> data = page.getContent();

        String title = "Danh sách bảng kê cân hàng";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVNH", "Năm kế hoạch", "Thời hạn NH", "Điểm kho", "Ngăn/Lô kho",
                "Số bảng kê", "Số phiếu nhập kho", "Ngày lập bảng kê", "Trạng thái"};
        String filename = "danh-sach-bang-ke-can-hang.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        Object[] objsb = null;
        Object[] objsc = null;
        for (int i = 0; i < data.size(); i++) {
            NhQdGiaoNvuNhapxuatHdr qd = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = qd.getSoQd();
            objs[2] = qd.getNamNhap();
            objs[3] = convertDate(qd.getTgianNkho());
            dataList.add(objs);
            if (userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
                qd.setDetail(qd.getDtlList().stream().filter(item -> item.getMaDvi().equals(userInfo.getDvql())).collect(Collectors.toList()).get(0));
            } else {
                List<NhQdGiaoNvuNxDdiem> dataDd = new ArrayList<>();
                for (NhQdGiaoNvuNhapxuatDtl nhQdGiaoNvuNhapxuatDtl : qd.getDtlList()) {
                    dataDd.addAll(nhQdGiaoNvuNhapxuatDtl.getChildren());
                }
                qd.setDetail(new NhQdGiaoNvuNhapxuatDtl());
                qd.getDetail().setChildren(dataDd);
            }
            for (int j = 0; j < qd.getDetail().getChildren().size(); j++) {
                objsb = new Object[rowsName.length];
                objsb[4] = qd.getDetail().getChildren().get(j).getTenDiemKho();
                objsb[5] = qd.getDetail().getChildren().get(j).getTenLoKho() != null ? qd.getDetail().getChildren().get(j).getTenLoKho() + " - " + qd.getDetail().getChildren().get(j).getTenNganKho() : qd.getDetail().getChildren().get(j).getTenNganKho();
                dataList.add(objsb);
                for (int k = 0; k < qd.getDetail().getChildren().get(j).getListBangKeCanHang().size(); k++) {
                    objsc = new Object[rowsName.length];
                    objsc[6] = qd.getDetail().getChildren().get(j).getListBangKeCanHang().get(k).getSoBangKe();
                    objsc[7] = qd.getDetail().getChildren().get(j).getListBangKeCanHang().get(k).getSoPhieuNhapKho();
                    objsc[8] = convertDate(qd.getDetail().getChildren().get(j).getListBangKeCanHang().get(k).getNgayTao());
                    objsc[9] = NhapXuatHangTrangThaiEnum.getTrangThaiDuyetById(qd.getDetail().getChildren().get(j).getListBangKeCanHang().get(k).getTrangThai());
                    dataList.add(objsc);
                }
            }
        }
        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

}
