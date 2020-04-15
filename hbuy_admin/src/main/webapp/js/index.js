// 配置
layui.config({
	base: './hpModules/' // 扩展模块目录
}).extend({ // 模块别名 ，引入自定义模块
	hpTab: 'hpTab/hpTab',
	hpRightMenu: 'hpRightMenu/hpRightMenu',
	hpFormAll: 'hpFormAll/hpFormAll',
});

//JavaScript代码区域
layui.use(['element', 'carousel','hpTheme', 'hpTab', 'hpLayedit', 'hpRightMenu'], function() {
	
	var element = layui.element;
	var carousel = layui.carousel; //轮播
	var hpTab = layui.hpTab;
	var hpRightMenu = layui.hpRightMenu;
	var hpTheme=layui.hpTheme;
	$ = layui.$;
	
    // 初始化主题
	hpTheme.init();
	 //初始化轮播
	carousel.render({
		elem: '#test1',
		width: '100%', //设置容器宽度
		interval: 1500,
		height: '500px',
		arrow: 'none', //不显示箭头
		anim: 'fade', //切换动画方式
	});

    // 初始化 动态tab
    hpTab.init();
    // 右键tab菜单
    hpRightMenu.init();

    //用户退出
	$("#exitUser").click(function () {
        layer.confirm('你确认退出么？', function(index){
            exitUser();  //执行退出
        	layer.close(index);  //关闭当前弹框
        });
    });

	//退出
	function exitUser() {
		$.ajax({
		    url:"adminusers/exitUser",
		    type:"POST",
		    success:function (rs) {
                if (rs == 'success'){
                    layer.msg('当前账号退出成功', {icon: 1,time:1000,anim: 4,shade:0.5});
                    //用定时器跳转到登录页面
                    setTimeout('window.location = "/"',1000);
                } else {
                    layer.msg('当前账号退出失败', {icon: 2,time:2000,anim: 2,shade:0.5});
                }
		    },
		    error:function (rs) {
                layer.msg('服务器异常', {icon: 3,time:2000,anim: 6,shade:0.5});
		    }
		});
    }


});