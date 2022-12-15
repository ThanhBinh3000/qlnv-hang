package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxuatKhLcntHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaPhanLoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhThopDxKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.XhThopDxKhBdgRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.object.HhDxKhLcntThopHdrReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.SearchXhThopDxKhBdg;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.XhThopChiTieuReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.XhThopDxKhBdgReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopDtl;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxKhMttThopDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttHdr;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhDxKhBanDauGiaPhanLo;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhThopDxKhBdgDtl;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.XhThopDxKhBdg;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhThopDxKhBdgService extends BaseServiceImpl {

    @Autowired
    private XhThopDxKhBdgRepository xhThopDxKhBdgRepository;

    @Autowired
    private XhThopDxKhBdgDtlRepository xhThopDxKhBdgDtlRepository;

    @Autowired
    private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;

    @Autowired
    private XhDxKhBanDauGiaPhanLoRepository xhDxKhBanDauGiaPhanLoRepository;

    public Page<XhThopDxKhBdg> searchPage(SearchXhThopDxKhBdg objReq) throws Exception{
        Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
                objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
        Page<XhThopDxKhBdg> data=xhThopDxKhBdgRepository.searchPage(
                objReq.getNamKh(),
                objReq.getLoaiVthh(),
                objReq.getCloaiVthh(),
                objReq.getNoiDungThop(),
                Contains.convertDateToString(objReq.getNgayThopTu()),
                Contains.convertDateToString(objReq.getNgayThopDen()),
                objReq.getTrangThai(),
                pageable);
        Map<String,String> hashMapDmhh = getListDanhMucHangHoa();
        data.getContent().forEach(f -> {
            f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
            f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmhh.get(f.getLoaiVthh()));
            f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmhh.get(f.getCloaiVthh()));
        });
        return data;
    }


    public XhThopDxKhBdg sumarryData(XhThopChiTieuReq objReq, HttpServletRequest req) throws Exception{

      List<XhDxKhBanDauGia> dxuatList = xhDxKhBanDauGiaRepository.listTongHop(
              objReq.getNamKh(),
              objReq.getLoaiVthh(),
              objReq.getCloaiVthh(),
              Contains.convertDateToString(objReq.getNgayDuyetTu()),
              Contains.convertDateToString(objReq.getNgayDuyetDen()));
      if (dxuatList.isEmpty()){
          throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
      }

//      Set thông tin hdr tổng hợp
      XhThopDxKhBdg thopHdr = new XhThopDxKhBdg();


//      Lấy danh mục chung
      Map<String, String> mapDmuc = getMapCategory();

      Map<String, String> listDanhMucHangHoa = getListDanhMucHangHoa();

      thopHdr.setNamKh(objReq.getNamKh());
      thopHdr.setLoaiVthh(objReq.getLoaiVthh());
      thopHdr.setTenLoaiVthh(listDanhMucHangHoa.get(objReq.getLoaiVthh()));
      thopHdr.setCloaiVthh(objReq.getCloaiVthh());
      thopHdr.setTenCloaiVthh(listDanhMucHangHoa.get(objReq.getCloaiVthh()));
//      for (XhDxKhBanDauGia dx : dxuatList) {
//          thopHdr.setTchuanCluong(dx.getTchuanCluong());
//        }

//      Add thong tin list dtl
      List<XhThopDxKhBdgDtl> thopDtls = new ArrayList<XhThopDxKhBdgDtl>();
      String tChuanCluong = "";
      for (XhDxKhBanDauGia dxuat : dxuatList) {
          XhThopDxKhBdgDtl thopDtl = new XhThopDxKhBdgDtl();
          // Set thong tin chung lay tu de xuat
          thopDtl.setIdDxHdr(dxuat.getId());
          thopDtl.setMaDvi(dxuat.getMaDvi());
          thopDtl.setTenDvi(dxuat.getTenDvi());
          thopDtl.setSoDxuat(dxuat.getSoDxuat());
          thopDtl.setNgayPduyet(dxuat.getNgayPduyet());
          thopDtl.setNgayTao(dxuat.getNgayTao());
          thopDtl.setTrichYeu(dxuat.getTrichYeu());
          thopDtl.setSoDviTsan(dxuat.getSoDviTsan());
          thopDtl.setTongTienKdienDonGia(dxuat.getTongTienKdienDonGia());
          thopDtl.setTongTienDatTruocDonGia(dxuat.getTongTienDatTruocDonGia());
          thopDtl.setMoTaHangHoa(dxuat.getMoTaHangHoa());
          thopDtl.setTchuanCluong(dxuat.getTchuanCluong());
          thopDtl.setDiaChi(dxuat.getDiaChi());
          thopDtl.setTgianDkienTu(dxuat.getTgianDkienTu());
          thopDtl.setTgianDkienDen(dxuat.getTgianDkienDen());
          thopDtl.setTgianTtoan(dxuat.getTgianTtoan());
          thopDtl.setTgianTtoanGhiChu(dxuat.getTgianTtoanGhiChu());
          thopDtl.setPthucTtoan(dxuat.getPthucTtoan());
          thopDtl.setTgianGnhan(dxuat.getTgianGnhan());
          thopDtl.setTgianGnhanGhiChu(dxuat.getTgianGnhanGhiChu());
          thopDtl.setPthucGnhan(dxuat.getPthucGnhan());
          thopDtl.setThongBaoKh(dxuat.getThongBaoKh());
          thopDtl.setKhoanTienDatTruoc(dxuat.getKhoanTienDatTruoc());
          List<XhDxKhBanDauGiaPhanLo> dtlsPhanLo = xhDxKhBanDauGiaPhanLoRepository.findByIdDxKhbdg(dxuat.getId());
          BigDecimal soLuong = BigDecimal.ZERO;
          for (XhDxKhBanDauGiaPhanLo phanLoDtl : dtlsPhanLo) {
              soLuong = soLuong.add(phanLoDtl.getTongSoLuong());
          }
          thopDtl.setTongSoLuong(soLuong);
          thopDtls.add(thopDtl);
          tChuanCluong = tChuanCluong.concat(dxuat.getTchuanCluong()+",");
      }
      thopHdr.setTchuanCluong(tChuanCluong);
      thopHdr.setThopDxKhBdgDtlList(thopDtls);
      return thopHdr;
  }

    @Transactional()
    public XhThopDxKhBdg create(XhThopDxKhBdgReq objReq, HttpServletRequest req) throws Exception {
        if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh())){
            throw new Exception("Loại vật tư hàng hóa không phù hợp");
        }
        // Set thong tin hdr tong hop
        XhThopDxKhBdg thopHdr = sumarryData(objReq,req);
        thopHdr.setId(objReq.getId());
        thopHdr.setNgayTao(getDateTimeNow());
        thopHdr.setNguoiTao(getUser().getUsername());
        thopHdr.setNgayDuyetTu(objReq.getNgayDuyetTu());
        thopHdr.setNgayDuyetDen(objReq.getNgayDuyetDen());
        thopHdr.setLoaiVthh(objReq.getLoaiVthh());
        thopHdr.setCloaiVthh(objReq.getCloaiVthh());
        thopHdr.setTrangThai(Contains.CHUATAO_QD);
        thopHdr.setNgayThop(new Date());
        thopHdr.setNoiDungThop(objReq.getNoiDungThop());
//        thopHdr.setSoQdPd(objReq.getSoQdPd());
        xhThopDxKhBdgRepository.save(thopHdr);
        for (XhThopDxKhBdgDtl dtl : thopHdr.getThopDxKhBdgDtlList()){
            dtl.setIdThopHdr(thopHdr.getId());
            xhThopDxKhBdgDtlRepository.save(dtl);
        }
        if (thopHdr.getId() > 0 && thopHdr.getThopDxKhBdgDtlList().size() > 0) {
            List<String> soDxuatList = thopHdr.getThopDxKhBdgDtlList().stream().map(XhThopDxKhBdgDtl::getSoDxuat)
                    .collect(Collectors.toList());
            List<XhDxKhBanDauGia> list = xhDxKhBanDauGiaRepository.findBySoDxuatIn(soDxuatList);
            for (XhDxKhBanDauGia bdg : list){
                bdg.setMaThop(String.valueOf(thopHdr.getId()));
                xhDxKhBanDauGiaRepository.save(bdg);
            }
            xhDxKhBanDauGiaRepository.updateStatusInList(soDxuatList, Contains.DATONGHOP);
        }
        return thopHdr;
    }


    @Transactional()
    public XhThopDxKhBdg update (XhThopDxKhBdgReq objReq) throws Exception{
      if(StringUtils.isEmpty(objReq.getId()))
          throw new Exception(" Sửa thất bại, không tìm thấy dữ liệu");

      Optional<XhThopDxKhBdg> qOptional = xhThopDxKhBdgRepository.findById(Long.valueOf(objReq.getId()));
      if (!qOptional.isPresent())
          throw new Exception("Không tìm thấy dữ liệu cần sửa");

      if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
          throw new Exception("Loại vật tư hành hóa không phù hợp");

      XhThopDxKhBdg dataDTB = qOptional.get();
      XhThopDxKhBdg dataMap = ObjectMapperUtils.map(objReq, XhThopDxKhBdg.class);

      updateObjectToObject(dataDTB, dataMap);
      xhThopDxKhBdgRepository.save(dataDTB);
      return dataDTB;
  }

  public XhThopDxKhBdg detail(String ids) throws Exception {
      if (StringUtils.isEmpty(ids))
          throw new UnsupportedOperationException("Không tồn tại bản ghi");

      Optional<XhThopDxKhBdg> qOptional = xhThopDxKhBdgRepository.findById(Long.parseLong(ids));

      if (!qOptional.isPresent())
          throw new UnsupportedOperationException("Không tồn tại bản ghi");

      XhThopDxKhBdg hdrThop = qOptional.get();

//      Lay danh muc chung
      Map<String, String> hashMapDmHh = getListDanhMucHangHoa();

      hdrThop.setTenLoaiVthh(hashMapDmHh.get(hdrThop.getLoaiVthh()));
      hdrThop.setTenCloaiVthh(hashMapDmHh.get(hdrThop.getCloaiVthh()));

      List<XhThopDxKhBdgDtl> listTh = xhThopDxKhBdgDtlRepository.findByIdThopHdr(hdrThop.getId());
      Map<String, String> mapDmucDvi = getMapTenDvi();
      listTh.forEach(f ->{
          f.setTenDvi(StringUtils.isEmpty(f.getMaDvi())? null : mapDmucDvi.get(f.getMaDvi()));
      });
      hdrThop.setThopDxKhBdgDtlList(listTh);

      return hdrThop;
  }

  public void delete(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getId()))
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");

        Optional<XhThopDxKhBdg> optional = xhThopDxKhBdgRepository.findById(idSearchReq.getId());
        if (!optional.isPresent())
            throw new Exception("Không tìm thấy dữ liệu cần xóa");
        List<XhThopDxKhBdgDtl> listDls = xhThopDxKhBdgDtlRepository.findByIdThopHdr(optional.get().getId());
        if(!CollectionUtils.isEmpty(listDls)){
            List<Long> idDxList = listDls.stream().map(XhThopDxKhBdgDtl::getIdDxHdr).collect(Collectors.toList());
            List<XhDxKhBanDauGia> listDxHdr = xhDxKhBanDauGiaRepository.findByIdIn(idDxList);
            if(!CollectionUtils.isEmpty(listDxHdr)){
                listDxHdr.stream().map(item ->{
                    item.setTrangThaiTh(Contains.CHUATONGHOP);
                    return item;
                }).collect(Collectors.toList());
            }
            xhDxKhBanDauGiaRepository.saveAll(listDxHdr);
        }
      xhThopDxKhBdgDtlRepository.deleteAll(listDls);
      xhThopDxKhBdgRepository.delete(optional.get());

  }

  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
        if (StringUtils.isEmpty(idSearchReq.getIdList()))
            throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
        List<XhThopDxKhBdg> listThop = xhThopDxKhBdgRepository.findAllByIdIn(idSearchReq.getIdList());
        for (XhThopDxKhBdg thopHdr: listThop){
            List<XhThopDxKhBdgDtl> listDls = xhThopDxKhBdgDtlRepository.findByIdThopHdr(thopHdr.getId());
            if (!CollectionUtils.isEmpty(listDls)){
                List<Long> idDxList = listDls.stream().map(XhThopDxKhBdgDtl:: getIdDxHdr).collect(Collectors.toList());
                List<XhDxKhBanDauGia>  listDxHdr = xhDxKhBanDauGiaRepository.findByIdIn(idDxList);
                if (!CollectionUtils.isEmpty(listDxHdr)){
                    listDxHdr.stream().map(item ->{
                        item.setTrangThaiTh(Contains.CHUATONGHOP);
                        return item;
                    }).collect(Collectors.toList());
                }
                xhDxKhBanDauGiaRepository.saveAll(listDxHdr);
            }
            xhThopDxKhBdgDtlRepository.deleteAll(listDls);
        }
      xhThopDxKhBdgRepository.deleteAllByIdIn(idSearchReq.getIdList());
  }


    public void export(SearchXhThopDxKhBdg searchReq, HttpServletResponse response) throws Exception {
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(Integer.MAX_VALUE);
        searchReq.setPaggingReq(paggingReq);
        Page<XhThopDxKhBdg> page = this.searchPage(searchReq);
        List<XhThopDxKhBdg> data = page.getContent();

        String title = "Danh sách tổng hợp kế hoạch mua trực tiếp";
        String[] rowsName = new String[] { "STT", "Mã tổng hợp", "Ngày tổng hợp", "Nội dung tổng hợp",
                "Năm kế hoạch","Số QĐ phê duyêt KH BDG ","Loại hàng hóa", "Trạng thái"};
        String filename = "Tong_hop_de_xuat_ke_hoach_lua_chon_nha_thau.xlsx";

        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < data.size(); i++) {
            XhThopDxKhBdg dx = data.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = dx.getId();
            objs[2] = dx.getNgayThop();
            objs[3] = dx.getNoiDungThop();
            objs[4] = dx.getNamKh();
            objs[5] = dx.getSoQdPd();
            objs[6] = dx.getTenLoaiVthh();
            objs[7] = dx.getTenTrangThai();
            dataList.add(objs);
        }

        ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
        ex.export();
    }

}
