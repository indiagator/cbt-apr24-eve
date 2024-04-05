package com.cbt.cbtapr24eve;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.Enumeration;


@RestController
public class MainRestController
{
    //@Autowired
    //@Qualifier("beannum1")
    //MyNumber number1;

    //@Autowired
    //@Qualifier("numberTwo")
    //MyNumber number2;

    MyNumber number1;
    MyNumber number2;

    MainRestController(@Qualifier("beannum1") MyNumber number1,
                       @Qualifier("numberTwo") MyNumber number2)
    {
        this.number1 = number1;
        this.number2 = number2;
    }

    @Autowired
    WebApplicationContext webApplicationContext;

    @GetMapping("greet")
    public String greet()
    {
        return "Hello World";
    }

    @GetMapping("get/numberone")
    public MyNumber getNumber1()
    {
        number1.increment();
        return number1;
    }

    @GetMapping("get/numbertwo")
    public MyNumber getNumber2()
    {
        return number2;
    }

    @GetMapping("get/mrc")
    public String getWebApplicationContext() {
        return webApplicationContext.getServletContext().getAttributeNames().nextElement();
    }
}
