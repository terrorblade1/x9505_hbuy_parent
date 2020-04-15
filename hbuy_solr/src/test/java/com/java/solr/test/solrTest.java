package com.java.solr.test;

import com.java.solr.model.Student;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Author: 乐科
 * Date: 2020/4/8 15:11
 */
public class solrTest {

    //定义solr的链接对象
    private HttpSolrClient solr = null;

    @Before
    public void before(){
        //1. 定义solr的服务器链接路径
        String solrUrl = "http://localhost:8888/solr/new_core";
        //2. 创建solr的链接对象
        solr = new HttpSolrClient.Builder(solrUrl).withConnectionTimeout(10000).withSocketTimeout(60000).build();
    }

    //测试solr的添加
    @Test
    public void test01(){
        //使用HttpSolrClient对象操作索引库
        //SolrInputDocument对应一行
        SolrInputDocument document = new SolrInputDocument();
        document.addField("xh",111);
        document.addField("name","张全蛋");
        document.addField("age",29);
        document.addField("sex","男");
        try {
            //执行添加
            UpdateResponse updateResponse = solr.add(document);
            //提交
            solr.commit();
            System.out.println("被添加的状态: "+updateResponse.toString());
            //被添加的状态: {responseHeader={status=0,QTime=710}}   status=0 表示操作成功
            if (updateResponse.getStatus() == 0){
                System.out.println("添加成功, 用时: "+updateResponse.getQTime()+" 毫秒");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("添加失败");
        }
    }

    //根据id删除
    @Test
    public void test02(){
        try {
            UpdateResponse updateResponse = solr.deleteById("eb6dee50-b309-47d9-b286-48fee9e80ff9");
            //提交
            solr.commit();
            System.out.println(updateResponse.getStatus() == 0 ? "删除成功":"删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("删除失败");
        }
    }

    //根据条件模糊删除
    @Test
    public void test03(){
        //定义删除条件
        try {
            //会将条件进行拆分, 然后分别模糊删除!
            UpdateResponse updateResponse = solr.deleteByQuery("name:赵六");
            solr.commit();  //提交
            System.out.println("结果: "+updateResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("删除失败");
        }
    }

    //修改: 需要根据索引id进行修改, 索引id不会发生变化
    @Test
    public void test04(){
        SolrInputDocument document=new SolrInputDocument();
        document.addField("xh",107);
        document.addField("name","老黄");
        document.addField("age",57);
        document.addField("sex","男");
        document.addField("id","a2d1267a-38aa-49ca-abce-6da93115d416");  //根据此id修改, 修改后id不变
        try {
            //执行添加 (覆盖原有的数据)
            UpdateResponse updateResponse = solr.add(document);
            //提交
            solr.commit();
            System.out.println("结果："+updateResponse);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("修改失败");
        }
    }

    //修改: 先删除, 再添加, 索引id会发生变化!
    @Test
    public void test05(){
        try {
            //删除原有数据
            solr.deleteByQuery("xh:107");
            //进行添加
            SolrInputDocument document=new SolrInputDocument();
            document.addField("xh",107);
            document.addField("age",18);
            document.addField("name","小白");
            document.addField("sex","女");
            UpdateResponse updateResponse = solr.add(document);
            solr.commit();  //提交
            System.out.println("结果："+updateResponse.toString());
            //修改前的索引id: a2d1267a-38aa-49ca-abce-6da93115d416
            //修改后的索引id: 34f64119-1ad2-456c-b1e5-4dbf7c8fa926
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("修改失败");
        }
    }

    //通过实体封装类的方式添加solr中的数据 (实体封装类的属性上要加注解@Field)
    @Test
    public void test06(){
        //新建实体封装类对象
        Student student = new Student(108, "小黑", 21, "男");
        try {
            //执行实体封装类对象的添加
            UpdateResponse updateResponse = solr.addBean(student);
            solr.commit();  //提交
            System.out.println("结果："+updateResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("添加失败");
        }
    }

    //通过实体封装类的方式完成solr引擎的批量添加
    @Test
    public void test07(){
        //新建要添加的对象
        Student student1 = new Student(109, "张三", 24, "男");
        Student student2 = new Student(110, "李四", 22, "男");
        Student student3 = new Student(111, "王五", 21, "女");
        Student student4 = new Student(112, "赵六", 32, "男");
        //创建List集合对象
        List<Student> students = Arrays.asList(student1,student2,student3,student4);
        try {
            //执行实体封装类对象的批量添加
            UpdateResponse updateResponse = solr.addBeans(students);
            solr.commit();  //提交
            System.out.println("结果："+updateResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("添加失败");
        }
    }

    //查询
    @Test
    public void test08() {
        //新建查询的条件对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询的条件
        solrQuery.set("q","*:*");  //查询所有
        //设置分页
        solrQuery.setStart(0);
        solrQuery.setRows(5);
        try {
            //执行查询 (查询不需要提交)
            QueryResponse queryResponse = solr.query(solrQuery);
            //System.out.println(queryResponse);
            List<Student> students = queryResponse.getBeans(Student.class);
            students.forEach(student -> System.out.println(student));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询失败");
        }
    }
}
