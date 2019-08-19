<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!--  @author 卢春宇
      @date 2019年8月9日 下午16:45:18
      @version 1.0  -->   
          
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
</head>
<body>
<div style="padding:10px 10px 10px 10px">
	<form id="staffEditForm" class="staffForm" method="post">
	    <table cellpadding="5">
	       <tr>
	            <td>员工号:</td>
	            <td><input class="easyui-textbox" readonly="true" value="${param.id}"  type="text" name="id" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>姓名:</td>
	             <%String fullName = request.getParameter("fullName"); 
	              fullName = java.net.URLDecoder.decode(fullName,"UTF-8");
	            %>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" value="<%=fullName%>" type="text" name="fullName" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>姓氏:</td>
	             <%String lastName = request.getParameter("lastName"); 
	              lastName = java.net.URLDecoder.decode(lastName,"UTF-8");
	            %>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" value="<%=lastName%>" type="text" name="lastName" style="width: 280px;"></input></td>
	        </tr>  
	        <tr>
	            <td>公司:</td>
	            <td>
	            <input id="o" name="o" class="easyui-combobox" style="width: 280px;"  panelHeight="auto" data-options="
				    valueField: 'id',
				    textField: 'text',
				    url: '/getCompany',
				    prompt: '请选择公司', 
				    required:true,
				    editable:false,
				    onSelect: function(rec){
				    var url = '/getDept?parentId='+rec.id;
				    $('#ou').combobox('reload', url);
				    $('#ou').combobox('clear');//清除三级联动部门默认选中项
				    $('#title').combobox('clear');//清除三级联动职位默认选中项
				    }">
				</td>
	        </tr> 
	        <tr>
	            <td>部门:</td>
	            <td>
	            <input id="ou" name="ou" class="easyui-combobox" style="width: 280px;" panelHeight="auto" data-options="
	            valueField:'id',
	            textField:'text',
	            prompt: '请选择部门',
	            required:true,
	            editable:false,
			    onSelect: function(rec){
			    var url = '/getPosition?id='+rec.id;
			    $('#title').combobox('reload', url);
			    $('#title').combobox('clear');//清除三级联动职位默认选中项
			    }">
	            </td>
	        </tr> 
	             
	        <tr>
	            <td>职位:</td>
	            <td>
	            <input id="title" name="title" class="easyui-combobox" style="width: 280px;" panelHeight="auto" data-options="
	            valueField:'id',
	            textField:'text',
	            prompt: '请选择职位',
	            editable:false,
	            required:true">
	            </td>
	        </tr> 
	        <tr>
	            <td>手机号码:</td>
	             <%String phone = request.getParameter("phone"); 
	             phone = java.net.URLDecoder.decode(phone,"UTF-8");
	            %>
	            <td><input class="easyui-textbox" data-options="required:true" validType="telephone" value="<%=phone%>" type="text" name="phone" style="width: 280px;"></input></td>
	        </tr> 
	        <tr>
	            <td>邮箱地址:</td>
	             <%String email = request.getParameter("email"); 
	             email = java.net.URLDecoder.decode(email,"UTF-8");
	            %>
	            <td><input class="easyui-textbox" data-options="required:true" validType="email" value="<%=email%>" type="text" name="email" style="width: 280px;"></input></td>
	        </tr> 
	        <tr>
	            <td>员工标签:</td>
	             <%String label = request.getParameter("label"); 
	             label = java.net.URLDecoder.decode(label,"UTF-8");
	            %>
	            <td><input id="label" name="label" class="easyui-combobox"  value="<%=label%>" style="width: 280px;" data-options=" 
	            prompt: '请选择标签',
	            url: '/getLabel',
				method:'get',
				valueField:'id',
				textField:'text',
				multiple:true,
				panelHeight:'auto',
				editable:false">
	            </td>
	        </tr>
	    </table>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="staffEditPage.submitForm()">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="staffEditPage.clearForm()">重置</a>
	</div>
</div>
<script type="text/javascript">

var staffEditPage = {
		submitForm : function(){
			if(!$('#staffEditForm').form('validate')){
				$.messager.alert('提示','表单还未填写完成!');
				return ;
			}
			
			$.post("/rest/staff/edit",$("#staffEditForm").serialize(), function(data){
				if(data.status == 200){
					$.messager.alert('提示','编辑内容成功!');
					$("#staffList").datagrid("reload");
					TT.closeCurrentWindow();
				}
			});
		},
		clearForm : function(){
			$('#staffEditForm').form('reset');/* 将刚才表单中输入的内容清空 */
			staffEditEditor.html('');
		}
};

	         $(function(){
	             //自定义电话校验规则
	             var phoneReg = /^[1][3,4,5,8]\d{9}$/
	             $.extend($.fn.validatebox.defaults.rules, { 
	                 telephone : {
	                     validator: function(value,param){ 
	                         return phoneReg.test(value);
	                     },
	                     message: '请输入有效的手机号码'
	                 }
	             }); 
	         });
</script>

</body>
</html>