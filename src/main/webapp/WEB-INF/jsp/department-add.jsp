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
		<input type="hidden" name="departmentId"/>
	    <table cellpadding="5">
	        <tr>
	            <td>部门编号:</td>
	            <td><input class="easyui-textbox easyui-validatebox" readonly="true" value="${param.id }"  type="text" name="deptID" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>部门名称:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="deptName" style="width: 280px;"></input></td>
	        </tr>      
	        <tr>
	            <td>上级部门:</td>
	            <td><input class="easyui-textbox easyui-validatebox" readonly="true" value="${param.parentName }"  type="text" name="o" style="width: 280px;"></input></td>
	        </tr>      
	    </table>
	</form>
	<div style="padding:10px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="departmentAddPage.submitForm()">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="departmentAddPage.clearForm()">重置</a>
	</div>
</div>
<script type="text/javascript">

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
						$.messager.alert('提示','新增部门成功!');/*如果返回的状态为200说明部门添加成功*/
    					$("#departmentList").datagrid("reload");/*部门添加成功后，部门列表要进行重新加载*/
    					TT.closeCurrentWindow();/* 关闭弹出窗口 */
					}
				});
			},
			clearForm : function(){
				$('#departmentAddForm').form('reset');/* 将刚才表单中输入的内容清空 */
				departmentAddEditor.html('');
			}
	};
</script>

<style>

</style>