<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--EasyUI的嵌套布局方式-->


<!--  @author 卢春宇
      @date 2019年8月6日 上午09:36:25
      @version 3.0  -->


<div class="easyui-panel" title="Nested Panel" data-options="width:'100%',minHeight:650,noheader:true,border:false" style="padding:10px;">
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
            <table class="easyui-datagrid" id="departmentList" data-options="toolbar:contentListToolbar,singleSelect:false,collapsible:true,method:'get',url:'/department/query/info',queryParams:{id:0}">
		    <thead>
		        <tr>
		            <th data-options="field:'id',width:120">部门编号</th>
		            <th data-options="field:'deptName',width:300">部门名称</th>
		            <th data-options="field:'o',width:300">上级部门</th>
		            <th data-options="field:'position',width:300">职位</th>
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
	var datagrid = $("#departmentList");/* 是获取部门列表 */
	tree.tree({
		onClick : function(node){/* 点击左边部门分类树的某个节点时，会做一下判断，判断是不是叶子节点*/
			if(!tree.tree("isLeaf",node.target)){
				datagrid.datagrid('reload', {
					id :node.id
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
        var nodePar = $("#departmentTree").tree("getParent",node.target); /*通过子节点获取父节点 */
        var parentId= node.id;
    	var ids = TT.getSelectionsIds("#departmentList");
    	if(ids.length == 0){
    		$.messager.alert('提示','必须选择一条部门信息才能新增!');
    		return ;
    	}
    	if(ids.indexOf(',') > 0){
    		$.messager.alert('提示','只能选择一条部门信息!');
    		return ;
    	}
    	$.post("/department/gen/id",function(data){
    		if(data.status==200){
    			var id=data.data;
    			var row = $('#departmentList').datagrid('getSelected');
    		    o=row.o
    		    deptName=row.deptName
    		    if(o=="无上级部门"){
    		       parentName="o="+deptName
    		    }
    		    else{
    		    parentName="ou="+deptName+","+o
    		    }
    			TT.createWindow({
    				url : "/department-add?id="+id+"&parentName="+encodeURI(encodeURI(parentName))+"&parentId="+parentId
    			});
    		}else{
    			$.messager.alert('提示', '生成id出错！');
    		}
    	})
    }
},{
    text:'编辑部门',
    iconCls:'icon-pencil',
    handler:function(){
    	var node = $("#departmentTree").tree("getSelected");/* 得到用户选中的部门节点 */
        var nodePar = $("#departmentTree").tree("getParent",node.target); /*通过子节点获取父节点 */
        var parentId= nodePar.id;	
    	var ids = TT.getSelectionsIds("#departmentList");
    	if(ids.length == 0){
    		$.messager.alert('提示','必须选择一条部门信息才能编辑!');
    		return ;
    	}
    	if(ids.indexOf(',') > 0){
    		$.messager.alert('提示','只能选择一条部门信息!');
    		return ;
    	}
    	//发送请求，生成id
    	
    	var row = $('#departmentList').datagrid('getSelected');
    	Id=row.id
        DeptName=row.deptName
        parentName=row.o
        position=row.position
    			TT.createWindow({
    				url : "/department-edit?Id="+Id+"&DeptName="+encodeURI(encodeURI(DeptName))+"&parentId="+parentId
    				+"&parentName="+encodeURI(encodeURI(parentName))+"&position="+encodeURI(encodeURI(position)) 	
    			});
    }
},{
    text:'删除部门',
    iconCls:'icon-cancel',
    handler:function(){
    	var node = $("#departmentTree").tree("getSelected");/* 得到用户选中的部门节点 */
        var nodePar = $("#departmentTree").tree("getParent",node.target); /*通过子节点获取父节点 */
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
        					$("#departmentTree").tree("reload",nodePar.target)
        				});
        			}
        			else{
        				$.messager.alert('提示','删除部门失败,请确认该部门是否还有员工！',undefined,function(){
        				});	
        			}
        			
        		});
    	    }
    	});
    }
}];

</script>

