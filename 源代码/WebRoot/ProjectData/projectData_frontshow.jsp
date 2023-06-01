<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.ProjectData" %>
<%@ page import="com.chengxusheji.po.Project" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="../check_user_logstate.jsp"/>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的projectObj信息
    List<Project> projectList = (List<Project>)request.getAttribute("projectList");
    ProjectData projectData = (ProjectData)request.getAttribute("projectData");
    
    if(projectData == null) {
    	 response.getWriter().println("<script>alert('你无权查看，请先申请权限！');top.location.href='" + basePath + "DataAuth/dataAuth_frontUserAdd.jsp';</script>");
    }

%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
  <TITLE>查看项目资料详情</TITLE>
  <link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
  <link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
  <link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
  <link href="<%=basePath %>plugins/animate.css" rel="stylesheet"> 
</head>
<body style="margin-top:70px;"> 
<jsp:include page="../header.jsp"></jsp:include>
<div class="container">
	<ul class="breadcrumb">
  		<li><a href="<%=basePath %>index.jsp">首页</a></li>
  		<li><a href="<%=basePath %>ProjectData/frontlist">项目资料信息</a></li>
  		<li class="active">详情查看</li>
	</ul>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">资料id:</div>
		<div class="col-md-10 col-xs-6"><%=projectData.getDataId()%></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">项目:</div>
		<div class="col-md-10 col-xs-6"><%=projectData.getProjectObj().getName() %></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">资料名称:</div>
		<div class="col-md-10 col-xs-6"><%=projectData.getDataname()%></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">资料介绍:</div>
		<div class="col-md-10 col-xs-6"><%=projectData.getDatacontent()%></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">文件路径:</div>
		<div class="col-md-10 col-xs-6"><a href="<%=basePath%><%=projectData.getDataurl()%>" target="_blank"><%=projectData.getDataurl()%></a></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">排序号:</div>
		<div class="col-md-10 col-xs-6"><%=projectData.getSortNumber()%></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">备注:</div>
		<div class="col-md-10 col-xs-6"><%=projectData.getRemark()%></div>
	</div>
	<div class="row bottom15"> 
		<div class="col-md-2 col-xs-4 text-right bold">创建人:</div>
		<div class="col-md-10 col-xs-6"><%=projectData.getCreateuser()%></div>
	</div>
	<div class="row bottom15">
		<div class="col-md-2 col-xs-4"></div>
		<div class="col-md-6 col-xs-6">
			<button onclick="history.back();" class="btn btn-primary">返回</button>
		</div>
	</div>
</div> 
<jsp:include page="../footer.jsp"></jsp:include>
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script>
var basePath = "<%=basePath%>";
$(function(){
        /*小屏幕导航点击关闭菜单*/
        $('.navbar-collapse a').click(function(){
            $('.navbar-collapse').collapse('hide');
        });
        new WOW().init();
 })
 </script> 
</body>
</html>

