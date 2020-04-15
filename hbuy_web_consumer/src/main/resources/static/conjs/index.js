$(function () {

    //初始化所有轮播图数据
    loadAllBanner();

    //加载所有轮播图
    function loadAllBanner() {
        $.ajax({
            type:"POST",
            dataType:"JSON",
            url:"/WebConsumerController/loadAllBanner",
            success:function (data) {
                var bannerStr = '';
                $.each(data,function (i, banner) {
                    bannerStr += '<li><img src="'+banner.imgurl+'" width="740" height="401" /></li>';
                });
                //将轮播图填充进ul中
                $("#bannerImg").html(bannerStr);
                $(".bxslider").bxSlider({  //启动轮播图
                    auto:true,
                    prevSelector:jq(".top_slide_wrap .op_prev")[0],nextSelector:jq(".top_slide_wrap .op_next")[0]
                });
            },
            error:function (data) {
                alert("服务器异常");
            }
        });
    }

});