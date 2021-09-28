package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.MemberService;
import com.atguigu.service.OrderService;
import com.atguigu.service.ReportService;
import com.atguigu.service.SetmealService;
import com.atguigu.util.DateUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    MemberService memberService;
    @Reference
    OrderService orderService;
    @Reference
    ReportService reportService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){

        try{

            Map<String,Object> map = new HashMap();

            Calendar calendar = Calendar.getInstance()
                    ;
            calendar.add(Calendar.MONTH,-12);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

            List<String> dateList = new ArrayList<>();

            for(int i = 0; i < 12;i++){
                calendar.add(calendar.MONTH,1);
                Date time = calendar.getTime();
                dateList.add(sdf.format(time));
            }
            //根据月份信息查询注册的会员
            List<Integer> memberList = memberService.getMemberByMonth(dateList);

            map.put("months",dateList);
            map.put("memberCount",memberList);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try{
            Map<String,Object> map = new HashMap<>();
            List<String> setmealNames = new ArrayList<>();

            List<Map> setmealCount = orderService.getSetMealCount();
            for (Map map1 : setmealCount) {
              String setmealName = (String) map1.get("name");
              setmealNames.add(setmealName);
            }
            map.put("setmealNames",setmealNames);
            map.put("setmealCount",setmealCount);
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try{
            Map<String,Object> map = reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);

        }
    }

    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException {
        //获取模板文件路径
        String realPath= request.getSession().getServletContext().
                   getRealPath("template") + File.separator + "report_template.xlsx";
        //获取数据
        Map<String,Object> result = reportService.getBusinessReportData();
        //取出数据
        String reportDate = (String) result.get("reportDate");
        Integer todayNewMember = (Integer) result.get("todayNewMember");
        Integer totalMember = (Integer) result.get("totalMember");
        Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
        Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
        Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
        Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
        Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
        Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
        Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
        Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
        List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
        //写入
        XSSFWorkbook sheets = new XSSFWorkbook(new File(realPath));
        XSSFSheet sheet = sheets.getSheetAt(0);
        XSSFRow row = sheet.getRow(2);
        //写入日期
        row.getCell(5).setCellValue(reportDate);

        //写入会员信息
        row = sheet.getRow(4);
        row.getCell(5).setCellValue(todayNewMember);
        row.getCell(7).setCellValue(totalMember);

        row= sheet.getRow(5);
        row.getCell(5).setCellValue(thisWeekNewMember);
        row.getCell(7).setCellValue(thisMonthNewMember);

        //预约出游数据统计
        row = sheet.getRow(7);
        row.getCell(5).setCellValue(todayOrderNumber);
        row.getCell(7).setCellValue(todayVisitsNumber);

        row = sheet.getRow(8);
        row.getCell(5).setCellValue(thisWeekOrderNumber);
        row.getCell(7).setCellValue(thisWeekVisitsNumber);


        row = sheet.getRow(9);
        row.getCell(5).setCellValue(thisMonthOrderNumber);
        row.getCell(7).setCellValue(thisMonthVisitsNumber);

        //热门套餐
        int rowNum = 12;
        for (Map map : hotSetmeal) {
            BigDecimal proportion = (BigDecimal) map.get("proportion");
            row = sheet.getRow(rowNum);
            row.getCell(4).setCellValue((String) map.get("name"));
            row.getCell(5).setCellValue((Long) map.get("setmeal_count"));
            row.getCell(6).setCellValue(proportion.doubleValue());
            ++rowNum;
        }
        //通过输出流进行文件下载
        ServletOutputStream out = response.getOutputStream();
        // 下载的数据类型（excel类型）
        response.setContentType("application/vnd.ms-excel");
        // 设置下载形式(通过附件的形式下载)
        response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
        sheets.write(out);

        out.flush();
        out.close();
        sheets.close();
    }
}
