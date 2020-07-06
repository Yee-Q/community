## 异想社区

#### 项目开发过程遇到的问题
1.
```
Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported
```
发送的请求格式必须使用 contentType: "application/json",
2.
```JSON parse error```
JSON 通常用于与服务端交换数据。
在向服务器发送数据时一般是字符串。
我们可以使用 JSON.stringify() 方法将 JavaScript 对象转换为字符串
```
data: JSON.stringify({
            "parentId": tid,
            "content": content,
            "type": 1
        }),
```
3.
使用 request 无法销毁 session
使用 SessionStatus sessionStatus 属性，sessionStatus.setComplete() 可以成功销毁
4.mybatis 中标签不能大写

### 优化日志
1. 使用 HTML5 web 存储来保存信息，实现不刷新页面登录 41-1
使用HTML5可以在本地存储用户的浏览数据。
早些时候,本地存储使用的是 cookie。但是Web 存储需要更加的安全与快速. 这些数据不会被保存在服务器上，但是这些数据只用于用户请求网站数据上.它也可以存储大量的数据，而不影响网站的性能.
数据以 键/值 对存在, web网页的数据只允许该网页访问使用。
https://www.runoob.com/html/html5-webstorage.html   // 菜鸟教程
2. 实现状态栏动态更新用户信息  -- ToDo
3. 实现回复列表的功能  42
4. 完成了二级评论页面的设计
5. 封装了返回结果集 44-
6. 使用 jquery 拼接 html 完成二级评论的显示
7. 使用 coment.js 完成时间格式转换 - 45
8. 根据标签检索相关帖子并展示 - 46
9. 添加标签再优化 - 48

### 学到了
js函数可以使用在某行加上 debugger; 作为断点

 