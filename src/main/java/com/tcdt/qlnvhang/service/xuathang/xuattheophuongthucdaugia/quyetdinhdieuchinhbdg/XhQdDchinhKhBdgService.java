package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.*;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.*;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinh.XhQdDchinhKhBdgReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinh.XhQdDchinhKhBdgSearchReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.feign.KeHoachService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.*;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgDtl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgHdr;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg.XhQdDchinhKhBdgPlDtl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhQdDchinhKhBdgService extends BaseServiceImpl {


    @Autowired
    private XhQdDchinhKhBdgHdrRepository xhQdDchinhKhBdgHdrRepository;

    @Autowired
    private XhQdDchinhKhBdgDtlRepository xhQdDchinhKhBdgDtlRepository;

    @Autowired
    private XhQdDchinhKhBdgPlRepository xhQdDchinhKhBdgPlRepository;

    @Autowired
    private XhQdDchinhKhBdgPlDtlRepository xhQdDchinhKhBdgPlDtlRepository;

    @Autowired
    private XhThopDxKhBdgRepository xhThopDxKhBdgRepository;

    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;

    @Autowired
    FileDinhKemRepository fileDinhKemRepository;

    @Autowired
    private KeHoachService keHoachService;

    @Autowired
    private XhTcTtinBdgHdrRepository xhTcTtinBdgHdrRepository;

    public Page<XhQdDchinhKhBdgHdr> searchPage(XhQdDchinhKhBdgSearchReq objReq)throws Exception{

        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhQdDchinhKhBdgHdr> data = xhQdDchinhKhBdgHdrRepository.searchPage(
                objReq.getNamKh(),
                objReq.getSoQdDc(),
                objReq.getTrichYeu(),
                Contains.convertDateToString(objReq.getNgayKyQdTu()),
                Contains.convertDateToString(objReq.getNgayKyQdDen()),
                objReq.getSoTrHdr(),
                objReq.getLoaiVthh(),
                objReq.getTrangThai(),
                objReq.getLastest(),
                objReq.getMaDvi(),
                pageable);
        Map<String,String> hashMapDmHh = getListDanhMucHangHoa();
        data.getContent().forEach(f->{
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
        });
        return data;
    }

    @Transactional
    public XhQdDchinhKhBdgHdr create(XhQdDchinhKhBdgReq objReq) throws Exception{
        // Vật tư
        if(objReq.getLoaiVthh().startsWith("02")){
            return createVatTu(objReq);
        }else{
            // Lương thực
            return createLuongThuc(objReq);
        }
    }

    @Transactional
    public XhQdDchinhKhBdgHdr createLuongThuc (XhQdDchinhKhBdgReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
            throw new Exception("Loại vật tư hàng hóa không phù hợp");

        if(!StringUtils.isEmpty(objReq.getSoQdPd())){
            List<XhQdDchinhKhBdgHdr> checkSoQd = xhQdDchinhKhBdgHdrRepository.findBySoQdDc(objReq.getSoQdPd());
            if (!checkSoQd.isEmpty()) {
                throw new Exception("Số quyết định " + objReq.getSoQdPd() + " đã tồn tại");
            }
        }

        if (objReq.getPhanLoai().equals("TH")){
            Optional<XhThopDxKhBdg> qOptionalTh = xhThopDxKhBdgRepository.findById(objReq.getIdThHdr());
            if (!qOptionalTh.isPresent()){
                throw new Exception("Không tìm thấy tổng hợp kế hoạch bán đấu giá");
            }
        }else {
            Optional<XhDxKhBanDauGia> byId = xhDxKhBanDauGiaRepository.findById(objReq.getIdTrHdr());
            if(!byId.isPresent()){
                throw new Exception("Không tìm thấy đề xuất kế hoạch bán đấu giá");
            }
        }
        XhQdDchinhKhBdgHdr dataMap = ObjectMapperUtils.map(objReq, XhQdDchinhKhBdgHdr.class);
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setLastest(objReq.getLastest());
        dataMap.setMaDvi(userInfo.getDepartment());
        XhQdDchinhKhBdgHdr created=xhQdDchinhKhBdgHdrRepository.save(dataMap);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),dataMap.getId(),"XH_DX_KH_BAN_DAU_GIA");
        created.setFileDinhKems(fileDinhKems);

        // Update trạng thái tổng hợp dxkhclnt
        if(objReq.getPhanLoai().equals("TH")){
            xhThopDxKhBdgRepository.updateTrangThai(dataMap.getIdThHdr(), Contains.DADUTHAO_QD);
        }else{
            xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(objReq.getSoTrHdr()), Contains.DADUTHAO_QD);
            xhDxKhBanDauGiaRepository.updateSoQdPd(Arrays.asList(objReq.getSoTrHdr()),dataMap.getSoQdPd());
            xhDxKhBanDauGiaRepository.updateNgayKyQd(Arrays.asList(objReq.getSoTrHdr()),dataMap.getNgayKyQd());
        }

        saveDetail(objReq,dataMap);
        return created;
    }

    @Transactional
    XhQdDchinhKhBdgHdr createVatTu(XhQdDchinhKhBdgReq objReq) throws Exception{
        UserInfo userInfo= SecurityContextService.getUser();
        List<XhQdDchinhKhBdgHdr> checkSoQd = xhQdDchinhKhBdgHdrRepository.findBySoQdDc(objReq.getSoQdPd());
        if (!checkSoQd.isEmpty()) {
            throw new Exception("Số quyết định " + objReq.getSoQdPd() + " đã tồn tại");
        }

        XhQdDchinhKhBdgHdr dataMap = ObjectMapperUtils.map(objReq, XhQdDchinhKhBdgHdr.class);
        dataMap.setTrangThai(Contains.DUTHAO);
        dataMap.setLastest(objReq.getLastest());
        dataMap.setMaDvi(userInfo.getDepartment());
        XhQdDchinhKhBdgHdr created=xhQdDchinhKhBdgHdrRepository.save(dataMap);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),dataMap.getId(),"XH_DX_KH_BAN_DAU_GIA");
        created.setFileDinhKems(fileDinhKems);

        // Update trạng thái tờ trình
        xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(objReq.getSoTrHdr()), Contains.DADUTHAO_QD);

        saveDetail(objReq,dataMap);

        return created;
    }

    @Transactional
    void saveDetail(XhQdDchinhKhBdgReq objReq, XhQdDchinhKhBdgHdr dataMap){
        xhQdDchinhKhBdgDtlRepository.deleteAllByIdQdHdr(dataMap.getId());
        for (XhQdPdKhBdgDtlReq  dx : objReq.getDsDeXuat()){
            XhQdDchinhKhBdgDtl qd = ObjectMapperUtils.map(dx, XhQdDchinhKhBdgDtl.class);
            xhQdDchinhKhBdgPlRepository.deleteByIdQdDtl(qd.getId());
            qd.setId(null);
            qd.setIdQdHdr(dataMap.getId());
            qd.setTrangThai(Contains.CHUACAPNHAT);
            xhQdDchinhKhBdgDtlRepository.save(qd);
            for (XhQdPdKhBdgPlReq gtList : ObjectUtils.isEmpty(dx.getDsPhanLoList()) ? dx.getChildren(): dx.getDsPhanLoList()){
                XhQdDchinhKhBdgPl gt = ObjectMapperUtils.map(gtList, XhQdDchinhKhBdgPl.class);
                xhQdDchinhKhBdgPlDtlRepository.deleteAllByIdPhanLo(gt.getId());
                gt.setId(null);
                gt.setIdQdDtl(qd.getId());
                gt.setTrangThai(Contains.CHUACAPNHAT);
                xhQdDchinhKhBdgPlRepository.save(gt);
                for (XhQdPdKhBdgPlDtlReq ddNhap : gtList.getChildren()){
                    XhQdDchinhKhBdgPlDtl dataDdNhap = new ModelMapper().map(ddNhap, XhQdDchinhKhBdgPlDtl.class);
                    dataDdNhap.setId(null);
                    dataDdNhap.setIdQdHdr(dataMap.getId());
                    dataDdNhap.setIdPhanLo(gt.getId());
                    xhQdDchinhKhBdgPlDtlRepository.save(dataDdNhap);
                }
            }
        }
    }

    public void validateData(XhQdDchinhKhBdgHdr objHdr) throws Exception{
        for (XhQdDchinhKhBdgDtl dtl : objHdr.getChildren()){
            for (XhQdDchinhKhBdgPl dsgthau : dtl.getChildren()){
                BigDecimal aLong =xhDxKhBanDauGiaRepository.countSLDalenKh(objHdr.getNamKh(), objHdr.getLoaiVthh(), dsgthau.getMaDvi(),NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
                BigDecimal soLuongTotal = aLong.add(dsgthau.getSoLuong());
                BigDecimal nhap = keHoachService.getChiTieuNhapXuat(objHdr.getNamKh(),objHdr.getLoaiVthh(), dsgthau.getMaDvi(), "NHAP" );
                if (soLuongTotal.compareTo(nhap) >0){
                    throw new Exception(dsgthau.getTenDvi()+ "Đã nhập quá số lượng chỉ tiêu vui lòng nhập lại");
                }
            }
        }
    }

    @Transactional
    public XhQdDchinhKhBdgHdr update(XhQdDchinhKhBdgReq objReq) throws Exception {
        // Vật tư
        if(objReq.getLoaiVthh().startsWith("02")){
            return updateVatTu(objReq);
        }else{
            // Lương thực
            return updateLuongThuc(objReq);
        }
    }

    @Transactional
    public XhQdDchinhKhBdgHdr updateLuongThuc(XhQdDchinhKhBdgReq objReq) throws Exception{
        if (StringUtils.isEmpty(objReq.getId())){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhQdDchinhKhBdgHdr> qOptional = xhQdDchinhKhBdgHdrRepository.findById(objReq.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        if(!StringUtils.isEmpty(objReq.getSoQdPd())){
            if (!objReq.getSoQdPd().equals(qOptional.get().getSoQdPd())) {
                List<XhQdDchinhKhBdgHdr> checkSoQd = xhQdDchinhKhBdgHdrRepository.findBySoQdDc(objReq.getSoQdPd());
                if (!checkSoQd.isEmpty()) {
                    throw new Exception("Số quyết định " + objReq.getSoQdPd() + " đã tồn tại");
                }
            }
        }

        if (objReq.getPhanLoai().equals("TH")){
            Optional<XhThopDxKhBdg> qOptionalTh = xhThopDxKhBdgRepository.findById(objReq.getIdThHdr());
            if (!qOptionalTh.isPresent()){
                throw new Exception("Không tìm thấy tổng hợp kế hoạch bán đấu giá");
            }
        }else {
            Optional<XhDxKhBanDauGia> byId = xhDxKhBanDauGiaRepository.findById(objReq.getIdTrHdr());
            if (!byId.isPresent()){
                throw new Exception("Không tìm thấy đề xuất kế hoạch bán đấu giá");
            }
        }

        XhQdDchinhKhBdgHdr dataDB = qOptional.get();
        XhQdDchinhKhBdgHdr dataMap = ObjectMapperUtils.map(objReq, XhQdDchinhKhBdgHdr.class);

        updateObjectToObject(dataDB, dataMap);

        xhQdDchinhKhBdgHdrRepository.save(dataDB);
        this.saveDetail(objReq,dataMap);
        return dataDB;

    }

    @Transactional
    public XhQdDchinhKhBdgHdr updateVatTu(XhQdDchinhKhBdgReq objReq) throws Exception{
        if (StringUtils.isEmpty(objReq.getId())){
            throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
        }

        Optional<XhQdDchinhKhBdgHdr> qOptional = xhQdDchinhKhBdgHdrRepository.findById(objReq.getId());
        if (!qOptional.isPresent()){
            throw new Exception("Không tìm thấy dữ liệu cần sửa");
        }

        if (!qOptional.get().getSoQdPd().equals(objReq.getSoQdPd())){
            List<XhQdDchinhKhBdgHdr> checkSoQd = xhQdDchinhKhBdgHdrRepository.findBySoQdDc(objReq.getSoQdPd());
            if (!checkSoQd.isEmpty()){
                throw new Exception("Số quết định" + objReq.getSoQdPd() + " đã tồn tại" );
            }
        }
        XhQdDchinhKhBdgHdr dataDB = qOptional.get();
        XhQdDchinhKhBdgHdr dataMap = ObjectMapperUtils.map(objReq, XhQdDchinhKhBdgHdr.class);

        updateObjectToObject(dataDB, dataMap);

        xhQdDchinhKhBdgHdrRepository.save(dataDB);

        xhQdDchinhKhBdgDtlRepository.deleteAllByIdQdHdr(dataMap.getId());
        XhQdDchinhKhBdgDtl qdDtl = new XhQdDchinhKhBdgDtl();
        qdDtl.setId(null);
        qdDtl.setIdQdHdr(dataDB.getId());
        qdDtl.setMaDvi(getUser().getDvql());
        qdDtl.setTrangThai(Contains.CHUACAPNHAT);
        xhQdDchinhKhBdgDtlRepository.save(qdDtl);

        this.saveDetail(objReq, dataDB);
        return dataDB;
    }

    public XhQdDchinhKhBdgHdr detail (String ids) throws Exception {
        if (StringUtils.isEmpty(ids))
            throw new Exception("Không tồn tại bản ghi");

        Optional<XhQdDchinhKhBdgHdr> qOptional = xhQdDchinhKhBdgHdrRepository.findById(Long.parseLong(ids));


        if (!qOptional.isPresent())
            throw new UnsupportedOperationException("Không tồn tại bản ghi");

        Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");

        qOptional.get().setTenLoaiVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getLoaiVthh()));
        qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getCloaiVthh()));
        qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
        List<XhQdDchinhKhBdgDtl> xhQdPdKhBdgDtlList = new ArrayList<>();
        for (XhQdDchinhKhBdgDtl dtl : xhQdDchinhKhBdgDtlRepository.findAllByIdQdHdr(Long.parseLong(ids))){
            List<XhQdDchinhKhBdgPl>  xhQdPdKhBdgPlList = new ArrayList<>();
            for (XhQdDchinhKhBdgPl dsg : xhQdDchinhKhBdgPlRepository.findByIdQdDtl(dtl.getId())){
                List<XhQdDchinhKhBdgPlDtl> xhQdPdKhBdgPlDtlList = xhQdDchinhKhBdgPlDtlRepository.findByIdPhanLo(dsg.getId());
                xhQdPdKhBdgPlDtlList.forEach( f ->{
                    f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
                    f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));
                    f.setTenNhakho(mapDmucDvi.get(f.getMaNhaKho()));
                    f.setTenNganKho(mapDmucDvi.get(f.getMaNganKho()));
                    f.setTenLoKho(mapDmucDvi.get(f.getMaLoKho()));
                    f.setTenLoaiVthh(hashMapDmHh.get(f.getLoaiVthh()));
                    f.setTenCloaiVthh(hashMapDmHh.get(f.getCloaiVthh()));
                });
                dsg.setTenDvi(mapDmucDvi.get(dsg.getMaDvi()));
                dsg.setTenDiemKho(mapDmucDvi.get(dsg.getMaDiemKho()));
                dsg.setTenNhakho(mapDmucDvi.get(dsg.getMaNhaKho()));
                dsg.setTenNganKho(mapDmucDvi.get(dsg.getMaNganKho()));
                dsg.setTenLoKho(mapDmucDvi.get(dsg.getMaLoKho()));
                dsg.setTenLoaiVthh(hashMapLoaiHdong.get(dsg.getTenLoaiVthh()));
                dsg.setTenCloaiVthh(hashMapDmHh.get(dsg.getCloaiVthh()));
                dsg.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dsg.getTrangThai()));
                dsg.setChildren(xhQdPdKhBdgPlDtlList);
                xhQdPdKhBdgPlList.add(dsg);
            };
            dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi()) ? null : mapDmucDvi.get(dtl.getMaDvi()));
            dtl.setChildren(xhQdPdKhBdgPlList);
            dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));
            xhQdPdKhBdgDtlList.add(dtl);
        }
        qOptional.get().setChildren(xhQdPdKhBdgDtlList);
        qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
       return qOptional.get();
    }

    @Transactional
    public void delete(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId()))
            throw new Exception("Xóa thất bại, KHông tìm thấy dữ liệu");

        Optional<XhQdDchinhKhBdgHdr> optional = xhQdDchinhKhBdgHdrRepository.findById(idSearchReq.getId());
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần xóa");

        if (!optional.get().getTrangThai().equals(Contains.DUTHAO) && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDV)){
            throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
        }

        List<XhQdDchinhKhBdgDtl> xhQdPdKhBdgDtl = xhQdDchinhKhBdgDtlRepository.findAllByIdQdHdr(optional.get().getId());
        if (!CollectionUtils.isEmpty(xhQdPdKhBdgDtl)){
            for (XhQdDchinhKhBdgDtl dtl : xhQdPdKhBdgDtl){
                List<XhQdDchinhKhBdgPl> byIdQdDtl = xhQdDchinhKhBdgPlRepository.findByIdQdDtl(dtl.getId());
                for (XhQdDchinhKhBdgPl phanLo : byIdQdDtl){
                    xhQdDchinhKhBdgPlDtlRepository.deleteAllByIdPhanLo(phanLo.getId());
                }
                xhQdDchinhKhBdgPlRepository.deleteByIdQdDtl(dtl.getId());
            }
            xhQdDchinhKhBdgDtlRepository.deleteAll(xhQdPdKhBdgDtl);
        }
        xhQdDchinhKhBdgHdrRepository.delete(optional.get());

        if (optional.get().getPhanLoai().equals("TH")){
            xhThopDxKhBdgRepository.updateTrangThai(optional.get().getIdThHdr(), NhapXuatHangTrangThaiEnum.CHUATAO_QD.getId());
        }else {
            xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(optional.get().getSoTrHdr()), NhapXuatHangTrangThaiEnum.CHUATONGHOP.getId());
        }
    }

    public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        List<XhQdDchinhKhBdgHdr> listHdr = xhQdDchinhKhBdgHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        if (listHdr.isEmpty()){
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        }
       for (XhQdDchinhKhBdgHdr bdg : listHdr) {
           if (!bdg.getTrangThai().equals(Contains.DUTHAO))
               throw new Exception("Chỉ thực hiện xóa bản nghi ở trạng thái bản nháp hoặc từ chối");
       }
        List<XhQdDchinhKhBdgHdr> list = xhQdDchinhKhBdgHdrRepository.findAllByIdIn(idSearchReq.getIdList());
        List<Long> idList=listHdr.stream().map(XhQdDchinhKhBdgHdr::getId).collect(Collectors.toList());
        List<XhQdDchinhKhBdgDtl> xhQdPdKhBdgDtls = xhQdDchinhKhBdgDtlRepository.findAllByIdQdHdrIn(idList);
        List<Long>  listIdDx = xhQdPdKhBdgDtls.stream().map(XhQdDchinhKhBdgDtl::getId).collect(Collectors.toList());
        List<XhQdDchinhKhBdgPl> xhQdPdKhBdgPls = xhQdDchinhKhBdgPlRepository.findAllByIdQdDtlIn(listIdDx);
        for (XhQdDchinhKhBdgPl plDtl : xhQdPdKhBdgPls){
            List<XhQdDchinhKhBdgPlDtl> phanLoDtl = xhQdDchinhKhBdgPlDtlRepository.findByIdPhanLo(plDtl.getId());
            xhQdDchinhKhBdgPlDtlRepository.deleteAll(phanLoDtl);
        }
        xhQdDchinhKhBdgPlRepository.deleteAll(xhQdPdKhBdgPls);
        xhQdDchinhKhBdgDtlRepository.deleteAll(xhQdPdKhBdgDtls);
        xhQdDchinhKhBdgHdrRepository.deleteAll(list);

    }

    public XhQdDchinhKhBdgHdr approve(StatusReq stReq) throws Exception{
        if (StringUtils.isEmpty(stReq.getId())){
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhQdDchinhKhBdgHdr detail = detail(String.valueOf(stReq.getId()));
        if(detail.getLoaiVthh().startsWith("02")){
            return this.approveVatTu(stReq,detail);
        }else{
            return this.approveLT(stReq,detail);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    XhQdDchinhKhBdgHdr approveVatTu(StatusReq stReq, XhQdDchinhKhBdgHdr dataDB) throws Exception {
        String status = stReq.getTrangThai() + dataDB.getTrangThai();
        switch (status) {
            case Contains.CHODUYET_LDV + Contains.DUTHAO:
            case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
                dataDB.setNguoiGuiDuyet(getUser().getUsername());
                dataDB.setNgayGuiDuyet(getDateTimeNow());
            case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
                dataDB.setNguoiPduyet(getUser().getUsername());
                dataDB.setNgayPduyet(getDateTimeNow());
                dataDB.setLdoTuchoi(stReq.getLyDo());
            case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
            case Contains.BAN_HANH + Contains.DADUYET_LDV:
                dataDB.setNguoiPduyet(getUser().getUsername());
                dataDB.setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        dataDB.setTrangThai(stReq.getTrangThai());
        if (stReq.getTrangThai().equals(Contains.BAN_HANH)){
            Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(dataDB.getIdTrHdr());
            if (qOptional.isPresent()){
                if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)){
                    throw new Exception("Đề xuất này đã được quyết định ");
                }
                // Update trạng thái tờ trình
                xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(dataDB.getSoTrHdr()), Contains.DABANHANH_QD);
            }else {
                throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
            }
            this.cloneProject(dataDB.getId());
            this.cloneForToChucBdg(dataDB);
        }
        XhQdDchinhKhBdgHdr createCheck = xhQdDchinhKhBdgHdrRepository.save(dataDB);
        return createCheck;
    }

    @Transactional(rollbackOn = Exception.class)
    XhQdDchinhKhBdgHdr approveLT(StatusReq stReq, XhQdDchinhKhBdgHdr dataDB) throws Exception{
        String status = stReq.getTrangThai() + dataDB.getTrangThai();
        switch (status) {
            case Contains.BAN_HANH + Contains.DUTHAO:
                dataDB.setNguoiPduyet(getUser().getUsername());
                dataDB.setNgayPduyet(getDateTimeNow());
                break;
            default:
                throw new Exception("Phê duyệt không thành công");
        }
        dataDB.setTrangThai(stReq.getTrangThai());
        if (stReq.getTrangThai().equals(Contains.BAN_HANH)){
            if (dataDB.getPhanLoai().equals("TH")){
                Optional<XhThopDxKhBdg> qOptional = xhThopDxKhBdgRepository.findById(dataDB.getIdThHdr());
                if (qOptional.isPresent()){
                    if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)){
                        throw new Exception("Tổng hợp kế hoạch này đã được quyết định");
                    }
                    xhThopDxKhBdgRepository.updateTrangThai(dataDB.getIdThHdr(), Contains.DABANHANH_QD);
                }else {
                    throw new Exception("Tổng hợp kế hoạch không được tìm thấy");
                }
            }else {
                Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(dataDB.getIdTrHdr());
                if (qOptional.isPresent()){
                    if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)){
                        throw new Exception("Đề xuất này đã được quyết định");
                    }
                  // Update trạng thái tờ trình
                    xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(dataDB.getSoTrHdr()), Contains.DABANHANH_QD);
                }else {
                    throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
                }
            }
            this.cloneProject(dataDB.getId());
            this.cloneForToChucBdg(dataDB);
//            this.validateData(dataDB);
        }
        XhQdDchinhKhBdgHdr createCheck = xhQdDchinhKhBdgHdrRepository.save(dataDB);
        return createCheck;
    }

    private void cloneProject(Long idClone) throws Exception {
        XhQdDchinhKhBdgHdr hdr = this.detail(idClone.toString());
        XhQdDchinhKhBdgHdr hdrClone = new XhQdDchinhKhBdgHdr();
        BeanUtils.copyProperties(hdr, hdrClone);
        hdrClone.setId(null);
        hdrClone.setLastest(true);
        hdrClone.setIdGoc(hdr.getId());
        xhQdDchinhKhBdgHdrRepository.save(hdrClone);
        for (XhQdDchinhKhBdgDtl dx : hdr.getChildren()){
            XhQdDchinhKhBdgDtl dxClone = new XhQdDchinhKhBdgDtl();
            BeanUtils.copyProperties(dx, dxClone);
            dxClone.setId(null);
            dxClone.setIdQdHdr(hdrClone.getId());
            xhQdDchinhKhBdgDtlRepository.save(dxClone);
            for (XhQdDchinhKhBdgPl phanLo : dx.getChildren()){
                XhQdDchinhKhBdgPl phanLoClone = new XhQdDchinhKhBdgPl();
                BeanUtils.copyProperties(phanLo, phanLoClone);
                phanLoClone.setId(null);
                phanLoClone.setIdQdDtl(dxClone.getId());
                xhQdDchinhKhBdgPlRepository.save(phanLoClone);
                for (XhQdDchinhKhBdgPlDtl dsDdNhap : phanLoClone.getChildren()){
                    XhQdDchinhKhBdgPlDtl dsDdNhapClone = new XhQdDchinhKhBdgPlDtl();
                    BeanUtils.copyProperties(dsDdNhap, dsDdNhapClone);
                    dsDdNhapClone.setId(null);
                    dsDdNhapClone.setIdPhanLo(phanLoClone.getId());
                    xhQdDchinhKhBdgPlDtlRepository.save(dsDdNhapClone);
                }
            }
        }
    }

    public  void  export(XhQdDchinhKhBdgSearchReq objReq, HttpServletResponse response) throws Exception{
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        objReq.setPaggingReq(paggingReq);
        Page<XhQdDchinhKhBdgHdr> page=this.searchPage(objReq);
        List<XhQdDchinhKhBdgHdr> data=page.getContent();
        String title = " Danh sách quyết định phê duyệt kế hoạch mua trưc tiếp";
        String[] rowsName =new String[]{"STT","Năm kế hoạch","Số QĐ PD KH BĐG","ngày ký QĐ","Trích yếu","Số KH/ Tờ trình","Mã tổng hợp","Loại hàng hóa","Chủng loại hàng hóa","Số ĐV tài sản","SL HĐ đã ký","Trạng Thái"};
        String fileName="danh-sach-dx-pd-kh-mua-truc-tiep.xlsx";
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs=null;
        for (int i=0;i<data.size();i++){
            XhQdDchinhKhBdgHdr pduyet=data.get(i);
            objs=new Object[rowsName.length];
            objs[0]=i;
            objs[1]=pduyet.getNamKh();
            objs[2]=pduyet.getSoQdPd();
            objs[3]=pduyet.getNgayKyQd();
            objs[4]=pduyet.getTrichYeu();
            objs[5]=pduyet.getSoTrHdr();
            objs[6]=pduyet.getIdThHdr();
            objs[7]=pduyet.getTenLoaiVthh();
            objs[8]=pduyet.getTenCloaiVthh();
            objs[9]=pduyet.getSoDviTsan();
            objs[10]=pduyet.getSlHdDaKy();
            objs[11]=pduyet.getTenTrangThai();
            dataList.add(objs);
        }
        ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
        ex.export();
    }

  private void cloneForToChucBdg(XhQdDchinhKhBdgHdr data) {
    List<XhTcTtinBdgHdr> listXhTcTtinBdgHdr =new ArrayList<>();
    data.getChildren().forEach(s->{
        XhTcTtinBdgHdr hdr = new XhTcTtinBdgHdr();
        hdr.setNam(data.getNamKh());
        hdr.setMaDvi(data.getMaDvi());
        hdr.setLoaiVthh(data.getLoaiVthh());
        hdr.setCloaiVthh(data.getCloaiVthh());
        hdr.setIdQdPdKh(data.getId());
        hdr.setSoQdPdKh(data.getSoQdPd());
        hdr.setIdQdDcKh(null);
        hdr.setSoQdDcKh(null);
        hdr.setIdQdPdKq(null);
        hdr.setSoQdPdKq(null);
        hdr.setIdKhDx(data.getIdTrHdr());
        hdr.setSoKhDx(data.getSoTrHdr());
//        hdr.setMaTh(data.getIdThHdr());
        hdr.setNgayQdPdKqBdg(null);
        hdr.setThoiHanGiaoNhan(s.getTgianGnhan());
        hdr.setThoiHanThanhToan(s.getTgianTtoan());
        hdr.setPhuongThucThanhToan(s.getPthucTtoan());
        hdr.setPhuongThucGiaoNhan(s.getPthucGnhan());
        hdr.setTrangThai(TrangThaiAllEnum.CHUA_CAP_NHAT.getId());
        hdr.setMaDviThucHien(s.getMaDvi());
        hdr.setTongTienGiaKhoiDiem(DataUtils.safeToLong(s.getTongTienKdienDonGia()));
        hdr.setTongTienDatTruoc(DataUtils.safeToLong(s.getKhoanTienDatTruoc()));
        hdr.setTongTienDatTruocDuocDuyet(DataUtils.safeToLong(s.getTongTienDatTruocDonGia()));
        hdr.setTongSoLuong(DataUtils.safeToLong(s.getTongSoLuong()));
        hdr.setPhanTramDatTruoc(DataUtils.safeToInt(s.getKhoanTienDatTruoc()));
        if (!DataUtils.isNullObject(s.getTgianDkienTu()) && !DataUtils.isNullObject(s.getTgianDkienTu())) {
            LocalDate localDateTu = s.getTgianDkienTu().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDateDen = s.getTgianDkienDen().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            hdr.setThoiGianToChucTu(localDateTu);
            hdr.setThoiGianToChucDen(localDateDen);
        }
        hdr.setSoDviTsan(s.getSoDviTsan());
        hdr.setSoDviTsanThanhCong(0);
        hdr.setSoDviTsanKhongThanh(0);
        listXhTcTtinBdgHdr.add(hdr);
    });
    xhTcTtinBdgHdrRepository.saveAll(listXhTcTtinBdgHdr);
  }
}
