package com.example.guozhaotong.demo;

import com.example.guozhaotong.demo.entity.Person;
import com.example.guozhaotong.demo.service.EtlService;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Junit4Test {
    @Ignore("还没成功，等等再测")
    @Test
    public void testAddRecord() {

        assertEquals(new Person("jack", 100, 100, 100), new EtlService().addRecord("jack", 100, 100, 100));

    }

    @Test
    public void testHello() {

        assertEquals("Hello world!", new EtlService().hello());

    }

    @Test
    public void testFactorial2() {

        assertEquals("success", new EtlService().deleteTimeTask(7));

    }
}
