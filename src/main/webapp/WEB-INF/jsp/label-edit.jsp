<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!--  @author 卢春宇
      @date 2019年8月6日 下午15:35:28
      @version 1.0  -->


<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div style="padding:10px 10px 10px 10px">
	<form id="labelEditForm" class="itemForm" method="post">
	<div id="hideTree">
	<ul id="labelTree" class="easyui-tree" data-options="url:'/label/list',animate: true,method : 'GET'"></ul>
    </div>
	    <table cellpadding="5">
	        <tr>
	            <td>标签名:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="labelName" style="width: 280px;"></input></td>
	        </tr>      
	    </table>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="labelEditPage.submitForm()">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="labelEditPage.clearForm()">重置</a>
	</div>
</div>
<script type="text/javascript">
var node = $("#labelTree").tree("getSelected");/* 得到用户选中的标签节点 */
var nodePar = $("#labelTree").tree("getParent",node.target); /*通过子节点获取父节点 */
var root = $("#labelTree").tree('getRoot'); 
var labelEditPage = {
		submitForm : function(){
			if(!$('#labelEditForm').form('validate')){
				$.messager.alert('提示','表单还未填写完成!');
				return ;
			}
			
			$.post("/rest/label/edit",$("#labelEditForm").serialize(), function(data){
				if(data.status == 200){
					$.messager.alert('提示','编辑内容成功!');
					$("#labelList").datagrid("reload");
					$("#labelTree").tree("reload",root.target)/*标签编辑成功后，标签列表要进行重新加载*/
					TT.closeCurrentWindow();/* 关闭弹出窗口 */
				}
			});
		},
		clearForm : function(){
			$('#labelEditForm').form('reset');/* 将刚才表单中输入的内容清空 */
			labelEditEditor.html('');
		}
};

$("#hideTree").hide();
</script>