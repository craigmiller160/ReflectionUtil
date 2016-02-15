package io.craigmiller160.reflection;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Craig on 2/14/2016.
 */
public class FindAndInvokeTest {

    @Test
    public void testFindAndInvoke() throws Exception{
        Object[] objects = getObjects();
        Object result = FindAndInvoke.findAndInvokeMethod(objects, "method1", "One", "Two");
        assertNotNull("No result returned", result);
        assertEquals("Result wrong type", result.getClass(), String.class);
        assertEquals("Result value is wrong", result, "One Two");
    }

    /**
     * Utility method for getting
     * the group of objects to
     * use in the tests.
     *
     * @return the objects for the tests.
     */
    private Object[] getObjects(){
        Object[] arr = new Object[2];

        arr[0] = new TestClass1();
        arr[1] = new TestClass2();

        return arr;
    }

    private class TestClass1{

        public String method1(String s1, String s2){
            return s1 + " " + s2;
        }

    }

    private class TestClass2{

        public String method2(String s1, Integer i1){
            return s1 + " " + i1;
        }

    }

}
