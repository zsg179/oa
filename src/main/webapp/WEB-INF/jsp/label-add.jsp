<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 引入EasyUI的css文件和js文件 -->


<!--  @author 卢春宇
      @date 2019年8月20日 上午10:16:35
      @version 1.0  -->


<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div class=label-add style="padding:10px 10px 10px 10px">
	<form id="labelAddForm" class="itemForm" method="post">
	    <table cellpadding="5">
	        <tr>
	            <td>标签名:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="cn" style="width: 280px;"></input></td>
	        </tr> 
	        <tr>
	            <td>员工id:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="id" style="width: 280px;"></input></td>
            </tr>
	    </table>
	</form>
	<div style="padding:10px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="labelAddPage.submitForm()">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="labelAddPage.clearForm()">重置</a>
	</div>
</div>
<script type="text/javascript">
var node = $("#labelTree").tree("getSelected");/* 得到用户选中的标签节点 */
var nodePar = $("#labelTree").tree("getParent",node.target); /*通过子节点获取父节点 */
	var labelAddPage  = {
			submitForm : function (){
            /* 如果表单输入不合法，那么会提示”表单还未填写完！” */
				if(!$('#labelAddForm').form('validate')){
					$.messager.alert('提示','表单还未填写完成!');
					return ;
				}
				
				/* 发起url为/label/save的请求 ，将表单中的数据序列化为key-value形式的字符串 */
				$.post("/label/save",$("#labelAddForm").serialize(), function(data){
					if(data.status == 200){
						TT.closeCurrentWindow();/* 关闭弹出窗口 */	
					    $.messager.alert('提示','新增标签成功!');/*如果返回的状态为200说明标签添加成功*/
					    $("#labelTree").tree("reload")/*标签添加成功后，标签列表要进行重新加载*/
					}
				});
			},
			clearForm : function(){
				$('#labelAddForm').form('reset');/* 将刚才表单中输入的内容清空 */
				labelAddEditor.html('');
			}
	};
</script>

<style>

</style>