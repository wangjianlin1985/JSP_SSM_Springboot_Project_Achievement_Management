package com.chengxusheji.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.utils.UserException;
import com.chengxusheji.service.DataAuthService;
import com.chengxusheji.service.ProAuthService;
import com.chengxusheji.po.DataAuth;
import com.chengxusheji.po.Project;
import com.chengxusheji.service.ProjectDataService;
import com.chengxusheji.po.ProjectData;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;

//DataAuth管理控制层
@Controller
@RequestMapping("/DataAuth")
public class DataAuthController extends BaseController {

    /*业务层对象*/
    @Resource DataAuthService dataAuthService;

    @Resource ProjectDataService projectDataService;
    @Resource ProAuthService proAuthService;
    @Resource UserInfoService userInfoService;
	@InitBinder("dataObj")
	public void initBinderdataObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("dataObj.");
	}
	@InitBinder("userObj")
	public void initBinderuserObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userObj.");
	}
	@InitBinder("dataAuth")
	public void initBinderDataAuth(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("dataAuth.");
	}
	/*跳转到添加DataAuth视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new DataAuth());
		/*查询所有的ProjectData信息*/
		List<ProjectData> projectDataList = projectDataService.queryAllProjectData();
		request.setAttribute("projectDataList", projectDataList);
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		return "DataAuth_add";
	}

	/*客户端ajax方式提交添加资料权限信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated DataAuth dataAuth, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        dataAuthService.addDataAuth(dataAuth);
        message = "资料权限添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	/*客户端ajax方式提交添加资料权限信息*/
	@RequestMapping(value = "/userAdd", method = RequestMethod.POST)
	public void userAdd(DataAuth dataAuth, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		String message = "";
		boolean success = false; 
		
		String user_name = session.getAttribute("user_name").toString();
		UserInfo userObj = new UserInfo();
		userObj.setUser_name(user_name);
		
		int dataId = dataAuth.getDataObj().getDataId();
		Project project = projectDataService.getProjectData(dataId).getProjectObj();
		if(proAuthService.queryProAuth(project, userObj, "").size() > 0) {
			message = "你参与了这个项目不需要申请权限！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		
		if(dataAuthService.queryDataAuth(dataAuth.getDataObj(), userObj, "", "").size() > 0) {
			message = "你已经申请过这个资料的权限！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		dataAuth.setUserObj(userObj);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dataAuth.setRequestTime(sdf.format(new java.util.Date()));
		
		dataAuth.setShenHeState("待审核");
		dataAuth.setShenHeReply("--");
		
        dataAuthService.addDataAuth(dataAuth);
        message = "资料权限添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	/*ajax方式按照查询条件分页查询资料权限信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("dataObj") ProjectData dataObj,@ModelAttribute("userObj") UserInfo userObj,String requestTime,String shenHeState,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (requestTime == null) requestTime = "";
		if (shenHeState == null) shenHeState = "";
		if(rows != 0)dataAuthService.setRows(rows);
		List<DataAuth> dataAuthList = dataAuthService.queryDataAuth(dataObj, userObj, requestTime, shenHeState, page);
	    /*计算总的页数和总的记录数*/
	    dataAuthService.queryTotalPageAndRecordNumber(dataObj, userObj, requestTime, shenHeState);
	    /*获取到总的页码数目*/
	    int totalPage = dataAuthService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = dataAuthService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(DataAuth dataAuth:dataAuthList) {
			JSONObject jsonDataAuth = dataAuth.getJsonObject();
			jsonArray.put(jsonDataAuth);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询资料权限信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<DataAuth> dataAuthList = dataAuthService.queryAllDataAuth();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(DataAuth dataAuth:dataAuthList) {
			JSONObject jsonDataAuth = new JSONObject();
			jsonDataAuth.accumulate("authId", dataAuth.getAuthId());
			jsonArray.put(jsonDataAuth);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询资料权限信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("dataObj") ProjectData dataObj,@ModelAttribute("userObj") UserInfo userObj,String requestTime,String shenHeState,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (requestTime == null) requestTime = "";
		if (shenHeState == null) shenHeState = "";
		List<DataAuth> dataAuthList = dataAuthService.queryDataAuth(dataObj, userObj, requestTime, shenHeState, currentPage);
	    /*计算总的页数和总的记录数*/
	    dataAuthService.queryTotalPageAndRecordNumber(dataObj, userObj, requestTime, shenHeState);
	    /*获取到总的页码数目*/
	    int totalPage = dataAuthService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = dataAuthService.getRecordNumber();
	    request.setAttribute("dataAuthList",  dataAuthList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("dataObj", dataObj);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("requestTime", requestTime);
	    request.setAttribute("shenHeState", shenHeState);
	    List<ProjectData> projectDataList = projectDataService.queryAllProjectData();
	    request.setAttribute("projectDataList", projectDataList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "DataAuth/dataAuth_frontquery_result"; 
	}
	
	
	/*前台按照查询条件分页查询资料权限信息*/
	@RequestMapping(value = { "/userFrontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String userFrontlist(@ModelAttribute("dataObj") ProjectData dataObj,@ModelAttribute("userObj") UserInfo userObj,String requestTime,String shenHeState,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (requestTime == null) requestTime = "";
		if (shenHeState == null) shenHeState = "";
		userObj = new UserInfo();
		userObj.setUser_name(session.getAttribute("user_name").toString());
		
		List<DataAuth> dataAuthList = dataAuthService.queryDataAuth(dataObj, userObj, requestTime, shenHeState, currentPage);
	    /*计算总的页数和总的记录数*/
	    dataAuthService.queryTotalPageAndRecordNumber(dataObj, userObj, requestTime, shenHeState);
	    /*获取到总的页码数目*/
	    int totalPage = dataAuthService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = dataAuthService.getRecordNumber();
	    request.setAttribute("dataAuthList",  dataAuthList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("dataObj", dataObj);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("requestTime", requestTime);
	    request.setAttribute("shenHeState", shenHeState);
	    List<ProjectData> projectDataList = projectDataService.queryAllProjectData();
	    request.setAttribute("projectDataList", projectDataList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "DataAuth/dataAuth_userFrontquery_result"; 
	}
	

     /*前台查询DataAuth信息*/
	@RequestMapping(value="/{authId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer authId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键authId获取DataAuth对象*/
        DataAuth dataAuth = dataAuthService.getDataAuth(authId);

        List<ProjectData> projectDataList = projectDataService.queryAllProjectData();
        request.setAttribute("projectDataList", projectDataList);
        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        request.setAttribute("dataAuth",  dataAuth);
        return "DataAuth/dataAuth_frontshow";
	}

	/*ajax方式显示资料权限修改jsp视图页*/
	@RequestMapping(value="/{authId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer authId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键authId获取DataAuth对象*/
        DataAuth dataAuth = dataAuthService.getDataAuth(authId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonDataAuth = dataAuth.getJsonObject();
		out.println(jsonDataAuth.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新资料权限信息*/
	@RequestMapping(value = "/{authId}/update", method = RequestMethod.POST)
	public void update(@Validated DataAuth dataAuth, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			dataAuthService.updateDataAuth(dataAuth);
			message = "资料权限更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "资料权限更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除资料权限信息*/
	@RequestMapping(value="/{authId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer authId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  dataAuthService.deleteDataAuth(authId);
	            request.setAttribute("message", "资料权限删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "资料权限删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条资料权限记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String authIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = dataAuthService.deleteDataAuths(authIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出资料权限信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("dataObj") ProjectData dataObj,@ModelAttribute("userObj") UserInfo userObj,String requestTime,String shenHeState, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(requestTime == null) requestTime = "";
        if(shenHeState == null) shenHeState = "";
        List<DataAuth> dataAuthList = dataAuthService.queryDataAuth(dataObj,userObj,requestTime,shenHeState);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "DataAuth信息记录"; 
        String[] headers = { "授权id","项目资料","用户","申请时间","申请状态"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<dataAuthList.size();i++) {
        	DataAuth dataAuth = dataAuthList.get(i); 
        	dataset.add(new String[]{dataAuth.getAuthId() + "",dataAuth.getDataObj().getDataname(),dataAuth.getUserObj().getName(),dataAuth.getRequestTime(),dataAuth.getShenHeState()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		OutputStream out = null;//创建一个输出流对象 
		try { 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"DataAuth.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,_title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
    }
}
