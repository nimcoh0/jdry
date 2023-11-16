package org.softauto.clazz;


/**
 * save class info as represented by sootclass
 */
public class ClassInfo {


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



    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public boolean isApplicationClass() {
        return isApplicationClass;
    }

    public void setApplicationClass(boolean applicationClass) {
        isApplicationClass = applicationClass;
    }

    public boolean isInnerClass() {
        return isInnerClass;
    }

    public void setInnerClass(boolean innerClass) {
        isInnerClass = innerClass;
    }

    public boolean isLibraryClass() {
        return isLibraryClass;
    }

    public void setLibraryClass(boolean libraryClass) {
        isLibraryClass = libraryClass;
    }

    public boolean isEnum() {
        return isEnum;
    }

    public void setEnum(boolean anEnum) {
        isEnum = anEnum;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

    public boolean isJavaLibraryClass() {
        return isJavaLibraryClass;
    }

    public void setJavaLibraryClass(boolean javaLibraryClass) {
        isJavaLibraryClass = javaLibraryClass;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isHasParameters() {
        return hasParameters;
    }

    public void setHasParameters(boolean hasParameters) {
        this.hasParameters = hasParameters;
    }

    public boolean isEntity() {
        return entity;
    }

    public void setEntity(boolean entity) {
        this.entity = entity;
    }
}
