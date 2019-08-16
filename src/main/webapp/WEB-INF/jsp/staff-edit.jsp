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
	    <table cellpadding="5">
	       <tr>
	            <td>员工号:</td>
	            <td><input class="easyui-textbox" readonly="true" value="${param.id}"  type="text" name="id" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>姓名:</td>
	             <%String fullName = request.getParameter("fullName"); 
	              fullName = java.net.URLDecoder.decode(fullName,"UTF-8");
	            %>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" value="<%=fullName%>" type="text" name="fullName" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>姓氏:</td>
	             <%String lastName = request.getParameter("lastName"); 
	              lastName = java.net.URLDecoder.decode(lastName,"UTF-8");
	            %>
	            <td><input class="easyui-textbox easyui-validatebox"  data-options="required:true" value="<%=lastName%>" type="text" name="lastName" style="width: 280px;"></input></td>
	        </tr>  
	        <tr>
	            <td>公司:</td>
	             <%String o = request.getParameter("o"); 
	              o = java.net.URLDecoder.decode(o,"UTF-8");
	            %>
	            <td>
	            <input id="o" name="o" class="easyui-combobox" style="width: 280px;" value="<%=o%>" panelHeight="auto" data-options="
				    valueField: 'id',
				    textField: 'text',
				    url: '/getCompany',
				    prompt: '请选择公司', 
				    required:true,
				    editable:false,
				    onSelect: function(rec){
				    var url = '/getDept?parentId='+rec.id;
				    $('#ou').combobox('reload', url);
				    $('#ou').combobox('clear');//清除三级联动部门默认选中项
				    $('#title').combobox('clear');//清除三级联动职位默认选中项
				    }">
				</td>
	        </tr> 
	        <tr>
	            <td>部门:</td>
	            <%String ou = request.getParameter("ou"); 
	              ou = java.net.URLDecoder.decode(ou,"UTF-8");
	            %>
	            <td>
	            <input id="ou" name="ou" class="easyui-combobox" value="<%=ou%>" style="width: 280px;" panelHeight="auto" data-options="
	            valueField:'id',
	            textField:'text',
	            prompt: '请选择部门',
	            required:true,
	            editable:false,
			    onSelect: function(rec){
			    var url = '/getPosition?id='+rec.id;
			    $('#title').combobox('reload', url);
			    $('#title').combobox('clear');//清除三级联动职位默认选中项
			    }">
	            </td>
	        </tr> 
	             
	        <tr>
	            <td>职位:</td>
	            <%String title = request.getParameter("title"); 
	              title = java.net.URLDecoder.decode(title,"UTF-8");
	            %>
	            <td>
	            <input id="title" name="title" class="easyui-combobox" value="<%=title%>" style="width: 280px;" panelHeight="auto" data-options="
	            valueField:'id',
	            textField:'text',
	            prompt: '请选择职位',
	            editable:false,
	            required:true">
	            </td>
	        </tr> 
	        <tr>
	            <td>手机号码:</td>
	             <%String phone = request.getParameter("phone"); 
	             phone = java.net.URLDecoder.decode(phone,"UTF-8");
	            %>
	            <td><input class="easyui-textbox" data-options="required:true" validType="telephone" value="<%=phone%>" type="text" name="phone" style="width: 280px;"></input></td>
	        </tr> 
	        <tr>
	            <td>邮箱地址:</td>
	             <%String email = request.getParameter("email"); 
	             email = java.net.URLDecoder.decode(email,"UTF-8");
	            %>
	            <td><input class="easyui-textbox" data-options="required:true" validType="email" value="<%=email%>" type="text" name="email" style="width: 280px;"></input></td>
	        </tr> 
	        <tr>
	            <td>员工标签:</td>
	             <%String lable = request.getParameter("lable"); 
	             lable = java.net.URLDecoder.decode(lable,"UTF-8");
	            %>
	            <td><input id="lable" name="lable" value="hhh" class="easyui-combobox" data-options=" prompt: '请选择标签'" style="width: 280px;"></td>
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

	         $(function(){
	             //自定义电话校验规则
	             var phoneReg = /^[1][3,4,5,8]\d{9}$/
	             $.extend($.fn.validatebox.defaults.rules, { 
	                 telephone : {
	                     validator: function(value,param){ 
	                         return phoneReg.test(value);
	                     },
	                     message: '请输入有效的手机号码'
	                 }
	             }); 
	         });
</script>

</body>
</html>