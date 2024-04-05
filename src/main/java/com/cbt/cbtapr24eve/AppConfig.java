package com.cbt.cbtapr24eve;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig
{

    @Bean("beannum1")
    @Scope("prototype")
    public MyNumber numberOne() // BEAN DEFINITION
    {
        MyNumber myNumber = new MyNumber();
        myNumber.setNum(76);
        return myNumber;
    }

    @Bean
    @Scope("prototype")
    public MyNumber numberTwo() // BEAN DEFINITION
    {
        MyNumber myNumber = new MyNumber();
        myNumber.setNum(89);
        return myNumber;
    }


}
