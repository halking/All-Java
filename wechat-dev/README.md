#SSM集成的基础项目，项目使用Maven管理

#MyBatis3.3.0

#Spring[MVC]4.1.2.RELEASE

项目使用Spring4.1.2.RELEASE + SpringMVC4.1.2.RELEASE + Mybatis3.3.0

项目集成了Mybatis分页插件和通用Mapper插件

项目使用的mysql数据库，根据需要可以切换为其他数据库


##MyBatis工具

###http://www.mybatis.tk

##推荐使用Mybatis通用Mapper3

###https://github.com/abel533/Mapper

##推荐使用Mybatis分页插件PageHelper

###https://github.com/pagehelper/Mybatis-PageHelper

根据table生成 model,mapper,mapper xml的命令:mvn mybatis-generator:generate

tomcat运行项目:右击项目run as --> run as server

所有的mapper,service,controller需要Junit Test Case,提交代码之前全部的Junit Test Case测试通过方可提交代码

test