import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


interface ISubject {
    public void eat(String msg, int num);
}
class RealSubject implements ISubject{
    @Override
    public void eat(String msg, int num) {
        System.out.println("吃"+num+"份"+msg);
    }

}

/**
 * 动态代理的标识接口，只有实现此接口的子类才具有动态代理的功能
 */
class ProxySubject implements InvocationHandler{
    private Object target; //绑定任意接口对象
    public Object bind(Object target){
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }
    public void prepare(){
        System.out.println("prepare");
    }
    public void close(){
        System.out.println("end");
    }
    /**
     *invoke()表示的是调用执行的方法，但是所有代理带返回给用户的接口对象都属于代理对象，
     * 当用户执行接口方法的时候所调用的实例化对象就是该代理主题动态创建的一个对象
     * @param proxy 被代理的对象信息
     * @param method 被调用的方法对象，反射调用方法
     * @param args 方法中的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.prepare();
        Object o = method.invoke(this.target, args);
        this.close();
        return o;
    }
}

public class TestDemo {
    public static void main(String[] args) {
        ISubject i = (ISubject) new ProxySubject().bind(new RealSubject());
        i.eat("鱼香肉丝",20);
    }
}
