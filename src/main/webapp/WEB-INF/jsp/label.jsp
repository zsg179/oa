<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="easyui-panel" title="Nested Panel" data-options="width:'100%',minHeight:500,noheader:true,border:false" style="padding:10px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'west',split:false" style="width:250px;padding:5px">
            <ul id="labelTree" class="easyui-tree" data-options="url:'/label/list',animate: true,method : 'GET'">
            </ul>
        </div>
        <!--面板右部-->
        <div data-options="region:'center'" style="padding:5px">
        <!--EasyUI的数据表格-->
        <!-- toolbar:contentListToolbar”这句代码的意思是定义了工具栏，工具栏中有多个功能（新增/编辑/删除）  -->
            <table class="easyui-datagrid" id="labelList" data-options="toolbar:contentListToolbar,singleSelect:false,collapsible:true,method:'get',url:'/label/query/info',queryParams:{id:200}">
		    <thead>
		        <tr>
		            <th data-options="field:'id',width:100">标签id</th>
		            <th data-options="field:'ou',width:100">标签名</th>
		            <th data-options="field:'member',width:400">员工信息</th>
		        </tr>
		    </thead>
		</table>
        </div>
    </div>
</div>
<script type="text/javascript">
$(function(){
	var tree = $("#labelTree");
	var datagrid = $("#labelList");
	tree.tree({
		onClick : function(node){
			if(tree.tree("isLeaf",node.target)){
				datagrid.datagrid('reload', {
					id:node.id
		        });
			}
		}
	});
});
var contentListToolbar = [{
    text:'新增标签',
    iconCls:'icon-add',
    handler:function(){
    	var node = $("#labelTree").tree("getSelected");
    	if(!node || !$("#labelTree").tree("isLeaf",node.target)){
    		$.messager.alert('提示','新增标签必须选择一条标签记录!');
    		return ;
    	}
    	TT.createWindow({
			url : "/label-add"
		}); 
    }
},{
    text:'编辑标签',
    iconCls:'icon-edit',
    handler:function(){
    	var ids = TT.getSelectionsIds("#labelList");
    	if(ids.length == 0){
    		$.messager.alert('提示','必须选择一条标签记录才能编辑!');
    		return ;
    	}
    	if(ids.indexOf(',') > 0){
    		$.messager.alert('提示','只能选择一条标签记录!');
    		return ;
    	}
		TT.createWindow({
			url : "/label-edit"
		});    	
    }
},{
    text:'删除标签',
    iconCls:'icon-cancel',
    handler:function(){
    	var ids = TT.getSelectionsIds("#labelList");
    	if(ids.length == 0){
    		$.messager.alert('提示','未选中标签记录!');
    		return ;
    	}
    	$.messager.confirm('确认','确定删除ID为 '+ids+' 的标签吗？',function(r){
    	    if (r){
    	    	var params = {"ids":ids};
            	$.post("/label/delete",params, function(data){
        			if(data.status == 200){
        				$.messager.alert('提示','删除标签成功!',undefined,function(){
        					$("#labelList").datagrid("reload");
        					$("#labelTree").tree("reload");
        				});
        			}
        		});
    	    }
    	});
    }
}];
</script>