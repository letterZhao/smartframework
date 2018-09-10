# smartframework
轻量级javaweb框架,持续更新中....

#**简介**：  
##**一、加载配置文件，读取路径下的类、实例化类、IOC容器管理bean**
ConfigConstant类 提供基础配置文件路径
ConfigHelper类 读取配置文件
ClassUtil类 加载配置文件下的类
@Controller @Service 定义Controller和Service类
ClassHelper类 封装了ClassUtil类，提供各种获取Class的方法
ReflectionUtil类 提供实例化Class的方法
BeanHelper类 IOC容器，提供获取实例方法




##**Dispactherservlet是核心类,框架主要的原理如下：**
1、提供handler类和request类，一个request对应一个handler类,在框架初始化时完成注册，用户发出请求时框架可以根据请求方法和请求路径得到request进而得到handler。handler中包含被请求的controller以及method，从而进行定位，最后通过反射动态执行代码得到结果result。
2、框架包括两种数据结构view和data,view返回jsp页面或者数据渲染过的jsp页面，data返回json数据,根据得到的result类型判断返回哪种类型的数据结构。
