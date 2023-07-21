package com.tcdt.qlnvhang.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class IdSearchReq {
  Long id;
  String maDvi;
  List<Long> idList = new ArrayList<>();
  List<Long> ids = new ArrayList<>();
}
