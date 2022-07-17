package com.inheritech.it.service;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;

public interface ExcelParseService {
    Workbook parseExcel(File file);
}
