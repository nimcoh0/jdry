package org.softauto.discovery.handlers.flow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.Discover;

public class ClassInfoBuilder {

    private static Logger logger = LogManager.getLogger(Discover.class);

    public static Builder newBuilder() { return new Builder();}

    ClassInfo classInfo;

    public ClassInfoBuilder(ClassInfo classInfo){
        this.classInfo = classInfo;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public static class Builder {

        boolean isAbstract ;
        boolean isApplicationClass ;
        boolean isInnerClass ;
        boolean isLibraryClass ;
        boolean isEnum ;
        boolean isInterface;
        boolean isJavaLibraryClass;
        boolean isPrivate;
        boolean isStatic;
        boolean isFinal ;
        boolean isProtected ;
        boolean isPublic ;
        boolean hasParameters ;
        boolean entity;

        public Builder setEntity(boolean entity) {
            this.entity = entity;
            return this;
        }

        public Builder setAbstract(boolean anAbstract) {
            isAbstract = anAbstract;
            return this;
        }

        public Builder setApplicationClass(boolean applicationClass) {
            isApplicationClass = applicationClass;
            return this;
        }

        public Builder setInnerClass(boolean innerClass) {
            isInnerClass = innerClass;
            return this;
        }

        public Builder setLibraryClass(boolean libraryClass) {
            isLibraryClass = libraryClass;
            return this;
        }

        public Builder setEnum(boolean anEnum) {
            isEnum = anEnum;
            return this;
        }

        public Builder setInterface(boolean anInterface) {
            isInterface = anInterface;
            return this;
        }

        public Builder setJavaLibraryClass(boolean javaLibraryClass) {
            isJavaLibraryClass = javaLibraryClass;
            return this;
        }

        public Builder setPrivate(boolean aPrivate) {
            isPrivate = aPrivate;
            return this;
        }

        public Builder setStatic(boolean aStatic) {
            isStatic = aStatic;
            return this;
        }

        public Builder setFinal(boolean aFinal) {
            isFinal = aFinal;
            return this;
        }

        public Builder setProtected(boolean aProtected) {
            isProtected = aProtected;
            return this;
        }

        public Builder setPublic(boolean aPublic) {
            isPublic = aPublic;
            return this;
        }

        public Builder setHasParameters(boolean hasParameters) {
            this.hasParameters = hasParameters;
            return this;
        }

        public ClassInfoBuilder build(){
            ClassInfo classInfo = new ClassInfo();
            try {
                classInfo.setInnerClass(isInnerClass);
                classInfo.setApplicationClass(isApplicationClass);
                classInfo.setAbstract(isAbstract);
                classInfo.setLibraryClass(isLibraryClass);
                classInfo.setEnum(isEnum);
                classInfo.setFinal(isFinal);
                classInfo.setInnerClass(isInnerClass);
                classInfo.setInterface(isInterface);
                classInfo.setJavaLibraryClass(isJavaLibraryClass);
                classInfo.setPrivate(isPrivate);
                classInfo.setProtected(isProtected);
                classInfo.setPublic(isPublic);
                classInfo.setStatic(isStatic);
                classInfo.setEntity(entity);
                classInfo.setHasParameters(hasParameters);
            } catch (Exception e) {
                logger.error("fail build class info " ,e);
            }
            return new ClassInfoBuilder(classInfo);
        }
    }
}
