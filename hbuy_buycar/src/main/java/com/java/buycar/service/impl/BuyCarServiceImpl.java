package com.java.buycar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.java.buycar.mapper.WebProductDetailMapper;
import com.java.buycar.utils.Base64Utils;
import com.java.model.BuyCar;
import com.java.model.Good;
import com.java.model.WebProductDetailEntity;
import com.java.model.WebUsersEntity;
import com.java.service.BuyCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Author: 乐科
 * Date: 2020/3/27 9:22
 * 购物车业务层接口实现类
 */
@Service
@Transactional(readOnly = false)
public class BuyCarServiceImpl implements BuyCarService {

    //依赖注入redis模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    //注入商品详情Mapper代理对象
    @Autowired
    private WebProductDetailMapper webProductDetailMapper;

    /**
     * 未登录情况下实现购物车的添加 (将数据存到浏览器的cookie中)
     * @param goodId  商品id
     * @param num  商品数量
     * @param request  请求对象
     * @param response  响应对象
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> addBuyCar(Integer goodId, Integer num, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //cookie的区分
        //1.根据服务器：http://localhost:8089
        //2.根据设置产生cookie的路径：buyCarCookie.setPath("/buyCar")与控制器的访问路径一致;
        //3.根据cookie的name进行区分

        //新建Map集合
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("goodId",goodId);
        map.put("num",num);
        map.put("code","fail");  //添加失败
        //获取页面中存放购物车数据的cookie
        //得到整个页面中的cookies
        Cookie[] cookies = request.getCookies();
        //定义一个空的buyCar购物车的cookie
        Cookie buyCarCookie = null;
        //判断cookies是否存在cookie
        if (cookies != null){
            for (int i = 0; i < cookies.length; i++) {
                //取到cookie的名字
                String cookieName = cookies[i].getName();
                //判断名字中是否存在"buyCar"
                if ("buyCar".equals(cookieName)){
                    //存在, 得到购物车cookie
                    buyCarCookie = cookies[i];
                    break;
                }
            }
        }
        //判断存放购物车的cookie是否存在
        if (buyCarCookie == null){  //购物车的cookie不存在
            System.out.println("页面中没有登录的情况下第1次添加购物车");
            //得到要添加的商品对象
            Good good= new Good(goodId,num);
            //新建购物车对象
            BuyCar buyCar = new BuyCar();
            //将要添加的商品加入到购物车对象中
            List<Good> goods = new ArrayList<Good>();
            goods.add(good);
            buyCar.setGoods(goods);
            /**
             * 重点：对购物车进加密
             * 加密之前要将购物车对象转为字符串（将购物车对象JSON化, 因为Base64加密工具加密数据为字符串）
             */
            String buyCarStr = JSON.toJSONString(buyCar);
            System.out.println(buyCarStr);  //打印购物车封装类对象中的JSON数据
            //将转后的JSON数据进行加密操作
            String baseBuyCar = Base64Utils.getBASE64(buyCarStr);
            //新建购物车cookie
            buyCarCookie = new Cookie("buyCar",baseBuyCar);
            //设置cookie的有效时间
            buyCarCookie.setMaxAge(3600*32);
            //设置cookie的路径要与控制器的访问路径一致
            buyCarCookie.setPath("/buyCar");
            //响应回客户端
            response.addCookie(buyCarCookie);
            map.put("code","success");
        } else {  //购物车的cookie存在
            System.out.println("存在buyCar的cookie");
            //取出购物车buyCarCookie中的value
            String baseBuyCar = buyCarCookie.getValue();
            BuyCar buyCar = null;  //定义购物车对象
            List<Good> goods = null;  //定义商品集合对象
            //判断buyCarCookie中value是否有数据
            if(!StrUtil.isBlank(baseBuyCar)&&!StrUtil.isNullOrUndefined(baseBuyCar)) {  //cookie中有数据
                System.out.println("存在buyCar的cookie, 且cookie中有value值");
                //表示存在值，不为空，解密得到购物车商品List集合
                buyCar = JSON.parseObject(Base64Utils.getFromBASE64(baseBuyCar), BuyCar.class);
                goods = buyCar.getGoods();  //取出以前的购物车商品数据
                //判断后面即将要加入的商品id有没有与之前购物车中的商品id重复
                Good good = null;  //定义一个空的新的商品变量
                int i = 0;
                //循环原有的商品数据
                for (; i < goods.size(); i++) {
                    if (goods.get(i).getGoodId().equals(goodId)) {
                        good = goods.get(i);  //存在相同的商品，将原有商品对象赋值给要添加的商品变量
                        break;
                    }
                }
                if (good != null) {  //判断原有购物车数据商品中存在要添加的商品
                    //判断新加入的商品ID与购物车中的商品ID有重复,只是修改原有商品的数量
                    int newNum = good.getNum() + num;  //加数量
                    goods.remove(good);  //将原有的商品对象删除掉
                    goods.add(i, new Good(goodId, newNum));  //在List集合中的原位置加上新的商品（修改了数量的商品对象）
                    System.out.println("存在buyCar的cookie, 且cookie中有value值, 且新加入的与之前有重复");
                } else {  //判断原有购物车数据商品中不存在要添加的商品
                    goods.add(new Good(goodId, num));  //直接将要添加的商品加入到List集合中
                    System.out.println("存在buyCar的cookie, 且cookie中有value值, 且新加入的与之前没有有重复");
                }
            } else {  //cookie中没有数据
                System.out.println("存在buyCar的cookie, 但是cookie中没有value值");
                //value值为空
                //新建商品集合
                goods = Arrays.asList(new Good(goodId,num));
                //新建购物车对象
                buyCar = new BuyCar();
            }
            //将商品集合重新设置到购物车对象中
            buyCar.setGoods(goods);
            //购物车对象进行加密,再将购物车cookie响应给客户端
            String buyCarStr = JSON.toJSONString(buyCar);
            System.out.println(buyCarStr);
            String miwen = Base64Utils.getBASE64(buyCarStr);
            //将加密的换行操作去掉
            miwen = miwen.replaceAll("\r\n","");
            buyCarCookie = new Cookie("buyCar",miwen);
            //设置cookie的有效时间
            buyCarCookie.setMaxAge(3600*32);
            //设置cookie的路径要与控制器的访问路径一致
            buyCarCookie.setPath("/buyCar");
            //响应回客户端
            response.addCookie(buyCarCookie);
            map.put("code","success");
        }
        return map;
    }

    /**
     * 已登录的情况下实现购物车的添加 (将数据存到redis中)
     * @param goodId  商品id
     * @param num  商品数量
     * @param uid  登录的用户id
     * @param request  请求对象
     * @param response  响应对象
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> addBuyCarAfterLogin(Integer goodId, Integer num, Integer uid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //新建返回的添加map集合
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodId",goodId);
        map.put("num",num);
        map.put("uid",uid);
        map.put("code","fail");  //添加失败
        //通过redis缓存模板得到操作value的缓存对象
        ValueOperations vop = redisTemplate.opsForValue();
        //定义购物车对象
        BuyCar buyCar = null;
        //定义要添加的商品对象
        Good good = null;
        int i = 0;
        //从redis中取出商品数据, 根据登录的用户取到其购物车中的数据
        String redisBuyCarStr = (String) vop.get("redisBuyCar," + uid);
        //判断redis中是否存在buyCar数据
        if (!StrUtil.isBlank(redisBuyCarStr) && !StrUtil.isNullOrUndefined(redisBuyCarStr)){
            //redis中存在购物车数据
            //将购物车数据反序列化为购物车对象BuyCar ( 此时无需解密, 因为存到redis中的数据并没有加密 )
            buyCar = JSON.parseObject(redisBuyCarStr, BuyCar.class);
            //获取购物车中的商品集合数据
            List<Good> redisGoods = buyCar.getGoods();
            //循环, 判断购物车中是否存在要添加的商品数据
            for (;i < redisGoods.size(); i++){
                if (goodId.equals(redisGoods.get(i).getGoodId())){  //存在要添加的商品数据
                    good = redisGoods.get(i);  //将该商品数据赋值给变量
                    break;
                }
            }

            if (good == null){
                System.out.println("redis中有数据, 没有重复商品, 直接往后面加");
                //购物车中不存在该商品数据, 直接在集合后面添加商品元素即可
                redisGoods.add(new Good(goodId,num));
            } else {
                System.out.println("redis中有数据, 存在该商品, 修改该商品数量");
                //购物车中存在该商品数据, 修改原有的商品数量即可
                int newNum = good.getNum() + num;  //计算增加后的该商品数量
                redisGoods.remove(good);  //把原有的商品集合元素移除
                redisGoods.add(i,new Good(goodId,newNum));  //加入修改后的商品对象元素
            }
            //将商品集合设置到购物车对象中
            buyCar.setGoods(redisGoods);
            System.out.println(JSON.toJSONString(buyCar));  //测试打印
        } else {
            System.out.println("redis中没有数据, 第一次添加, 直接新建购物车进行添加");
            //redis中不存在购物车数据, 直接新建购物车对象进行添加
            buyCar = new BuyCar();
            buyCar.setGoods(Arrays.asList(new Good(goodId,num)));
            System.out.println(JSON.toJSONString(buyCar));  //测试打印
        }
        //将购物车对象数据装入到redis数据库中
        vop.set("redisBuyCar,"+uid,JSON.toJSONString(buyCar));
        map.put("code","success");
        return map;
    }

    /**
     * 用户登录时合并购物车数据
     * @param uid  登录的用户id
     * @param request  请求对象
     * @param response  响应对象
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> appendBuyCarByLogin(Integer uid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //新建返回的添加map集合
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid",uid);
        map.put("code","fail");  //合并失败
        //得到整个页面中的cookies
        Cookie[] cookies = request.getCookies();
        //定义空的buyCar的cookie
        Cookie buyCarCookie = null;
        //判断cookies中是否存在cookie
        if (cookies != null){
            //cookies不为空, 循环判断名字为buyCar的cookie是否存在
            for (int i = 0; i < cookies.length; i++) {
                if ("buyCar".equals(cookies[i].getName())){
                    //将名字为buyCar的cookie赋值给buyCarCookie
                    buyCarCookie = cookies[i];
                    break;
                }
            }
        }
        //判断buyCarCookie是否为空
        if (buyCarCookie != null){  //购物车的cookie存在
            //buyCarCookie存在, 取出buyCarCookie中的value
            String baseBuyCar = buyCarCookie.getValue();
            //判断buyCarCookie中的value值是否为空
            if (!StrUtil.isBlank(baseBuyCar) && !StrUtil.isNullOrUndefined(baseBuyCar)){
                //buyCarCookie中的value不为空, 解密得到购物车对象
                BuyCar buyCar = JSON.parseObject(Base64Utils.getFromBASE64(baseBuyCar), BuyCar.class);
                //取出cookie中的商品集合
                List<Good> goodsCookie = buyCar.getGoods();
                //通过redis缓存模板得到操作value的缓存对象
                ValueOperations vop = redisTemplate.opsForValue();
                //从redis中取出商品数据, 根据登录的用户取到其购物车中的数据
                String redisBuyCarStr = (String) vop.get("redisBuyCar," + uid);
                //判断redis中是否存在buyCar数据
                if (!StrUtil.isBlank(redisBuyCarStr) && !StrUtil.isNullOrUndefined(redisBuyCarStr)){
                    System.out.println("cookie中存在数据, 且redis中也存在数据, 匹配相同商品后, 进行合并");
                    //redis中存在buyCar数据, 并获取redis中的购物车对象
                    BuyCar redisBuyCar = JSON.parseObject(redisBuyCarStr, BuyCar.class);
                    //得到redis中的集合
                    List<Good> goodsRedis = redisBuyCar.getGoods();
                    Good newGood = null;
                    //新建一个空的商品集合, 把不同的商品数据全部存放在此集合中
                    List<Good> newList = new ArrayList<Good>();
                    //此处双重for循环作用是找出相同的元素, 将数量进行合并
                    for (int i = 0; i < goodsRedis.size(); i++) {  //循环redis中的商品
                        for (int j = 0; j < goodsCookie.size(); j++) {  //循环cookie中的商品
                            if (goodsRedis.get(i).getGoodId().equals(goodsCookie.get(j).getGoodId())){
                                //存在重复商品, 把商品对象数量进行合并, 赋值给新建的商品变量
                                newGood = new Good(goodsRedis.get(i).getGoodId(), goodsRedis.get(i).getNum() + goodsCookie.get(j).getNum());
                                //将匹配上的移除, 没有必要再次匹配
                                goodsCookie.remove(goodsCookie.get(j));
                                break;
                            } else {
                                //不存在重复的, 直接赋值给变量
                                newGood = goodsRedis.get(i);
                            }
                        }
                        //将新建的商品变量数据加入到新建的商品集合中
                        newList.add(newGood);
                    }
                    if (goodsCookie.size() > 0){  //cookie中还存在商品集合元素
                        //cookie中与redis中的不重复的商品, 直接加入新的商品集合后面
                        newList.addAll(goodsCookie);
                    }
                    //重新加入到redis中
                    redisBuyCar.setGoods(newList);
                    System.out.println(JSON.toJSONString(redisBuyCar));
                    //将匹配过的购物车数据JSON化并加入到redis中
                    vop.set("redisBuyCar,"+uid,JSON.toJSONString(redisBuyCar));
                    map.put("code","success");
                } else {
                    System.out.println("cookie中存在数据, redis中不存在数据, 直接将cookie中的数据加入到redis中");
                    //redis中不存在buyCar数据
                    //直接将cookie中的buyCar对象加入到redis中, 完成合并
                    vop.set("redisBuyCar,"+uid,JSON.toJSONString(buyCar));
                    map.put("code","success");
                }
            } else {
                System.out.println("购物车的cookie存在, 但是没有数据, 不用合并");
            }
            //清空buyCar的cookie (当cookie存在时)
            Cookie cookie = new Cookie("buyCar", "");
            cookie.setPath("/buyCar");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        } else {
            System.out.println("不存在购物车的cookie, 不用合并");
        }
        return map;
    }

    /**
     * 查询购物车数据
     * @param token  登录用户的令牌
     * @param request  请求对象
     * @return
     * @throws Exception
     */
    @Override
    public List<Good> findBuyCar(String token, HttpServletRequest request) throws Exception {
        List<Good> goods = null;
        if (token == null){
            //得到整个页面中的cookies
            Cookie[] cookies = request.getCookies();
            //定义空的buyCar的cookie
            Cookie buyCarCookie = null;
            //判断cookies中是否存在cookie
            if (cookies != null){
                //cookies不为空, 循环判断名字为buyCar的cookie是否存在
                for (int i = 0; i < cookies.length; i++) {
                    if ("buyCar".equals(cookies[i].getName())){
                        //将名字为buyCar的cookie赋值给buyCarCookie
                        buyCarCookie = cookies[i];
                        break;
                    }
                }
            }
            if (buyCarCookie != null){
                //购物车cookie存在
                String baseCookie = buyCarCookie.getValue();
                if (!StrUtil.isBlank(baseCookie) && !StrUtil.isNullOrUndefined(baseCookie)){
                    //购物车cookie中的value不为空, 解密得到购物车对象
                    BuyCar buyCar = JSON.parseObject(Base64Utils.getFromBASE64(baseCookie), BuyCar.class);
                    //取出cookie中的商品集合
                    goods = buyCar.getGoods();
                }
            }
        } else {
            //令牌不为null, 从redis中查找用户数据
            ValueOperations vop = redisTemplate.opsForValue();
            WebUsersEntity loginUser = (WebUsersEntity) vop.get("sessionId" + token);
            if (loginUser == null){
                //得到整个页面中的cookies
                Cookie[] cookies = request.getCookies();
                //定义空的buyCar的cookie
                Cookie buyCarCookie = null;
                //判断cookies中是否存在cookie
                if (cookies != null){
                    //cookies不为空, 循环判断名字为buyCar的cookie是否存在
                    for (int i = 0; i < cookies.length; i++) {
                        if ("buyCar".equals(cookies[i].getName())){
                            //将名字为buyCar的cookie赋值给buyCarCookie
                            buyCarCookie = cookies[i];
                            break;
                        }
                    }
                }
                if (buyCarCookie != null){
                    //购物车cookie存在
                    String baseCookie = buyCarCookie.getValue();
                    if (!StrUtil.isBlank(baseCookie) && !StrUtil.isNullOrUndefined(baseCookie)){
                        //购物车cookie中的value不为空, 解密得到购物车对象
                        BuyCar buyCar = JSON.parseObject(Base64Utils.getFromBASE64(baseCookie), BuyCar.class);
                        //取出cookie中的商品集合
                        goods = buyCar.getGoods();
                    }
                }
            } else {  //用户已登录
                //从redis中取出商品数据, 根据登录的用户取到其购物车中的数据
                String redisBuyCarStr = (String) vop.get("redisBuyCar," + loginUser.getId());
                //判断redis中是否存在buyCar数据
                if (!StrUtil.isBlank(redisBuyCarStr) && !StrUtil.isNullOrUndefined(redisBuyCarStr)) {
                    System.out.println("cookie中存在数据, 且redis中也存在数据, 匹配相同商品后, 进行合并");
                    //redis中存在buyCar数据, 并获取redis中的购物车对象
                    BuyCar redisBuyCar = JSON.parseObject(redisBuyCarStr, BuyCar.class);
                    //得到redis中的集合
                    goods = redisBuyCar.getGoods();
                }
            }
        }
        if (goods != null){
            //通过商品集合中的goodId去mysql数据库中查询出商品详情的其他数据
            for (Good good:goods){
                //获取商品详情数据
                WebProductDetailEntity productDetail = webProductDetailMapper.queryObjectById(good.getGoodId());
                good.setAvatorimg(productDetail.getAvatorimg());
                good.setPrice(productDetail.getPrice());
                good.setTitle(productDetail.getTitle());
                good.setSubtitle(productDetail.getSubtitle());
            }
        }
        return goods;
    }

    /**
     * 删除提交后的购物车商品数据 (redis中)
     * @param uid  用户id
     * @param proIds  选中的商品id
     * @throws Exception
     */
    @Override
    public void removeGoodByProIds(Integer uid, String proIds) throws Exception {
        //取到redis的操作模板
        ValueOperations vop = redisTemplate.opsForValue();
        //从redis中取出商品数据, 根据登录的用户取到其购物车中的数据
        String redisBuyCarStr = (String) vop.get("redisBuyCar," + uid);
        //判断redis中是否存在buyCar数据
        if (!StrUtil.isBlank(redisBuyCarStr) && !StrUtil.isNullOrUndefined(redisBuyCarStr)) {
            //redis中存在buyCar数据, 获取redis中的购物车对象
            BuyCar redisBuyCar = JSON.parseObject(redisBuyCarStr, BuyCar.class);
            //得到redis中的商品集合
            List<Good> goods = redisBuyCar.getGoods();
            //得到选中商品的数组
            String[] proIdArr = proIds.split(",");
            for (String proId:proIdArr){
                for (Good good:goods){
                    if (proId.equals(good.getGoodId().toString())){
                        goods.remove(good);  //删除
                        break;
                    }
                }
            }
            //重新加入到redis中
            //redisBuyCar.setGoods(goods);
            System.out.println(JSON.toJSONString(redisBuyCar));
            //将删除后的购物车数据JSON化并装入到redis中 (覆盖掉之前的)
            vop.set("redisBuyCar,"+uid, JSON.toJSONString(redisBuyCar));
        }
    }
}
