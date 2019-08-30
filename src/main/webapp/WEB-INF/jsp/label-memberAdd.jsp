<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 引入EasyUI的css文件和js文件 -->


<!--  @author 卢春宇
      @date 2019年8月23日 上午09:08:35
      @version 1.0  -->


<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div class=memberAdd style="padding:10px 10px 10px 10px">
	<form id="memberAddForm" class="itemForm" method="post">
	<input type="hidden" name="labelId" value="${param.labelId}"/>
	    <table cellpadding="5">
	        <tr>
	            <td>员工id:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="id" style="width: 280px;"></input></td>
	        </tr>      
	    </table>
	</form>
	<div style="padding:10px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="memberAddPage.submitForm()">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="memberAddPage.clearForm()">重置</a>
	</div>
</div>
<script type="text/javascript">
	var memberAddPage  = {
			submitForm : function (){
            /* 如果表单输入不合法，那么会提示”表单还未填写完！” */
				if(!$('#memberAddForm').form('validate')){
					$.messager.alert('提示','表单还未填写完成!');
					return ;
				}
				
				$.post("/label/addMember",$("#memberAddForm").serialize(), function(data){
					if(data.status == 200){
						TT.closeCurrentWindow();/* 关闭弹出窗口 */	
					    $.messager.alert('提示','新增员工成功!');/*如果返回的状态为200说明员工添加成功*/
					    $("#labelList").datagrid("reload");
					    $("#labelTree").tree("reload")/*员工添加成功后，标签树要进行重新加载*/
					}
					else{
						 $.messager.alert('提示',data.msg);
					}
				});
			},
			clearForm : function(){
				$('#memberAddForm').form('reset');/* 将刚才表单中输入的内容清空 */
				memberAddEditor.html('');
			}
	};
</script>

<style>

</style>