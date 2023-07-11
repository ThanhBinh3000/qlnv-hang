package com.tcdt.qlnvhang.service.xuathang;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.FileDKemJoinHoSoKyThuatDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bblaymaubangiaomau.BienBanLayMau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoBienBan;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoBienBanCt;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoKyThuat;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoKyThuatCt;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.bbanlaymau.BienBanLayMauRepository;
import com.tcdt.qlnvhang.repository.kiemtrachatluong.NhHoSoBienBanRepository;
import com.tcdt.qlnvhang.repository.vattu.hosokythuat.NhHoSoKyThuatRepository;
import com.tcdt.qlnvhang.repository.xuathang.XhHoSoKyThuatRepository;
import com.tcdt.qlnvhang.request.xuathang.SearchHoSoKyThuatReq;
import com.tcdt.qlnvhang.response.xuathang.NhHoSoKyThuatDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.hosokythuat.NhHoSoKyThuatService;
import com.tcdt.qlnvhang.table.xuathang.hosokythuat.XhHoSoKyThuatDtl;
import com.tcdt.qlnvhang.table.xuathang.hosokythuat.XhHoSoKyThuatHdr;
import com.tcdt.qlnvhang.table.xuathang.hosokythuat.XhHoSoKyThuatRow;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.tcdt.qlnvhang.util.Contains.HO_SO_KY_THUAT.*;

@Service
public class XhHoSoKyThuatService extends BaseServiceImpl {

  @Autowired
  private XhHoSoKyThuatRepository xhHoSoKyThuatRepository;
  @Autowired
  private NhHoSoKyThuatRepository nhHoSoKyThuatRepository;
  @Autowired
  private NhHoSoBienBanRepository nhHoSoBienBanRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;
  @Autowired
  private NhHoSoKyThuatService nhHoSoKyThuatService;
  @Autowired
  private BienBanLayMauRepository bienBanLayMauRepository;

  public Page<NhHoSoKyThuatDTO> searchPage(CustomUserDetails currentUser, SearchHoSoKyThuatReq req) throws Exception {

    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<NhHoSoKyThuatDTO> search = xhHoSoKyThuatRepository.search(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();

    search.getContent().forEach(s -> {
      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
        s.setTenDvi(objDonVi.get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaDiemKho())) {
        s.setTenDiemKho(mapDmucDvi.get(s.getMaDiemKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaNhaKho())) {
        s.setTenNhaKho(mapDmucDvi.get(s.getMaNhaKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaNganKho())) {
        s.setTenNganKho(mapDmucDvi.get(s.getMaNganKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaLoKho())) {
        s.setTenLoKho(mapDmucDvi.get(s.getMaLoKho()).get("tenDvi").toString());
      }
      if (mapVthh.get((s.getLoaiVthh())) != null) {
        s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
      }
      if (mapVthh.get((s.getCloaiVthh())) != null) {
        s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
      }
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhHoSoKyThuatHdr create(CustomUserDetails currentUser, XhHoSoKyThuatHdr objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    /*if (!DataUtils.isNullObject(objReq.getSoHs())) {
      Optional<XhHoSoKyThuatHdr> optional = xhHoSoKyThuatRepository.findBySoHs(objReq.getSoHs());
      if (optional.isPresent()) {
        throw new Exception("số hồ sơ đã tồn tại");
      }
    }*/
    XhHoSoKyThuatHdr data = new XhHoSoKyThuatHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);

    data.getXhHoSoKyThuatDtl().forEach(s -> {
      s.setXhHoSoKyThuatHdr(data);
      s.getXhHoSoKyThuatRow().forEach(s1 -> {
        s1.setXhHoSoKyThuatDtl(s);
      });
    });
    XhHoSoKyThuatHdr created = xhHoSoKyThuatRepository.save(data);
    return created;
  }

  @Transactional
  public XhHoSoKyThuatHdr update(CustomUserDetails currentUser, XhHoSoKyThuatHdr objReq) throws Exception {
    Optional<XhHoSoKyThuatHdr> updateRow = xhHoSoKyThuatRepository.findById(objReq.getId());
    if (updateRow.isPresent()) {
      XhHoSoKyThuatHdr xhHoSoKyThuatHdr = updateRow.get();

      xhHoSoKyThuatHdr.getXhHoSoKyThuatDtl().forEach(s -> {
        s.setXhHoSoKyThuatHdr(null);
        s.getXhHoSoKyThuatRow().forEach(s1 -> {
          s1.setXhHoSoKyThuatDtl(null);
        });
      });

      BeanUtils.copyProperties(objReq, xhHoSoKyThuatHdr);
      xhHoSoKyThuatHdr.getXhHoSoKyThuatDtl().forEach(s -> {
        s.setXhHoSoKyThuatHdr(xhHoSoKyThuatHdr);
    /*    if (!DataUtils.isNullOrEmpty(s.getFileDinhKem())) {
          FileDKemJoinHoSoKyThuatDtl fileDKemJoinHoSoKyThuatDtl = s.getFileDinhKem().get(0);
          fileDKemJoinHoSoKyThuatDtl.setDataType(XhHoSoKyThuatDtl.TABLE_NAME + "_DINH_KEM");
          fileDKemJoinHoSoKyThuatDtl.setCreateDate(new Date());
        }*/

        s.getXhHoSoKyThuatRow().forEach(s1 -> {

          s1.setXhHoSoKyThuatDtl(s);
        });
      });
      /*XhHoSoKyThuatHdr cloneHdr = objectMapper.readValue(objectMapper.writeValueAsString(objReq), XhHoSoKyThuatHdr.class);
      Map<String, FileDinhKem> fileDinhKemMap = new ArrayMap<>();
      cloneHdr.getXhHoSoKyThuatDtl().forEach(s -> {
        if (!DataUtils.isNullOrEmpty(s.getFileDinhKem())) {
          fileDinhKemMap.put(s.getFileDinhKem().get(0).getIdVirtual(), s.getFileDinhKem().get(0));
        }
        s.getXhHoSoKyThuatRow().forEach(s1 -> {
          if (!DataUtils.isNullOrEmpty(s1.getFileDinhKem())) {
            fileDinhKemMap.put(s1.getFileDinhKem().get(0).getIdVirtual(), s1.getFileDinhKem().get(0));
          }
        });
      });
      System.out.println("1");
      cloneHdr.getXhHoSoKyThuatDtl().forEach(s -> {
        s.getXhHoSoKyThuatRow().forEach(s1 -> {
          System.out.println(s1.getFileDinhKem().size());
        });
      });*/

      xhHoSoKyThuatRepository.save(xhHoSoKyThuatHdr);
      /*System.out.println("2");
      cloneHdr.getXhHoSoKyThuatDtl().forEach(s -> {
        s.getXhHoSoKyThuatRow().forEach(s1 -> {
          System.out.println(s1.getFileDinhKem().size());
        });
      });
*/
      System.out.println("a");
      /*xhHoSoKyThuatHdr.getXhHoSoKyThuatDtl().forEach(s -> {
        fileDinhKemService.delete(s.getId(), Lists.newArrayList(XhHoSoKyThuatDtl.TABLE_NAME + "_CAN_CU", XhHoSoKyThuatDtl.TABLE_NAME + "_DINH_KEM"));
        if (!DataUtils.isNullOrEmpty((s.getCanCu()))) {
          List<FileDinhKemReq> listFileReq = new ArrayList<>();
          s.getCanCu().forEach(canCu -> {
            FileDinhKemReq fileReq = new FileDinhKemReq();
            BeanUtils.copyProperties(canCu, fileReq);
            listFileReq.add(fileReq);
          });
          fileDinhKemService.saveListFileDinhKem(listFileReq, s.getId(), XhHoSoKyThuatDtl.TABLE_NAME + "_CAN_CU");
        }
        if (!DataUtils.isNullOrEmpty((s.getFileDinhKem()))) {
          List<FileDinhKemReq> listFileReq = new ArrayList<>();
          s.getFileDinhKem().forEach(dinhKem -> {
            FileDinhKemReq fileReq = new FileDinhKemReq();
            BeanUtils.copyProperties(dinhKem, fileReq);
            listFileReq.add(fileReq);
          });
          fileDinhKemService.saveListFileDinhKem(listFileReq, s.getId(), XhHoSoKyThuatDtl.TABLE_NAME + "_DINH_KEM");
        }
        s.getXhHoSoKyThuatRow().forEach(s1 -> {
          if (!DataUtils.isNullOrEmpty((s1.getFileDinhKem()))) {
            List<FileDinhKemReq> listFileReq = new ArrayList<>();
            s1.getFileDinhKem().forEach(dinhKem -> {
              FileDinhKemReq fileReq = new FileDinhKemReq();
              BeanUtils.copyProperties(dinhKem, fileReq);
              listFileReq.add(fileReq);
            });
            fileDinhKemService.delete(s1.getId(), Lists.newArrayList(XhHoSoKyThuatRow.TABLE_NAME + "_DINH_KEM"));
            fileDinhKemService.saveListFileDinhKem(listFileReq, s1.getId(), XhHoSoKyThuatRow.TABLE_NAME + "_DINH_KEM");
          }
        });
      });*/
      return xhHoSoKyThuatHdr;
    }
    return null;
  }

  @Transactional
  public XhHoSoKyThuatHdr detailXh(SearchHoSoKyThuatReq objReq) throws Exception {
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();

    if (DataUtils.isNullObject(objReq.getId())) throw new Exception("Tham số không hợp lệ.");
    XhHoSoKyThuatHdr xhHskt = xhHoSoKyThuatRepository.findByIdHsktNhAndType(objReq.getId(), objReq.getType());
    if (DataUtils.isNullObject(xhHskt)) {
      //tim kiem tu nhap hang va mapper
//      NhHoSoKyThuatDTO nhHoSoKyThuatDTO = xhHoSoKyThuatRepository.findHoSoKyThuatNh(DataUtils.safeToLong(objReq.getId()));
      NhHoSoKyThuat nhHoSoKyThuat = nhHoSoKyThuatService.detail(objReq.getId());
      if (!DataUtils.isNullObject(nhHoSoKyThuat)) {
        Optional<BienBanLayMau> firstBySoBienBan = bienBanLayMauRepository.findFirstBySoBienBan(nhHoSoKyThuat.getSoBbLayMau());
        if (!firstBySoBienBan.isPresent()) {
          throw new Exception("Không tìm thấy biên bản lấy mẫu liên quan.");
        }
        //map danh sach bien ban
        List<NhHoSoBienBan> listHoSoBienBan = nhHoSoKyThuat.getListHoSoBienBan();
        List<XhHoSoKyThuatDtl> listDtl = new ArrayList<>();
        for (NhHoSoBienBan nhHoSoBienBan : listHoSoBienBan) {
          XhHoSoKyThuatDtl xhHoSoKyThuatDtl = new XhHoSoKyThuatDtl();
          xhHoSoKyThuatDtl.setSoBienBan(nhHoSoBienBan.getSoBienBan());
          xhHoSoKyThuatDtl.setNgayLapBb(DataUtils.convertToLocalDate(nhHoSoBienBan.getNgayTao()));
          xhHoSoKyThuatDtl.setSoHskt(nhHoSoBienBan.getSoHoSoKyThuat());
          xhHoSoKyThuatDtl.setMaDviNhapHskt(nhHoSoBienBan.getMaDvi());
          xhHoSoKyThuatDtl.setNgayTao(DataUtils.convertToLocalDate(nhHoSoBienBan.getNgayTao()).atStartOfDay());
          xhHoSoKyThuatDtl.setIdBbLayMau(null);
          xhHoSoKyThuatDtl.setSoBbLayMau(nhHoSoBienBan.getSoBbLayMau());
          xhHoSoKyThuatDtl.setSoQdGiaoNvNh(nhHoSoBienBan.getSoQdGiaoNvNh());
          xhHoSoKyThuatDtl.setIdQdGiaoNvNh(nhHoSoBienBan.getIdQdGiaoNvNh());
          xhHoSoKyThuatDtl.setDviCungCap(nhHoSoBienBan.getSoHd());
          xhHoSoKyThuatDtl.setTgianKtra(DataUtils.convertToLocalDate(nhHoSoBienBan.getTgianKtra()));
          xhHoSoKyThuatDtl.setKquaKtra("");
          xhHoSoKyThuatDtl.setDiaDiemKtra(nhHoSoBienBan.getDiaDiemKiemTra());
          xhHoSoKyThuatDtl.setLoaiVthh(nhHoSoBienBan.getLoaiVthh());
          xhHoSoKyThuatDtl.setCloaiVthh(nhHoSoBienBan.getCloaiVthh());
          xhHoSoKyThuatDtl.setTenVthh("");
          xhHoSoKyThuatDtl.setSoLuongNhap(DataUtils.safeToLong(nhHoSoBienBan.getSoLuongNhap()));
          xhHoSoKyThuatDtl.setTgianNhap(DataUtils.convertToLocalDate(nhHoSoBienBan.getTgianNhap()));
          xhHoSoKyThuatDtl.setPpLayMau(nhHoSoBienBan.getPpLayMau());
          xhHoSoKyThuatDtl.setKyMaHieu(nhHoSoBienBan.getKyMaHieu());
          xhHoSoKyThuatDtl.setSoSerial(nhHoSoBienBan.getSoSerial());
          xhHoSoKyThuatDtl.setNoiDung(nhHoSoBienBan.getNoiDung());
          xhHoSoKyThuatDtl.setKetLuan(nhHoSoBienBan.getKetLuan());
          xhHoSoKyThuatDtl.setLoaiBb(nhHoSoBienBan.getLoaiBb());
          xhHoSoKyThuatDtl.setThoiDiemLap(THOI_DIEM_NHAP_HANG);
          xhHoSoKyThuatDtl.setCanCu(new ArrayList<>());
          xhHoSoKyThuatDtl.setVanBanBsung(new ArrayList<>());
          xhHoSoKyThuatDtl.setTgianBsung(DataUtils.convertToLocalDate(nhHoSoBienBan.getTgianBsung()));
          xhHoSoKyThuatDtl.setMapVthh(mapVthh);
          String sFileDinhKem = objectMapper.writeValueAsString(nhHoSoBienBan.getFileDinhKems());
          List<FileDKemJoinHoSoKyThuatDtl> listFileDinhKem = objectMapper.readValue(sFileDinhKem, new TypeReference<List<FileDKemJoinHoSoKyThuatDtl>>() {
          });
          xhHoSoKyThuatDtl.setFileDinhKem(listFileDinhKem);

          //map chi tiet bien ban
          if (nhHoSoBienBan.getLoaiBb().equals(BB_KTRA_HO_SO_KY_THUAT)) {
            List<NhHoSoKyThuatCt> children = nhHoSoKyThuat.getChildren();
            List<XhHoSoKyThuatRow> listHoSo = new ArrayList<>();
            for (NhHoSoKyThuatCt child : children) {
              XhHoSoKyThuatRow xhHoSoKyThuatRow = new XhHoSoKyThuatRow();
              xhHoSoKyThuatRow.setTen(child.getTenHoSo());
              xhHoSoKyThuatRow.setLoai(child.getLoaiTaiLieu());
              xhHoSoKyThuatRow.setSoLuong(DataUtils.safeToLong(child.getSoLuong()));
              xhHoSoKyThuatRow.setGhiChu(child.getGhiChu());
              xhHoSoKyThuatRow.setTrangThai("Đã Ký");
              xhHoSoKyThuatRow.setType(HO_SO);
              xhHoSoKyThuatRow.setFileDinhKem(Lists.newArrayList());
              listHoSo.add(xhHoSoKyThuatRow);
            }
            xhHoSoKyThuatDtl.setXhHoSoKyThuatRow(listHoSo);
          }

          List<NhHoSoBienBanCt> children = nhHoSoBienBan.getChildren();
          List<XhHoSoKyThuatRow> listNlq = new ArrayList<>();
          for (NhHoSoBienBanCt child : children) {
            XhHoSoKyThuatRow nlq = new XhHoSoKyThuatRow();
            nlq.setTen(child.getDaiDien());
            nlq.setLoai(child.getLoaiDaiDien());
            nlq.setType(NGUOI_LIEN_QUAN);
            listNlq.add(nlq);
          }
          listDtl.add(xhHoSoKyThuatDtl);
        }
        //3 bb mac dinh
        Queue<String> loaiBb = new LinkedList<>();
        loaiBb.add(BBAN_KTRA_NGOAI_QUAN);
        loaiBb.add(BB_KTRA_VAN_HANH);
        loaiBb.add(BB_KTRA_HO_SO_KY_THUAT);
        for (int i = 0; i < 3; i++) {
          XhHoSoKyThuatDtl dtlBbKt = new XhHoSoKyThuatDtl();
          dtlBbKt.setLoaiBb(loaiBb.poll());
          dtlBbKt.setThoiDiemLap(THOI_DIEM_XUAT_HANG);
          listDtl.add(dtlBbKt);
        }

        xhHskt = new XhHoSoKyThuatHdr();
        //xhHskt.setIdHsktNh(objReq.getId());
        xhHskt.setIdHsktNh(nhHoSoKyThuat.getId());
        xhHskt.setSoHsktNh(nhHoSoKyThuat.getSoHoSoKyThuat());
        xhHskt.setSoBbLayMauNh(nhHoSoKyThuat.getSoBbLayMau());
        xhHskt.setSoQdGiaoNvNh(nhHoSoKyThuat.getSoQdGiaoNvNh());
        xhHskt.setNgayTaoNh(DataUtils.convertToLocalDate(nhHoSoKyThuat.getNgayTao()));
        xhHskt.setNgayDuyetNh(DataUtils.convertToLocalDate(nhHoSoKyThuat.getNgayPduyet()));
        String maDiaDiem = "";
        if (DataUtils.isNullOrEmpty(firstBySoBienBan.get().getMaLoKho())) {
          maDiaDiem = firstBySoBienBan.get().getMaNganKho();
        } else {
          maDiaDiem = firstBySoBienBan.get().getMaLoKho();
        }
        xhHskt.setMaDiaDiem(maDiaDiem);
        xhHskt.setXhHoSoKyThuatDtl(listDtl);
        xhHskt.setMapDmucDvi(mapDmucDvi);
        xhHskt.setMapVthh(mapVthh);
      }
    } else {
      xhHskt.setMapDmucDvi(mapDmucDvi);
      xhHskt.setMapVthh(mapVthh);
    }
    return xhHskt;
  }
}
