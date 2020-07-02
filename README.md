## 异想社区

#### 项目开发过程遇到的问题
```
Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported
```
发送的请求格式必须使用 contentType: "application/json",
------------------------
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
-------------------------

