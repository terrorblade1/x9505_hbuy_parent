package com.java.product.service.impl;

import com.java.model.WebProductDetailEntity;
import com.java.service.WebProductDetailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author yk
 *    WebProductDetail业务层实现类
 * @date 2020-03-09 17:42:42
 */
@Service
@Transactional
public class WebProductDetailServiceImpl extends BaseServiceImpl<WebProductDetailEntity> implements WebProductDetailService {

    //引入freemarker的模板对象
    @Autowired
    private Configuration configuration;

    /**
     * 根据自定义freemarker模板生成商品详情的静态页面
     * @throws Exception
     */
    @Scheduled(cron = "0/30 * * * * ?")  //每隔30s执行一次
    @Override
    public void makeProductDetail() throws Exception {
        FileWriter fw = null;

        //1. 查询获取所有商品详情及其详情图片数据
        List<WebProductDetailEntity> productDetails = baseMapper.queryAll();
        //2. 定义生成html文件的文件夹
        File file = new File("E:\\ProductDetail");
        //3. 判断文件夹是否存在(不存在则创建)
        if (!file.exists()){
            file.mkdirs();
        }
        //4.1. 通过循环遍历商品详情数据, 生成商品静态页面
        for (WebProductDetailEntity productDetail:productDetails){
            //4.2. 定义生成静态页面的目标文件路径
            String filePath = "E:\\ProductDetail\\"+productDetail.getId()+".html";
            //4.3. 通过文件路径得到目标文件
            File newFile = new File(filePath);  //此时目标文件是空的
            //4.4. 得到目标文件的输入流对象
            fw = new FileWriter(newFile);
            //4.5. 得到生成静态资源的模板对象
            Template template = configuration.getTemplate("product.ftl");
            //4.5. 通过目标文件的数据和输入流对象生成静态文件
            template.process(productDetail,fw);
            fw.close();  //关闭流
        }
    }

    /*@Scheduled(cron = "0/2 * * * * ?")  //每隔2秒钟执行
    public void testTask01(){
        System.out.println("-----"+new Date()+"task 01 测试-----");
    }

    @Scheduled(cron = "0/3 * * * * ?")  //每隔3秒钟执行
    public void testTask02(){
        System.out.println("*****"+new Date()+"task 02 测试*****");
    }

    @Scheduled(cron = "15,30,45 * * * * ?")  //每分钟的第15,30,45秒执行
    public void testTask03(){
        System.out.println("~~~~~"+new Date()+"task 03 测试~~~~~");
    }*/
}
