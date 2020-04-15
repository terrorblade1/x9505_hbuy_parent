layui.use(['jquery','layer','table','form','laydate'], function() {
    var $ = layui.jquery,   //jquery
        layer = layui.layer,  //弹出层
        table = layui.table,  //数据表格
        form = layui.form,  //表单
        laydate = layui.laydate;   //日期

    //判断是否验证成功的变量
    var yzmIf = false;

    //拦截后的提示
    if ($("#loginMsg").val() == 'loginMsg'){
        layer.msg('账号未登录, 请先登录', {icon: 3,time:1500,anim: 6,shade:0.5});
    }

    //随机验证码的验证
    $("#yzm").blur(function () {
        //获取用户输入的验证码并转为小写
        var verifyCodeInput = $(this).val().toLowerCase();
        checkVerifyCode(verifyCodeInput);  //验证
    });


    //验证
    function checkVerifyCode(verifyCodeInput) {
        $.ajax({
            url:"adminusers/checkVerifyCode",
            type:"POST",
            data:{
                "verifyCodeInput":verifyCodeInput
            },
            success:function (rs) {
                if (rs == 'success'){
                    yzmIf = true;
                    layer.tips('验证码输入正确', $("#yzm"), {tips: [2,'green'],time:2000});
                } else {
                    yzmIf = false;
                    layer.tips('验证码输入错误', $("#yzm"), {tips: [2,'#fc1505'],time:2000});
                }
            },
            error:function (rs) {
                layer.msg('服务器异常', {icon: 3,time:2000,anim: 6,shade:0.5});
            }
        });
    }

    //监听登录的form表单
    form.on('submit(login)', function(data){
        console.log(data);
        if (yzmIf){  //验证码正确,执行登录
            login(data.field);
        } else {  //验证码错误
            layer.msg('验证码错误',{icon:2,time:2000,anim:3,shade:0.5});
        }
        return false;  //阻止表单跳转
    });

    //登录
    function login(userJson) {
        $.ajax({
            url:"adminusers/login",
            type:"POST",
            data:userJson,
            success:function (rs) {
                if (rs == 'success'){
                    layer.msg('登录成功', {icon: 1,time:1000,anim: 4,shade:0.5});
                    //用定时器跳转到商城后台管理平台首页
                    setTimeout('window.location = "model/toIndex"',500);
                } else {
                    layer.msg('登录失败', {icon: 2,time:2000,anim: 3,shade:0.5});
                }
            },
            error:function (rs) {
                layer.msg('服务器异常', {icon: 3,time:2000,anim: 6,shade:0.5});
            }
        });
    }

});