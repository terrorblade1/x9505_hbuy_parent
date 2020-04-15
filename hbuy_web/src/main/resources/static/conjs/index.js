$(function () {

    //定义定时器
    var InterValObj;

    //js轮询
    InterValObj = window.setInterval(getToken, 3000);

    //加载所有的导航菜单数据
    loadAllWebMenus();

    //取令牌
    getToken();

    function loadAllWebMenus() {
        $.ajax({
            type:"POST",
            dataType:"JSON",
            url:"webmenu/loadAll",
            success:function (data) {
                var webMenuStr = '';
                $.each(data,function (i, webMenu) {
                    webMenuStr += '<li><a href="'+webMenu.url+'">'+webMenu.title+'</a></li>';
                });
                $(".menu_r").html(webMenuStr);  //填充
            },
            error:function (data) {
                alert("服务器异常");
            }
        });
    }

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

    //用户注销
    $("#exitUser").click(function () {
        $.ajax({
            type:"POST",
            url:"http://localhost:8091/webusers/exitUser",
            xhrFields:{withCredentials:true}, //允许ajax的跨域访问获取cookie
            success:function (data) {
                console.log(data);
                if(data){
                    InterValObj = window.setInterval(getToken, 3000);  //注销成功, 重启js轮循
                    $("#loginHeader").replaceWith('<span class="fl" id="loginHeader">你好，请<a href="model/toLogin">登录</a>&nbsp; <a href="model/toReg" style="color:#ff4e00;">免费注册</a>&nbsp; </span>');
                    alert("注销成功")
                }
            },
            error:function () {
                alert("服务器异常！！")
            }
        });
    });

});