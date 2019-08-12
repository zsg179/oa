<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--EasyUI的嵌套布局方式-->


<!--  @author 卢春宇
      @date 2019年8月9日 上午11:20:13
      @version 1.0  -->
<div class="easyui-panel" title="Nested Panel" data-options="width:'100%',minHeight:500,noheader:true,border:false" style="padding:10px;">
    <div class="easyui-layout" data-options="fit:true">
        <!--面板左部-->
        <div data-options="region:'west',split:false" style="width:250px;padding:5px">
            <ul id="staffTree" class="easyui-tree" data-options="url:'/staff/list',animate: true,method : 'GET'">
            </ul>
        </div>
        <!--面板右部-->
        <div data-options="region:'center'" style="padding:5px">
        <!--EasyUI的数据表格-->
        <!-- toolbar:contentListToolbar”这句代码的意思是定义了工具栏，工具栏中有多个功能（新增/编辑/删除）  -->
            <table class="easyui-datagrid" id="staffList" data-options="toolbar:contentListToolbar,singleSelect:false,collapsible:true,method:'get',url:'/staff/query/info',queryParams:{id:0}">
		    <thead>
		        <tr>
		            <th data-options="field:'id',width:50">员工号</th>
		            <th data-options="field:'fullName',width:90">姓名</th>
		            <th data-options="field:'lastName',width:50">姓氏</th>
		            <th data-options="field:'title',width:90">职位</th>
		            <th data-options="field:'ou',width:100">部门</th>
		            <th data-options="field:'o',width:130">公司</th>
		            <th data-options="field:'phone',width:160">手机号码</th>
		            <th data-options="field:'email',width:160">邮箱地址</th>
		            <th data-options="field:'label',width:223">员工标签</th>
		        </tr>
		    </thead>
		</table>
        </div>
    </div>
</div>

<!--增加员工/编辑员工/删除员工js判断是否符合条件，符合条件则进行下一步操作-->
<script type="text/javascript">
$(function(){/* 函数是在页面加载完之后触发执行的js代码  */
	var tree = $("#staffTree");/* 获取部门树 */
	var datagrid = $("#staffList");/* 是获取员工列表 */
	tree.tree({
		onClick : function(node){/* 点击左边部门分类树的某个节点时，会做一下判断，判断是不是叶子节点*/
			if(tree.tree("isLeaf",node.target)){
				datagrid.datagrid('reload', {
					id :node.id
		        });
			}
		}
	});
});
var contentListToolbar = [{
    text:'新增员工',
    iconCls:'icon-add',
    handler:function(){/* 点击‘新增’触发的函数 */
    	var node = $("#staffTree").tree("getSelected");/* 得到用户选中的员工节点 */
    	/* 如果点击的不是叶子结点或者没有选择节点，则弹出一个提示框，告诉用户 必须选择员工 */
    	if(!node || !$("#staffTree").tree("isLeaf",node.target)){
    		$.messager.alert('提示','新增员工必须选择一条员工记录!');
    		return ;
    	}
    	 //发送请求，生成id
    	$.post("/staff/gen/id",function(data){
    		if(data.status==200){
    			var id=data.data;
    			TT.createWindow({
    				url : "/staff-add?id="+id
    			});
    		}else{
    			$.messager.alert('提示', '生成id出错！');
    		}
    	}) 
    }
},{
    text:'编辑员工',
    iconCls:'icon-pencil',
    handler:function(){
    	var ids = TT.getSelectionsIds("#staffList");
    	if(ids.length == 0){
    		$.messager.alert('提示','必须选择一条员工记录才能编辑!');
    		return ;
    	}
    	if(ids.indexOf(',') > 0){
    		$.messager.alert('提示','只能选择一条员工记录!');
    		return ;
    	}
    	  var row = $('#staffList').datagrid('getSelected');
          var id=row.id
  		  TT.createWindow({
  			url : "/staff-edit?id="+id
  		});    	 
    }
},{
    text:'删除员工',
    iconCls:'icon-cancel',
    handler:function(){
    	var ids = TT.getSelectionsIds("#staffList");
    	if(ids.length == 0){
    		$.messager.alert('提示','未选中员工!');
    		return ;
    	}
    	$.messager.confirm('确认','确定删除编号为 '+ids+' 的员工吗？',function(r){
    	    if (r){
    	    	var params = {"ids":ids};
            	$.post("/staff/delete",params, function(data){
        			if(data.status == 200){
        				$.messager.alert('提示','删除员工成功!',undefined,function(){
        					$("#staffList").datagrid("reload");
        				});
        			}
        		});
    	    }
    	});
    }
}];
</script>