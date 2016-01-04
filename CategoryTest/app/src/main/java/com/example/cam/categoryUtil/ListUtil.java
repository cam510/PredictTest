package com.example.cam.categoryUtil;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * List
 *
 * @author Daehee Han (@daniel_booknara)
 * @since 2014/05/06
 * @version 1.0.0
 *
 */
public class ListUtil {
    private static final String TAG = ListUtil.class.getSimpleName();
    private ListUtil() { }

    public static List<PackageVO> removeDuplicates(List<PackageVO> list) {
        Set set = new HashSet();
        List uniqueList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                uniqueList.add(element);
        }

//        list.clear();
//        list.addAll(uniqueList);
        return uniqueList;
    }
}