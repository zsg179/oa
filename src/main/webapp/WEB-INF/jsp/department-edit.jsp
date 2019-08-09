<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!--  @author 卢春宇
      @date 2019年8月6日 上午09:36:25
      @version 3.0  -->


<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div style="padding:10px 10px 10px 10px">
	<form id="departmentEditForm" class="itemForm" method="post">
		<input type="hidden" name="departmentId"/>
		<input type="hidden" name="id"/>
	    <table cellpadding="5">
	        <tr>
	            <td>部门编号:</td>
	            <td><input class="easyui-textbox" readonly="true"  value="${param.id }" type="text" name="deptID" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>部门名称:</td>
	            <td><input class="easyui-textbox" class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="deptName" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>上级部门:</td>
	            <%String parentName = request.getParameter("parentName"); 
	              parentName = java.net.URLDecoder.decode(parentName,"UTF-8");
	            %>
	            <td><input class="easyui-textbox" readonly="true" value="<%=parentName %>" type="text" name="o" style="width: 280px;"></input></td>
	        </tr>
	    </table>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="departmentEditPage.submitForm()">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="departmentEditPage.clearForm()">重置</a>
	</div>
</div>
<script type="text/javascript">
var departmentEditEditor ;
$(function(){
	departmentEditEditor = TT.createEditor("#departmentEditForm [name=department]");
});

var departmentEditPage = {
		submitForm : function(){
			if(!$('#departmentEditForm').form('validate')){
				$.messager.alert('提示','表单还未填写完成!');
				return ;
			}
			
			$.post("/rest/department/edit",$("#departmentEditForm").serialize(), function(data){
				if(data.status == 200){
					$.messager.alert('提示','编辑内容成功!');
					$("#departmentList").datagrid("reload");
					TT.closeCurrentWindow();
				}
			});
		},
		clearForm : function(){
			$('#departmentEditForm').form('reset');/* 将刚才表单中输入的内容清空 */
			departmentAddEditor.html('');
		}
};

</script>