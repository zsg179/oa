<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 引入EasyUI的css文件和js文件 -->


<!--  @author 卢春宇
      @date 2019年8月6日 上午09:36:25
      @version 3.0  -->


<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div class=department-add style="padding:10px 10px 10px 10px">
	<form id="departmentAddForm" class="itemForm" method="post">
	<div id="hideTree">
	<ul id="departmentTree" class="easyui-tree" data-options="url:'/department/list',animate: true,method : 'GET'"></ul>
    </div>
		<input type="hidden" name="parentId" value="${param.parentId}"/>
	    <table cellpadding="5">
	        <tr>
	            <td>部门编号:</td>
	            <td><input class="easyui-textbox easyui-validatebox" readonly="true" value="${param.id }"  type="text" name="id" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>部门名称:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="deptName" style="width: 280px;"></input></td>
	        </tr>      
	        <tr>
	            <td>上级部门:</td>
	            <%String parentName = request.getParameter("parentName"); 
	              parentName = java.net.URLDecoder.decode(parentName,"UTF-8");
	            %>
	            <td><input class="easyui-textbox easyui-validatebox" readonly="true" value="<%=parentName%>"  type="text" name="o" style="width: 280px;"></input></td>
	        </tr> 
	        <tr>
	            <td>职位:</td>
	            <td><input class="easyui-textbox easyui-validatebox" prompt="输入多个职位时用分号隔开" data-options="required:true" type="text" name="position" style="width: 280px;"></input></td>
	        </tr>     
	    </table>
	</form>
	<div style="padding:10px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="departmentAddPage.submitForm()">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="departmentAddPage.clearForm()">重置</a>
	</div>
</div>
<script type="text/javascript">
var node = $("#departmentTree").tree("getSelected");/* 得到用户选中的部门节点 */
	var departmentAddPage  = {
			submitForm : function (){
            /* 如果表单输入不合法，那么会提示”表单还未填写完！” */
				if(!$('#departmentAddForm').form('validate')){
					$.messager.alert('提示','表单还未填写完成!');
					return ;
				}
				
				/* 发起url为/department/save的请求 ，将表单中的数据序列化为key-value形式的字符串 */
				$.post("/department/save",$("#departmentAddForm").serialize(), function(data){
					if(data.status == 200){
						TT.closeCurrentWindow();/* 关闭弹出窗口 */	
					    $.messager.alert('提示','新增部门成功!');/*如果返回的状态为200说明部门添加成功*/
					    $("#departmentTree").tree("reload",node.target)/*部门添加成功后，部门列表要进行重新加载*/
					}
				});
			},
			clearForm : function(){
				$('#departmentAddForm').form('reset');/* 将刚才表单中输入的内容清空 */
				departmentAddEditor.html('');
			}
	};
	$("#hideTree").hide();
</script>

<style>

</style>