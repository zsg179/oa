<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="easyui-panel" title="Nested Panel" data-options="width:'100%',minHeight:650,noheader:true,border:false" style="padding:10px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'west',split:false" style="width:250px;padding:5px">
            <ul id="labelTree" class="easyui-tree" data-options="url:'/label/list',animate: true,method : 'GET'">
            </ul>
        </div>
        <div id="labelMenu" class="easyui-menu"  style="width:120px;" data-options="onClick:menuHandler">
               <div data-options="iconCls:'icon-add',name:'addMember'">添加员工</div>
               <div class="menu-sep"></div>
               <div data-options="iconCls:'icon-remove',name:'deleteMember'">删除员工</div>
               <div class="menu-sep"></div>
               <div data-options="iconCls:'icon-pencil',name:'rename'">重命名标签</div>
               <div class="menu-sep"></div>
               <div data-options="iconCls:'icon-cancel',name:'delete'">删除标签</div>
        </div>
        
        <!--面板右部-->
        <div data-options="region:'center'" style="padding:5px">
        <!--EasyUI的数据表格-->
        <!-- toolbar:contentListToolbar”这句代码的意思是定义了工具栏，工具栏中有多个功能（新增/编辑/删除）  -->
            <table class="easyui-datagrid" id="labelList" data-options="toolbar:contentListToolbar,singleSelect:true,collapsible:true,method:'get',url:'/label/query/info',queryParams:{id:200}">
		    <thead>
		        <tr>
		            <th data-options="field:'member',width:900">员工信息</th>
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
    text:'添加标签',
    iconCls:'icon-add',
    handler:function(){
    	var tree = $("#labelTree");
    	var node = tree.tree("getSelected");
    	var id= node.id;
    	$.post("/department/gen/id",function(data){
    		if(data.status==200){
    		var labelId=data.data;
    	    TT.createWindow({
			  url : "/label-add?id="+id
		   }); 
    	  }
    	})
    }
}]

$(function(){
	$("#labelTree").tree({
		onContextMenu: function(e,node){
            e.preventDefault();
            $(this).tree('select',node.target);
            $('#labelMenu').menu('show',{
                left: e.pageX,
                top: e.pageY
            });
        },
        onAfterEdit : function(node){
        	$.post("/rest/label/edit",{id:node.id,name:node.text},function(data){
    			if(data.status == 200){
    					$("#labelTree").tree("reload")
    			}
    			else{
    				$.messager.alert('提示','重命名标签失败!')
    			}
    		});
        }
	});
});
function menuHandler(item){
	var tree = $("#labelTree");
	var node = tree.tree("getSelected");
	var labelId= node.id;
	var datagrid = $("#labelList");
	if(item.name === "addMember"){
		TT.createWindow({
			url : "/label-memberAdd?&labelId="+labelId
		});
	}else if(item.name === "deleteMember"){
		TT.createWindow({
			url : "/label-memberDelete?&labelId="+labelId
		});
	}else if(item.name === "rename"){
		tree.tree('beginEdit',node.target);
	}else if(item.name === "delete"){
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的标签吗？',function(r){
			if(r){
            	$.post("/label/delete",{id:node.id}, function(data){
        			if(data.status == 200){
        				$.messager.alert('提示','删除标签成功!',undefined,function(){
        					tree.tree("remove",node.target);
        					$("#labelList").datagrid("reload");
        					$("#labelTree").tree("reload")
        				})
        			} 
        			else{
            			$.messager.alert('提示','删除标签失败!员工至少保留一个标签，请单独删除员工标签')	 
        			}
        		});
			}
		});
	}
}

/* $('#labelTree').tree({
	onLoadSuccess: function (node, data) {
	    if (data.length > 0) {
	          //找到第一个元素
	          var n = $('#labelTree').tree('find', data[0].id);
	          //调用选中事件
	          $('#labelTree').tree('select', n.target);
	          //调用其它函数
	           display(data[0].id);
	             }
	      }, 
})
 $("#labelList").datagrid({
	 
})  */
</script>