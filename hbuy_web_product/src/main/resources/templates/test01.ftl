<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>test01.ftl</title>
</head>
<body>
    <div align="center">
        <h1>test01.ftl测试页面</h1>
        <hr>
        <h2>1. 普通属性的传值</h2>
        <h3>username: ${username}</h3>
        <h3>age: ${age}</h3>
        <h3>money: ${money}</h3>
        <h3>date: ${date?string('yyyy/MM/dd HH:mm:ss')}</h3>
        <h3>char: ${char}</h3>
        <h3>boolean: ${boolean?string("true","false")}</h3>
        <hr>
        <h2>2. 对象属性的传值</h2>
        <h3>id: ${productDetail.id}</h3>
        <h3>title: ${productDetail.title}</h3>
        <h3>subtitle: ${productDetail.subtitle}</h3>
        <h3>avatorimg: ${productDetail.avatorimg}</h3>
        <h3>price: ${productDetail.price}</h3>
        <h3>color: ${productDetail.color}</h3>
        <h3>num: ${productDetail.num}</h3>
        <h3>href: ${productDetail.href}</h3>
        <h3>updatetime: ${productDetail.updatetime?string('yyyy/MM/dd HH:mm:ss')}</h3>
        <hr>
        <h2>3. List集合的传值</h2>
        <#list strs as str>
            <h3>${str}</h3>
        </#list>
        <hr>
        <h2>4. Map集合的传值</h2>
        <h3>username: ${map['username']}</h3>
        <h3>date: ${map['date']?string('yyyy/MM/dd HH:mm:ss')}</h3>
        <h3>char: ${map['char']}</h3>
        <h3>num: ${map['num']}</h3>
        <h3>float: ${map['float']}</h3>
    </div>
</body>
</html>