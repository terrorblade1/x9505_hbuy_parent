layui.use(['jquery','layer','table','form','laydate'], function() {
    var $ = layui.jquery,   //jquery
        layer = layui.layer,  //弹出层
        table = layui.table,  //数据表格
        form = layui.form,  //表单
        laydate = layui.laydate;   //日期

    //取出令牌
    getToken();

    //定义定时器
    var InterValObj;

    //js轮询
    InterValObj = window.setInterval(getToken, 10000);

    //定义验证码数组
    var show_num = [];

    //验证码的初始化
    draw(show_num);

    //随机产生的验证码
    var code;

    //表单验证
    form.verify({
        username: function(value, item){ //value：表单的值、item：表单的DOM对象
            if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
                return '用户名不能有特殊字符';
            }
            if(/(^\_)|(\__)|(\_+$)/.test(value)){
                return '用户名首尾不能出现下划线\'_\'';
            }
            if(/^\d+\d+\d$/.test(value)){
                return '用户名不能全为数字';
            }
            if (value.length<3||value.length>12){
                return '用户名长度为3~12位';
            }
        }
        //我们既支持上述函数式的方式，也支持下述数组的形式
        //数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
        ,pwd: [
            /^[\S]{6,12}$/
            ,'密码必须6到12位，且不能出现空格'
        ]
        ,canvas:function (value, item) {
            if (value!=code){
                return '验证码不正确';
            }
        }
    });

    //监听表单提交(登录)
    form.on('submit(demo2)', function(data){
        console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
        //登录
        loginUser(data.field);
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //切换验证码
    $("#canvas").on('click', function () {
        draw(show_num);//生成验证码方法
    });

    /*********************************************自定义函数*********************************************/

    //用户登录
    function loginUser(loginData) {
        $.ajax({
            url:"webusers/loginUser",
            type:"POST",
            data:loginData,
            success:function (rs) {
                console.log(rs);
                if (rs.code == 0){
                    $("#loginHeader").replaceWith('<span class="fl" id="loginHeader">你好，欢迎'+rs.loginUser.uname+'<img src="'+rs.loginUser.userheader+'" width="25px;" height="25px;"/>&nbsp; &nbsp; </span>');
                    layer.msg("登录成功",{"icon":1,anim:4,shade:0.6,"time":1200});
                } else {
                    layer.msg('登录失败', {icon: 2, time: 2000, anim: 3, shade: 0.5});
                }
            },
            error:function (rs) {
                layer.msg('服务器异常', {icon: 3, time: 2000, anim: 6, shade: 0.5});
            }
        });
    }

    //用户注销
    $("#exitUser").click(function () {
            $.ajax({
                type:"POST",
                url:"http://localhost:8091/webusers/exitUser",
                xhrFields:{withCredentials:true}, //允许ajax的跨域访问
                success:function (rs) {
                    if(rs){
                        InterValObj = window.setInterval(getToken, 3000);  //注销成功, 重启js轮循
                        $("#loginHeader").replaceWith('<span class="fl" id="loginHeader">你好，请<a href="model/toLogin">登录</a>&nbsp; <a href="model/toReg" style="color:#ff4e00;">免费注册</a>&nbsp; </span>');
                        layer.msg("注销成功",{"icon":1,anim:4,shade:0.6,"time":1200});
                    } else {
                        layer.msg('注销失败', {icon: 2, time: 2000, anim: 3, shade: 0.5});
                    }
                },
                error:function (rs) {
                    layer.msg('服务器异常', {icon: 3, time: 2000, anim: 6, shade: 0.5});
                }
            });
    });

    //取出令牌
    function getToken() {
        console.log("-------正在获取令牌-------");
        $.ajax({
            url:"http://localhost:8091/webusers/getToken",
            xhrFields:{withCredentials:true}, //允许ajax的跨域访问
            type:"POST",
            success:function (rs) {
                console.log("token: "+rs);
                if (rs != ''){  //取到了令牌token
                    //关掉js轮询
                    window.clearInterval(InterValObj);
                    //使用令牌从redis中查找用户数据
                    getRedisLoginUser(rs);
                }
            },
            error:function (rs) {
                alert("服务器异常");
            }
        });
    }

    //获取到了令牌, 使用令牌从redis中查找用户数据
    function getRedisLoginUser(token) {
        $.ajax({
            type:"POST",
            url:"http://localhost:8091/webusers/getRedisLoginUser",
            data:{
                "token":token
            },
            success:function (rs) {
                console.log(rs);
                if (rs != ''){
                    $("#loginHeader").replaceWith('<span class="fl" id="loginHeader">你好，欢迎'+rs.uname+'<img src="'+rs.userheader+'" width="25px;" height="25px;"/>&nbsp; &nbsp; </span>');
                }
            },
            error:function (rs) {
                alert("服务器异常");
            }
        });
    }

    //产生验证码
    function draw(show_num) {
        code = '';
        var canvas_width = $('#canvas').width();//获取默认的验证码标签的宽度
        var canvas_height = $('#canvas').height();//获取默认的验证码标签的高度
        var canvas = document.getElementById("canvas");//获取到canvas的对象，演员
        var context = canvas.getContext("2d");//获取到canvas画图的环境，演员表演的舞台
        canvas.width = canvas_width;//把默认的宽度赋值给新生产的验证码宽度
        canvas.height = canvas_height;//把默认的高度赋值给新生产的验证码高度
        var sCode = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,E,F,G,H,J,K,L,M,N,P,Q,R,S,T,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0";
        var aCode = sCode.split(",");//获取验证码字符的数组
        var aLength = aCode.length;//获取到数组的长度
        for (var i = 0; i < 4; i++) {
            var j = Math.floor(Math.random() * aLength);//获取到随机的索引值  0-61
            var deg = Math.random() * 30 * Math.PI / 180;//产生0~30之间的随机弧度
            var txt = aCode[j];//得到随机的一个内容
            show_num[i] = txt.toLowerCase();//将大写字母转为小写(获取到的数据全部为小写)
            code += show_num[i];
            var x = 10 + i * 36;//文字在canvas上的x坐标
            var y = 23 + Math.random() * 8;//文字在canvas上的y坐标
            context.font = "bold 33px 微软雅黑";
            context.translate(x, y);
            context.rotate(deg);
            context.fillStyle = randomColor();
            context.fillText(txt, 0, 0);
            context.rotate(-deg);
            context.translate(-x, -y);
        }
        $("#yzmHiddle").val(show_num.join(""));
        for (var i = 0; i < 20; i++) { //验证码上显示线条
            context.strokeStyle = randomColor();
            context.beginPath();
            context.moveTo(Math.random() * canvas_width, Math.random() * canvas_height);
            context.lineTo(Math.random() * canvas_width, Math.random() * canvas_height);
            context.stroke();
        }
        for (var i = 0; i <= 115; i++) { //验证码上显示小点
            context.strokeStyle = randomColor();
            context.beginPath();
            var x = Math.random() * canvas_width;
            var y = Math.random() * canvas_height;
            context.moveTo(x, y);
            context.lineTo(x + 1, y + 1);
            context.stroke();
        }
        console.log(code);
    }

    //得到随机的颜色值
    function randomColor() {
        var r = Math.floor(Math.random() * 256);
        var g = Math.floor(Math.random() * 256);
        var b = Math.floor(Math.random() * 256);
        return "rgb(" + r + "," + g + "," + b + ")";
    }


});