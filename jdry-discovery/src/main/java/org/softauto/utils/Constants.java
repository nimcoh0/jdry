package org.softauto.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Constants {

    public static final Set<String> CREATE = new HashSet<>(Arrays.asList("create", "save","add"));

    public static final Set<String> READ = new HashSet<>(Arrays.asList("get", "find"));

    public static final Set<String> UPDATE = new HashSet<>(Arrays.asList("update"));

    public static final Set<String> DELETE = new HashSet<>(Arrays.asList("delete","remove"));

    public static final Set<String> ENTITY = new HashSet<>(Arrays.asList("org.softauto.annotations.EntityForTesting","io.swagger.annotations.ApiModel"," jakarta.persistence.Entity"));

}
