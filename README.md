## 基于spring aop的插件体系
基于spring aop，为容器中发bean，动态启停一些插件功能，比如日志打印、方法执行次数统计、方法耗时统计等。

为什么要“动态启停”？
- 部分功能可能web工程并非全天候需要；
- 系统某些功能是体验版，试用到期后停用新特性；

#### 模块&功能
````
    <modules>
        <module>plugin-core</module>
        <module>plugin-samples</module>
        <module>plugin-web</module>
    </modules>
````
plugin-samples 一些插件样例，插件必须实现Advice接口。
plugin-web 创建管理的web模块。

plugins 目录下，是打包jar的插件。



##### 插件的本地配置文件
默认位于 user.home/.plugins/PluginConfigs.dat
json结构，如下：
```
{
	"lastModify":1601971233204,
	"plugins":[
		{
			"active":false,
			"className":"com.ljheee.test.RequestLogPlugin",
			"id":"3",
			"jаrRеmоtеUrl":"file:/Users/lijianhua/Documents/IdeaProjects/dynamic-plugin/plugins/dynamic-plugin.jar",
			"name":"服务参数日志打印",
			"version":"V1.0.0"
		}
	]
}
```

##### 插件站点信息
PluginSite的json结构
```
{
  "plugins": [
    {
      "active": true,
      "className": "com.tuling.plugin.ServerLogPlugin",
      "id": "1",
      "jarRemoteUrl": "file:/Users/lijianhua/Documents/IdeaProjects/dynamic-plugin/plugins/dynamic-plugin.jar",
      "name": "服务参数日志打印"
    },
    {
      "active": true,
      "className": "com.tuling.plugin.CountingBeforeAdvice",
      "id": "2",
      "jarRemoteUrl": "file:/Users/lijianhua/Documents/IdeaProjects/dynamic-plugin/plugins/dynamic-plugin.jar",
      "name": "方法执行统计"
    }
  ],
  "name": "我的插件小仓库"
}
```

## 原理篇
#### Advice 与 Advised
###### Advice
Advice，意为通知，也叫增强，对应我们的AOP增强逻辑；
我们实现的AOP增强逻辑，常常实现MethodBeforeAdvice；

###### Advised
Advised，意为“被增强的”，也就是被AOP增强的，通常是代理对象（由jdk动态代理或cglib生成）；
我们对某些接口进行aop增强，增强之后，这些接口都是Advised。


#### 使用 Advised.addAdvice 的前提
使用 Advised.addAdvice 的前提是，该接口已经被aop增强过，已经是个Advised了。

##### web模块的演示
UserController.active 方法演示了该过程；
UserService的实现类UserServiceImpl是个普通类；
未经过aop增强，直接使用Advised.addAdvice，会出现
```
java.lang.ClassCastException: com.ljheee.plugin.app.service.UserServiceImpl cannot be cast to org.springframework.aop.framework.Advised
```
将UserServiceImpl变成aop代理对象后，就能正常工作；
因此将UserServiceImpl.getUser方法加上@Async，就变成aop代理，就能正常工作了。




访问页面
http://localhost:8080/pluginManager.jsp
