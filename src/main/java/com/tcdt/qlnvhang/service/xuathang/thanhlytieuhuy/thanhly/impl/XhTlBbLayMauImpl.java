package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.impl;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScPhieuXuatKhoHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScQuyetDinhNhapHangRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScQuyetDinhXuatHangRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.suachua.ScPhieuXuatKhoReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhNhapHangReq;
import com.tcdt.qlnvhang.request.suachua.ScQuyetDinhXuatHangReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlKtraClReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQdGiaoNvHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrDTO;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScPhieuXuatKhoService;
import com.tcdt.qlnvhang.service.suachuahang.ScQuyetDinhScService;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlBbLayMauService;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachService;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlKtraClService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.*;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhTlBbLayMauImpl extends BaseServiceImpl implements XhTlBbLayMauService {
    @Autowired
    private XhTlBbLayMauHdrRepository hdrRepository;
    @Autowired
    private XhTlBbLayMauDtlRepository dtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private XhTlQdGiaoNvHdrRepository xhTlQdGiaoNvHdrRepository;

    @Autowired
    private XhTlQdGiaoNvDtlRepository xhTlQdGiaoNvDtlRepository;

    @Autowired
    private XhTlDanhSachService xhTlDanhSachService;

    @Override
    public Page<XhTlBbLayMauHdr> searchPage(XhTlBbLayMauReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhTlBbLayMauHdr> bienBanLayMaus = hdrRepository.searchPage(req,pageable);
        return bienBanLayMaus;
    }

    @Override
    public XhTlBbLayMauHdr create(XhTlBbLayMauReq req) throws Exception {
        UserInfo currentUser = SecurityContextService.getUser();
        if (currentUser == null) {
            throw new Exception("Bad request.");
        }
        if (!currentUser.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        XhTlBbLayMauHdr data = new XhTlBbLayMauHdr();
        BeanUtils.copyProperties(req, data);
        data.setMaDvi(currentUser.getDvql());
        data.setIdKtv(currentUser.getId());
        data.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
        data.setId(Long.parseLong(req.getSoBienBan().split("/")[0]));
        XhTlBbLayMauHdr created = hdrRepository.save(data);
        saveFileDinhKem(req.getFileDinhKemReq(), created.getId(), XhTlBbLayMauHdr.TABLE_NAME);
        List<XhTlBbLayMauDtl> dtls = saveDtl(req, data.getId());
        created.setChildren(dtls);
        return created;
    }

    public List<XhTlBbLayMauDtl> saveDtl(XhTlBbLayMauReq req, Long idHdr){
        dtlRepository.deleteAllByIdHdr(idHdr);
        req.getChildren().forEach(item -> {
            item.setId(null);
            item.setIdHdr(idHdr);
            dtlRepository.save(item);
        });
        return req.getChildren();
    }

    @Override
    public XhTlBbLayMauHdr update(XhTlBbLayMauReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<XhTlBbLayMauHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhTlBbLayMauHdr data = optional.get();
        BeanUtils.copyProperties(req, data);
        fileDinhKemService.delete(data.getId(), Collections.singleton(XhTlBbLayMauHdr.TABLE_NAME));
        saveFileDinhKem(req.getFileDinhKemReq(), data.getId(), XhTlBbLayMauHdr.TABLE_NAME);
        List<XhTlBbLayMauDtl> dtls = saveDtl(req, data.getId());
        data.setChildren(dtls);
        return data;
    }

    @Override
    public XhTlBbLayMauHdr detail(Long id) throws Exception {
        Optional<XhTlBbLayMauHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhTlBbLayMauHdr data = optional.get();
        data.setFileDinhKem(fileDinhKemService.search(id, Collections.singleton(XhTlBbLayMauHdr.TABLE_NAME)));
        data.setChildren(dtlRepository.findAllByIdHdr(id));
//        if(!Objects.isNull(data.getIdPhieuXuatKho())){
//            data.setScPhieuXuatKhoHdr(scPhieuXuatKhoService.detail(data.getIdPhieuXuatKho()));
//        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        data.setTenDvi(mapDmucDvi.getOrDefault(data.getMaDvi(),null));
        data.setMapDmucDvi(mapDmucDvi);
        data.setMapVthh(mapVthh);
        if(!Objects.isNull(data.getIdKtv())){
            data.setTenKtv(userInfoRepository.findById(data.getIdKtv()).get().getFullName());
        }
        if(!Objects.isNull(data.getIdLdcc())){
            data.setTenLdcc(userInfoRepository.findById(data.getIdLdcc()).get().getFullName());
        }
        return data;
    }

    @Override
    public XhTlBbLayMauHdr approve(XhTlBbLayMauReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhTlBbLayMauHdr> optional = hdrRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new Exception("Bản ghi không tồn tại");
        }
        XhTlBbLayMauHdr hdr = optional.get();
        String status = hdr.getTrangThai() + req.getTrangThai();
        if(!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
            throw new Exception("Đơn vị gửi duyệt phải là cấp chi cục");
        }
        switch (status) {
            // Re approve : gửi lại duyệt
            case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
                break;
            // Arena các cấp duuyệt
            case Contains.DUTHAO + Contains.CHODUYET_LDCC:
                break;
            case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
                hdr.setIdLdcc(userInfo.getId());
                break;
            // Arena từ chối
            case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
                hdr.setLyDoTuChoi(req.getLyDoTuChoi());
                hdr.setIdLdcc(userInfo.getId());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        hdr.setTrangThai(req.getTrangThai());
        XhTlBbLayMauHdr save = hdrRepository.save(hdr);
        return save;
    }

    @Override
    public void delete(Long id) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        if (!userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)) {
            throw new Exception("Chức năng chỉ dành cho cấp chi cục");
        }
        Optional<XhTlBbLayMauHdr> optional = hdrRepository.findById(id);
        if (!optional.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        hdrRepository.delete(optional.get());
        fileDinhKemService.delete(optional.get().getId(), Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME));
        dtlRepository.deleteAllByIdHdr(optional.get().getId());
    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(XhTlBbLayMauReq req, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        req.setPaggingReq(paggingReq);
        Page<XhTlBbLayMauHdr> page = searchPage(req);
        List<XhTlBbLayMauHdr> data = page.getContent();

        String title = "Danh sách biên bản lấy mẫu";
        String[] rowsName = new String[]{"STT", "Số QĐ giao NVXH của Cục", "Năm KH", "Thời hạn thanh lý",
            "Điểm kho / Nhà kho / Ngăn kho / Lô kho", "Số BB LM/BGM", "Ngày lấy mẫu", "Số BB tịnh kho",
                "Ngày lập BB tịnh kho", "Số BB tịnh kho", "Trạng thái"};
        String fileName = "danh-sach-bien-ban-lay-mau.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhTlBbLayMauHdr dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
//            objs[1] = ;
//            objs[2] = dx.getNamKh();
//            objs[3] = dx.getThoiHanDieuChuyen();
            objs[4] = dx.getTenDiemKho() + "/" + dx.getTenNhaKho() + "/" + dx.getTenNganKho() + "/" + dx.getTenLoKho();
            objs[5] = dx.getSoBienBan();
            objs[6] = dx.getNgayLayMau();
//            objs[7] = dx.getSoPhieuNhapKho();
//            objs[8] = dx.getNgayNhapKho();
//            objs[9] = dx.getSoPhieuKiemTraCl();
            objs[10] = dx.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
        ex.export();
    }

    @Override
    public List<XhTlBbLayMauHdr> dsTaoKtraCluong(XhTlBbLayMauReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        req.setMaDviSr(userInfo.getDvql());
        List<XhTlBbLayMauHdr> list = hdrRepository.searchDsTaoKtraCl(req);
        return list;
    }

    @Override
    public Page<XhTlQdGiaoNvHdr> searchBbLm(XhTlBbLayMauReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        XhTlQdGiaoNvHdrReq reqQd = new XhTlQdGiaoNvHdrReq();
        reqQd.setNam(req.getNam());
        reqQd.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
        reqQd.setPhanLoai(req.getPhanLoai());
        if(userInfo.getCapDvi().equals(Contains.CAP_CUC)){
            reqQd.setMaDviSr(userInfo.getDvql());
        }
        if(userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
            reqQd.setMaDviSr(userInfo.getDvql().substring(0, 6));
        }
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
        Page<XhTlQdGiaoNvHdr> search = xhTlQdGiaoNvHdrRepository.searchPageViewFromAnother(reqQd, pageable);
        search.getContent().forEach(item -> {
            try {
                req.setIdQdXh(item.getId());
                List<XhTlBbLayMauHdr> hdrList  = hdrRepository.findAllByIdQdXh(req);
                hdrList.forEach(hdr -> {
                    hdr.setTenDvi(mapDmucDvi.getOrDefault(hdr.getMaDvi(),null));
                    hdr.setMapDmucDvi(mapDmucDvi);
                    hdr.setMapVthh(mapVthh);
                });
                item.setListXhTlBbLayMauHdr(hdrList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return search;
    }

}
