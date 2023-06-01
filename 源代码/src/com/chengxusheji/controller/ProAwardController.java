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
import com.chengxusheji.service.ProAwardService;
import com.chengxusheji.po.ProAward;
import com.chengxusheji.service.ProjectService;
import com.chengxusheji.po.Project;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;

//ProAward管理控制层
@Controller
@RequestMapping("/ProAward")
public class ProAwardController extends BaseController {

    /*业务层对象*/
    @Resource ProAwardService proAwardService;

    @Resource ProjectService projectService;
    @Resource UserInfoService userInfoService;
	@InitBinder("projectObj")
	public void initBinderprojectObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("projectObj.");
	}
	@InitBinder("userObj")
	public void initBinderuserObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userObj.");
	}
	@InitBinder("proAward")
	public void initBinderProAward(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("proAward.");
	}
	/*跳转到添加ProAward视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new ProAward());
		/*查询所有的Project信息*/
		List<Project> projectList = projectService.queryAllProject();
		request.setAttribute("projectList", projectList);
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		return "ProAward_add";
	}

	/*客户端ajax方式提交添加项目获奖信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated ProAward proAward, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		proAward.setAccessory(this.handleFileUpload(request, "accessoryFile"));
        proAwardService.addProAward(proAward);
        message = "项目获奖添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询项目获奖信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("projectObj") Project projectObj,String awardName,@ModelAttribute("userObj") UserInfo userObj,String awardTime,String createTime,String createUser,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (awardName == null) awardName = "";
		if (awardTime == null) awardTime = "";
		if (createTime == null) createTime = "";
		if (createUser == null) createUser = "";
		if(rows != 0)proAwardService.setRows(rows);
		List<ProAward> proAwardList = proAwardService.queryProAward(projectObj, awardName, userObj, awardTime, createTime, createUser, page);
	    /*计算总的页数和总的记录数*/
	    proAwardService.queryTotalPageAndRecordNumber(projectObj, awardName, userObj, awardTime, createTime, createUser);
	    /*获取到总的页码数目*/
	    int totalPage = proAwardService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = proAwardService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(ProAward proAward:proAwardList) {
			JSONObject jsonProAward = proAward.getJsonObject();
			jsonArray.put(jsonProAward);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询项目获奖信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<ProAward> proAwardList = proAwardService.queryAllProAward();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(ProAward proAward:proAwardList) {
			JSONObject jsonProAward = new JSONObject();
			jsonProAward.accumulate("awardId", proAward.getAwardId());
			jsonProAward.accumulate("awardName", proAward.getAwardName());
			jsonArray.put(jsonProAward);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询项目获奖信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("projectObj") Project projectObj,String awardName,@ModelAttribute("userObj") UserInfo userObj,String awardTime,String createTime,String createUser,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (awardName == null) awardName = "";
		if (awardTime == null) awardTime = "";
		if (createTime == null) createTime = "";
		if (createUser == null) createUser = "";
		List<ProAward> proAwardList = proAwardService.queryProAward(projectObj, awardName, userObj, awardTime, createTime, createUser, currentPage);
	    /*计算总的页数和总的记录数*/
	    proAwardService.queryTotalPageAndRecordNumber(projectObj, awardName, userObj, awardTime, createTime, createUser);
	    /*获取到总的页码数目*/
	    int totalPage = proAwardService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = proAwardService.getRecordNumber();
	    request.setAttribute("proAwardList",  proAwardList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("projectObj", projectObj);
	    request.setAttribute("awardName", awardName);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("awardTime", awardTime);
	    request.setAttribute("createTime", createTime);
	    request.setAttribute("createUser", createUser);
	    List<Project> projectList = projectService.queryAllProject();
	    request.setAttribute("projectList", projectList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "ProAward/proAward_frontquery_result"; 
	}
	
	
	/*前台按照查询条件分页查询项目获奖信息*/
	@RequestMapping(value = { "/userFrontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String userFrontlist(@ModelAttribute("projectObj") Project projectObj,String awardName,@ModelAttribute("userObj") UserInfo userObj,String awardTime,String createTime,String createUser,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (awardName == null) awardName = "";
		if (awardTime == null) awardTime = "";
		if (createTime == null) createTime = "";
		if (createUser == null) createUser = "";
		userObj = new UserInfo();
		userObj.setUser_name(session.getAttribute("user_name").toString());
		
		List<ProAward> proAwardList = proAwardService.queryProAward(projectObj, awardName, userObj, awardTime, createTime, createUser, currentPage);
	    /*计算总的页数和总的记录数*/
	    proAwardService.queryTotalPageAndRecordNumber(projectObj, awardName, userObj, awardTime, createTime, createUser);
	    /*获取到总的页码数目*/
	    int totalPage = proAwardService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = proAwardService.getRecordNumber();
	    request.setAttribute("proAwardList",  proAwardList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("projectObj", projectObj);
	    request.setAttribute("awardName", awardName);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("awardTime", awardTime);
	    request.setAttribute("createTime", createTime);
	    request.setAttribute("createUser", createUser);
	    List<Project> projectList = projectService.queryAllProject();
	    request.setAttribute("projectList", projectList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "ProAward/proAward_userFrontquery_result"; 
	}
	

     /*前台查询ProAward信息*/
	@RequestMapping(value="/{awardId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer awardId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键awardId获取ProAward对象*/
        ProAward proAward = proAwardService.getProAward(awardId);

        List<Project> projectList = projectService.queryAllProject();
        request.setAttribute("projectList", projectList);
        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        request.setAttribute("proAward",  proAward);
        return "ProAward/proAward_frontshow";
	}

	/*ajax方式显示项目获奖修改jsp视图页*/
	@RequestMapping(value="/{awardId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer awardId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键awardId获取ProAward对象*/
        ProAward proAward = proAwardService.getProAward(awardId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonProAward = proAward.getJsonObject();
		out.println(jsonProAward.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新项目获奖信息*/
	@RequestMapping(value = "/{awardId}/update", method = RequestMethod.POST)
	public void update(@Validated ProAward proAward, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String accessoryFileName = this.handleFileUpload(request, "accessoryFile");
		if(!accessoryFileName.equals(""))proAward.setAccessory(accessoryFileName);
		try {
			proAwardService.updateProAward(proAward);
			message = "项目获奖更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "项目获奖更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除项目获奖信息*/
	@RequestMapping(value="/{awardId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer awardId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  proAwardService.deleteProAward(awardId);
	            request.setAttribute("message", "项目获奖删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "项目获奖删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条项目获奖记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String awardIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = proAwardService.deleteProAwards(awardIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出项目获奖信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("projectObj") Project projectObj,String awardName,@ModelAttribute("userObj") UserInfo userObj,String awardTime,String createTime,String createUser, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(awardName == null) awardName = "";
        if(awardTime == null) awardTime = "";
        if(createTime == null) createTime = "";
        if(createUser == null) createUser = "";
        List<ProAward> proAwardList = proAwardService.queryProAward(projectObj,awardName,userObj,awardTime,createTime,createUser);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "ProAward信息记录"; 
        String[] headers = { "获奖项目","获奖名称","级别","获奖用户","获奖时间","创建时间","创建人"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<proAwardList.size();i++) {
        	ProAward proAward = proAwardList.get(i); 
        	dataset.add(new String[]{proAward.getProjectObj().getName(),proAward.getAwardName(),proAward.getLevel(),proAward.getUserObj().getName(),proAward.getAwardTime(),proAward.getCreateTime(),proAward.getCreateUser()});
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
			response.setHeader("Content-disposition","attachment; filename="+"ProAward.xls");//filename是下载的xls的名，建议最好用英文 
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
