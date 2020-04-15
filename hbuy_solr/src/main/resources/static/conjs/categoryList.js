jQuery(function () {

    //点击搜索进行查询
    jQuery("#solrBtn").click(function () {
        //获取要搜索的关键词
        var solrPar = jQuery("#solrPar").val();
        //判断关键词是否为空
        if (solrPar != ''){
            //发送ajax请求访问服务器
            loadProductBySolr(solrPar);
        } else {
            alert("请输入要搜索关键词");
        }
    });

    //根据条件加载solr引擎中的商品数据
    function loadProductBySolr(solrPar){
        jQuery.ajax({
            url:"solr/loadProductBySolr",
            type:"POST",
            data:{"solrPar":solrPar},
            success:function (data) {
                if (data != ''){
                    var productStr = '';
                    //显示商品列表
                    jQuery.each(data,function (i, product) {
                        productStr += '<li>';
                        productStr += '<div class="img"><a href="#"><img src="'+product.avatorImg+'"></a></div>';
                        productStr += '<div class="price">';
                        productStr += '<font>￥<span>'+product.price+'</span></font> &nbsp; 26R';
                        productStr += '</div>';
                        productStr += '<div class="name"><a href="'+product.pid+'">'+product.title+'</a></div>';
                        productStr += '<div class="carbg">';
                        productStr += '<a href="#" class="ss">收藏</a>';
                        productStr += '<a href="#" class="j_car">加入购物车</a>';
                        productStr += '</div>';
                        productStr += '</li>';
                    });
                    jQuery("#cate_list").html(productStr);  //填充
                } else {
                    alert("无相关商品");
                }
            },
            error:function (data) {
                alert("服务器异常");
            }
        });
    }

});