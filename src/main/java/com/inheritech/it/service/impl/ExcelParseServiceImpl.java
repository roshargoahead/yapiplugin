package com.inheritech.it.service.impl;

import com.inheritech.it.service.ExcelParseService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;

public class ExcelParseServiceImpl implements ExcelParseService {

    @Override
    public Workbook parseExcel(File file) {
        try {
            return new HSSFWorkbook(POIFSFileSystem.create(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
