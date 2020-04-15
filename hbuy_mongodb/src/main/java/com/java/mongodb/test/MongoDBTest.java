package com.java.mongodb.test;

import com.java.mongodb.utils.MongoDBUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Author: 乐科
 * Date: 2020/3/23 20:51
 * MongoDB数据库操作的测试类
 */
public class MongoDBTest {

    //MongoDB集合的链接对象
    private MongoCollection<Document> collection = MongoDBUtils.getCollection();

    //测试mongoDB数据库的链接
    @Test
    public void test01(){
        System.out.println(collection);
    }

    //查询所有的文档数据
    @Test
    public void test02(){
        //执行集合对象的查询所有文档数据操作
        FindIterable<Document> documents = collection.find();
        //进行MongoDB中库的文档数据的遍历
        documents.iterator().forEachRemaining(temp -> System.out.println(temp));
    }

    //多条件查询
    @Test
    public void test03(){
        //定义查询条件的JSON格式字符串
        String praStr = "{\n" +
                "   $and:[\n" +
                "     {$or:[\n" +
                "        {\"price\":{$lt:180}},\n" +
                "        {\"price\":{$gt:229.8}}\n" +
                "     ]},\n" +
                "     {\"author\":{$regex:/a/i}}\n" +
                "   ]\n" +
                "}";
        //构建查询的条件
        Document praDocument = Document.parse(praStr);
        //执行条件查询(条件为BSON类型, Document为BSON类的子类, 同样可以描述BSON类型)
        FindIterable<Document> documents = collection.find(praDocument);
        //进行MongoDB中库的文档数据的遍历
        documents.iterator().forEachRemaining(temp -> System.out.println(temp));
    }

    @Test
    public void test04(){
        //新建查询条件
        Document praDocument = new Document();
        //往此条件对象中拼接加入条件
        praDocument.append("bookName","平凡的世界");
        praDocument.append("author","路遥");
        //执行条件查询(条件为BSON类型, Document为BSON类的子类, 同样可以描述BSON类型)
        FindIterable<Document> documents = collection.find(praDocument);
        //进行MongoDB中库的文档数据的遍历
        documents.iterator().forEachRemaining(temp -> System.out.println(temp));
    }

    //分页
    @Test
    public void test05(){
        Document praDocument = new Document();
        praDocument.append("author","admin");
        //执行第1页的分页查询 (每页3条)
        FindIterable<Document> documents = collection.find(praDocument).skip((1 - 1) * 3).limit(3);
        //进行MongoDB中库的文档数据的遍历
        documents.iterator().forEachRemaining(temp -> System.out.println(temp));
    }

    //BSON
    @Test
    public void test06(){
        Bson ltPrice = Filters.lt("price", 180);  //构建 price < 180 的条件
        Bson gtPrice = Filters.gt("price", 239);  // price > 239
        //创建price小于180或者大于239的条件
        Bson price = Filters.or(ltPrice, gtPrice);
        //构建右边的作者模糊条件 (Pattern.CASE_INSENSITIVE: 表示查询的字符在任意位置且不区分大小写)
        Pattern pattern = Pattern.compile("a", Pattern.CASE_INSENSITIVE);
        //构建根据字符'a'模糊查询, 在任意位置且不区分大小写
        Bson regex = Filters.regex("author", pattern);
        //将左右两边条件整合起来
        //查询范围price小于180或者price大于229.8并且作者名字中存在a(不区分大小写)的书籍)
        Bson bson = Filters.and(price, regex);
        //执行条件查询
        FindIterable<Document> documents = collection.find(bson);
        //进行MongoDB中库的文档数据的遍历
        documents.iterator().forEachRemaining(temp -> System.out.println(temp));
    }

    //添加单个(条件 字符串--->document )
    @Test
    public void test07(){
        Document parseDocument = Document.parse("{'bookName':'平凡的世界','price':'320.9','author':'路遥'}");
        collection.insertOne(parseDocument);
    }

    //添加单个
    @Test
    public void test08(){
        Document document = new Document();
        document.append("bookName","平凡的世界1");
        document.append("author","路遥");
        document.append("price",199.9);
        document.append("count",300);
        collection.insertOne(document);
    }

    //批量添加
    @Test
    public void test09(){
        Document document1 = Document.parse("{'bookName':'平凡的世界2','price':'220.9','author':'路遥','count':999}");
        Document document2 = Document.parse("{'bookName':'平凡的世界3','price':'423.9','author':'路遥','count':149}");
        Document document3 = Document.parse("{'bookName':'平凡的世界4','price':'251.9','author':'路遥','count':931}");
        collection.insertMany(Arrays.asList(document1,document2,document3));
    }

    //根据条件删除
    @Test
    public void test10(){
        //构建删除的条件
        Document document = Document.parse("{'price':199.9}");
        //删除
        DeleteResult deleteResult = collection.deleteMany(document);
        System.out.println(deleteResult);
    }

    //根据条件删除
    @Test
    public void test11(){
        Document document = new Document();
        document.append("price",11);
        DeleteResult deleteResult = collection.deleteMany(document);
        System.out.println(deleteResult);
    }

    //用BSON构建删除的条件, 进行多条件删除(书名+作者)
    @Test
    public void test12(){
        //构建书名条件
        Bson bson1 = Filters.in("bookName", "平凡的世界");
        //构建作者条件
        Bson bson2 = Filters.in("author", "路遥");
        //将两个条件结合
        Bson and = Filters.and(bson1, bson2);
        //执行删除
        DeleteResult deleteResult = collection.deleteMany(and);  //删除所有符合条件的数据
        //DeleteResult deleteResult = collection.deleteOne(and);   最多只删除符合条件的第一条数据
        System.out.println(deleteResult);
    }

    //根据条件修改
    @Test
    public void test13(){
        //新建修改条件
        Bson in = Filters.in("author", "路遥");
        //新建修改内容
        Document parse = Document.parse("{$set:{\"price\":239.9}}");
        //修改
        UpdateResult updateResult = collection.updateMany(in, parse);
        System.out.println(updateResult);
    }

    //多条件修改
    @Test
    public void test14(){
        //新建修改条件
        Bson author = Filters.in("author", "路遥");  //作者条件
        Bson bookName = Filters.in("bookName", "平凡的世界2");  //书名条件
        //将两个条件拼接起来
        Bson and = Filters.and(author, bookName);
        //新建修改内容
        Document parse = Document.parse("{$set:{\"price\":88.8}}");
        //执行修改
        //UpdateResult updateResult = collection.updateOne(and, parse);   最多只修改符合条件的第一条数据
        UpdateResult updateResult = collection.updateMany(and, parse);  //修改所有符合条件的数据
        System.out.println("修改的数据量: "+updateResult.getModifiedCount());
    }
}
