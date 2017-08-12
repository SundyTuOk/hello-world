package com.sf.energy.prepayment.servlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.sf.energy.prepayment.dao.ApKaiHuInfoDao;
import com.sf.energy.prepayment.model.ApKaiHuInfo;
import com.sf.energy.util.ConnDB;
import com.sf.energy.util.DataValidation;
import com.sf.energy.util.ExportHelper;
import com.sf.energy.util.OperationLogImplement;
import com.sf.energy.util.OperationLogInterface;

public class ApKaiHuInfoManage extends HttpServlet
{
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat ldf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try
        {
            findMethod(request, response);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (RowsExceededException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (WriteException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doGet(request, response);
    }

    private void findMethod(HttpServletRequest request,
            HttpServletResponse response) throws SQLException, IOException,
            ParseException, RowsExceededException, WriteException
    {
        String method = request.getParameter("method");

        if ("parginate".equals(method))
            parginate(request, response);

        if ("delete".equals(method))
            delete(request, response);

        if ("export".equals(method))
            export(request, response);
    }

    private void export(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ParseException,
            RowsExceededException, WriteException
    {
    	ApKaiHuInfoDao akd = new ApKaiHuInfoDao();
        Date start = new Date();
        if (request.getParameter("Start") != null)
        {
            start = df.parse(request.getParameter("Start"));
        }

        Date end = new Date();
        if (request.getParameter("End") != null)
        {
            end = df.parse(request.getParameter("End"));
        }
        end.setTime(end.getTime() + 1000 * 60 * 60 * 24);

        String sortLable = "SortLable";
        if (request.getParameter("SortLable") != null)
        {
            sortLable = request.getParameter("SortLable");
        }

        String sortType = "Asc";
        if (request.getParameter("SortType") != null)
        {
            sortType = request.getParameter("SortType");
        }

        boolean isAsc = true;
        if (sortType.equalsIgnoreCase("Asc"))
        {
            isAsc = true;
        }

        if (sortType.equalsIgnoreCase("Desc"))
        {
            isAsc = false;
        }

        int pageCount = 10;
        if (request.getParameter("PageCount") != null
                && DataValidation.isNumber(request.getParameter("PageCount")))
        {
            pageCount = Integer.parseInt(request.getParameter("PageCount"));
        }

        int pageIndex = 0;
        if (request.getParameter("PageIndex") != null
                && DataValidation.isNumber(request.getParameter("PageIndex")))
        {
            pageIndex = Integer.parseInt(request.getParameter("PageIndex"));
        }

        List<ApKaiHuInfo> list = akd.search(start, end, sortLable, isAsc);

        File file = getExportFile(list);

        FileInputStream fis = new FileInputStream(file);
        byte[] fb = new byte[fis.available()];
        fis.read(fb);
        response.setHeader("Content-disposition", "attachment; filename="
                + new String("开户信息导出文件.xls".getBytes("gb2312"), "iso8859-1"));

        ByteArrayInputStream bais = new ByteArrayInputStream(fb);
        ServletOutputStream sos = response.getOutputStream();
        int b;
        while ((b = bais.read()) != -1)
        {
            sos.write(b);
        }
        bais.close();

        sos.flush();
        sos.close();
        fis.close();

        // 要关闭所用的流才能成功删除文件，否则删除失败，导致临时文件堆积
        file.delete();
    }

    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException
    {
    	ApKaiHuInfoDao akd = new ApKaiHuInfoDao();
        String[] IDArray = null;
        if (request.getParameter("IDArray") != null)
        {
            IDArray = request.getParameter("IDArray").split(",");
        }

        int[] array = new int[IDArray.length];

        for (int i = 0; i < IDArray.length; i++)
        {
            if (DataValidation.isNumber(IDArray[i])){
                array[i] = Integer.parseInt(IDArray[i]);
                
                String info = akd.getKaiHuInfoByID(array[i]);
                String userLoginName = (String) request.getSession().getAttribute(
        				"userName");
        		OperationLogInterface log = new OperationLogImplement();
        		// 写入日志
        		log.writeLog(userLoginName, "开户报表", "删除开户信息,"+info);
            }
        }

        String info = "";
        if (akd.deleteInfo(array))
        {
            info = "success";
        }
        else
        {
            info = "fail";
        }

        PrintWriter out = response.getWriter();
        out.println(info);
        out.flush();
        out.close();
    }

    private void parginate(HttpServletRequest request,
            HttpServletResponse response) throws SQLException, IOException,
            ParseException
    {
    	ApKaiHuInfoDao akd = new ApKaiHuInfoDao();
        Date start = new Date();
        if (request.getParameter("Start") != null)
        {
            start = df.parse(request.getParameter("Start"));
        }

        Date end = new Date();
        if (request.getParameter("End") != null)
        {
            end = df.parse(request.getParameter("End"));
        }
        end.setTime(end.getTime() + 1000 * 60 * 60 * 24);

        String sortLable = "SortLable";
        if (request.getParameter("SortLable") != null)
        {
            sortLable = request.getParameter("SortLable");
        }

        String sortType = "Asc";
        if (request.getParameter("SortType") != null)
        {
            sortType = request.getParameter("SortType");
        }

        boolean isAsc = true;
        if (sortType.equalsIgnoreCase("Asc"))
        {
            isAsc = true;
        }

        if (sortType.equalsIgnoreCase("Desc"))
        {
            isAsc = false;
        }

        int pageCount = 10;
        if (request.getParameter("PageCount") != null
                && DataValidation.isNumber(request.getParameter("PageCount")))
        {
            pageCount = Integer.parseInt(request.getParameter("PageCount"));
        }

        int pageIndex = 0;
        if (request.getParameter("PageIndex") != null
                && DataValidation.isNumber(request.getParameter("PageIndex")))
        {
            pageIndex = Integer.parseInt(request.getParameter("PageIndex"));
        }

        Map<String, Object> result = null;
        if (end.after(start))
            result = akd.searchParginate(start, end, sortLable, isAsc,
                    pageCount, pageIndex);
        int totalCount = 0;
        List<ApKaiHuInfo> list = null;
        if (result != null)
        {
            list = (List<ApKaiHuInfo>) result.get("List");

            if (result.get("Count") != null)
            {
                totalCount = Integer.parseInt(result.get("Count").toString());
            }
        }

        JSONArray main = new JSONArray();

        JSONObject count = new JSONObject();
        count.put("TotalCount", totalCount);
        main.add(count);

        if (list != null && list.size() > 0)
        {
            for (ApKaiHuInfo a : list)
            {
                main.add(buildNode(a));
            }
        }

        PrintWriter out = response.getWriter();
        out.println(main.toString());
        out.flush();
        out.close();
    }

    private JSONObject buildNode(ApKaiHuInfo a) throws SQLException
    {
        JSONObject jo = new JSONObject();
        
        String meterPotocol = "1";//默认97
		String sqlString = "select TexingValue from TEXINGVALUE where  METERTEXING_id=18 and METESTYLE_ID=(SELECT METESTYLE_ID from AMMETER WHERE AMMETER_ID=" + a.getAmmeter_ID() + ")";
		Connection conn = null;
		PreparedStatement ps =null;
		ResultSet rs =null;
		try{
			conn = ConnDB.getConnection();
			ps = conn.prepareStatement(sqlString);
			rs = ps.executeQuery();
			if(rs.next()){
				meterPotocol = rs.getString("TexingValue");
			}
		}catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			ConnDB.release(conn, ps,rs);
		}
		
		
		
		jo.put("meterPotocol", meterPotocol);
        jo.put("ID", a.getID());
        jo.put("AreaName", a.getAreaName());
        jo.put("ArchName", a.getArchName());
        jo.put("Floor", a.getFloor());
        jo.put("AmmeterName", a.getAmmeter_Name());
        jo.put("KaiHuTime", ldf.format(a.getTheTime()));
        jo.put("OldSY", a.getOldsy());
        //jo.put("OldMoneyLeft", a.getOldmoneyleft());//zxm 20150721
        jo.put("NewZvalue", a.getNewzValue());
        jo.put("SYValue", a.getSyValue());
        //jo.put("NewMoneyLeft", a.getNewmoneyleft());//zxm 20150721
        jo.put("TZValue", a.getTzValue());
        jo.put("TZMoney", a.getTheMoney());
        jo.put("UserName", a.getUserName());

        return jo;
    }

    private File getExportFile(List<ApKaiHuInfo> list)
            throws RowsExceededException, WriteException, IOException, SQLException
    {
    	ExportHelper dh = ExportHelper.getInstance();
        File file = null;
        String[] theTitles = { "区域", "建筑", "楼层", "房间", "开户时间", "旧表剩余量","旧表剩余金额",
                "新表总用量", "新表剩余电量", "新表剩余金额","透支电量", "剩余金额", "操作员" };
        List<String> firstLine = new LinkedList<String>();

        List<List<String>> data = new LinkedList<List<String>>();

        // 将标题加进去
        for (String title : theTitles)
        {
            firstLine.add(title);
        }

        data.add(firstLine);

        // 将查询结果数据加进去
        if (list != null)
        {
            for (int i = 0; i < list.size(); i++)
            {
                ApKaiHuInfo am = list.get(i);
                List<String> item = new LinkedList<String>();

                String meterPotocol = "1";//默认97
        		String sqlString = "select TexingValue from TEXINGVALUE where  METERTEXING_id=18 and METESTYLE_ID=(SELECT METESTYLE_ID from AMMETER WHERE AMMETER_ID=" + am.getAmmeter_ID() + ")";
        		Connection conn = null;
        		PreparedStatement ps =null;
        		ResultSet rs =null;
        		try{
        			conn = ConnDB.getConnection();
        			ps = conn.prepareStatement(sqlString);
        			rs = ps.executeQuery();
        			if(rs.next()){
            			meterPotocol = rs.getString("TexingValue");
            		}
        		}catch (SQLException e)
        		{
        			e.printStackTrace();
        		} finally
        		{
        			ConnDB.release(conn, ps,rs);
        		}
        		
                
                // "区域"
                item.add(am.getAreaName());

                // "建筑"
                item.add(am.getArchName());

                // "楼层"
                item.add(am.getFloor() + "");

                // "房间"
                item.add(am.getAmmeter_Name());

                // "开户时间"
                item.add(ldf.format(am.getTheTime()));
                
                if("1".equals(meterPotocol)){//97 电量
                	// "旧表剩余量"
                    item.add(am.getOldsy() + "");                
                    
                    // "旧表剩余金额"
                    item.add("-");
                    
                    // "新表总用量"
                    item.add(am.getNewzValue() + "");

                    // "新表剩余电量"
                    item.add(am.getSyValue() + "");
                    
                    //新表剩余金额
                    item.add("-");
                }else {//07 金额
                	// "旧表剩余量"
                    item.add("-");                
                    
                    // "旧表剩余金额"
                    item.add(am.getOldsy() + "");
                    
                    // "新表总用量"
                    item.add(am.getNewzValue() + "");

                    // "新表剩余电量"
                    item.add("-");
                    
                    //新表剩余金额
                    item.add(am.getSyValue() + "");
				}

                // "透支电量"
                item.add(am.getTzValue() + "");

                // "剩余金额"
                item.add(am.getTheMoney() + "");

                // "操作员"
                item.add(am.getUserName());

                data.add(item);
            }
        }

        file = dh.getExportFile(data);

        return file;
    }
}
