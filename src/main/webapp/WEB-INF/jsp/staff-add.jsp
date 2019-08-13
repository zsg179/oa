<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  
<!--  @author 卢春宇
      @date 2019年8月日 下午15:19:43
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
<div class=staff-add style="padding:10px 10px 10px 10px">
	<form id="staffAddForm" class="itemForm" method="post">
		<input type="hidden" name="id" value="${param.id }"/>
		<!-- <input type="hidden" name="fullName"/>
		<input type="hidden" name="lastName"/>
		<input type="hidden" name="title"/>
		<input type="hidden" name="ou"/>
		<input type="hidden" name="o"/>
		<input type="hidden" name="phone"/>
		<input type="hidden" name="email"/>
		<input type="hidden" name="label"/> -->
	    <table cellpadding="5">
	        <tr>
	            <td>员工号:</td>
	            <td><input class="easyui-textbox" readonly="true" value="${param.id }"  type="text" name="employeeNumber" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>姓名:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="fullName" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>姓氏:</td>
	            <td><input class="easyui-textbox"  data-options="required:true" type="text" name="lastName" style="width: 280px;"></input></td>
	        </tr> 
	        <tr>
                <td>公司：</td>
                <td><!-- <select id="o" class="easyui-combobox"  > </select> -->
                <input id="o" class="easyui-combobox" data-options="
				    valueField: 'id',
				    textField: 'text',
				    url: '/getCompany',
				    onSelect: function(rec){
				    var url = '/getDept?parentId='+rec.id;
				    $('#ou').combobox('reload', url);
				    }">
                </td>
            </tr>
	        <tr>
	            <td>部门:</td>
	            <td><!-- <input id="ou" class="easyui-combobox" data-options="valueField:'id',textField:'text'"> -->
	            <select id="ou" class="easyui-combobox" valueField='id' textField='text'>
	            
	            </select >
	            
	            </td>
	        </tr>     
	        <tr>
	            <td>职位:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="title" style="width: 280px;"></input></td>
	        </tr>  
	        <tr>
	            <td>手机号码:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="phone" style="width: 280px;"></input></td>
	        </tr> 
	        <tr>
	            <td>邮箱地址:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="email" style="width: 280px;"></input></td>
	        </tr> 
	        <tr>
	            <td>员工标签:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="label" style="width: 280px;"></input></td>
	        </tr>  		             
	    </table>
	</form>
	<div style="padding:10px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="staffAddPage.submitForm()">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="staffAddPage.clearForm()">重置</a>
	</div>
</div>
<script type="text/javascript">

	var staffAddPage  = {
			submitForm : function (){
            /* 如果表单输入不合法，那么会提示”表单还未填写完！” */
				if(!$('#staffAddForm').form('validate')){
					$.messager.alert('提示','表单还未填写完成!');
					return ;
				}
				
				/* 发起url为/staff/save的请求 ，将表单中的数据序列化为key-value形式的字符串 */
				$.post("/staff/save",$("#staffAddForm").serialize(), function(data){
					if(data.status == 200){
						$.messager.alert('提示','新增员工成功!');/*如果返回的状态为200说明员工添加成功*/
    					$("#staffList").datagrid("reload");/*部门添加成功后，部门列表要进行重新加载*/
    					TT.closeCurrentWindow();/* 关闭弹出窗口 */
					}
				});
			},
			clearForm : function(){
				$('#staffAddForm').form('reset');/* 将刚才表单中输入的内容清空 */
				staffAddEditor.html('');
			}
	};
</script>

</body>
</html>