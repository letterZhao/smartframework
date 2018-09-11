
#smartframework
黄勇<<架构探险:从零开始写JavaWeb框架>>,轻量级javaweb框架smartframework源码,持续更新中....  

#**简介**：  
##**一、实现IOC功能(加载配置文件，读取路径下的类、实例化类、IOC容器管理bean)**  
ConfigConstant类 提供基础配置文件路径  
ConfigHelper类 读取配置文件  
ClassUtil类 加载配置文件下的类  
@Controller @Service 定义Controller和Service类  
ClassHelper类 封装了ClassUtil类，提供各种获取Class的方法  
ReflectionUtil类 提供实例化Class的方法  
BeanHelper类 IOC容器，提供获取实例方法    
#**二、实现AOP功能**  
最简单的做法是通过beanHelp类获取所有Controller或Service注解的类，然后遍历获取这些类的成员变量，看是否是AutoWired注解的类，如果是通过ReflectionUtil类的setFiled方法来实现DI注入  
#**三、实现请求和Controller层的映射关系**  
Request类: 封装请求路径和请求方法  
Handler类: 封装Controller和Method  
ControllerHelper类: 根据ClassHepler类获取所有Controller注解的方法，通过反射获取所有带Action注解的方法，并获取请求路径和请求方法，将Request和Controller关系映射到一个Map中,根据Request可以随时获取Map中的Value;    
#**四、初始化框架**  
HelpLoader类 初始化ClassHelper、BeanHelper、IOCHelper、ControllerHelper    
##**五、请求转发器DispatcherServlet**   
首先从request中获取请求方法和请求路径，封装成一个request对象，通过ControllerHelper.getHandler(requestMethod,requestPath);方法获取Handler类，然后通过BeanHelp.getBean()方法获取实例对象，随后通过HttpRequest获取请求参数封装为Param类，通过ReflectionUtil.invokeMethod(controllerBean,actionMethod);方法返回结果，然后判断返回结果的类型，返回视图或者数据
