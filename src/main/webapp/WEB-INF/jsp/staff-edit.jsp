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
	            <td><input class="easyui-textbox" readonly="true" value="${param.id}"  type="text" name="id" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>姓名:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="fullName" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>姓氏:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="lastName" style="width: 280px;"></input></td>
	        </tr>       
	        <tr>
	            <td>职位:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="title" style="width: 280px;"></input></td>
	        </tr>  
	        <tr>
	            <td>部门:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="ou" style="width: 280px;"></input></td>
	        </tr> 
	        <tr>
	            <td>公司:</td>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" type="text" name="o" style="width: 280px;"></input></td>
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
	            <td><input id="lable" name="lable" class="easyui-combobox easyui-validatebox"  data-options="required:true" style="width: 280px;"></td>
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


function combobox_checkbox(_id, optionsJson, hight) {
    $('#'+_id).combobox({
        data: optionsJson,
        valueField: 'id',
        textField: 'text',
        panelHeight: hight,
        multiple: true,
        editable: false,
     
        onLoadSuccess: function () { // 下拉框数据加载成功调用
            $("#"+_id).combobox('clear'); //清空
        },
        onSelect: function (row) { // 选中一个选项时调用
            var opts = $(this).combobox('options');
                //设置选中选项所对应的复选框为选中状态
                $('#'+row.domId + ' input[type="checkbox"]').prop("checked", true);
        },
        onUnselect: function (row) { // 取消选中一个选项时调用
            var opts = $(this).combobox('options');
                //设置选中选项所对应的复选框为非选中状态
                $('#'+row.domId + ' input[type="checkbox"]').prop("checked", false);
                var selectedList = $("#"+_id).combobox('getValues');
                // 如果“所有”是选中状态,则将其取消选中
                if (selectedList[0] === "") {
                    // 将“所有”选项移出数组，并且将该项的复选框设为非选中
                    selectedList.splice(0, 1);
                    $('#'+opts.data[0].domId + ' input[type="checkbox"]').prop("checked", false);
                }
                $("#"+_id).combobox('clear');//清空
                $("#"+_id).combobox('setValues', selectedList); // 重新复制选中项


        }
    });
}

var tttData = [
               {id: '1', text: '选项1'},
               {id: '2', text: '选项2'},
               {id: '3', text: '选项3'},
               {id: '4', text: '选项4'},
               {id: '5', text: '选项5'},
               {id: '6', text: '选项6'},
           ];
           combobox_checkbox('lable', tttData, 'auto');

</script>

</body>
</html>