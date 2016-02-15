package io.craigmiller160.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Craig on 2/14/2016.
 */
public class FindAndInvoke {

    public static Object findAndInvokeMethod(Object object, String methodSig, Object...newParams)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        ObjectAndMethod oam = getMatchingMethodForSingle(object, methodSig, newParams);
        if(oam == null){
            throw new NoSuchMethodException("No matching method found: " + methodSig + " " + Arrays.toString(newParams));
        }

        if(oam.isVarArgs()){
            newParams = MethodUtils.convertParamsForVarArgsMethod(oam.getMethod(), newParams);
        }

        return oam.getMethod().invoke(oam.getObject(), newParams);
    }

    public static Object findAndInvokeMethod(Object[] objects, String methodSig, Object...newParams)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        ObjectAndMethod oam = getMatchingMethod(objects, methodSig, newParams);
        if(oam == null){
            throw new NoSuchMethodException("No matching method found: " + methodSig + " " + Arrays.toString(newParams));
        }

        if(oam.isVarArgs()){
            newParams = MethodUtils.convertParamsForVarArgsMethod(oam.getMethod(), newParams);
        }

        return oam.getMethod().invoke(oam.getObject(), newParams);
    }

    public static Object findAndInvokeMethod(Collection<?> objects, String methodSig, Object...newParams)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return findAndInvokeMethod(objects.toArray(), methodSig, newParams);
    }

    //TODO document this
    private static ObjectAndMethod getMatchingMethod(Object[] objects, String methodSig, Object...newParams) throws NoSuchMethodException{
        List<ObjectAndMethod> potentialMatches = getPotentialMatchingMethodsFromMultiple(objects, methodSig);

        for(ObjectAndMethod oam : potentialMatches){
            if(MethodUtils.isValidInvocation(oam.getMethod(), newParams)){
                return oam;
            }
        }
        return null;
    }

    private static ObjectAndMethod getMatchingMethodForSingle(Object object, String methodSig, Object...newParams) throws NoSuchMethodException{
        List<ObjectAndMethod> potentialMatches = getPotentialMatchesFromSingle(methodSig, object);

        for(ObjectAndMethod oam : potentialMatches){
            if(MethodUtils.isValidInvocation(oam.getMethod(), newParams)){
                return oam;
            }
        }
        return null;
    }

    /**
     * Get all potentially matching methods from the collection of
     * objects provided to this class. A potential match is a method whose
     * signature matches the provided String, but whose parameter types
     * haven't been checked yet.
     *
     * @param objects the collection of objects to search for a matching method.
     * @param methodSig the signature of the method to search for a match
     *                  of.
     * @return a list of all matching methods paired with the objects they're
     *          from.
     * @throws NoSuchMethodException if no potentially matching methods are found.
     */
    private static List<ObjectAndMethod> getPotentialMatchingMethodsFromMultiple(Object[] objects, String methodSig) throws NoSuchMethodException{
        List<ObjectAndMethod> matchingMethods = new ArrayList<>();
        for(Object obj : objects){
            matchingMethods.addAll(getPotentialMatchesFromSingle(methodSig, obj));
        }

        //If no matches are found, throw an exception
        if(matchingMethods.size() == 0){
            throw new NoSuchMethodException("No methods exist matching this signature: " + methodSig);
        }

        return matchingMethods;
    }

    /**
     * Get all potentially matching methods from a single class.
     * A potential match is a method whose signature matches the
     * provided String, but whose parameter types haven't been
     * checked yet.
     *
     * @param methodSig the signature of the method to find a match
     *                  for.
     * @param obj the object to search for methods with a matching
     *            signature.
     * @return a list of any potential matches found, or an empty
     *          list if none are found.
     */
    private static List<ObjectAndMethod> getPotentialMatchesFromSingle(String methodSig, Object obj){
        List<ObjectAndMethod> matches = new ArrayList<>();
        Method[] methods = obj.getClass().getMethods();
        for(Method m : methods){
            if(m.getName().equals(methodSig)){
                matches.add(new ObjectAndMethod(obj, m));
            }
        }

        return matches;
    }

}
