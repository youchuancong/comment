package com.ycc.core.daogenerate.service;

import com.ycc.core.daogenerate.bean.Table;

public interface IFreeMarkerService {
public void generate(String templatedir,String outputdir,Table table,String pk);
}
