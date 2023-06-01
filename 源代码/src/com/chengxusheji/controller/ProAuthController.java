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
import com.chengxusheji.service.ProAuthService;
import com.chengxusheji.po.ProAuth;
import com.chengxusheji.service.ProjectService;
import com.chengxusheji.po.Project;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;

//ProAuth管理控制层
@Controller
@RequestMapping("/ProAuth")
public class ProAuthController extends BaseController {

    /*业务层对象*/
    @Resource ProAuthService proAuthService;

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
	@InitBinder("proAuth")
	public void initBinderProAuth(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("proAuth.");
	}
	/*跳转到添加ProAuth视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new ProAuth());
		/*查询所有的Project信息*/
		List<Project> projectList = projectService.queryAllProject();
		request.setAttribute("projectList", projectList);
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		return "ProAuth_add";
	}

	/*客户端ajax方式提交添加项目人员信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated ProAuth proAuth, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		if(proAuthService.queryProAuth(proAuth.getProjectObj(), proAuth.getUserObj(), "").size() > 0) {
			message = "已经登记这个项目成员了！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
        proAuthService.addProAuth(proAuth);
        message = "项目人员添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询项目人员信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("projectObj") Project projectObj,@ModelAttribute("userObj") UserInfo userObj,String addTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (addTime == null) addTime = "";
		if(rows != 0)proAuthService.setRows(rows);
		List<ProAuth> proAuthList = proAuthService.queryProAuth(projectObj, userObj, addTime, page);
	    /*计算总的页数和总的记录数*/
	    proAuthService.queryTotalPageAndRecordNumber(projectObj, userObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = proAuthService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = proAuthService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(ProAuth proAuth:proAuthList) {
			JSONObject jsonProAuth = proAuth.getJsonObject();
			jsonArray.put(jsonProAuth);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询项目人员信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<ProAuth> proAuthList = proAuthService.queryAllProAuth();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(ProAuth proAuth:proAuthList) {
			JSONObject jsonProAuth = new JSONObject();
			jsonProAuth.accumulate("authId", proAuth.getAuthId());
			jsonArray.put(jsonProAuth);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询项目人员信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("projectObj") Project projectObj,@ModelAttribute("userObj") UserInfo userObj,String addTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (addTime == null) addTime = "";
		List<ProAuth> proAuthList = proAuthService.queryProAuth(projectObj, userObj, addTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    proAuthService.queryTotalPageAndRecordNumber(projectObj, userObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = proAuthService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = proAuthService.getRecordNumber();
	    request.setAttribute("proAuthList",  proAuthList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("projectObj", projectObj);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("addTime", addTime);
	    List<Project> projectList = projectService.queryAllProject();
	    request.setAttribute("projectList", projectList);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "ProAuth/proAuth_frontquery_result"; 
	}

     /*前台查询ProAuth信息*/
	@RequestMapping(value="/{authId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer authId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键authId获取ProAuth对象*/
        ProAuth proAuth = proAuthService.getProAuth(authId);

        List<Project> projectList = projectService.queryAllProject();
        request.setAttribute("projectList", projectList);
        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        request.setAttribute("proAuth",  proAuth);
        return "ProAuth/proAuth_frontshow";
	}

	/*ajax方式显示项目人员修改jsp视图页*/
	@RequestMapping(value="/{authId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer authId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键authId获取ProAuth对象*/
        ProAuth proAuth = proAuthService.getProAuth(authId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonProAuth = proAuth.getJsonObject();
		out.println(jsonProAuth.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新项目人员信息*/
	@RequestMapping(value = "/{authId}/update", method = RequestMethod.POST)
	public void update(@Validated ProAuth proAuth, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			proAuthService.updateProAuth(proAuth);
			message = "项目人员更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "项目人员更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除项目人员信息*/
	@RequestMapping(value="/{authId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer authId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  proAuthService.deleteProAuth(authId);
	            request.setAttribute("message", "项目人员删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "项目人员删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条项目人员记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String authIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = proAuthService.deleteProAuths(authIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出项目人员信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("projectObj") Project projectObj,@ModelAttribute("userObj") UserInfo userObj,String addTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(addTime == null) addTime = "";
        List<ProAuth> proAuthList = proAuthService.queryProAuth(projectObj,userObj,addTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "ProAuth信息记录"; 
        String[] headers = { "记录id","项目","用户","备注","加入时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<proAuthList.size();i++) {
        	ProAuth proAuth = proAuthList.get(i); 
        	dataset.add(new String[]{proAuth.getAuthId() + "",proAuth.getProjectObj().getName(),proAuth.getUserObj().getName(),proAuth.getRemark(),proAuth.getAddTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"ProAuth.xls");//filename是下载的xls的名，建议最好用英文 
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
