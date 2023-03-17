package com.tcdt.qlnvhang.service.xuathang.bantructiep.ktracluong.hosokythuat;

import com.tcdt.qlnvhang.entities.xuathang.bantructiep.kehoach.pheduyet.XhQdPdKhBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.hosokythuat.XhHSoKthuatBttDtl;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.hosokythuat.XhHSoKthuatBttHdr;
import com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.thongtin.XhTcTtinBtt;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.hosokythuat.XhHSoKthuatBttDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.bantructiep.ktracluong.hosokythuat.XhHSoKthuatBttHdrRepository;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.hosokythuat.XhHSoKthuatBttDtlReq;
import com.tcdt.qlnvhang.request.xuathang.bantructiep.ktracluong.hosokythuat.XhHSoKthuatBttHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class XhHSoKthuatBttServiceImpI extends BaseServiceImpl implements XhHSoKthuatBttService {

    @Autowired
    private XhHSoKthuatBttHdrRepository xhHSoKthuatBttHdrRepository;

    @Autowired
    private XhHSoKthuatBttDtlRepository xhHSoKthuatBttDtlRepository;

    @Autowired
    private FileDinhKemService fileDinhKemService;


    @Override
    public Page<XhHSoKthuatBttHdr> searchPage(XhHSoKthuatBttHdrReq req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
                req.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhHSoKthuatBttHdr> data = xhHSoKthuatBttHdrRepository.searchPage(
                req,
                pageable);

        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
        data.getContent().forEach(f ->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : hashMapDvi.get(f.getMaDvi()));
            f.setTenDiemKho(StringUtils.isEmpty(f.getMaDiemKho()) ? null : hashMapDvi.get(f.getMaDiemKho()));
            f.setTenNhaKho(StringUtils.isEmpty(f.getMaNhaKho()) ? null : hashMapDvi.get(f.getMaNhaKho()));
            f.setTenNganKho(StringUtils.isEmpty(f.getMaNganKho()) ? null : hashMapDvi.get(f.getMaNganKho()));
            f.setTenLoKho(StringUtils.isEmpty(f.getMaLoKho()) ? null : hashMapDvi.get(f.getMaLoKho()));
        });
        return data;
    }

    @Override
    public XhHSoKthuatBttHdr create(XhHSoKthuatBttHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        XhHSoKthuatBttHdr data = new XhHSoKthuatBttHdr();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgayTao(getDateTimeNow());
        data.setNguoiTaoId(userInfo.getId());
        data.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
        data.setMaDvi(userInfo.getDvql());
        data.setId(Long.parseLong(req.getSoHoSoKyThuat().split("/")[0]));
        xhHSoKthuatBttHdrRepository.save(data);
        saveDetail(req, data.getId());
        return data;
    }

    void  saveDetail (XhHSoKthuatBttHdrReq req , Long idHdr){
        xhHSoKthuatBttDtlRepository.deleteAllByIdHdr(idHdr);
        for (XhHSoKthuatBttDtlReq dtlReq : req.getChildren()){
            XhHSoKthuatBttDtl dtl = new XhHSoKthuatBttDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdHdr(idHdr);
            if (!DataUtils.isNullObject(dtlReq.getFileDinhKems())) {
                List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(dtlReq.getFileDinhKems()), dtl.getId(), XhHSoKthuatBttDtl.TABLE_NAME);
                dtl.setFileDinhKems(fileDinhKems.get(0));
            }
             xhHSoKthuatBttDtlRepository.save(dtl);
        }
    }

    @Override
    public XhHSoKthuatBttHdr update(XhHSoKthuatBttHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();

        if (StringUtils.isEmpty(req.getId())){
            throw new Exception("Sủa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhHSoKthuatBttHdr> qOptional = xhHSoKthuatBttHdrRepository.findById(req.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Hồ sơ kĩ thuật không tồn tại");
        }

        XhHSoKthuatBttHdr dataDB = qOptional.get();
        BeanUtils.copyProperties(req, dataDB, "id");
        dataDB.setNgaySua(getDateTimeNow());
        dataDB.setNguoiSuaId(userInfo.getId());
        xhHSoKthuatBttHdrRepository.save(dataDB);
        this.saveDetail(req, dataDB.getId());
        return dataDB;
    }

    @Override
    public XhHSoKthuatBttHdr detail(Long id) throws Exception {
        if (StringUtils.isEmpty(id))
            throw new Exception("Không tồn tại bản ghi");

        Optional<XhHSoKthuatBttHdr> qOptional = xhHSoKthuatBttHdrRepository.findById(id);

        if (!qOptional.isPresent()){
            throw new UnsupportedOperationException("Bản ghi không tồn tại");
        }
        Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");

        XhHSoKthuatBttHdr data = qOptional.get();

        data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
        data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
        data.setTenDiemKho(hashMapDvi.get(data.getMaDiemKho()));
        data.setTenNhaKho(hashMapDvi.get(data.getMaNhaKho()));
        data.setTenNganKho(hashMapDvi.get(data.getMaNganKho()));
        data.setTenLoKho(hashMapDvi.get(data.getMaLoKho()));

        List<XhHSoKthuatBttDtl> byIdHdr = xhHSoKthuatBttDtlRepository.findAllByIdHdr(data.getId());
        for (XhHSoKthuatBttDtl dtl : byIdHdr){
            List<FileDinhKem> fileDinhKems = fileDinhKemService.search(dtl.getId(), Arrays.asList(XhHSoKthuatBttDtl.TABLE_NAME));
            if (!DataUtils.isNullOrEmpty(fileDinhKems)) {
                dtl.setFileDinhKems(fileDinhKems.get(0));
            }
        }
        data.setChildren(byIdHdr);
        return data;
    }

    @Override
    public XhHSoKthuatBttHdr approve(XhHSoKthuatBttHdrReq req) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo();
        Optional<XhHSoKthuatBttHdr> optional = xhHSoKthuatBttHdrRepository.findById(req.getId());
        if (!optional.isPresent()){
            throw new Exception("Hồ sơ kỹ thuật không tồn tại.");
        }
        XhHSoKthuatBttHdr data = optional.get();
        String trangThai = req.getTrangThai() + data.getTrangThai();
        if (
                (NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId() + NhapXuatHangTrangThaiEnum.DUTHAO.getId()).equals(trangThai) ||
                        (NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId() + NhapXuatHangTrangThaiEnum.TUCHOI_TP.getId()).equals(trangThai) ||
                        (NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId() + NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId()).equals(trangThai)
        ) {
            data.setNguoiGuiDuyetId(userInfo.getId());
            data.setNgayGuiDuyet(getDateTimeNow());
        } else if (
                (NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId()).equals(trangThai) ||
                        (NhapXuatHangTrangThaiEnum.TUCHOI_TP.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId()).equals(trangThai)
        ) {
            data.setNguoiPduyetId(userInfo.getId());
            data.setLyDoTuChoi(req.getLyDoTuChoi());
        } else if (
                (NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId()).equals(trangThai) ||
                        (NhapXuatHangTrangThaiEnum.TUCHOI_LDC.getId() + NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId()).equals(trangThai)
        ) {
            data.setNgayPduyet(getDateTimeNow());
            data.setNguoiPduyetId(userInfo.getId());
            data.setLyDoTuChoi(req.getLyDoTuChoi());
        } else {
            throw new Exception("Phê duyệt không thành công");
        }
        data.setTrangThai(req.getTrangThai());
        xhHSoKthuatBttHdrRepository.save(data);
        return data;
    }

    @Override
    public void delete(Long id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new Exception("Xóa thất bại, KHông tìm thấy dữ liệu");
        }
        Optional<XhHSoKthuatBttHdr> optional = xhHSoKthuatBttHdrRepository.findById(id);
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần xóa");

        XhHSoKthuatBttHdr data = optional.get();
        if (NhapXuatHangTrangThaiEnum.DADUYET_LDCC.getId().equals(data.getTrangThai())) {
            throw new Exception("Không thể xóa biên bản đã đã duyệt");
        }

        List<XhHSoKthuatBttDtl> dtlList = xhHSoKthuatBttDtlRepository.findAllByIdHdr(data.getId());
        if (!CollectionUtils.isEmpty(dtlList)){
            for (XhHSoKthuatBttDtl dtl : dtlList){
                fileDinhKemService.delete(dtl.getId(), Collections.singleton(XhHSoKthuatBttDtl.TABLE_NAME));
                xhHSoKthuatBttDtlRepository.deleteAll(dtlList);
            }
        }
        xhHSoKthuatBttHdrRepository.delete(data);

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {
        if (StringUtils.isEmpty(listMulti)){
            throw new Exception("Xóa thất bại không tìm thấy dữ liệu");
        }

        List<XhHSoKthuatBttHdr> list = xhHSoKthuatBttHdrRepository.findAllByIdIn(listMulti);
        if (list.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }

        for (XhHSoKthuatBttHdr hdr : list){
            if (!hdr.getTrangThai().equals(Contains.DUTHAO)) {
                throw new Exception("Chỉ thực hiện xóa bản ghi ở trạng thái dự thảo");
            }else {
                this.delete(hdr.getId());
            }
        }
    }

    @Override
    public void export(XhHSoKthuatBttHdrReq req, HttpServletResponse response) throws Exception {

    }
}
