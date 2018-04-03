package Hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class HytrixCommandTest extends HystrixCommand<String>{

    private final String name;


    public HytrixCommandTest(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        return "Hystrix test >>"+name;
    }

    public static final void main(String[] args){
        String msg = new HytrixCommandTest("huang").execute();
        System.out.println(msg);
    }
}
