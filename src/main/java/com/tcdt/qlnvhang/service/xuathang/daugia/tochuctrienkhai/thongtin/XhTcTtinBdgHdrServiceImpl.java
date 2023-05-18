package com.tcdt.qlnvhang.service.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgNlqRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgPloRepository;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaNtgReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaPloReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgNlq;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgPlo;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class XhTcTtinBdgHdrServiceImpl extends BaseServiceImpl implements XhTcTtinBdgHdrService {

    @Autowired
    private XhTcTtinBdgHdrRepository xhTcTtinBdgHdrRepository;

    @Autowired
    private XhTcTtinBdgNlqRepository xhTcTtinBdgNlqRepository;

    @Autowired
    private XhTcTtinBdgDtlRepository xhTcTtinBdgDtlRepository;

    @Autowired
    private XhTcTtinBdgPloRepository xhTcTtinBdgPloRepository;

    @Autowired
    private XhQdPdKhBdgDtlRepository xhQdPdKhBdgDtlRepository;

    @Autowired
    FileDinhKemService fileDinhKemService;


    @Override
    public Page<XhTcTtinBdgHdr> searchPage(ThongTinDauGiaReq objReq) throws Exception {
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhTcTtinBdgHdr> page = xhTcTtinBdgHdrRepository.search(objReq,pageable);
        return page;
    }

    @Override
    public XhTcTtinBdgHdr create(ThongTinDauGiaReq req) throws Exception {
        XhTcTtinBdgHdr data = new XhTcTtinBdgHdr();
        BeanUtils.copyProperties(req, data, "id");
        data.setMaDvi(getUser().getDvql());
        data.setNguoiTaoId(getUser().getId());
        data.setNgayTao(new Date());
        data.setId(Long.valueOf(req.getMaThongBao().split("/")[0]));

        Optional<XhQdPdKhBdgDtl> byId = xhQdPdKhBdgDtlRepository.findById(data.getIdQdPdDtl());
        if(byId.isPresent()){
            byId.get().setTrangThai(NhapXuatHangTrangThaiEnum.DANGCAPNHAT.getId());
            xhQdPdKhBdgDtlRepository.save(byId.get());
        }else{
            throw new Exception("Không tìm thấy Quyết định phê duyệt kế hoạch bán đấu giá");
        }

        List<XhTcTtinBdgHdr> byIdQdPdDtl = xhTcTtinBdgHdrRepository.findByIdQdPdDtlOrderByLanDauGia(data.getIdQdPdDtl());
        data.setLanDauGia(byIdQdPdDtl.size()+1);
        XhTcTtinBdgHdr created =  xhTcTtinBdgHdrRepository.save(data);

        if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
            List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhTcTtinBdgHdr.TABLE_NAME+ "_CAN_CU");
            created.setFileDinhKem(fileDinhKem);
        }
        if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
            List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhTcTtinBdgHdr.TABLE_NAME);
            created.setFileDinhKems(fileDinhKems);
        }
        this.saveDetail(req, data.getId(),false);
        return created;
    }

    void saveDetail(ThongTinDauGiaReq req, Long id , boolean isUpdate) {
        if(isUpdate){
            xhTcTtinBdgNlqRepository.deleteByIdTtinHdr(id);
        }
        for (ThongTinDauGiaNtgReq nlqReq : req.getListNguoiTgia()) {
            XhTcTtinBdgNlq nlq = new XhTcTtinBdgNlq();
            BeanUtils.copyProperties(nlqReq,nlq,"id");
            nlq.setId(null);
            nlq.setIdTtinHdr(id);
            xhTcTtinBdgNlqRepository.save(nlq);
        }
        if(isUpdate){
            xhTcTtinBdgDtlRepository.deleteByIdTtinHdr(id);
        }
        for (ThongTinDauGiaDtlReq dtlReq : req.getChildren()) {
            XhTcTtinBdgDtl dtl = new XhTcTtinBdgDtl();
            BeanUtils.copyProperties(dtlReq, dtl, "id");
            dtl.setIdTtinHdr(id);
            xhTcTtinBdgDtlRepository.save(dtl);
            if(isUpdate){
                xhTcTtinBdgPloRepository.deleteAllByIdTtinDtl(dtlReq.getId());
            }
            for (ThongTinDauGiaPloReq ploReq : dtlReq.getChildren()) {
                XhTcTtinBdgPlo ploDtl = new XhTcTtinBdgPlo();
                BeanUtils.copyProperties(ploReq,ploDtl,"id");
                ploDtl.setIdTtinDtl(dtl.getId());
                ploDtl.setId(null);
                xhTcTtinBdgPloRepository.save(ploDtl);
            }
        }
    }

    @Override
    public XhTcTtinBdgHdr update(ThongTinDauGiaReq req) throws Exception {
        Optional<XhTcTtinBdgHdr> byId = xhTcTtinBdgHdrRepository.findById(req.getId());
        if (!byId.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhTcTtinBdgHdr data = byId.get();
        BeanUtils.copyProperties(req, data, "id");
        data.setNgaySua(new Date());
        data.setNguoiSuaId(getUser().getId());
        XhTcTtinBdgHdr created = xhTcTtinBdgHdrRepository.save(data);

        fileDinhKemService.delete(data.getId(), Collections.singleton(XhTcTtinBdgHdr.TABLE_NAME + "_CAN_CU"));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhTcTtinBdgHdr.TABLE_NAME + "_CAN_CU");
        created.setFileDinhKem(fileDinhKem);

        fileDinhKemService.delete(data.getId(), Collections.singleton(XhTcTtinBdgHdr.TABLE_NAME));
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhTcTtinBdgHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);

        this.saveDetail(req, data.getId(),true);
        return created;
    }

    @Override
    public XhTcTtinBdgHdr detail(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhTcTtinBdgHdr> byId = xhTcTtinBdgHdrRepository.findById(id);
        if (!byId.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhTcTtinBdgHdr data = byId.get();

        data.setListNguoiTgia(xhTcTtinBdgNlqRepository.findByIdTtinHdr(id));
        Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");
        Map<String,String> listDanhMucHangHoa = getListDanhMucHangHoa();

        List<XhTcTtinBdgDtl> byIdTtinHdr = xhTcTtinBdgDtlRepository.findByIdTtinHdr(id);
        byIdTtinHdr.forEach(item -> {
            item.setTenDvi(listDanhMucDvi.get(item.getMaDvi()));
            List<XhTcTtinBdgPlo> byIdTtinDtl = xhTcTtinBdgPloRepository.findByIdTtinDtl(item.getId());
            byIdTtinDtl.forEach(x -> {
                x.setTenDiemKho(listDanhMucDvi.get(x.getMaDiemKho()));
                x.setTenNhaKho(listDanhMucDvi.get(x.getMaNhaKho()));
                x.setTenNganKho(listDanhMucDvi.get(x.getMaNganKho()));
                x.setTenLoKho(listDanhMucDvi.get(x.getMaLoKho()));
            });
            item.setChildren(byIdTtinDtl);
        });
        data.setChildren(byIdTtinHdr);
        data.setTenLoaiVthh(listDanhMucHangHoa.get(data.getLoaiVthh()));
        data.setTenCloaiVthh(listDanhMucHangHoa.get(data.getCloaiVthh()));
        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhTcTtinBdgHdr.TABLE_NAME+ "_CAN_CU"));
        data.setFileDinhKem(fileDinhKem);
        List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhTcTtinBdgHdr.TABLE_NAME));
        data.setFileDinhKems(fileDinhKems);
        return data;
    }

    @Override
    public XhTcTtinBdgHdr approve(ThongTinDauGiaReq req) throws Exception {
        return null;
    }

    @Override
    public void delete(Long id) throws Exception {
        if (ObjectUtils.isEmpty(id)) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        Optional<XhTcTtinBdgHdr> byId = xhTcTtinBdgHdrRepository.findById(id);
        if (!byId.isPresent()) {
            throw new Exception("Không tìm thấy dữ liệu");
        }
        XhTcTtinBdgHdr data = byId.get();

        xhTcTtinBdgNlqRepository.deleteByIdTtinHdr(id);

        List<XhTcTtinBdgDtl> byIdTtinHdr = xhTcTtinBdgDtlRepository.findByIdTtinHdr(id);
        byIdTtinHdr.forEach(item -> {
            xhTcTtinBdgPloRepository.deleteAllByIdTtinDtl(item.getId());
        });

        xhTcTtinBdgDtlRepository.deleteByIdTtinHdr(id);
        fileDinhKemService.delete(data.getId(), Collections.singleton(XhTcTtinBdgHdr.TABLE_NAME));
        fileDinhKemService.delete(data.getId(), Collections.singleton(XhTcTtinBdgHdr.TABLE_NAME+"_CAN_CU"));

        xhTcTtinBdgHdrRepository.delete(data);

    }

    @Override
    public void deleteMulti(List<Long> listMulti) throws Exception {

    }

    @Override
    public void export(ThongTinDauGiaReq req, HttpServletResponse response) throws Exception {

    }

//  public Page<XhTcTtinBdgHdr> searchPage(CustomUserDetails currentUser, ThongTinDauGiaReq req) throws Exception {
//    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//
//    req.setDvql(currentUser.getUser().getDvql());
//    Page<XhTcTtinBdgHdr> search = xhTcTtinBdgHdrRepository.search(req, pageable);
//    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
//    Map<String, String> mapVthh = getListDanhMucHangHoa();
//    search.getContent().forEach(s -> {
//      if (mapDmucDvi.get((s.getMaDvi())) != null) {
//        s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
//      }
//      if (mapVthh.get((s.getLoaiVthh())) != null) {
//        s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
//      }
//      if (mapVthh.get((s.getCloaiVthh())) != null) {
//        s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
//      }
//    });
//    return search;
//  }
//
//  public List<XhTcTtinBdgHdr> detail(CustomUserDetails currentUser, List<Long> ids) throws Exception {
//    if (DataUtils.isNullOrEmpty(ids))
//      throw new Exception("Tham số không hợp lệ.");
//    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
//    Map<String, String> mapVthh = getListDanhMucHangHoa();
//    List<XhTcTtinBdgHdr> allById = xhTcTtinBdgHdrRepository.findAllById(ids);
//    allById.forEach(data -> {
//      List<XhTcTtinBdgDtl> listDetail = xhTcTtinBdgDtlRepository.findByIdTtinHdr(data.getId());
//      listDetail.forEach(s -> {
//        List<FileDinhKem> canCu = fileDinhKemService.search(s.getId(), Arrays.asList(XhTcTtinBdgDtl.TABLE_NAME + "_CAN_CU"));
//        s.setCanCu(canCu);
//
//        List<FileDinhKem> fileDinhKem = fileDinhKemService.search(s.getId(), Arrays.asList(XhTcTtinBdgDtl.TABLE_NAME + "_DINH_KEM"));
//        if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
//          s.setFileDinhKem(fileDinhKem.get(0));
//        }
//
//        //tai san
//        List<XhTcTtinBdgTaiSan> listTaiSan = xhTcTtinBdgTaiSanRepository.findByIdTtinDtl(s.getId());
//        setTenTaiSan(listTaiSan, mapDmucDvi, mapVthh);
//        s.setListTaiSan(listTaiSan);
//
//        //nguoi lien quan
//        List<XhTcTtinBdgNlq> listNlq = xhTcTtinBdgNlqRepository.findByIdTtinDtl(s.getId());
//        s.setListNguoiLienQuan(listNlq);
//      });
//
//      //tai san tu quyet dinh de lam template
//      List<XhTcTtinBdgTaiSan> listTaiSanTmp = new ArrayList<>();
////      List<XhQdPdKhBdgPlDtl> listPhanLo = xhQdPdKhBdgPlDtlRepository.findByIdQdHdr(data.getIdQdPdKh());
//
////      listPhanLo.forEach(s -> {
////        XhTcTtinBdgTaiSan row = new XhTcTtinBdgTaiSan();
////        row.setIdTtinHdr(data.getId());
////        row.setIdTtinDtl(null);
//////        row.setMaDvi(s.getMaDvi());
////        row.setMaDiaDiem(DataUtils.isNullOrEmpty(s.getMaLoKho()) ? s.getMaNganKho() : s.getMaLoKho());
////        row.setSoLuong(DataUtils.safeToLong(s.getSoLuong()));
////        row.setDonGia(DataUtils.safeToLong(s.getDonGiaVat()));
////        row.setDonGiaCaoNhat(0L);
//////        row.setCloaiVthh(s.getCloaiVthh());
////        row.setMaDvTaiSan(s.getMaDviTsan());
////        row.setTonKho(0L);
////        row.setDonViTinh(s.getDviTinh());
//////        row.setGiaKhoiDiem(DataUtils.safeToLong(s.getGiaKhoiDiem()));
//////        row.setSoTienDatTruoc(DataUtils.safeToLong(s.getTienDatTruoc()));
////        row.setSoLanTraGia(0);
////        row.setNguoiTraGiaCaoNhat("");
////        listTaiSanTmp.add(row);
////      });
//      setTenTaiSan(listTaiSanTmp, mapDmucDvi, mapVthh);
//
//      data.setDetail(listDetail);
//      data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
//      data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
//      data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
//      data.setListTaiSanQd(listTaiSanTmp);
//    });
//    //Map<String, String> mapLoaiGia = qlnvDmService.getListDanhMucChung("LOAI_GIA");
//    return allById;
//  }
//
//  @Transactional(rollbackFor = Exception.class)
//  public XhTcTtinBdgHdr update(CustomUserDetails currentUser, ThongTinDauGiaReq req) throws Exception {
//    if (DataUtils.isNullObject(req.getId()))
//      throw new Exception("Tham số không hợp lệ.");
//    XhTcTtinBdgHdr currentRow = xhTcTtinBdgHdrRepository.findById(req.getId()).orElse(null);
//    if (DataUtils.isNullObject(currentRow))
//      throw new Exception("Không tìm thấy dữ liệu.");
//    BeanUtils.copyProperties(req, currentRow, "id");
//
//    req.getDetail().forEach(s -> {
//
//      if (!DataUtils.isNullOrEmpty(s.getCanCu())) {
//        fileDinhKemService.saveListFileDinhKem(s.getCanCu(), s.getId(), XhTcTtinBdgDtl.TABLE_NAME + "_CAN_CU");
//      }
//      if (!DataUtils.isNullObject(s.getFileDinhKem())) {
//        fileDinhKemService.saveListFileDinhKem(Arrays.asList(s.getFileDinhKem()), s.getId(), XhTcTtinBdgDtl.TABLE_NAME + "_DINH_KEM");
//      }
//    });
//    xhTcTtinBdgTaiSanRepository.deleteByIdTtinHdr(req.getId());
//    xhTcTtinBdgNlqRepository.deleteByIdTtinHdr(req.getId());
//    return currentRow;
//  }
//
//  private void setTenTaiSan(List<XhTcTtinBdgTaiSan> listTaiSan, Map<String, String> mapDmucDvi, Map<String, String> mapVthh) {
//    listTaiSan.forEach(taiSan -> {
//      String maChiCuc = taiSan.getMaDvTaiSan().length() >= 8 ? taiSan.getMaDvTaiSan().substring(0, 8) : "";
//      String maDiemKho = taiSan.getMaDvTaiSan().length() >= 10 ? taiSan.getMaDvTaiSan().substring(0, 10) : "";
//      String maNhaKho = taiSan.getMaDvTaiSan().length() >= 12 ? taiSan.getMaDvTaiSan().substring(0, 12) : "";
//      String maNganKho = taiSan.getMaDvTaiSan().length() >= 14 ? taiSan.getMaDvTaiSan().substring(0, 14) : "";
//      String maLoKho = taiSan.getMaDvTaiSan().length() >= 16 ? taiSan.getMaDvTaiSan().substring(0, 16) : "";
//      String tenDvi = mapDmucDvi.containsKey(taiSan.getMaDvi()) ? mapDmucDvi.get(taiSan.getMaDvi()) : null;
//      String tenChiCuc = mapDmucDvi.containsKey(maChiCuc) ? mapDmucDvi.get(maChiCuc) : null;
//      String tenDiemKho = mapDmucDvi.containsKey(maDiemKho) ? mapDmucDvi.get(maDiemKho) : null;
//      String tenNhaKho = mapDmucDvi.containsKey(maNhaKho) ? mapDmucDvi.get(maNhaKho) : null;
//      String tenNganKho = mapDmucDvi.containsKey(maNganKho) ? mapDmucDvi.get(maNganKho) : null;
//      String tenLoKho = mapDmucDvi.containsKey(maLoKho) ? mapDmucDvi.get(maLoKho) : null;
//      taiSan.setTenDiemKho(tenChiCuc);
//      taiSan.setTenDiemKho(tenDiemKho);
//      taiSan.setTenNhaKho(tenNhaKho);
//      taiSan.setTenNganKho(tenNganKho);
//      taiSan.setTenLoKho(tenLoKho);
//      taiSan.setTenDvi(tenDvi);
//
//      String loaiVthh = taiSan.getCloaiVthh().length() >= 4 ? taiSan.getCloaiVthh() : "";
//      String tenLoaiVthh = mapVthh.containsKey(loaiVthh) ? mapVthh.get(loaiVthh) : null;
//      String tenCloaiVthh = mapVthh.containsKey(taiSan.getCloaiVthh()) ? mapVthh.get(taiSan.getCloaiVthh()) : null;
//      taiSan.setTenLoaiVthh(tenLoaiVthh);
//      taiSan.setTenCloaiVthh(tenCloaiVthh);
//    });
//  }
}

