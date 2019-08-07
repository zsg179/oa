<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--EasyUI的嵌套布局方式-->


<!--  @author 卢春宇
      @date 2019年8月6日 上午09:36:25
      @version 3.0  -->


<div class="easyui-panel" title="Nested Panel" data-options="width:'100%',minHeight:500,noheader:true,border:false" style="padding:10px;">
    <div class="easyui-layout" data-options="fit:true">
        <!--面板左部-->
        <div data-options="region:'west',split:false" style="width:250px;padding:5px">
            <ul id="departmentTree" class="easyui-tree" data-options="url:'/department/list',animate: true,method : 'GET'">
            </ul>
        </div>
        <!--面板右部-->
        <div data-options="region:'center'" style="padding:5px">
            <!--EasyUI的数据表格-->
            <!-- toolbar:contentListToolbar”这句代码的意思是定义了工具栏，工具栏中有多个功能（新增/编辑/删除）  -->
            <table class="easyui-datagrid" id="departmentList" data-options="toolbar:contentListToolbar,singleSelect:false,collapsible:true,pagination:true,method:'get',pageSize:20,url:'/department/query/list',queryParams:{categoryId:0}">
		    <thead>
		        <tr>
		            <th data-options="field:'id',width:120">部门编号</th>
		            <th data-options="field:'deptName',width:300">部门名称</th>
		        </tr>
		    </thead>
		</table>
        </div>
    </div>
</div>

<!--增加部门/编辑部门/删除部门js判断是否符合条件，符合条件则进行下一步操作-->
<script type="text/javascript">
$(function(){/* 函数是在页面加载完之后触发执行的js代码  */
	var tree = $("#departmentTree");/* 获取部门树 */
	var datagrid = $("#departmentTree");/* 是获取部门列表 */
	tree.tree({
		onClick : function(node){/* 点击左边部门分类树的某个节点时，会做一下判断，判断是不是叶子节点*/
			if(tree.tree("isLeaf",node.target)){
				datagrid.datagrid('reload', {
					categoryId :node.id
		        });
			}
		}
	});
});
var contentListToolbar = [{
    text:'新增部门',
    iconCls:'icon-add',
    handler:function(){/* 点击‘新增’触发的函数 */
    	var node = $("#departmentTree").tree("getSelected");/* 得到用户选中的部门节点 */
/* 如果选中的不是节点或者不是叶子节点，那么这时就弹出一个提示框。如果点击的是叶子节点，则弹出一个提示框，告诉用户 不可对其进行操作*/
    	if(!node){
    		$.messager.alert('提示','新增部门必须选择一个部门分类!');
    		return ;
    	}
    	else if($("#departmentTree").tree("isLeaf",node.target)){
    		$.messager.alert('提示','不可对员工进行操作!');
    		return ;
    	}
    	TT.createWindow({
			url : "/department-add"
		}); 
    }
},{
    text:'编辑部门',
    iconCls:'icon-pencil',
    handler:function(){
    	var ids = TT.getSelectionsIds("#departmentList");
    	if(ids.length == 0){
    		$.messager.alert('提示','必须选择一条部门信息才能编辑!');
    		return ;
    	}
    	if(ids.indexOf(',') > 0){
    		$.messager.alert('提示','只能选择一条部门信息!');
    		return ;
    	}
		TT.createWindow({
			url : "/department-edit"
		});    	
    }
},{
    text:'删除部门',
    iconCls:'icon-cancel',
    handler:function(){
    	var ids = TT.getSelectionsIds("#departmentList");
    	if(ids.length == 0){
    		$.messager.alert('提示','未选中部门!');
    		return ;
    	}
    	$.messager.confirm('确认','确定删除编号为 '+ids+' 的部门吗？',function(r){
    	    if (r){
    	    	var params = {"ids":ids};
            	$.post("/department/delete",params, function(data){
        			if(data.status == 200){
        				$.messager.alert('提示','删除部门成功!',undefined,function(){
        					$("#departmentList").datagrid("reload");
        				});
        			}
        		});
    	    }
    	});
    }
}];
</script>