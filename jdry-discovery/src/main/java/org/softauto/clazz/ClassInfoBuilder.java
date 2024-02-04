package org.softauto.clazz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.Discover;

public class ClassInfoBuilder {

    private static Logger logger = LogManager.getLogger(Discover.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    public static Builder newBuilder() { return new Builder();}

    ClassInfo classInfo;

    public ClassInfoBuilder(ClassInfo classInfo){
        this.classInfo = classInfo;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public static class Builder {

        private boolean isAbstract ;
        private boolean isApplicationClass ;
        private boolean isInnerClass ;
        private boolean isLibraryClass ;
        private boolean isEnum ;
        private boolean isInterface;
        private boolean isJavaLibraryClass;
        private boolean isPrivate;
        private boolean isStatic;
        private boolean isFinal ;
        private boolean isProtected ;
        private boolean isPublic ;
        private boolean hasParameters ;
        private boolean entity;
        private boolean generic;
        private boolean singleton;

        public Builder setGeneric(boolean generic) {
            this.generic = generic;
            return this;
        }

        public Builder setSingleton(boolean singleton) {
            this.singleton = singleton;
            return this;
        }

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
                classInfo.setSingleton(singleton);
                classInfo.setGeneric(generic);
                classInfo.setHasParameters(hasParameters);
            } catch (Exception e) {
                logger.error(JDRY,"fail build class info " ,e);
            }
            return new ClassInfoBuilder(classInfo);
        }
    }
}
