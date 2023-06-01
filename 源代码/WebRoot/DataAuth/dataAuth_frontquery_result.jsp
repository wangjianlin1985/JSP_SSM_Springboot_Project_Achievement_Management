<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.DataAuth" %>
<%@ page import="com.chengxusheji.po.ProjectData" %>
<%@ page import="com.chengxusheji.po.UserInfo" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<DataAuth> dataAuthList = (List<DataAuth>)request.getAttribute("dataAuthList");
    //获取所有的dataObj信息
    List<ProjectData> projectDataList = (List<ProjectData>)request.getAttribute("projectDataList");
    //获取所有的userObj信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    ProjectData dataObj = (ProjectData)request.getAttribute("dataObj");
    UserInfo userObj = (UserInfo)request.getAttribute("userObj");
    String requestTime = (String)request.getAttribute("requestTime"); //申请时间查询关键字
    String shenHeState = (String)request.getAttribute("shenHeState"); //申请状态查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>资料权限查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="row"> 
		<div class="col-md-9 wow fadeInDown" data-wow-duration="0.5s">
			<div>
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
			    	<li><a href="<%=basePath %>index.jsp">首页</a></li>
			    	<li role="presentation" class="active"><a href="#dataAuthListPanel" aria-controls="dataAuthListPanel" role="tab" data-toggle="tab">资料权限列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>DataAuth/dataAuth_frontAdd.jsp" style="display:none;">添加资料权限</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="dataAuthListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>授权id</td><td>项目资料</td><td>用户</td><td>申请时间</td><td>申请状态</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<dataAuthList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		DataAuth dataAuth = dataAuthList.get(i); //获取到资料权限对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><%=dataAuth.getAuthId() %></td>
 											<td><%=dataAuth.getDataObj().getDataname() %></td>
 											<td><%=dataAuth.getUserObj().getName() %></td>
 											<td><%=dataAuth.getRequestTime() %></td>
 											<td><%=dataAuth.getShenHeState() %></td>
 											<td>
 												<a href="<%=basePath  %>DataAuth/<%=dataAuth.getAuthId() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="dataAuthEdit('<%=dataAuth.getAuthId() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="dataAuthDelete('<%=dataAuth.getAuthId() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
 											</td> 
 										</tr>
 										<%}%>
				    				</table>
				    				</div>
				    			</div>
				    		</div>

				    		<div class="row">
					            <div class="col-md-12">
						            <nav class="pull-left">
						                <ul class="pagination">
						                    <li><a href="#" onclick="GoToPage(<%=currentPage-1 %>,<%=totalPage %>);" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
						                     <%
						                    	int startPage = currentPage - 5;
						                    	int endPage = currentPage + 5;
						                    	if(startPage < 1) startPage=1;
						                    	if(endPage > totalPage) endPage = totalPage;
						                    	for(int i=startPage;i<=endPage;i++) {
						                    %>
						                    <li class="<%= currentPage==i?"active":"" %>"><a href="#"  onclick="GoToPage(<%=i %>,<%=totalPage %>);"><%=i %></a></li>
						                    <%  } %> 
						                    <li><a href="#" onclick="GoToPage(<%=currentPage+1 %>,<%=totalPage %>);"><span aria-hidden="true">&raquo;</span></a></li>
						                </ul>
						            </nav>
						            <div class="pull-right" style="line-height:75px;" >共有<%=recordNumber %>条记录，当前第 <%=currentPage %>/<%=totalPage %> 页</div>
					            </div>
				            </div> 
				    </div>
				</div>
			</div>
		</div>
	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>资料权限查询</h1>
		</div>
		<form name="dataAuthQueryForm" id="dataAuthQueryForm" action="<%=basePath %>DataAuth/frontlist" class="mar_t15" method="post">
            <div class="form-group">
            	<label for="dataObj_dataId">项目资料：</label>
                <select id="dataObj_dataId" name="dataObj.dataId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(ProjectData projectDataTemp:projectDataList) {
	 					String selected = "";
 					if(dataObj!=null && dataObj.getDataId()!=null && dataObj.getDataId().intValue()==projectDataTemp.getDataId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=projectDataTemp.getDataId() %>" <%=selected %>><%=projectDataTemp.getDataname() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
            <div class="form-group">
            	<label for="userObj_user_name">用户：</label>
                <select id="userObj_user_name" name="userObj.user_name" class="form-control">
                	<option value="">不限制</option>
	 				<%
	 				for(UserInfo userInfoTemp:userInfoList) {
	 					String selected = "";
 					if(userObj!=null && userObj.getUser_name()!=null && userObj.getUser_name().equals(userInfoTemp.getUser_name()))
 						selected = "selected";
	 				%>
 				 <option value="<%=userInfoTemp.getUser_name() %>" <%=selected %>><%=userInfoTemp.getName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="requestTime">申请时间:</label>
				<input type="text" id="requestTime" name="requestTime" class="form-control"  placeholder="请选择申请时间" value="<%=requestTime %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
			<div class="form-group">
				<label for="shenHeState">申请状态:</label>
				<input type="text" id="shenHeState" name="shenHeState" value="<%=shenHeState %>" class="form-control" placeholder="请输入申请状态">
			</div>






            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="dataAuthEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;资料权限信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="dataAuthEditForm" id="dataAuthEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="dataAuth_authId_edit" class="col-md-3 text-right">授权id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="dataAuth_authId_edit" name="dataAuth.authId" class="form-control" placeholder="请输入授权id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="dataAuth_dataObj_dataId_edit" class="col-md-3 text-right">项目资料:</label>
		  	 <div class="col-md-9">
			    <select id="dataAuth_dataObj_dataId_edit" name="dataAuth.dataObj.dataId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="dataAuth_userObj_user_name_edit" class="col-md-3 text-right">用户:</label>
		  	 <div class="col-md-9">
			    <select id="dataAuth_userObj_user_name_edit" name="dataAuth.userObj.user_name" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="dataAuth_requestTime_edit" class="col-md-3 text-right">申请时间:</label>
		  	 <div class="col-md-9">
                <div class="input-group date dataAuth_requestTime_edit col-md-12" data-link-field="dataAuth_requestTime_edit">
                    <input class="form-control" id="dataAuth_requestTime_edit" name="dataAuth.requestTime" size="16" type="text" value="" placeholder="请选择申请时间" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="dataAuth_shenHeState_edit" class="col-md-3 text-right">申请状态:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="dataAuth_shenHeState_edit" name="dataAuth.shenHeState" class="form-control" placeholder="请输入申请状态">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="dataAuth_shenHeReply_edit" class="col-md-3 text-right">审核回复:</label>
		  	 <div class="col-md-9">
			    <textarea id="dataAuth_shenHeReply_edit" name="dataAuth.shenHeReply" rows="8" class="form-control" placeholder="请输入审核回复"></textarea>
			 </div>
		  </div>
		</form> 
	    <style>#dataAuthEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxDataAuthModify();">提交</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<jsp:include page="../footer.jsp"></jsp:include> 
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap-datetimepicker.min.js"></script>
<script src="<%=basePath %>plugins/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jsdate.js"></script>
<script>
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.dataAuthQueryForm.currentPage.value = currentPage;
    document.dataAuthQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.dataAuthQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.dataAuthQueryForm.currentPage.value = pageValue;
    documentdataAuthQueryForm.submit();
}

/*弹出修改资料权限界面并初始化数据*/
function dataAuthEdit(authId) {
	$.ajax({
		url :  basePath + "DataAuth/" + authId + "/update",
		type : "get",
		dataType: "json",
		success : function (dataAuth, response, status) {
			if (dataAuth) {
				$("#dataAuth_authId_edit").val(dataAuth.authId);
				$.ajax({
					url: basePath + "ProjectData/listAll",
					type: "get",
					success: function(projectDatas,response,status) { 
						$("#dataAuth_dataObj_dataId_edit").empty();
						var html="";
		        		$(projectDatas).each(function(i,projectData){
		        			html += "<option value='" + projectData.dataId + "'>" + projectData.dataname + "</option>";
		        		});
		        		$("#dataAuth_dataObj_dataId_edit").html(html);
		        		$("#dataAuth_dataObj_dataId_edit").val(dataAuth.dataObjPri);
					}
				});
				$.ajax({
					url: basePath + "UserInfo/listAll",
					type: "get",
					success: function(userInfos,response,status) { 
						$("#dataAuth_userObj_user_name_edit").empty();
						var html="";
		        		$(userInfos).each(function(i,userInfo){
		        			html += "<option value='" + userInfo.user_name + "'>" + userInfo.name + "</option>";
		        		});
		        		$("#dataAuth_userObj_user_name_edit").html(html);
		        		$("#dataAuth_userObj_user_name_edit").val(dataAuth.userObjPri);
					}
				});
				$("#dataAuth_requestTime_edit").val(dataAuth.requestTime);
				$("#dataAuth_shenHeState_edit").val(dataAuth.shenHeState);
				$("#dataAuth_shenHeReply_edit").val(dataAuth.shenHeReply);
				$('#dataAuthEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除资料权限信息*/
function dataAuthDelete(authId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "DataAuth/deletes",
			data : {
				authIds : authId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#dataAuthQueryForm").submit();
					//location.href= basePath + "DataAuth/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交资料权限信息表单给服务器端修改*/
function ajaxDataAuthModify() {
	$.ajax({
		url :  basePath + "DataAuth/" + $("#dataAuth_authId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#dataAuthEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#dataAuthQueryForm").submit();
            }else{
                alert(obj.message);
            } 
		},
		processData: false,
		contentType: false,
	});
}

$(function(){
	/*小屏幕导航点击关闭菜单*/
    $('.navbar-collapse a').click(function(){
        $('.navbar-collapse').collapse('hide');
    });
    new WOW().init();

    /*申请时间组件*/
    $('.dataAuth_requestTime_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd hh:ii:ss',
    	weekStart: 1,
    	todayBtn:  1,
    	autoclose: 1,
    	minuteStep: 1,
    	todayHighlight: 1,
    	startView: 2,
    	forceParse: 0
    });
})
</script>
</body>
</html>

