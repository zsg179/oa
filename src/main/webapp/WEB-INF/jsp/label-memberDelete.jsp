<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 引入EasyUI的css文件和js文件 -->


<!--  @author 卢春宇
      @date 2019年8月23日 上午09:34:22
      @version 1.0  -->


<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div class=memberDelete style="padding:10px 10px 10px 10px">
	<form id="memberDeleteForm" class="itemForm" method="post">
	<input type="hidden" name="labelId" value="${param.labelId}"/>
	    <table cellpadding="5">
	        <tr>
	            <td>员工id:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="id" style="width: 280px;"></input></td>
	        </tr>      
	    </table>
	</form>
	<div style="padding:10px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="memberDeletePage.submitForm()">删除</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="memberDeletePage.clearForm()">重置</a>
	</div>
</div>
<script type="text/javascript">
	var memberDeletePage  = {
			submitForm : function (){
            /* 如果表单输入不合法，那么会提示”表单还未填写完！” */
				if(!$('#memberDeleteForm').form('validate')){
					$.messager.alert('提示','表单还未填写完成!');
					return ;
				}
				
				$.post("/label/deleteMember",$("#memberDeleteForm").serialize(), function(data){
					if(data.status == 200){
					    $.messager.alert('提示','删除员工成功!');/*如果返回的状态为200说明员工删除成功*/
					    
					    //$("#labelList").datagrid("reload");/*员工删除成功后，员工列表要进行重新加载*/
					    //$("#labelTree").tree("reload")/*员工删除成功后，标签树要进行重新加载*/
					    //TT.closeCurrentWindow();/* 关闭弹出窗口 */	
					    
					}
					else if(data.status == 150){
					    $.messager.alert('提示','必须保留至少一个员工标签，移除员工失败。!');/*如果返回的状态为200说明员工删除成功*/
					    	
					}
					else if(data.status == 151){
					    $.messager.alert('提示','该员工不属于标签下，请到正确的标签下删除!');/*如果返回的状态为200说明员工删除成功*/
					    
					}
					else{
						 $.messager.alert('提示','删除员工失败，请确认员工id和员工标签是否唯一！');
					}
				});
			},
			clearForm : function(){
				$('#memberDeleteForm').form('reset');/* 将刚才表单中输入的内容清空 */
				memberDeleteEditor.html('');
			}
	};
</script>

<style>

</style>