<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!--  @author 卢春宇
      @date 2019年8月6日 上午09:36:25
      @version 3.0  -->


<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div style="padding:10px 10px 10px 10px">
	<form id="departmentEditForm" class="itemForm" method="post">
	<div id="hideTree">
	<ul id="departmentTree" class="easyui-tree" data-options="url:'/department/list',animate: true,method : 'GET'"></ul>
    </div>
	<input type="hidden" name="parentId" value="${param.parentId}"/>
	    <table cellpadding="5">
	        <tr>
	            <td>部门编号:</td>
	            <td><input class="easyui-textbox easyui-validatebox" readonly="true"  value="${param.Id }" type="text" name="Id" style="width: 280px;"></input></td>

	        </tr>
	        <tr>
	            <td>部门名称:</td>
	            <%String DeptName = request.getParameter("DeptName"); 
	            DeptName = java.net.URLDecoder.decode(DeptName,"UTF-8");
	            %>
	            <td><input id=parent class="easyui-textbox easyui-validatebox"  data-options="required:true" value="<%=DeptName%>" type="text" name="DeptName" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>上级部门:</td>
	            <%String parentName = request.getParameter("parentName"); 
	              parentName = java.net.URLDecoder.decode(parentName,"UTF-8");
	            %>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" value="<%=parentName%>"  type="text" name="o" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>职位:</td>
	            <%String position = request.getParameter("position"); 
	              position = java.net.URLDecoder.decode(position,"UTF-8");
	            %>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" value="<%=position%>" type="text" name="position" style="width: 280px;"></input></td>
	        </tr>
	    </table>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="departmentEditPage.submitForm()">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="departmentEditPage.clearForm()">重置</a>
	</div>
</div>
<script type="text/javascript">
var node = $("#departmentTree").tree("getSelected");/* 得到用户选中的部门节点 */
var nodePar = $("#departmentTree").tree("getParent",node.target); /*通过子节点获取父节点 */
var root = $("#departmentTree").tree('getRoot'); 

var departmentEditPage = {
		submitForm : function(){
			if(!$('#departmentEditForm').form('validate')){
				$.messager.alert('提示','表单还未填写完成!');
				return ;
			}
			
			$.post("/rest/department/edit",$("#departmentEditForm").serialize(), function(data){
				if(data.status == 200){
					TT.closeCurrentWindow();/* 关闭弹出窗口 */
					$.messager.alert('提示','编辑内容成功!');
					$("#departmentTree").tree("reload",root.target)/*部门编辑成功后，部门树要进行重新加载*/
					$('#departmentList').datagrid('loadData', { total: 0, rows: [] });  
				}
			});
		},
		clearForm : function(){
			$('#departmentEditForm').form('reset');/* 将刚才表单中输入的内容清空 */
			departmentEditEditor.html('');
		}
};
$("#hideTree").hide();
</script>